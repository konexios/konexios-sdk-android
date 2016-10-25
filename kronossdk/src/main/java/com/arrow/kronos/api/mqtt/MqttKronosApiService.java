package com.arrow.kronos.api.mqtt;


import android.text.TextUtils;

import com.arrow.kronos.api.Constants;
import com.arrow.kronos.api.ServerEndpoint;
import com.arrow.kronos.api.common.ApiRequestSigner;
import com.arrow.kronos.api.models.ConfigResponse;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

/**
 * Created by osminin on 6/22/2016.
 */

public final class MqttKronosApiService extends AbstractMqttKronosApiService {

    private String mHost;
    private String mMqttPrefix;

    protected MqttConnectOptions getMqttOptions() {
        String userName = mMqttPrefix + ":" + mGatewayId;
        String apiKey = ApiRequestSigner.getInstance().getApiKey();
        apiKey = !TextUtils.isEmpty(apiKey) ? apiKey : getApiKey();
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

    @Override
    public void setMqttEndpoint(String host, String prefix) {
        mHost = host;
        mMqttPrefix = prefix;
    }

    @Override
    protected void onConfigResponse(ConfigResponse response) {
        super.onConfigResponse(response);
        if (!isConnected()) {
            connectMqtt();
        }
    }
}
