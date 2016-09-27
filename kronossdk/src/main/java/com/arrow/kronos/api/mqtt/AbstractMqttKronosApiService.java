package com.arrow.kronos.api.mqtt;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.arrow.kronos.api.AbstractKronosApiService;
import com.arrow.kronos.api.Constants;
import com.arrow.kronos.api.models.ConfigResponse;
import com.arrow.kronos.api.models.GatewayEventModel;
import com.google.firebase.crash.FirebaseCrash;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created by osminin on 6/17/2016.
 */

public abstract class AbstractMqttKronosApiService extends AbstractKronosApiService implements AbstractKronosApiService.GatewayRegisterListener {
    private static final String TAG = AbstractMqttKronosApiService.class.getName();
    protected static final String MESSAGE_TOPIC_PREFIX = "krs.tel.gts.";
    private static final String SUBSCRIBE_TOPIC_PREFIX = "krs.cmd.stg.";
    private final static int DEFAULT_CONNECTION_TIMEOUT_SECS = 60;
    private final static int DEFAULT_KEEP_ALIVE_INTERVAL_SECS = 60;
    private static final int QOS = 0;
    private final IMqttActionListener mMqttTelemetryCallback = new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            FirebaseCrash.logcat(Log.VERBOSE, TAG, "data sent to cloud: " + asyncActionToken.getResponse().toString());
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            FirebaseCrash.logcat(Log.ERROR, TAG, "data sent to cloud: " + asyncActionToken.getException());
            FirebaseCrash.report(exception);
        }
    };
    private MqttAsyncClient mMqttClient;
    protected String mGatewayHid;
    private final IMqttActionListener mMqttConnectCallback = new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            FirebaseCrash.logcat(Log.DEBUG, TAG, "MQTT connect onSuccess");
            try {
                String topic = SUBSCRIBE_TOPIC_PREFIX + mGatewayHid;
                mMqttClient.subscribe(topic, QOS);
            } catch (MqttException e) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "subscribe");
                FirebaseCrash.report(e);
            }

        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            FirebaseCrash.logcat(Log.ERROR, TAG, "MQTT connect onSuccess");
            FirebaseCrash.report(exception);
            exception.printStackTrace();
        }
    };
    private final MqttCallback mMqttIncomingMessageListener = new MqttCallback() {
        @Override
        public void connectionLost(Throwable cause) {
            FirebaseCrash.logcat(Log.ERROR, TAG, "connectionLost");
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            FirebaseCrash.logcat(Log.VERBOSE, TAG, "IMqttMessageListener messageArrived");
            String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
            GatewayEventModel model = getGson().fromJson(payload, GatewayEventModel.class);
            mServerCommandsListener.onServerCommand(model);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            FirebaseCrash.logcat(Log.VERBOSE, TAG, "deliveryComplete");
        }
    };

    @Override
    public void connect() {
        FirebaseCrash.logcat(Log.VERBOSE, TAG, "connect");
        registerGateway(this);
    }

    @Override
    public void disconnect() {
        FirebaseCrash.logcat(Log.VERBOSE, TAG, "disconnect");
        try {
            mMqttClient.disconnect();
        } catch (MqttException e) {
            FirebaseCrash.logcat(Log.ERROR, TAG, "disconnect");
            FirebaseCrash.report(e);
        }
    }

    @Override
    public void sendSingleTelemetry(Bundle bundle) {
        String json = bundle.getString(Constants.EXTRA_DATA_LABEL_TELEMETRY);
        MqttMessage message = new MqttMessage(json.getBytes());
        String topic = getPublisherTopic();
        sendMqttMessage(topic, message);
    }

    @Override
    public void sendBatchTelemetry(List<Bundle> telemetry) {
        String payload = formatBatchPayload(telemetry);
        String topic = "krs.tel.bat.gts." + mGatewayHid;
        MqttMessage message = new MqttMessage(payload.toString().getBytes());
        sendMqttMessage(topic, message);
    }

    private void sendMqttMessage(String topic, MqttMessage message) {
        if (mMqttClient != null && mMqttClient.isConnected()) {
            try {
                mMqttClient.publish(topic, message).setActionCallback(mMqttTelemetryCallback);
            } catch (MqttException e) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "sendMqttMessage");
                FirebaseCrash.report(e);
            }
        }
    }

    @Override
    public void onGatewayRegistered(String gatewayHid) {
        FirebaseCrash.logcat(Log.VERBOSE, TAG, "onGatewayRegistered");
        mGatewayHid = gatewayHid;
        connectMqtt();
    }

    protected final void connectMqtt() {
        MqttConnectOptions connOpts = getMqttOptions();
        try {
            String clientId = getClientId();
            if (mMqttClient != null) {
                mMqttClient.disconnect();
            }
            mMqttClient = new MqttAsyncClient(getHost(), clientId, null);
            mMqttClient.setCallback(mMqttIncomingMessageListener);
            mMqttClient.connect(connOpts, null, mMqttConnectCallback);
        } catch (MqttException e) {
            FirebaseCrash.logcat(Log.ERROR, TAG, "connectMqtt");
            FirebaseCrash.report(e);
        }
    }

    protected MqttConnectOptions getMqttOptions() {
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setConnectionTimeout(DEFAULT_CONNECTION_TIMEOUT_SECS);
        connOpts.setKeepAliveInterval(DEFAULT_KEEP_ALIVE_INTERVAL_SECS);
        return connOpts;
    }

    protected String getClientId() {
        return MqttClient.generateClientId();
    }

    protected abstract String getPublisherTopic();

    protected abstract String getHost();

    @Override
    public void onGatewayRegistered(ConfigResponse aws) {
        //nothing to do here
    }
}
