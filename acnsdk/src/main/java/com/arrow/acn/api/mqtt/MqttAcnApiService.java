package com.arrow.acn.api.mqtt;


import android.text.TextUtils;

import com.arrow.acn.api.common.RetrofitHolder;
import com.arrow.acn.api.listeners.ServerCommandsListener;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

/**
 * Created by osminin on 6/22/2016.
 */

public final class MqttAcnApiService extends AbstractMqttAcnApiService {

    private String mHost;
    private String mMqttPrefix;

    public MqttAcnApiService(String host, String mqttPrefix, String gatewayId, ServerCommandsListener listener) {
        super(gatewayId, listener);
        mHost = host;
        mMqttPrefix = mqttPrefix;
    }

    protected MqttConnectOptions getMqttOptions() {
        String userName = mMqttPrefix + ":" + mGatewayId;
        String apiKey = RetrofitHolder.getApiKey();
        apiKey = !TextUtils.isEmpty(apiKey) ? apiKey : mConfigResponse.getKey().getApiKey();
        MqttConnectOptions connOpts = super.getMqttOptions();
        connOpts.setUserName(userName);
        connOpts.setPassword(apiKey.toCharArray());
        connOpts.setCleanSession(false);
        return connOpts;
    }

    @Override
    protected String getPublisherTopic(String deviceType, String externalId) {
        return MESSAGE_TOPIC_PREFIX + mGatewayId;
    }

    protected String getHost() {
        return mHost;
    }

    @Override
    public boolean hasBatchMode() {
        return true;
    }
}
