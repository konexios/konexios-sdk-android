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

package com.konexios.api;

import androidx.annotation.Keep;

import com.konexios.api.models.ConfigResponse;
import com.konexios.api.mqtt.MqttAcnApiService;
import com.konexios.api.mqtt.aws.AwsAcnApiService;
import com.konexios.api.mqtt.azure.AzureAcnApiService;
import com.konexios.api.mqtt.ibm.IbmAcnApiService;

import static com.konexios.api.models.ConfigResponse.CloudPlatform.ARROW_CONNECT;
import static com.konexios.api.models.ConfigResponse.CloudPlatform.AWS;
import static com.konexios.api.models.ConfigResponse.CloudPlatform.AZURE;
import static com.konexios.api.models.ConfigResponse.CloudPlatform.IBM;

@Keep
final class SenderServiceFactoryImpl implements SenderServiceFactory {
    @Override
    public TelemetrySenderInterface createTelemetrySender(SenderServiceArgsProvider provider) {
        TelemetrySenderInterface service = null;
        ConfigResponse.CloudPlatform platform = provider.getConfigResponse().getCloudPlatform();
        if (ARROW_CONNECT == platform) {
            service = new MqttAcnApiService(provider.getMqttHost(), provider.getMqttPrefix(), provider.getGatewayId(),
                    provider.getRetrofitHolder(), provider.getServerCommandsListener(),
                    provider.getIotConnectApiService());
        } else if (IBM == platform) {
            service = new IbmAcnApiService(provider.getGatewayId(), provider.getConfigResponse(),
                    provider.getServerCommandsListener(), provider.getIotConnectApiService());
        } else if (AWS == platform) {
            service = new AwsAcnApiService(provider.getGatewayId(), provider.getConfigResponse(),
                    provider.getServerCommandsListener(), provider.getIotConnectApiService());
        } else if (AZURE == platform) {
            service = new AzureAcnApiService(provider.getGatewayUid(),
                    provider.getConfigResponse().getAzure().getAccessKey(),
                    provider.getConfigResponse().getAzure().getHost(),
                    provider.getServerCommandsListener(), provider.getIotConnectApiService());
        }
        return service;
    }
}
