package com.arrow.kronos.sample.device.androidinternal;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.arrow.kronos.api.models.DeviceRegistrationModel;
import com.arrow.kronos.sample.device.DeviceAbstract;
import com.arrow.kronos.sample.device.DeviceType;
import com.arrow.kronos.sample.device.IotParameter;
import com.arrow.kronos.sample.device.TelemetriesNames;
import com.google.android.gms.location.LocationRequest;
import com.google.firebase.crash.FirebaseCrash;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import rx.Notification;
import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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

/**
 * Created by osminin on 6/27/2016.
 */

public final class DeviceAndroid extends DeviceAbstract implements SensorEventListener {
    private final static String TAG = DeviceAndroid.class.getSimpleName();

    private final static String DEVICE_TYPE_NAME = "android";
    private final static String DEVICE_UID_PREFIX = "android-";
    public final static int DEFAULT_INTERVAL = 20000; // 20 secs
    public final static int DEFAULT_FASTEST_INTERVAL = 10000; // 10 sec

    private SensorManager mSensorManager;
    private List<Sensor> mSensors;
    private Subscription mSubscription;
    private Observable<Long> mPollingTimer = Observable
            .interval(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(Schedulers.computation());

    public DeviceAndroid(Context context) {
        super(context);
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensors = new ArrayList<>();
    }

    @Override
    public DeviceType getDeviceType() {
        return DeviceType.AndroidInternal;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        handleSensorChangedEvent(event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public String getDeviceUId() {
        final TelephonyManager tm = (TelephonyManager) mContext.
                getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(mContext.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString().replace("00000000-","").replace("ffffffff-", "");
        return DEVICE_UID_PREFIX + deviceId;
    }

    @Override
    public void enable() {
        List<Sensor> sensors = AndroidSensorUtils.getSensorsList(mSensorManager);
        for (Sensor sensor : sensors) {
            mSensors.add(sensor);
            mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            FirebaseCrash.logcat(Log.INFO, TAG, "enable() sensor: " + sensor.getName());
        }
        initializeLocationService();
        mGoogleApiClient.connect();
        // create location request
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(DEFAULT_INTERVAL);
        locationRequest.setFastestInterval(DEFAULT_FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        checkAndRegisterDevice();

        mSubscription = mPollingTimer.subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                pollingTask();
            }
        });
    }

    @Override
    public void disable() {
        for (Sensor sensor : mSensors) {
            mSensorManager.unregisterListener(this, sensor);
        }
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
            mGoogleApiClient = null;
        }
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public String getDeviceTypeName() {
        return DEVICE_TYPE_NAME;
    }

    private void handleSensorChangedEvent(SensorEvent event) {
        int sensorType = event.sensor.getType();
        String x = "", y = "", z = "";
        if (event.values.length >= 1) {
            x = Float.toString(event.values[0]);
        }
        if (event.values.length >= 2) {
            y = Float.toString(event.values[1]);
        }
        if (event.values.length == 3){
            z = Float.toString(event.values[2]);
        }
        switch (sensorType) {
            case TYPE_ACCELEROMETER:
                putIotParams(new IotParameter(TelemetriesNames.ACCELEROMETER_X, x));
                putIotParams(new IotParameter(TelemetriesNames.ACCELEROMETER_Y, y));
                putIotParams(new IotParameter(TelemetriesNames.ACCELEROMETER_Z, z));
                break;
            case TYPE_AMBIENT_TEMPERATURE:
                putIotParams(new IotParameter(TelemetriesNames.TEMPERATURE, x));
                break;
            case TYPE_GYROSCOPE:
            case TYPE_GYROSCOPE_UNCALIBRATED:
                putIotParams(new IotParameter(TelemetriesNames.GYROSCOPE_X, x));
                putIotParams(new IotParameter(TelemetriesNames.GYROSCOPE_Y, y));
                putIotParams(new IotParameter(TelemetriesNames.GYROSCOPE_Z, z));
                break;
            case TYPE_HEART_RATE:
                putIotParams(new IotParameter(TelemetriesNames.HEART_RATE, x));
                break;
            case TYPE_LIGHT:
                putIotParams(new IotParameter(TelemetriesNames.LIGHT, x));
                break;
            case TYPE_MAGNETIC_FIELD:
            case TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                putIotParams(new IotParameter(TelemetriesNames.MAGNOMETER_X, x));
                putIotParams(new IotParameter(TelemetriesNames.MAGNOMETER_Y, y));
                putIotParams(new IotParameter(TelemetriesNames.MAGNOMETER_Z, z));
                break;
            case TYPE_PRESSURE:
                putIotParams(new IotParameter(TelemetriesNames.BAROMETER, x));
                break;
            case TYPE_RELATIVE_HUMIDITY:
                putIotParams(new IotParameter(TelemetriesNames.HUMIDITY, x));
                break;
            case TYPE_STEP_COUNTER:
                putIotParams(new IotParameter(TelemetriesNames.STEPS, x));
                break;
        }
    }

    @Override
    protected DeviceRegistrationModel getRegisterPayload() {
        DeviceRegistrationModel payload = super.getRegisterPayload();
        //payload.setProperties(mProperties.getPropertiesAsJson());
        //payload.setInfo(mProperties.getInfoAsJson());
        return payload;
    }

}
