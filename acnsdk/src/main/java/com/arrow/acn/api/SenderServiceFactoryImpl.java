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

import com.arrow.acn.api.common.RetrofitHolder;
import com.arrow.acn.api.listeners.ServerCommandsListener;
import com.arrow.acn.api.models.ConfigResponse;
import com.arrow.acn.api.mqtt.MqttAcnApiService;
import com.arrow.acn.api.mqtt.aws.AwsAcnApiService;
import com.arrow.acn.api.mqtt.azure.AzureAcnApiService;
import com.arrow.acn.api.mqtt.ibm.IbmAcnApiService;

import static com.arrow.acn.api.models.ConfigResponse.CloudPlatform.ARROW_CONNECT;
import static com.arrow.acn.api.models.ConfigResponse.CloudPlatform.AWS;
import static com.arrow.acn.api.models.ConfigResponse.CloudPlatform.AZURE;
import static com.arrow.acn.api.models.ConfigResponse.CloudPlatform.IBM;

/**
 * Created by osminin on 4/5/2017.
 */

final class SenderServiceFactoryImpl implements SenderServiceFactory {
    @Override
    public TelemetrySenderInterface createTelemetrySender(RetrofitHolder retrofitHolder,
                                                          ConfigResponse configResponse,
                                                          String gatewayUid,
                                                          String gatewayId,
                                                          String mqttHost,
                                                          String mqttPrefix,
                                                          ServerCommandsListener serverCommandsListener) {
        TelemetrySenderInterface service = null;
        ConfigResponse.CloudPlatform platform = configResponse.getCloudPlatform();
        if (ARROW_CONNECT == platform) {
            service = new MqttAcnApiService(mqttHost, mqttPrefix, gatewayId,
                    retrofitHolder, serverCommandsListener);
        } else if (IBM == platform) {
            service = new IbmAcnApiService(gatewayId, configResponse);
        } else if (AWS == platform) {
            service = new AwsAcnApiService(gatewayId, configResponse);
        } else if (AZURE == platform) {
            service = new AzureAcnApiService(gatewayUid,
                    configResponse.getAzure().getAccessKey(),
                    configResponse.getAzure().getHost());
        }
        return service;
    }
}
