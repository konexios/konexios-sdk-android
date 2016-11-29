package com.arrow.kronos.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arrow.kronos.api.KronosApiService;
import com.arrow.kronos.api.KronosApiServiceFactory;
import com.arrow.kronos.api.listeners.GatewayRegisterListener;
import com.arrow.kronos.api.listeners.GetGatewayConfigListener;
import com.arrow.kronos.api.listeners.RegisterDeviceListener;
import com.arrow.kronos.api.models.AccountResponse;
import com.arrow.kronos.api.models.ApiError;
import com.arrow.kronos.api.models.ConfigResponse;
import com.arrow.kronos.api.models.DeviceRegistrationModel;
import com.arrow.kronos.api.models.GatewayModel;
import com.arrow.kronos.api.models.GatewayResponse;
import com.arrow.kronos.api.models.GatewayType;
import com.arrow.kronos.api.models.TelemetryModel;
import com.arrow.kronos.sample.device.TelemetriesNames;
import com.arrow.kronos.sample.device.androidinternal.AndroidSensorUtils;
import com.arrow.kronos.sample.device.androidinternal.DeviceAndroid;
import com.google.firebase.crash.FirebaseCrash;

import java.util.HashSet;
import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.hardware.Sensor.TYPE_ACCELEROMETER;
import static android.hardware.Sensor.TYPE_AMBIENT_TEMPERATURE;
import static android.hardware.Sensor.TYPE_GYROSCOPE;
import static android.hardware.Sensor.TYPE_GYROSCOPE_UNCALIBRATED;
import static android.hardware.Sensor.TYPE_HEART_RATE;
import static android.hardware.Sensor.TYPE_LIGHT;
import static android.hardware.Sensor.TYPE_MAGNETIC_FIELD;
import static android.hardware.Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED;
import static android.hardware.Sensor.TYPE_PRESSURE;
import static android.hardware.Sensor.TYPE_RELATIVE_HUMIDITY;
import static android.hardware.Sensor.TYPE_STEP_COUNTER;


public class MainActivity extends AppCompatActivity implements InternalSensorsView, TelemetrySender {
    public static final String MQTT_CONNECT_URL_DEV = "tcp://pegasusqueue01-dev.cloudapp.net:46953";
    public static final String MQTT_CLIENT_PREFIX_DEV = "/themis.dev";
    public final static String KEY_GATEWAY_ID = "gateway-id";
    public final static String SOFTWARE_NAME = "JMyIotGateway";
    public final static int MAJOR = 0;
    public final static int MINOR = 16;
    protected static final String DEFAULT_ZERO_LABEL = "0";
    private final static String TAG = MainActivity.class.getSimpleName();
    private static final int TELEMETRY_VALUE_MAX_LENGTH = 11;
    @BindView(R.id.android_internal_details_timer)
    TextView mTimer;
    @BindView(R.id.android_internal_details_device_id)
    TextView mId;
    @BindView((R.id.device_details_switch))
    SwitchCompat mDeviceSwitch;
    @BindView(R.id.android_internal_telemetry_layout)
    ViewGroup mTelemetryContainer;
    @BindString(R.string.device_details_degree_sign)
    String mDegreeSign;
    @BindString(R.string.device_details_meters_per_square_second)
    String mMetersPerSquareSecond;
    @BindString(R.string.device_details_steps)
    String mStepsUnit;
    private KronosApiService mTelemetrySendService;
    private AccountResponse mAccountResponse;
    private String mGatewayHid;
    private DeviceAndroid mDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        if (intent != null) {
            mAccountResponse = intent.getParcelableExtra(LoginActivity.ACCOUNT_RESPONSE_EXTRA);
        }

        //Once instance of KronosApiService is created it could be got by getKronosApiService() call
        mTelemetrySendService = KronosApiServiceFactory.getKronosApiService();
        mTelemetrySendService.initialize(new Handler());

        registerGateway();

        //ui initialization
        addTelemetryFields((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        setInitialStates();

        //device creation
        mDevice = new DeviceAndroid(this);
        mDevice.setView(this);
        mDevice.setUserHid(mAccountResponse.getHid());
        mDevice.setSender(this);
    }

    protected void setInitialStates() {
        mId.setText("");
        mTimer.setText("00:00:00");

        for (int i = 0; i < mTelemetryContainer.getChildCount(); ++i) {
            View container = mTelemetryContainer.getChildAt(i);
            TextView val1 = ButterKnife.findById(container, R.id.telemetry_val_1);
            TextView val2 = ButterKnife.findById(container, R.id.telemetry_val_2);
            TextView val3 = ButterKnife.findById(container, R.id.telemetry_val_3);
            val1.setText("0");
            if (val2 != null && val3 != null) {
                val2.setText("0");
                val3.setText("0");
            }
        }
        mDeviceSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDeviceSwitch.isChecked()) {
                    mDevice.enable();
                } else {
                    mDevice.disable();
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTelemetrySendService != null && mTelemetrySendService.isConnected()) {
            mTelemetrySendService.disconnect();
        }
    }

    private void registerGateway() {
        mTelemetrySendService = KronosApiServiceFactory.getKronosApiService();
        String uid = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        FirebaseCrash.logcat(Log.DEBUG, TAG, "registerGateway() UID: " + uid);

        String name = String.format("%s %s", Build.MANUFACTURER, Build.MODEL);
        String osName = String.format("Android %s", Build.VERSION.RELEASE);
        String swName = SOFTWARE_NAME;
        String userHid = mAccountResponse.getHid();

        GatewayModel gatewayModel = new GatewayModel();
        gatewayModel.setName(name);
        gatewayModel.setOsName(osName);
        gatewayModel.setSoftwareName(swName);
        gatewayModel.setUid(uid);
        gatewayModel.setType(GatewayType.Mobile);
        gatewayModel.setUserHid(userHid);
        gatewayModel.setSoftwareVersion(
                String.format("%d.%d", MAJOR, MINOR));

        mTelemetrySendService.registerGateway(gatewayModel, new GatewayRegisterListener() {
            @Override
            public void onGatewayRegistered(GatewayResponse response) {
                mGatewayHid = response.getHid();
                mDevice.setGatewayHid(mGatewayHid);
                getGatewayConfig();
            }

            @Override
            public void onGatewayRegisterFailed(ApiError error) {
                Log.e(TAG, "registerGateway failed: code = " + error.getStatus() +
                        ", message: " + error.getMessage());
            }
        });
    }

    private void getGatewayConfig() {
        mTelemetrySendService.getGatewayConfig(mGatewayHid, new GetGatewayConfigListener() {
            @Override
            public void onGatewayConfigReceived(ConfigResponse response) {
                mTelemetrySendService.setMqttEndpoint(MQTT_CONNECT_URL_DEV, MQTT_CLIENT_PREFIX_DEV);
                mTelemetrySendService.connect();
                //allow user to start collecting telemetry from internal sensors
                mDeviceSwitch.setEnabled(true);
            }

            @Override
            public void onGatewayConfigFailed(ApiError error) {
                Log.e(TAG, "getGatewayConfig failed: code = " + error.getStatus() +
                        ", message: " + error.getMessage());
            }
        });
    }

    @Override
    public void setDeviceId(String deviceId) {
        mId.setText(" " + deviceId);
    }

    @Override
    public void update(Map<String, String> parametersMap) {
        for (int i = 0; i < mTelemetryContainer.getChildCount(); ++i) {
            View container = mTelemetryContainer.getChildAt(i);
            int sensorType = (int) container.getTag();
            TextView val1 = ButterKnife.findById(container, R.id.telemetry_val_1);
            TextView val2 = ButterKnife.findById(container, R.id.telemetry_val_2);
            TextView val3 = ButterKnife.findById(container, R.id.telemetry_val_3);
            switch (sensorType) {
                case TYPE_ACCELEROMETER:
                    formatAndSetTextWithDefault(parametersMap.get(TelemetriesNames.ACCELEROMETER_X), mMetersPerSquareSecond, DEFAULT_ZERO_LABEL, val1);
                    formatAndSetTextWithDefault(parametersMap.get(TelemetriesNames.ACCELEROMETER_Y), mMetersPerSquareSecond, DEFAULT_ZERO_LABEL, val2);
                    formatAndSetTextWithDefault(parametersMap.get(TelemetriesNames.ACCELEROMETER_Z), mMetersPerSquareSecond, DEFAULT_ZERO_LABEL, val3);
                    break;
                case TYPE_AMBIENT_TEMPERATURE:
                    formatAndSetTextWithDefault(parametersMap.get(TelemetriesNames.TEMPERATURE), mDegreeSign, DEFAULT_ZERO_LABEL, val1);
                    break;
                case TYPE_GYROSCOPE:
                case TYPE_GYROSCOPE_UNCALIBRATED:
                    formatAndSetTextWithDefault(parametersMap.get(TelemetriesNames.GYROSCOPE_X), "", DEFAULT_ZERO_LABEL, val1);
                    formatAndSetTextWithDefault(parametersMap.get(TelemetriesNames.GYROSCOPE_Y), "", DEFAULT_ZERO_LABEL, val2);
                    formatAndSetTextWithDefault(parametersMap.get(TelemetriesNames.GYROSCOPE_Z), "", DEFAULT_ZERO_LABEL, val3);
                    break;
                case TYPE_HEART_RATE:
                    formatAndSetTextWithDefault(parametersMap.get(TelemetriesNames.HEART_RATE), "", DEFAULT_ZERO_LABEL, val1);
                    break;
                case TYPE_LIGHT:
                    formatAndSetTextWithDefault(parametersMap.get(TelemetriesNames.LIGHT), "", DEFAULT_ZERO_LABEL, val1);
                    break;
                case TYPE_MAGNETIC_FIELD:
                case TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                    formatAndSetTextWithDefault(parametersMap.get(TelemetriesNames.MAGNOMETER_X), "", DEFAULT_ZERO_LABEL, val1);
                    formatAndSetTextWithDefault(parametersMap.get(TelemetriesNames.MAGNOMETER_Y), "", DEFAULT_ZERO_LABEL, val2);
                    formatAndSetTextWithDefault(parametersMap.get(TelemetriesNames.MAGNOMETER_Z), "", DEFAULT_ZERO_LABEL, val3);
                    break;
                case TYPE_PRESSURE:
                    formatAndSetTextWithDefault(parametersMap.get(TelemetriesNames.BAROMETER), "", DEFAULT_ZERO_LABEL, val1);
                    break;
                case TYPE_RELATIVE_HUMIDITY:
                    formatAndSetTextWithDefault(parametersMap.get(TelemetriesNames.HUMIDITY), "", DEFAULT_ZERO_LABEL, val1);
                    break;
                case TYPE_STEP_COUNTER:
                    formatAndSetTextWithDefault(parametersMap.get(TelemetriesNames.STEPS), mStepsUnit, DEFAULT_ZERO_LABEL, val1);
                    break;
            }
        }
    }

    @Override
    public void sendTelemetry(TelemetryModel model) {
        mTelemetrySendService.sendSingleTelemetry(model);
    }

    @Override
    public void registerDevice(DeviceRegistrationModel model, RegisterDeviceListener listener) {
        mTelemetrySendService.registerDevice(model, listener);
    }

    private void addTelemetryFields(LayoutInflater inflater) {
        Integer[] availableSensors = AndroidSensorUtils.getAvailableSensors(this);
        mTelemetryContainer.removeAllViews();
        HashSet<Integer> sensorTypes = new HashSet<>();
        for (int sensorType : availableSensors) {
            View container;
            int layoutResId = 0;
            int labelResId = 0;
            switch (sensorType) {
                case TYPE_ACCELEROMETER:
                    layoutResId = R.layout.layout_telemetry_3_values;
                    labelResId = R.string.device_details_accelerometer;
                    break;
                case TYPE_AMBIENT_TEMPERATURE:
                    layoutResId = R.layout.layout_telemetry_1_value;
                    labelResId = R.string.device_details_temperature;
                    break;
                case TYPE_GYROSCOPE:
                case TYPE_GYROSCOPE_UNCALIBRATED:
                    layoutResId = R.layout.layout_telemetry_3_values;
                    labelResId = R.string.device_details_gyroscope;
                    break;
                case TYPE_HEART_RATE:
                    layoutResId = R.layout.layout_telemetry_1_value;
                    labelResId = R.string.device_details_heart_rate;
                    break;
                case TYPE_LIGHT:
                    layoutResId = R.layout.layout_telemetry_1_value;
                    labelResId = R.string.telemetry_param_light;
                    break;
                case TYPE_MAGNETIC_FIELD:
                case TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                    layoutResId = R.layout.layout_telemetry_3_values;
                    labelResId = R.string.device_details_magnetometer;
                    break;
                case TYPE_PRESSURE:
                    layoutResId = R.layout.layout_telemetry_1_value;
                    labelResId = R.string.device_details_pressure;
                    break;
                case TYPE_RELATIVE_HUMIDITY:
                    layoutResId = R.layout.layout_telemetry_1_value;
                    labelResId = R.string.telemetry_param_humidity;
                    break;
                case TYPE_STEP_COUNTER:
                    layoutResId = R.layout.layout_telemetry_1_value;
                    labelResId = R.string.device_details_total_steps;
                    break;
            }
            if ((sensorType == TYPE_GYROSCOPE_UNCALIBRATED && mTelemetryContainer.findViewWithTag(TYPE_GYROSCOPE) != null) ||
                    (sensorType == TYPE_MAGNETIC_FIELD_UNCALIBRATED && mTelemetryContainer.findViewWithTag(TYPE_MAGNETIC_FIELD) != null)) {
                continue;
            }
            if (sensorTypes.add(sensorType)) {
                container = inflater.inflate(layoutResId, null);
                TextView label = ButterKnife.findById(container, R.id.telemetry_label);
                label.setText(labelResId);
                container.setTag(sensorType);
                mTelemetryContainer.addView(container);
            }
        }
    }

    private void formatAndSetTextWithDefault(String value, String unit, String defaultVal, TextView textView) {
        if (textView != null) {
            if (!TextUtils.isEmpty(value)) {
                value = value.substring(0, Math.min(TELEMETRY_VALUE_MAX_LENGTH, value.length()));
                textView.setText(String.format("%s %s", value, unit));
            } else {
                textView.setText(String.format("%s %s", defaultVal, unit));
            }
        }
    }
}
