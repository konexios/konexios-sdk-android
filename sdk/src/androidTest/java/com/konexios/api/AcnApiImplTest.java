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


import android.support.test.runner.AndroidJUnit4;

import com.konexios.api.common.RetrofitHolder;
import com.konexios.api.fakes.FakeRestService;
import com.konexios.api.fakes.FakeRetrofitHolder;
import com.konexios.api.fakes.FakeSenderServiceFactory;
import com.konexios.api.listeners.CheckinGatewayListener;
import com.konexios.api.listeners.ConnectionListener;
import com.konexios.api.listeners.GetGatewayConfigListener;
import com.konexios.api.models.ApiError;
import com.konexios.api.models.CommonResponse;
import com.konexios.api.models.ConfigResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.konexios.api.fakes.FakeData.API_KEY;
import static com.konexios.api.fakes.FakeData.API_SECRET_KEY;
import static com.konexios.api.fakes.FakeData.AUTH_METHOD;
import static com.konexios.api.fakes.FakeData.AUTH_TOKEN;
import static com.konexios.api.fakes.FakeData.AWS_HOST;
import static com.konexios.api.fakes.FakeData.AWS_PORT;
import static com.konexios.api.fakes.FakeData.AWS_PRIVATE_KEY;
import static com.konexios.api.fakes.FakeData.AZURE_ACCESS_KEY;
import static com.konexios.api.fakes.FakeData.AZURE_HOST;
import static com.konexios.api.fakes.FakeData.CA_CERT;
import static com.konexios.api.fakes.FakeData.CLIENT_CERT;
import static com.konexios.api.fakes.FakeData.GATEWAY_HID;
import static com.konexios.api.fakes.FakeData.GATEWAY_TYPE;
import static com.konexios.api.fakes.FakeData.GATEWAY_UID;
import static com.konexios.api.fakes.FakeData.IBM_GATEWAY_ID;
import static com.konexios.api.fakes.FakeData.MQTT_HOST;
import static com.konexios.api.fakes.FakeData.MQTT_PREFIX;
import static com.konexios.api.fakes.FakeData.ORGANIZATION_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by osminin on 4/4/2017.
 */

@RunWith(AndroidJUnit4.class)
public class AcnApiImplTest {
    private ApiService mApiService;
    private FakeRestService mRestService;
    private RetrofitHolder mRetrofitHolder;
    private SenderServiceFactory mFakeSenderServiceFactory;

    @Before
    public void init() {
        mRestService = new FakeRestService();
        mRetrofitHolder = new FakeRetrofitHolder(mRestService);
        mFakeSenderServiceFactory = new FakeSenderServiceFactory();
        mApiService = new ApiImpl(mRetrofitHolder, mFakeSenderServiceFactory, true);
        ((ApiImpl)mApiService).setRestEndpoint(null, null, null);
        ((ApiImpl)mApiService).setMqttEndpoint(MQTT_HOST, MQTT_PREFIX);
    }

    @Test
    public void test_connectMqtt() throws InterruptedException {
        String cloud = "ArrowConnect";

        final ConfigResponse expectedResponse = new ConfigResponse();
        expectedResponse.setCloudPlatform(cloud);
        test_getConfig(expectedResponse);
    }

    @Test
    public void test_connectIbm() {
        String cloud = "IBm";
        ConfigResponse.Ibm ibm = new ConfigResponse.Ibm();
        ibm.setAuthMethod(AUTH_METHOD);
        ibm.setAuthToken(AUTH_TOKEN);
        ibm.setGatewayId(IBM_GATEWAY_ID);
        ibm.setGatewayType(GATEWAY_TYPE);
        ibm.setOrganicationId(ORGANIZATION_ID);

        final ConfigResponse expectedResponse = new ConfigResponse();
        expectedResponse.setCloudPlatform(cloud);
        expectedResponse.setIbm(ibm);
        test_getConfig(expectedResponse);
    }

    @Test
    public void test_connectAws() {
        String cloud = "AwS";
        ConfigResponse.Aws aws = new ConfigResponse.Aws();
        aws.setCaCert(CA_CERT);
        aws.setClientCert(CLIENT_CERT);
        aws.setHost(AWS_HOST);
        aws.setPort(AWS_PORT);
        aws.setPrivateKey(AWS_PRIVATE_KEY);
        final ConfigResponse expectedResponse = new ConfigResponse();
        expectedResponse.setCloudPlatform(cloud);
        expectedResponse.setAws(aws);
        test_getConfig(expectedResponse);
    }

    @Test
    public void test_connectAzure() {
        String cloud = "Azure";
        ConfigResponse.Azure azure = new ConfigResponse.Azure();
        azure.setHost(AZURE_HOST);
        azure.setAccessKey(AZURE_ACCESS_KEY);
        final ConfigResponse expectedResponse = new ConfigResponse();
        expectedResponse.setCloudPlatform(cloud);
        expectedResponse.setAzure(azure);
        test_getConfig(expectedResponse);
    }


    private void test_getConfig(final ConfigResponse expectedResponse) {
        ConfigResponse.Key key = new ConfigResponse.Key();
        key.setSecretKey(API_SECRET_KEY);
        key.setApiKey(API_KEY);
        expectedResponse.setKey(key);
        //response body that will be returned from fake service
        mRestService.setResponse(expectedResponse);
        //getting fake config
        mApiService.getGatewayConfig(GATEWAY_HID, new GetGatewayConfigListener() {
            @Override
            public void onGatewayConfigReceived(ConfigResponse response) {
                assertEquals(expectedResponse.getCloudPlatform(), response.getCloudPlatform());
                assertEquals(expectedResponse.getKey().getApiKey(), mRetrofitHolder.getApiKey());
                assertEquals(expectedResponse.getKey().getSecretKey(), mRetrofitHolder.getSecretKey());
                test_checkin();
            }

            @Override
            public void onGatewayConfigFailed(ApiError error) {
                assertNull(error);
            }
        });
    }

    private void test_checkin() {
        CommonResponse response = new CommonResponse();
        response.setHid(GATEWAY_HID);
        mRestService.setResponse(response);
        mApiService.checkinGateway(GATEWAY_HID, GATEWAY_UID, new CheckinGatewayListener() {
            @Override
            public void onCheckinGatewaySuccess() {
                test_connect();
            }

            @Override
            public void onCheckinGatewayError(ApiError error) {
                assertNull(error);
            }
        });
    }

    private void test_connect() {
        mApiService.connect(new ConnectionListener() {
            @Override
            public void onConnectionSuccess() {
                //test passed
            }

            @Override
            public void onConnectionError(ApiError error) {
                assertNull(error);
            }
        });
    }
}
