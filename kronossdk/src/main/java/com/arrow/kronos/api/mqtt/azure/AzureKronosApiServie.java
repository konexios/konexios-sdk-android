package com.arrow.kronos.api.mqtt.azure;

import android.util.Log;

import com.arrow.kronos.api.TelemetrySenderInterface;
import com.arrow.kronos.api.models.TelemetryModel;
import com.google.firebase.crash.FirebaseCrash;
import com.microsoft.azure.sdk.iot.device.DeviceClient;
import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;
import com.microsoft.azure.sdk.iot.device.IotHubEventCallback;
import com.microsoft.azure.sdk.iot.device.IotHubMessageResult;
import com.microsoft.azure.sdk.iot.device.IotHubStatusCode;
import com.microsoft.azure.sdk.iot.device.Message;

import java.io.IOException;
import java.util.List;

import static com.microsoft.azure.sdk.iot.device.DeviceClient.HOSTNAME_ATTRIBUTE;
import static com.microsoft.azure.sdk.iot.device.DeviceClient.SHARED_ACCESS_KEY_ATTRIBUTE;

/**
 * Created by osminin on 25.01.2017.
 */

public final class AzureKronosApiServie implements TelemetrySenderInterface {
    private static final String TAG = AzureKronosApiServie.class.getName();

    private DeviceClient mClient;
    private final String mConnectionString;

    public AzureKronosApiServie(String accessKey, String host) {
        mConnectionString = HOSTNAME_ATTRIBUTE + host + ";"
                + SHARED_ACCESS_KEY_ATTRIBUTE + accessKey;
    }

    @Override
    public void connect() {
        if (mClient == null) {
            IotHubClientProtocol protocol = IotHubClientProtocol.MQTT;
            try {
                mClient = new DeviceClient(mConnectionString, protocol);
                mClient.open();
                MessageCallbackMqtt callback = new MessageCallbackMqtt();
                mClient.setMessageCallback(callback, null);
            } catch (Exception e) {
                FirebaseCrash.report(e);
                FirebaseCrash.logcat(Log.ERROR, TAG, e.toString());
            }
        }
    }

    @Override
    public void disconnect() {
        if (mClient != null) {
            try {
                mClient.close();
            } catch (IOException e) {
                FirebaseCrash.report(e);
                FirebaseCrash.logcat(Log.ERROR, TAG, e.toString());
            }
            mClient = null;
        }
    }

    @Override
    public void sendSingleTelemetry(TelemetryModel telemetry) {
        String json = telemetry.getTelemetry();
        Message msg = new Message(json);
        EventCallback evtCallback = new EventCallback();
        mClient.sendEventAsync(msg, evtCallback, msg);
    }

    @Override
    public void sendBatchTelemetry(List<TelemetryModel> telemetry) {

    }

    @Override
    public boolean hasBatchMode() {
        return false;
    }

    @Override
    public boolean isConnected() {
        return mClient != null;
    }

    private static class MessageCallbackMqtt implements com.microsoft.azure.sdk.iot.device.MessageCallback
    {
        public IotHubMessageResult execute(Message msg, Object context)
        {
            FirebaseCrash.logcat(Log.VERBOSE, TAG, "data sent to cloud: " + msg.getMessageId());
            return IotHubMessageResult.COMPLETE;
        }
    }

    private static class EventCallback implements IotHubEventCallback {
        public void execute(IotHubStatusCode status, Object context){
            Message msg = (Message) context;
            FirebaseCrash.logcat(Log.VERBOSE, TAG, "IoT Hub responded to message: "
                    + msg.getMessageId() + " with status " + status.name());
        }
    }
}
