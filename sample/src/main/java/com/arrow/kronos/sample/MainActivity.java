package com.arrow.kronos.sample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.arrow.kronos.api.listeners.CommonRequestListener;
import com.arrow.kronos.api.listeners.FindGatewayListener;
import com.arrow.kronos.api.listeners.GatewayRegisterListener;
import com.arrow.kronos.api.listeners.GatewayUpdateListener;
import com.arrow.kronos.api.listeners.GetNodesListListener;
import com.arrow.kronos.api.listeners.ListNodeTypesListener;
import com.arrow.kronos.api.listeners.ListResultListener;
import com.arrow.kronos.api.models.CommonResponse;
import com.arrow.kronos.api.models.GatewayModel;
import com.arrow.kronos.api.models.GatewayType;
import com.arrow.kronos.api.models.ListResultModel;
import com.arrow.kronos.api.models.NodeModel;
import com.arrow.kronos.api.models.NodeRegistrationModel;
import com.arrow.kronos.api.models.NodeTypeModel;
import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.JsonObject;
import com.arrow.kronos.api.Constants;
import com.arrow.kronos.api.KronosApiService;
import com.arrow.kronos.api.KronosApiServiceFactory;
import com.arrow.kronos.api.listeners.RegisterDeviceListener;
import com.arrow.kronos.api.models.AccountResponse;
import com.arrow.kronos.api.models.GatewayResponse;
import com.arrow.kronos.api.models.DeviceRegistrationModel;

import java.util.List;

import static com.arrow.kronos.api.Constants.Preference.KEY_GATEWAY_ID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = MainActivity.class.getSimpleName();

    public static final String MQTT_CONNECT_URL_DEV = "tcp://pegasusqueue01-dev.cloudapp.net:46953";
    public static final String MQTT_CLIENT_PREFIX_DEV = "/themis.dev";

    private KronosApiService mTelemetrySendService;

    private AccountResponse mAccountResponse;
    private String mDeviceHid;

    private String mGatewayHid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        if (intent != null) {
            mAccountResponse = intent.getParcelableExtra(LoginActivity.ACCOUNT_RESPONSE_EXTRA);
        }

        Button registerDeviceButton = (Button) findViewById(R.id.register_device);
        registerDeviceButton.setOnClickListener(this);

        Button sendTelemetryButton = (Button) findViewById(R.id.send_telemetry);
        sendTelemetryButton.setOnClickListener(this);
        Button registerGatewayButton = (Button) findViewById(R.id.register_gateway);
        registerGatewayButton.setOnClickListener(this);
        Button findGatewayButton = (Button) findViewById(R.id.find_gateway);
        findGatewayButton.setOnClickListener(this);
        Button updateGatewayButton = (Button) findViewById(R.id.update_gateway);
        updateGatewayButton.setOnClickListener(this);

        //Once instance of KronosApiService is created it could be got by getKronosApiService() call
        mTelemetrySendService = KronosApiServiceFactory.getKronosApiService();
        //initialize service with a context and bind it with activity's lifecycle
        mTelemetrySendService.initialize(this);

        //mTelemetrySendService.setMqttEndpoint(MQTT_CONNECT_URL_DEV, MQTT_CLIENT_PREFIX_DEV);
        //register new gateway and initiate persistent connection (it makes sense only in case when some of {ConnectionType.MQTT, ConnectionType.AWS,
        // ConnectionType.IBM} is chosen)
        //mTelemetrySendService.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTelemetrySendService.disconnect();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_device:
                registerDevice();
                break;
            case R.id.send_telemetry:
                sendTelemetry();
                break;
            case R.id.register_gateway:
                registerGateway();
                break;
            case R.id.find_gateway:
                findGateway();
                break;
            case R.id.update_gateway:
                updateGateway();
                break;
        }
    }

    private void registerDevice() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String gatewayHid = sp.getString(KEY_GATEWAY_ID, "");
        DeviceRegistrationModel payload = new DeviceRegistrationModel();
        //some random values to show registering process
        payload.setUid("android-7cfe-e895-1988-cfc60033c5565");
        payload.setName("AndroidInternal");
        payload.setGatewayHid(gatewayHid);
        payload.setUserHid(mAccountResponse.getHid());
        payload.setType("android");
        mTelemetrySendService.registerDevice(payload, new RegisterDeviceListener() {
            @Override
            public void onDeviceRegistered(CommonResponse response) {
                mDeviceHid = response.getHid();
                Log.v(TAG, "onDeviceRegistered");
            }

            @Override
            public void onDeviceRegistrationFailed() {
                Log.v(TAG, "onDeviceRegistrationFailed");
            }
        });
    }

    private void sendTelemetry() {
        Bundle bundle = new Bundle();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("_|timestamp", "1474896307844");
        jsonObject.addProperty("_|deviceHid", mDeviceHid);
        jsonObject.addProperty("f|light", "84.0");
        bundle.putString(Constants.EXTRA_DATA_LABEL_TELEMETRY, jsonObject.toString());
        mTelemetrySendService.sendSingleTelemetry(bundle);
    }

    private void registerGateway() {
        mTelemetrySendService = KronosApiServiceFactory.getKronosApiService();
        String uid = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        FirebaseCrash.logcat(Log.DEBUG, TAG, "registerGateway() UID: " + uid);

        String name = String.format("%s %s", Build.MANUFACTURER, Build.MODEL);
        String osName = String.format("Android %s", Build.VERSION.RELEASE);
        String swName = Constants.SOFTWARE_NAME;
        String userHid = mAccountResponse.getHid();

        GatewayModel gatewayModel = new GatewayModel();
        gatewayModel.setName(name);
        gatewayModel.setOsName(osName);
        gatewayModel.setSoftwareName(swName);
        gatewayModel.setUid(uid);
        gatewayModel.setType(GatewayType.Mobile);
        gatewayModel.setUserHid(userHid);
        gatewayModel.setSoftwareVersion(
                String.format("%d.%d", Constants.MAJOR, Constants.MINOR));

        mTelemetrySendService.registerGateway(gatewayModel, new GatewayRegisterListener() {
            @Override
            public void onGatewayRegistered(GatewayResponse response) {
                mGatewayHid = response.getHid();
            }

            @Override
            public void onGatewayRegisterFailed() {
                Log.v(TAG, "onGatewayRegisterFailed");
            }
        });
    }

    private void findGateway() {
        mTelemetrySendService.findGateway(mGatewayHid, new FindGatewayListener() {
            @Override
            public void onGatewayFound(GatewayModel gatewayModel) {
                Log.v(TAG, "onGatewayFound");
            }

            @Override
            public void onGatewayFindError() {
                Log.v(TAG, "onGatewayFindError");
            }
        });
    }

    private void updateGateway() {
        String uid = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        FirebaseCrash.logcat(Log.DEBUG, TAG, "registerGateway() UID: " + uid);

        String name = String.format("%s %s", Build.MANUFACTURER, Build.MODEL);
        String osName = String.format("Android %s", Build.VERSION.RELEASE);
        String swName = Constants.SOFTWARE_NAME;
        String userHid = mAccountResponse.getHid();

        GatewayModel gatewayModel = new GatewayModel();
        gatewayModel.setName(name);
        gatewayModel.setOsName(osName);
        gatewayModel.setSoftwareName(swName);
        gatewayModel.setUid(uid);
        gatewayModel.setType(GatewayType.Mobile);
        gatewayModel.setUserHid(userHid);
        gatewayModel.setSoftwareVersion(
                String.format("%d.%d", Constants.MAJOR, Constants.MINOR + 1));
        mTelemetrySendService.updateGateway(mGatewayHid, gatewayModel, new GatewayUpdateListener() {
            @Override
            public void onGatewayUpdated(GatewayResponse response) {
                Log.v(TAG, "onGatewayUpdated");
            }

            @Override
            public void onGatewayUpdateFailed() {
                Log.d(TAG, "onGatewayUpdateFailed");
            }
        });
    }

    private void getNodeList() {
        mTelemetrySendService.getNodesList(new ListResultListener<NodeModel>() {
            @Override
            public void onRequestSuccess(List<NodeModel> list) {
                Log.v(TAG, "getNodesList");
            }

            @Override
            public void onRequestError() {
                Log.e(TAG, "onGetListNodesFailed");
            }
        });
    }

    private void createNewNode() {
        NodeRegistrationModel model = new NodeRegistrationModel();
        model.setEnabled(true);
        model.setDescription("new node");
        model.setName("NewNone");
        model.setNodeTypeHid("5febbcc3c6befd3e28024090ea7dffa0bc318801");
        model.setParentNodeHid("74f248083d0f83e70b030239ac85db6dfb7e8d6b");
        mTelemetrySendService.createNewNode(model, new CommonRequestListener() {
            @Override
            public void onRequestSuccess(CommonResponse response) {
                Log.v(TAG, "createNewNode");
            }

            @Override
            public void onRequestError() {
                Log.e(TAG, "createNewNode");
            }
        });
    }

    void getListNodeTypes() {
        mTelemetrySendService.getListNodeTypes(new ListNodeTypesListener() {
            @Override
            public void onListNodeTypesSuccess(ListResultModel<NodeTypeModel> result) {
                Log.v(TAG, "getListNodeTypes");
            }

            @Override
            public void onListNodeTypesFiled() {
                Log.e(TAG, "getListNodeTypes");
            }
        });
    }
}
