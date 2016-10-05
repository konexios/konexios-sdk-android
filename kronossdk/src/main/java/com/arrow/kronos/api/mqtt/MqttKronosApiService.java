package com.arrow.kronos.api.mqtt;


import com.arrow.kronos.api.Constants;
import com.arrow.kronos.api.ServerEndpoint;
import com.arrow.kronos.api.common.ApiRequestSigner;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

/**
 * Created by osminin on 6/22/2016.
 */

public final class MqttKronosApiService extends AbstractMqttKronosApiService {

    private String mHost;
    private String mMqttPrefix;

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

    @Override
    public void setMqttEndpoint(String host, String prefix) {
        mHost = host;
        mMqttPrefix = prefix;
    }
}
