package com.arrow.acn.api.mqtt;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.arrow.acn.api.AbstractTelemetrySenderService;
import com.arrow.acn.api.common.ErrorUtils;
import com.arrow.acn.api.listeners.ConnectionListener;
import com.arrow.acn.api.listeners.ServerCommandsListener;
import com.arrow.acn.api.models.ConfigResponse;
import com.arrow.acn.api.models.GatewayEventModel;
import com.arrow.acn.api.models.TelemetryModel;
import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;

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

public abstract class AbstractMqttAcnApiService extends AbstractTelemetrySenderService {
    protected static final String MESSAGE_TOPIC_PREFIX = "krs.tel.gts.";
    private static final String TAG = AbstractMqttAcnApiService.class.getName();
    private static final String SUBSCRIBE_TOPIC_PREFIX = "krs.cmd.stg.";
    private final static int DEFAULT_CONNECTION_TIMEOUT_SECS = 60;
    private final static int DEFAULT_KEEP_ALIVE_INTERVAL_SECS = 60;
    private static final int MAX_INFLIGHT_COUNT = 10;
    private static final int QOS = 0;
    private final IMqttActionListener mMqttTelemetryCallback = new IMqttActionListener() {
        @Override
        public void onSuccess(@NonNull IMqttToken asyncActionToken) {
            FirebaseCrash.logcat(Log.VERBOSE, TAG, "data sent to cloud: " + asyncActionToken.getResponse().toString());
        }

        @Override
        public void onFailure(@NonNull IMqttToken asyncActionToken, Throwable exception) {
            FirebaseCrash.logcat(Log.ERROR, TAG, "data sent to cloud: " + asyncActionToken.getException());
            FirebaseCrash.report(exception);
        }
    };
    protected String mGatewayId;
    private ServerCommandsListener mServerCommandsListener;
    protected ConfigResponse mConfigResponse;
    @NonNull
    private Gson mGson = new Gson();
    private ConnectionListener mExternalConnListener;
    private final MqttCallback mMqttIncomingMessageListener = new MqttCallback() {
        @Override
        public void connectionLost(Throwable cause) {
            FirebaseCrash.logcat(Log.ERROR, TAG, "connectionLost");
        }

        @Override
        public void messageArrived(String topic, @NonNull MqttMessage message) throws Exception {
            FirebaseCrash.logcat(Log.VERBOSE, TAG, "IMqttMessageListener messageArrived");
            String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
            GatewayEventModel model = mGson.fromJson(payload, GatewayEventModel.class);
            mServerCommandsListener.onServerCommand(model);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            FirebaseCrash.logcat(Log.VERBOSE, TAG, "deliveryComplete");
        }
    };
    @Nullable
    private MqttAsyncClient mMqttClient;
    private final IMqttActionListener mMqttConnectCallback = new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            FirebaseCrash.logcat(Log.DEBUG, TAG, "MQTT connect onSuccess");
            mExternalConnListener.onConnectionSuccess();
            try {
                String topic = getSubscribeTopic();
                FirebaseCrash.logcat(Log.VERBOSE, TAG, "subscribing to topic: " + topic);
                mMqttClient.subscribe(topic, QOS);
            } catch (MqttException e) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "subscribe");
                FirebaseCrash.report(e);
            }

        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, @NonNull Throwable exception) {
            FirebaseCrash.logcat(Log.ERROR, TAG, "MQTT connect failure");
            mExternalConnListener.onConnectionError(ErrorUtils.parseError(exception));
            FirebaseCrash.report(exception);
            exception.printStackTrace();
        }
    };

    public AbstractMqttAcnApiService(String gatewayId, ServerCommandsListener serverCommandsListener) {
        mServerCommandsListener = serverCommandsListener;
        mGatewayId = gatewayId;
    }

    public AbstractMqttAcnApiService(String gatewayId, ConfigResponse configResponse) {
        mGatewayId = gatewayId;
        mConfigResponse = configResponse;
    }

    public AbstractMqttAcnApiService(String gatewayId) {
        mGatewayId = gatewayId;
    }

    @Override
    public void connect(ConnectionListener listener) {
        mExternalConnListener = listener;
        connectMqtt();
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
    public void sendSingleTelemetry(@NonNull TelemetryModel telemetry) {
        String json = telemetry.getTelemetry();
        MqttMessage message = new MqttMessage(json.getBytes());
        String topic = getPublisherTopic(telemetry.getDeviceType(), telemetry.getDeviceExternalId());
        sendMqttMessage(topic, message);
    }

    @Override
    public void sendBatchTelemetry(List<TelemetryModel> telemetry) {
        String payload = formatBatchPayload(telemetry);
        String topic = "krs.tel.bat.gts." + mGatewayId;
        MqttMessage message = new MqttMessage(payload.toString().getBytes());
        sendMqttMessage(topic, message);
    }

    private void sendMqttMessage(String topic, MqttMessage message) {
        if (mMqttClient != null && mMqttClient.isConnected()
                && mMqttClient.getPendingDeliveryTokens().length <= MAX_INFLIGHT_COUNT) {
            try {
                mMqttClient.publish(topic, message).setActionCallback(mMqttTelemetryCallback);
            } catch (MqttException e) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "sendMqttMessage");
                FirebaseCrash.report(e);
            }
        }
    }

    protected void connectMqtt() {
        MqttConnectOptions connOpts = getMqttOptions();
        try {
            String clientId = getClientId();
            if (mMqttClient != null) {
                mMqttClient.disconnect();
            }
            FirebaseCrash.logcat(Log.VERBOSE, TAG, "connecting to host: " + getHost() + ", clientId: " + clientId);
            mMqttClient = new MqttAsyncClient(getHost(), clientId, null);
            mMqttClient.setCallback(mMqttIncomingMessageListener);
            mMqttClient.connect(connOpts, null, mMqttConnectCallback);
        } catch (MqttException e) {
            FirebaseCrash.logcat(Log.ERROR, TAG, "connectMqtt");
            mExternalConnListener.onConnectionError(ErrorUtils.parseError(e));
            FirebaseCrash.report(e);
        }
    }

    protected MqttConnectOptions getMqttOptions() {
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setConnectionTimeout(DEFAULT_CONNECTION_TIMEOUT_SECS);
        connOpts.setKeepAliveInterval(DEFAULT_KEEP_ALIVE_INTERVAL_SECS);
        connOpts.setAutomaticReconnect(true);
        return connOpts;
    }

    protected String getClientId() {
        return MqttClient.generateClientId();
    }

    @NonNull
    protected String getSubscribeTopic() {
        return SUBSCRIBE_TOPIC_PREFIX + mGatewayId;
    }

    protected abstract String getPublisherTopic(String deviceType, String externalId);

    protected abstract String getHost();

    @Override
    public boolean isConnected() {
        return mMqttClient != null && mMqttClient.isConnected();
    }
}
