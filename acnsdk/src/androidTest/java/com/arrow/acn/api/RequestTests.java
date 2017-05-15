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

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.test.runner.AndroidJUnit4;

import com.arrow.acn.api.listeners.CheckinGatewayListener;
import com.arrow.acn.api.listeners.CommonRequestListener;
import com.arrow.acn.api.listeners.FindGatewayListener;
import com.arrow.acn.api.listeners.GatewayCommandsListener;
import com.arrow.acn.api.listeners.GatewayRegisterListener;
import com.arrow.acn.api.listeners.GatewayUpdateListener;
import com.arrow.acn.api.listeners.GetGatewayConfigListener;
import com.arrow.acn.api.listeners.GetGatewaysListener;
import com.arrow.acn.api.listeners.ListResultListener;
import com.arrow.acn.api.listeners.RegisterAccountListener;
import com.arrow.acn.api.models.AccountRequest;
import com.arrow.acn.api.models.AccountResponse;
import com.arrow.acn.api.models.ApiError;
import com.arrow.acn.api.models.CommonResponse;
import com.arrow.acn.api.models.ConfigResponse;
import com.arrow.acn.api.models.DeviceModel;
import com.arrow.acn.api.models.GatewayCommand;
import com.arrow.acn.api.models.GatewayModel;
import com.arrow.acn.api.models.GatewayResponse;
import com.arrow.acn.api.models.GatewayType;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.arrow.acn.api.models.ConfigResponse.CloudPlatform.ARROW_CONNECT;
import static com.arrow.acn.api.models.ConfigResponse.CloudPlatform.AWS;
import static com.arrow.acn.api.models.ConfigResponse.CloudPlatform.AZURE;
import static com.arrow.acn.api.models.ConfigResponse.CloudPlatform.IBM;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by osminin on 5/4/2017.
 */

@RunWith(AndroidJUnit4.class)
public class RequestTests {
    public static final String BASE_IOT_CONNECT_URL_DEV = "http://pgsdev01.arrowconnect.io:12001";
    //TODO: define ApiKey and ApiSecret variables in gradle.properties file
    public static final String DEFAULT_API_KEY = BuildConfig.API_KEY;
    public static final String DEFAULT_API_SECRET = BuildConfig.API_SECRET;
    public static final String MQTT_CONNECT_URL_DEV = "tcp://pgsdev01.arrowconnect.io:1883";
    public static final String MQTT_CLIENT_PREFIX_DEV = "/themis.dev";

    public static final String APPLICATION_HID = "f959da337b2192eb87c09310680c61fda989648c";
    public static final String USER_HID = "84b5b9c84359bbe243e05447fdbdf4ad7a2a166e";
    public static final String GATEWAY_HID = "e8a31f38bada1cd932c327bfc05b69a1aacf86f9";
    public static final String GATEWAY_UID = "Uid" + new Random(System.currentTimeMillis()).nextInt();

    private AcnApiService mService;

    @Before
    public void setUp() throws Exception {
        ExecutorService service = new SynchronousExecutorService();
        mService = new AcnApi.Builder()
                .setRestEndpoint(BASE_IOT_CONNECT_URL_DEV, DEFAULT_API_KEY, DEFAULT_API_SECRET)
                .setMqttEndpoint(MQTT_CONNECT_URL_DEV, MQTT_CLIENT_PREFIX_DEV)
                .setCallbackExecutor(service)
                .setHttpExecutorService(service)
                .setDebug(true)
                .build();
    }

    @Test
    public void login() throws Exception {
        final AccountRequest model = new AccountRequest();
        model.setName("Some Name");
        model.setEmail("somemail@mail.com");
        model.setPassword("password");

        mService.registerAccount(model, new RegisterAccountListener() {
            @Override
            public void onAccountRegistered(AccountResponse accountResponse) {
                assertNotNull(accountResponse);
                assertEquals(model.getName(), accountResponse.getName());
                assertEquals(model.getEmail(), accountResponse.getEmail());
                assertNotNull(accountResponse.getHid());
                assertFalse(accountResponse.getHid().isEmpty());
                assertNotNull(accountResponse.getApplicationHid());
                assertFalse(accountResponse.getApplicationHid().isEmpty());
            }

            @Override
            public void onAccountRegisterFailed(ApiError e) {
                assertNull(e);
            }
        });
    }

    @Test
    public void registerGateway() throws Exception {
        String name = String.format("%s %s", Build.MANUFACTURER, Build.MODEL);
        String osName = String.format("Android %s", Build.VERSION.RELEASE);
        String swName = "JMyIotGatewayTest";

        GatewayModel gatewayModel = new GatewayModel();
        gatewayModel.setName(name);
        gatewayModel.setOsName(osName);
        gatewayModel.setSoftwareName(swName);
        gatewayModel.setUid(GATEWAY_UID);
        gatewayModel.setType(GatewayType.Mobile);
        gatewayModel.setUserHid(USER_HID);
        gatewayModel.setApplicationHid(APPLICATION_HID);
        gatewayModel.setSoftwareVersion(
                String.format("%d.%d", 0, 1));

        mService.registerGateway(gatewayModel, new GatewayRegisterListener() {
            @Override
            public void onGatewayRegistered(@NonNull GatewayResponse response) {
                assertNotNull(response);
                assertNotNull(response.getHid());
                assertFalse(response.getHid().isEmpty());
            }

            @Override
            public void onGatewayRegisterFailed(@NonNull ApiError e) {
                assertNull(e);
            }
        });
    }

    @Test
    public void gatewayConfig() throws Exception {
        mService.getGatewayConfig(GATEWAY_HID, new GetGatewayConfigListener() {
            @Override
            public void onGatewayConfigReceived(ConfigResponse response) {
                assertNotNull(response);
                assertNotNull(response.getCloudPlatform());
                ConfigResponse.CloudPlatform platform = response.getCloudPlatform();
                if (ARROW_CONNECT == platform) {
                    assertNotNull(response.getKey());
                    assertNotNull(response.getKey().getApiKey());
                    assertFalse(response.getKey().getApiKey().isEmpty());
                    assertNotNull(response.getKey().getSecretKey());
                    assertFalse(response.getKey().getSecretKey().isEmpty());
                } else if (IBM == platform) {
                    //TODO: implement this case
                } else if (AWS == platform) {
                    //TODO: implement this case
                } else if (AZURE == platform) {
                    //TODO: implement this case
                }
            }

            @Override
            public void onGatewayConfigFailed(@NonNull ApiError e) {
                assertNull(e);
            }
        });
    }

    @Test
    public void checkin() throws Exception {
        mService.checkinGateway(GATEWAY_HID, GATEWAY_UID, new CheckinGatewayListener() {
            @Override
            public void onCheckinGatewaySuccess() {
                //passed
            }

            @Override
            public void onCheckinGatewayError(ApiError error) {
                assertNull(error);
            }
        });
    }

    @Test
    public void updateGateway() throws Exception {
        String name = String.format("%s %s", Build.MANUFACTURER, Build.MODEL);
        String osName = String.format("Android %s", Build.VERSION.RELEASE);
        String swName = "JMyIotGatewayTest";

        GatewayModel gatewayModel = new GatewayModel();
        gatewayModel.setName(name);
        gatewayModel.setOsName(osName);
        gatewayModel.setSoftwareName(swName);
        gatewayModel.setUid(GATEWAY_UID);
        gatewayModel.setType(GatewayType.Mobile);
        gatewayModel.setUserHid(USER_HID);
        gatewayModel.setApplicationHid(APPLICATION_HID);
        gatewayModel.setSoftwareVersion(
                String.format("%d.%d", 0, 2));

        mService.updateGateway(GATEWAY_HID, gatewayModel, new GatewayUpdateListener() {

            @Override
            public void onGatewayUpdated(GatewayResponse response) {
                assertNotNull(response);
                assertNotNull(response.getHid());
                assertFalse(response.getHid().isEmpty());
                assertEquals(GATEWAY_HID, response.getHid());
            }

            @Override
            public void onGatewayUpdateFailed(ApiError error) {
                assertNull(error);
            }
        });
    }

    @Test
    public void getDevicesByGatewayHid() throws Exception {
        mService.getDevicesList(GATEWAY_HID, new ListResultListener<DeviceModel>() {
            @Override
            public void onRequestSuccess(List<DeviceModel> list) {
                assertNotNull(list);
            }

            @Override
            public void onRequestError(ApiError error) {
                assertNull(error);
            }
        });
    }

    @Test
    public void heartBeat() throws Exception {
        mService.gatewayHeartbeat(GATEWAY_HID, new CommonRequestListener() {
            @Override
            public void onRequestSuccess(CommonResponse response) {
                assertNotNull(response);
            }

            @Override
            public void onRequestError(ApiError error) {
                assertNull(error);
            }
        });
    }

    @Test
    public void findAllGateways() throws Exception {
        mService.findAllGateways(new GetGatewaysListener() {
            @Override
            public void onGatewaysReceived(List<GatewayModel> response) {
                assertNotNull(response);
            }

            @Override
            public void onGatewaysFailed(ApiError error) {
                assertNull(error);
            }
        });
    }

    @Test
    public void findGateway() throws Exception {
        mService.findGateway(GATEWAY_HID, new FindGatewayListener() {
            @Override
            public void onGatewayFound(GatewayModel gatewayModel) {
                assertNotNull(gatewayModel);
                assertEquals(GATEWAY_UID, gatewayModel.getUid());
                assertEquals(USER_HID, gatewayModel.getUserHid());
            }

            @Override
            public void onGatewayFindError(ApiError error) {
                assertNull(error);
            }
        });
    }

    @Test
    public void sendGatewayCommand() throws Exception {
        GatewayCommand command = new GatewayCommand();
        command.setCommand("Command");
        //TODO: replace with device hid
        command.setDeviceHid("devHid");
        command.setPayload(new Gson().toJson(new JsonObject()));
        mService.sendCommandGateway(GATEWAY_HID, command, new GatewayCommandsListener() {
            @Override
            public void onGatewayCommandSent(CommonResponse response) {
                assertNotNull(response);
            }

            @Override
            public void onGatewayCommandFailed(ApiError error) {
                assertNull(error);
            }
        });
    }

    private class SynchronousExecutorService implements ExecutorService {

        @Override
        public void shutdown() {

        }

        @NonNull
        @Override
        public List<Runnable> shutdownNow() {
            return null;
        }

        @Override
        public boolean isShutdown() {
            return false;
        }

        @Override
        public boolean isTerminated() {
            return false;
        }

        @Override
        public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
            return false;
        }

        @NonNull
        @Override
        public <T> Future<T> submit(Callable<T> task) {
            return null;
        }

        @NonNull
        @Override
        public <T> Future<T> submit(Runnable task, T result) {
            return null;
        }

        @NonNull
        @Override
        public Future<?> submit(Runnable task) {
            return null;
        }

        @NonNull
        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
            return null;
        }

        @NonNull
        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
                throws InterruptedException {
            return null;
        }

        @NonNull
        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
            return null;
        }

        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
                throws InterruptedException, ExecutionException, TimeoutException {
            return null;
        }

        @Override
        public void execute(Runnable command) {
            command.run();
        }
    }
}
