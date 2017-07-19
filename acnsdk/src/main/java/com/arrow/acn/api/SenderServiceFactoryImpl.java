/*
 * Copyright (c) 2017 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors: Arrow Electronics, Inc.
 */

package com.arrow.acn.api;

import android.support.annotation.Keep;

import com.arrow.acn.api.models.ConfigResponse;
import com.arrow.acn.api.mqtt.MqttAcnApiService;
import com.arrow.acn.api.mqtt.aws.AwsAcnApiService;
import com.arrow.acn.api.mqtt.azure.AzureAcnApiService;
import com.arrow.acn.api.mqtt.ibm.IbmAcnApiService;

import static com.arrow.acn.api.models.ConfigResponse.CloudPlatform.ARROW_CONNECT;
import static com.arrow.acn.api.models.ConfigResponse.CloudPlatform.AWS;
import static com.arrow.acn.api.models.ConfigResponse.CloudPlatform.AZURE;
import static com.arrow.acn.api.models.ConfigResponse.CloudPlatform.IBM;

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
