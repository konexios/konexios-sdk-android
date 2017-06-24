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
import com.arrow.acn.api.listeners.TelemetryRequestListener;
import com.arrow.acn.api.models.ApiError;
import com.arrow.acn.api.models.ConfigResponse;
import com.arrow.acn.api.models.TelemetryModel;

import java.util.List;

import static com.arrow.acn.api.fakes.FakeData.AWS_HOST;
import static com.arrow.acn.api.fakes.FakeData.AWS_PORT;
import static com.arrow.acn.api.fakes.FakeData.AWS_PRIVATE_KEY;
import static com.arrow.acn.api.fakes.FakeData.AZURE_ACCESS_KEY;
import static com.arrow.acn.api.fakes.FakeData.AZURE_HOST;
import static com.arrow.acn.api.fakes.FakeData.CA_CERT;
import static com.arrow.acn.api.fakes.FakeData.CLIENT_CERT;
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
        } else if(platform == ConfigResponse.CloudPlatform.AWS) {
            assertEquals(CA_CERT, configResponse.getAws().getCaCert());
            assertEquals(CLIENT_CERT, configResponse.getAws().getClientCert());
            assertEquals(AWS_HOST, configResponse.getAws().getHost());
            assertEquals(AWS_PORT, configResponse.getAws().getPort());
            assertEquals(AWS_PRIVATE_KEY, configResponse.getAws().getPrivateKey());
        } else if(platform == ConfigResponse.CloudPlatform.AZURE) {
            assertEquals(AZURE_HOST, configResponse.getAzure().getHost());
            assertEquals(AZURE_ACCESS_KEY, configResponse.getAzure().getAccessKey());
        } else {
            listener.onConnectionError(new ApiError(ApiError.COMMON_ERROR_CODE, "no such platform type"));
        }
        listener.onConnectionSuccess();
    }

    @Override
    public void disconnect() {

    }

    @Override
    public void sendSingleTelemetry(TelemetryModel telemetry, TelemetryRequestListener listener) {

    }

    @Override
    public void sendBatchTelemetry(List<TelemetryModel> telemetry, TelemetryRequestListener listener) {

    }

    @Override
    public boolean hasBatchMode() {
        return false;
    }

    @Override
    public boolean isConnected() {
        return false;
    }
}
