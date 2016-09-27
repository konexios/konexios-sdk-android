package com.arrow.kronos.sample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.JsonObject;
import com.arrow.kronos.api.Constants;
import com.arrow.kronos.api.KronosApiService;
import com.arrow.kronos.api.KronosApiServiceFactory;
import com.arrow.kronos.api.listeners.RegisterDeviceListener;
import com.arrow.kronos.api.models.AccountResponse;
import com.arrow.kronos.api.models.GatewayResponse;
import com.arrow.kronos.api.models.RegisterDeviceRequest;

import static com.arrow.kronos.api.Constants.Preference.KEY_GATEWAY_ID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = MainActivity.class.getSimpleName();

    private KronosApiService mTelemetrySendService;

    private AccountResponse mAccountResponse;
    private GatewayResponse mGatewayResponse;

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

        //Once instance of KronosApiService is created it could be got by getKronosApiService() call
        mTelemetrySendService = KronosApiServiceFactory.getKronosApiService();
        //initialize service with a context and bind it with activity's lifecycle
        mTelemetrySendService.initialize(this);
        //register new gateway and initiate persistent connection (it makes sense only in case when some of {ConnectionType.MQTT, ConnectionType.AWS,
        // ConnectionType.IBM} is chosen)
        mTelemetrySendService.connect();
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
        }
    }

    private void registerDevice() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String gatewayHid = sp.getString(KEY_GATEWAY_ID, "");
        RegisterDeviceRequest payload = new RegisterDeviceRequest();
        //some random values to show registering process
        payload.setUid("android-7cfe-e895-1988-cfc60033c5565");
        payload.setName("AndroidInternal");
        payload.setGatewayHid(gatewayHid);
        payload.setUserHid(mAccountResponse.getHid());
        payload.setType("android");
        mTelemetrySendService.registerDevice(payload, new RegisterDeviceListener() {
            @Override
            public void onDeviceRegistered(GatewayResponse response) {
                mGatewayResponse = response;
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
        jsonObject.addProperty("_|deviceHid", mGatewayResponse.getHid());
        jsonObject.addProperty("f|light", "84.0");
        bundle.putString(Constants.EXTRA_DATA_LABEL_TELEMETRY, jsonObject.toString());
        mTelemetrySendService.sendSingleTelemetry(bundle);
    }
}
