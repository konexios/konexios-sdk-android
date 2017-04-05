/*
 * Copyright (c) 2017 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors: Arrow Electronics, Inc.
 */

package com.arrow.acn.api.fakes;

import com.arrow.acn.api.TelemetrySenderInterface;
import com.arrow.acn.api.common.RetrofitHolder;
import com.arrow.acn.api.listeners.ConnectionListener;
import com.arrow.acn.api.listeners.ServerCommandsListener;
import com.arrow.acn.api.models.ApiError;
import com.arrow.acn.api.models.ConfigResponse;
import com.arrow.acn.api.models.TelemetryModel;

import java.util.List;

import static com.arrow.acn.api.fakes.FakeData.GATEWAY_UID;
import static com.arrow.acn.api.fakes.FakeData.MQTT_HOST;
import static com.arrow.acn.api.fakes.FakeData.MQTT_PREFIX;
import static com.arrow.acn.api.fakes.FakeData.ORGANIZATION_ID;
import static com.arrow.acn.api.fakes.FakeData.AUTH_METHOD;
import static com.arrow.acn.api.fakes.FakeData.AUTH_TOKEN;
import static com.arrow.acn.api.fakes.FakeData.GATEWAY_TYPE;
import static com.arrow.acn.api.fakes.FakeData.IBM_GATEWAY_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by osminin on 4/5/2017.
 */

public final class FakeTelemetrySender implements TelemetrySenderInterface {

    private RetrofitHolder retrofitHolder;
    private ConfigResponse configResponse;
    private String gatewayUid;
    private String gatewayId;
    private String mqttHost;
    private String mqttPrefix;
    private ServerCommandsListener serverCommandsListener;
    private ConfigResponse.CloudPlatform platform;

    public FakeTelemetrySender(RetrofitHolder retrofitHolder, ConfigResponse configResponse,
                               String gatewayUid, String gatewayId, String mqttHost,
                               String mqttPrefix, ServerCommandsListener serverCommandsListener) {
        this.retrofitHolder = retrofitHolder;
        this.configResponse = configResponse;
        this.gatewayUid = gatewayUid;
        this.gatewayId = gatewayId;
        this.mqttHost = mqttHost;
        this.mqttPrefix = mqttPrefix;
        this.serverCommandsListener = serverCommandsListener;
        this.platform = configResponse.getCloudPlatform();
    }

    @Override
    public void connect(ConnectionListener listener) {
        if (platform == ConfigResponse.CloudPlatform.ARROW_CONNECT) {
            assertEquals(MQTT_HOST, mqttHost);
            assertEquals(MQTT_PREFIX, mqttPrefix);
            assertEquals(GATEWAY_UID, gatewayUid);
            assertNull(configResponse.getAws());
            assertNull(configResponse.getAzure());
            assertNull(configResponse.getIbm());
        } else if (platform == ConfigResponse.CloudPlatform.IBM) {
            assertEquals(ORGANIZATION_ID, configResponse.getIbm().getOrganicationId());
            assertEquals(GATEWAY_TYPE, configResponse.getIbm().getGatewayType());
            assertEquals(IBM_GATEWAY_ID, configResponse.getIbm().getGatewayId());
            assertEquals(AUTH_METHOD, configResponse.getIbm().getAuthMethod());
            assertEquals(AUTH_TOKEN, configResponse.getIbm().getAuthToken());
            assertNull(configResponse.getAws());
            assertNull(configResponse.getAzure());
        } else {
            listener.onConnectionError(new ApiError(ApiError.COMMON_ERROR_CODE, "no such platform type"));
        }
        listener.onConnectionSuccess();
    }

    @Override
    public void disconnect() {

    }

    @Override
    public void sendSingleTelemetry(TelemetryModel telemetry) {

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
        return false;
    }

    public RetrofitHolder getRetrofitHolder() {
        return retrofitHolder;
    }

    public ConfigResponse getConfigResponse() {
        return configResponse;
    }

    public String getGatewayUid() {
        return gatewayUid;
    }

    public String getGatewayId() {
        return gatewayId;
    }

    public String getMqttHost() {
        return mqttHost;
    }

    public String getMqttPrefix() {
        return mqttPrefix;
    }

    public ServerCommandsListener getServerCommandsListener() {
        return serverCommandsListener;
    }

    public ConfigResponse.CloudPlatform getPlatform() {
        return platform;
    }
}
