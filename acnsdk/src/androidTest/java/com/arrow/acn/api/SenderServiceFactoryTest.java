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

import com.arrow.acn.api.fakes.FakeRestService;
import com.arrow.acn.api.fakes.FakeRetrofitHolder;
import com.arrow.acn.api.models.ConfigResponse;
import com.arrow.acn.api.mqtt.AbstractMqttAcnApiService;
import com.arrow.acn.api.mqtt.MqttAcnApiService;
import com.arrow.acn.api.mqtt.aws.AwsAcnApiService;
import com.arrow.acn.api.mqtt.azure.AzureAcnApiService;
import com.arrow.acn.api.mqtt.ibm.IbmAcnApiService;

import org.junit.Before;
import org.junit.Test;

import static com.arrow.acn.api.fakes.FakeData.AUTH_METHOD;
import static com.arrow.acn.api.fakes.FakeData.AUTH_TOKEN;
import static com.arrow.acn.api.fakes.FakeData.AWS_HOST;
import static com.arrow.acn.api.fakes.FakeData.AWS_PORT;
import static com.arrow.acn.api.fakes.FakeData.AWS_PRIVATE_KEY;
import static com.arrow.acn.api.fakes.FakeData.AZURE_ACCESS_KEY;
import static com.arrow.acn.api.fakes.FakeData.AZURE_HOST;
import static com.arrow.acn.api.fakes.FakeData.CA_CERT;
import static com.arrow.acn.api.fakes.FakeData.CLIENT_CERT;
import static com.arrow.acn.api.fakes.FakeData.GATEWAY_HID;
import static com.arrow.acn.api.fakes.FakeData.GATEWAY_TYPE;
import static com.arrow.acn.api.fakes.FakeData.GATEWAY_UID;
import static com.arrow.acn.api.fakes.FakeData.IBM_GATEWAY_ID;
import static com.arrow.acn.api.fakes.FakeData.MQTT_HOST;
import static com.arrow.acn.api.fakes.FakeData.MQTT_PREFIX;
import static com.arrow.acn.api.fakes.FakeData.ORGANIZATION_ID;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public final class SenderServiceFactoryTest {

    private SenderServiceFactory mFactory;

    @Before
    public void setUp() throws Exception {
        mFactory = new SenderServiceFactoryImpl();
    }

    @Test
    public void test_arrowconnect() throws Exception {
        String cloud = "ArrowConnect";
        final ConfigResponse response = new ConfigResponse();
        response.setCloudPlatform(cloud);
        TelemetrySenderInterface sender = mFactory.createTelemetrySender(new FakeRetrofitHolder(new FakeRestService()),
                response, GATEWAY_UID, GATEWAY_HID, MQTT_HOST, MQTT_PREFIX, null);
        assertNotNull(sender);
        assertThat(sender, instanceOf(MqttAcnApiService.class));
        assertThat(sender, instanceOf(AbstractMqttAcnApiService.class));
        assertThat(sender, instanceOf(AbstractTelemetrySenderService.class));
        assertThat(sender, instanceOf(TelemetrySenderInterface.class));
    }

    @Test
    public void test_ibm() throws Exception {
        String cloud = "IBM";
        ConfigResponse.Ibm ibm = new ConfigResponse.Ibm();
        ibm.setAuthMethod(AUTH_METHOD);
        ibm.setAuthToken(AUTH_TOKEN);
        ibm.setGatewayId(IBM_GATEWAY_ID);
        ibm.setGatewayType(GATEWAY_TYPE);
        ibm.setOrganicationId(ORGANIZATION_ID);
        final ConfigResponse response = new ConfigResponse();
        response.setCloudPlatform(cloud);
        response.setIbm(ibm);
        TelemetrySenderInterface sender = mFactory.createTelemetrySender(new FakeRetrofitHolder(new FakeRestService()),
                response, GATEWAY_UID, GATEWAY_HID, MQTT_HOST, MQTT_PREFIX, null);
        assertNotNull(sender);
        assertThat(sender, instanceOf(IbmAcnApiService.class));
        assertThat(sender, instanceOf(AbstractMqttAcnApiService.class));
        assertThat(sender, instanceOf(AbstractTelemetrySenderService.class));
        assertThat(sender, instanceOf(TelemetrySenderInterface.class));
    }

    @Test
    public void test_aws() throws Exception {
        String cloud = "AwS";
        ConfigResponse.Aws aws = new ConfigResponse.Aws();
        aws.setCaCert(CA_CERT);
        aws.setClientCert(CLIENT_CERT);
        aws.setHost(AWS_HOST);
        aws.setPort(AWS_PORT);
        aws.setPrivateKey(AWS_PRIVATE_KEY);
        final ConfigResponse response = new ConfigResponse();
        response.setCloudPlatform(cloud);
        response.setAws(aws);
        TelemetrySenderInterface sender = mFactory.createTelemetrySender(new FakeRetrofitHolder(new FakeRestService()),
                response, GATEWAY_UID, GATEWAY_HID, MQTT_HOST, MQTT_PREFIX, null);
        assertNotNull(sender);
        assertThat(sender, instanceOf(AwsAcnApiService.class));
        assertThat(sender, instanceOf(AbstractMqttAcnApiService.class));
        assertThat(sender, instanceOf(AbstractTelemetrySenderService.class));
        assertThat(sender, instanceOf(TelemetrySenderInterface.class));
    }

    @Test
    public void test_azure() throws Exception {
        String cloud = "Azure";
        ConfigResponse.Azure azure = new ConfigResponse.Azure();
        azure.setHost(AZURE_HOST);
        azure.setAccessKey(AZURE_ACCESS_KEY);
        final ConfigResponse response = new ConfigResponse();
        response.setCloudPlatform(cloud);
        response.setAzure(azure);
        TelemetrySenderInterface sender = mFactory.createTelemetrySender(new FakeRetrofitHolder(new FakeRestService()),
                response, GATEWAY_UID, GATEWAY_HID, MQTT_HOST, MQTT_PREFIX, null);
        assertNotNull(sender);
        assertThat(sender, instanceOf(AzureAcnApiService.class));
        assertThat(sender, instanceOf(AbstractMqttAcnApiService.class));
        assertThat(sender, instanceOf(AbstractTelemetrySenderService.class));
        assertThat(sender, instanceOf(TelemetrySenderInterface.class));
    }
}
