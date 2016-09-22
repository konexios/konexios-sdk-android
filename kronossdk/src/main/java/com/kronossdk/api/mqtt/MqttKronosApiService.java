package com.kronossdk.api.mqtt;

import android.content.Context;

import com.kronossdk.api.Constants;
import com.kronossdk.api.ServerEndpoint;
import com.kronossdk.api.common.ApiRequestSigner;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

/**
 * Created by osminin on 6/22/2016.
 */

public final class MqttKronosApiService extends AbstractMqttKronosApiService {

    private String mHost;
    private String mMqttPrefix;

    @Override
    public void setRestEndpoint(ServerEndpoint endpoint) {
        super.setRestEndpoint(endpoint);
        mHost = endpoint == ServerEndpoint.DEMO ? Constants.MQTT_CONNECT_URL_DEV_DEMO :
                Constants.MQTT_CONNECT_URL_DEV;
        mMqttPrefix = endpoint == ServerEndpoint.DEMO ? Constants.MQTT_CLIENT_PREFIX_DEMO :
                Constants.MQTT_CLIENT_PREFIX_DEV;
    }

    protected MqttConnectOptions getMqttOptions() {
        String userName = mMqttPrefix + ":" + mGatewayHid;
        String apiKey = ApiRequestSigner.getInstance().getApiKey() != null ? ApiRequestSigner.getInstance().getApiKey() :
                Constants.DEFAULT_API_KEY;
        MqttConnectOptions connOpts = super.getMqttOptions();
        connOpts.setUserName(userName);
        connOpts.setPassword(apiKey.toCharArray());
        connOpts.setCleanSession(false);
        return connOpts;
    }

    @Override
    protected String getPublisherTopic() {
        return MESSAGE_TOPIC_PREFIX + mGatewayHid;
    }

    protected String getHost() {
        return mHost;
    }

    @Override
    public boolean hasBatchMode() {
        return true;
    }
}
