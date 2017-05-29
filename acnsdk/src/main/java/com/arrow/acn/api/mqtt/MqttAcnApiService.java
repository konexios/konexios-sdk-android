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
import android.text.TextUtils;

import com.arrow.acn.api.common.RetrofitHolder;
import com.arrow.acn.api.listeners.ServerCommandsListener;
import com.arrow.acn.api.rest.IotConnectAPIService;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

/**
 * Created by osminin on 6/22/2016.
 */

@Keep
public final class MqttAcnApiService extends AbstractMqttAcnApiService {

    private final String mHost;
    private final String mMqttPrefix;
    private final RetrofitHolder mRetrofitHolder;

    public MqttAcnApiService(String host, String mqttPrefix, String gatewayId,
                             RetrofitHolder retrofitHolder, ServerCommandsListener listener,
                             IotConnectAPIService restService) {
        super(gatewayId, listener, restService);
        mHost = host;
        mMqttPrefix = mqttPrefix;
        mRetrofitHolder = retrofitHolder;
    }

    protected MqttConnectOptions getMqttOptions() {
        String userName = mMqttPrefix + ":" + mGatewayId;
        String apiKey = mRetrofitHolder.getApiKey();
        apiKey = !TextUtils.isEmpty(apiKey) ? apiKey : mConfigResponse.getKey().getApiKey();
        MqttConnectOptions connOpts = super.getMqttOptions();
        connOpts.setUserName(userName);
        connOpts.setPassword(apiKey.toCharArray());
        connOpts.setCleanSession(false);
        return connOpts;
    }

    @NonNull
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
