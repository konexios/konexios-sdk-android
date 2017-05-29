/*
 * Copyright (c) 2017 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors: Arrow Electronics, Inc.
 */

package com.arrow.acn.api.mqtt;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.arrow.acn.api.AbstractTelemetrySenderService;
import com.arrow.acn.api.common.ErrorUtils;
import com.arrow.acn.api.common.GatewayPayloadSigner;
import com.arrow.acn.api.listeners.CommonRequestListener;
import com.arrow.acn.api.listeners.ConnectionListener;
import com.arrow.acn.api.listeners.ServerCommandsListener;
import com.arrow.acn.api.models.CommonResponse;
import com.arrow.acn.api.models.ConfigResponse;
import com.arrow.acn.api.models.GatewayEventModel;
import com.arrow.acn.api.models.TelemetryModel;
import com.arrow.acn.api.rest.IotConnectAPIService;
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
import java.util.Map;

import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import timber.log.Timber;

/**
 * Created by osminin on 6/17/2016.
 */

@Keep
public abstract class AbstractMqttAcnApiService extends AbstractTelemetrySenderService {
    protected static final String MESSAGE_TOPIC_PREFIX = "krs.tel.gts.";
    private static final String SUBSCRIBE_TOPIC_PREFIX = "krs.cmd.stg.";
    private final static int DEFAULT_CONNECTION_TIMEOUT_SECS = 60;
    private final static int DEFAULT_KEEP_ALIVE_INTERVAL_SECS = 60;
    private static final int MAX_INFLIGHT_COUNT = 10;
    private static final int QOS = 0;
    private final IMqttActionListener mMqttTelemetryCallback = new IMqttActionListener() {
        @Override
        public void onSuccess(@NonNull IMqttToken asyncActionToken) {
            Timber.v("data sent to cloud: " + asyncActionToken.getResponse().toString());
        }

        @Override
        public void onFailure(@NonNull IMqttToken asyncActionToken, Throwable exception) {
            Timber.e(exception);
        }
    };
    protected String mGatewayId;
    protected ConfigResponse mConfigResponse;
    private ServerCommandsListener mServerCommandsListener;
    private IotConnectAPIService mRestService;
    @NonNull
    private Gson mGson = new Gson();
    private final MqttCallback mMqttIncomingMessageListener = new MqttCallback() {
        @Override
        public void connectionLost(Throwable cause) {
            Timber.e("connectionLost");
        }

        @Override
        public void messageArrived(String topic, @NonNull MqttMessage message) throws Exception {
            Timber.v("IMqttMessageListener messageArrived");
            String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
            GatewayEventModel model = mGson.fromJson(payload, GatewayEventModel.class);
            if (verifySignature(model)) {
                mServerCommandsListener.onServerCommand(model);
            } else {
                mRestService.putFailed(model.getHid()).enqueue(restCallback);
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            Timber.v("deliveryComplete");
        }
    };
    private ConnectionListener mExternalConnListener;
    @Nullable
    private MqttAsyncClient mMqttClient;
    private final IMqttActionListener mMqttConnectCallback = new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            Timber.d("MQTT connect onSuccess");
            mExternalConnListener.onConnectionSuccess();
            try {
                String topic = getSubscribeTopic();
                Timber.v("subscribing to topic: " + topic);
                mMqttClient.subscribe(topic, QOS);
            } catch (MqttException e) {
                Timber.e(e);
            }

        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, @NonNull Throwable exception) {
            mExternalConnListener.onConnectionError(ErrorUtils.parseError(exception));
            Timber.e(exception);
            exception.printStackTrace();
        }
    };

    private Callback<CommonResponse> restCallback = new Callback<CommonResponse>() {
        @Override
        public void onResponse(Call<CommonResponse> call, retrofit2.Response<CommonResponse> response) {
            Timber.v("onResponse: code: %s", response.code());
        }

        @Override
        public void onFailure(Call<CommonResponse> call, Throwable t) {
            Timber.e("onFailure: ");
        }
    };

    public AbstractMqttAcnApiService(String gatewayId,
                                     ServerCommandsListener serverCommandsListener,
                                     IotConnectAPIService restService) {
        mServerCommandsListener = serverCommandsListener;
        mGatewayId = gatewayId;
        mRestService = restService;
    }

    public AbstractMqttAcnApiService(String gatewayId,
                                     ConfigResponse configResponse,
                                     ServerCommandsListener listener,
                                     IotConnectAPIService restService) {
        mGatewayId = gatewayId;
        mConfigResponse = configResponse;
        mServerCommandsListener = listener;
        mRestService = restService;
    }

    @Override
    public void connect(ConnectionListener listener) {
        mExternalConnListener = listener;
        connectMqtt();
    }

    @Override
    public void disconnect() {
        Timber.v("disconnect");
        try {
            mMqttClient.disconnect();
        } catch (MqttException e) {
            Timber.e(e);
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
                Timber.e(e);
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
            Timber.v("connecting to host: " + getHost() + ", clientId: " + clientId);
            mMqttClient = new MqttAsyncClient(getHost(), clientId, null);
            mMqttClient.setCallback(mMqttIncomingMessageListener);
            mMqttClient.connect(connOpts, null, mMqttConnectCallback);
        } catch (MqttException e) {
            mExternalConnListener.onConnectionError(ErrorUtils.parseError(e));
            Timber.e(e);
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

    private boolean verifySignature(GatewayEventModel model) {
        boolean res;
        if (TextUtils.isEmpty(model.getSignature())) {
            res = true;
        } else {
            GatewayPayloadSigner signer = GatewayPayloadSigner.create(mConfigResponse.getKey().getSecretKey())
                    .withApiKey(mConfigResponse.getKey().getApiKey())
                    .withEncrypted(model.isEncrypted())
                    .withName(model.getName())
                    .withHid(model.getHid());
            for(Map.Entry<String, String> entry : model.getParameters().entrySet()) {
                signer.withParameter(entry.getKey(), entry.getValue());
            }
            String signature = signer.signV1();
            res = signature.equals(model.getSignature());
        }
        return res;
    }
}
