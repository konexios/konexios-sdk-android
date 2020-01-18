/*
 * Copyright (c) 2017-2019 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *     Arrow Electronics, Inc.
 *     Konexios, Inc.
 */

package com.konexios.api.mqtt;


import android.text.TextUtils;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.konexios.api.common.RetrofitHolder;
import com.konexios.api.listeners.ServerCommandsListener;
import com.konexios.api.rest.RestApiService;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

/**
 * telemetry sender based on pure mqtt
 */

@Keep
public final class MqttAcnApiService extends AbstractMqttAcnApiService {

    private final String mHost;
    private final String mMqttPrefix;
    private final RetrofitHolder mRetrofitHolder;

    public MqttAcnApiService(String host, String mqttPrefix, String gatewayId,
                             RetrofitHolder retrofitHolder, ServerCommandsListener listener,
                             RestApiService restService) {
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
