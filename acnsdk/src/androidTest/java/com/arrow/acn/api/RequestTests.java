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

import com.arrow.acn.api.fakes.SynchronousExecutorService;
import com.arrow.acn.api.listeners.CheckinGatewayListener;
import com.arrow.acn.api.listeners.CommonRequestListener;
import com.arrow.acn.api.listeners.DeleteDeviceActionListener;
import com.arrow.acn.api.listeners.FindDeviceListener;
import com.arrow.acn.api.listeners.FindDeviceStateListener;
import com.arrow.acn.api.listeners.FindGatewayListener;
import com.arrow.acn.api.listeners.GatewayCommandsListener;
import com.arrow.acn.api.listeners.GatewayRegisterListener;
import com.arrow.acn.api.listeners.GatewayUpdateListener;
import com.arrow.acn.api.listeners.GetGatewayConfigListener;
import com.arrow.acn.api.listeners.GetGatewaysListener;
import com.arrow.acn.api.listeners.ListNodeTypesListener;
import com.arrow.acn.api.listeners.ListResultListener;
import com.arrow.acn.api.listeners.MessageStatusListener;
import com.arrow.acn.api.listeners.PagingResultListener;
import com.arrow.acn.api.listeners.PostDeviceActionListener;
import com.arrow.acn.api.listeners.RegisterAccountListener;
import com.arrow.acn.api.listeners.RegisterDeviceListener;
import com.arrow.acn.api.listeners.TelemetryCountListener;
import com.arrow.acn.api.listeners.UpdateDeviceActionListener;
import com.arrow.acn.api.models.AccountRequest;
import com.arrow.acn.api.models.AccountResponse;
import com.arrow.acn.api.models.ApiError;
import com.arrow.acn.api.models.AuditLogModel;
import com.arrow.acn.api.models.AuditLogsQuery;
import com.arrow.acn.api.models.CommonResponse;
import com.arrow.acn.api.models.ConfigResponse;
import com.arrow.acn.api.models.DeviceActionModel;
import com.arrow.acn.api.models.DeviceActionTypeModel;
import com.arrow.acn.api.models.DeviceEventModel;
import com.arrow.acn.api.models.DeviceModel;
import com.arrow.acn.api.models.DeviceRegistrationModel;
import com.arrow.acn.api.models.DeviceRegistrationResponse;
import com.arrow.acn.api.models.DeviceTypeModel;
import com.arrow.acn.api.models.DeviceTypeRegistrationModel;
import com.arrow.acn.api.models.ErrorBodyModel;
import com.arrow.acn.api.models.FindDeviceStateResponse;
import com.arrow.acn.api.models.FindDevicesRequest;
import com.arrow.acn.api.models.FindTelemetryRequest;
import com.arrow.acn.api.models.GatewayCommand;
import com.arrow.acn.api.models.GatewayModel;
import com.arrow.acn.api.models.GatewayResponse;
import com.arrow.acn.api.models.GatewayType;
import com.arrow.acn.api.models.HistoricalEventsRequest;
import com.arrow.acn.api.models.ListResultModel;
import com.arrow.acn.api.models.MessageStatusResponse;
import com.arrow.acn.api.models.NewDeviceStateTransactionRequest;
import com.arrow.acn.api.models.NodeModel;
import com.arrow.acn.api.models.NodeRegistrationModel;
import com.arrow.acn.api.models.NodeTypeModel;
import com.arrow.acn.api.models.NodeTypeRegistrationModel;
import com.arrow.acn.api.models.PagingResultModel;
import com.arrow.acn.api.models.TelemetryCountRequest;
import com.arrow.acn.api.models.TelemetryCountResponse;
import com.arrow.acn.api.models.TelemetryItemModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;

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

    public static final String DEVICE_UID = "deviceUid" + new Random(System.currentTimeMillis()).nextInt();
    //public static final String DEVICE_HID = "36cf857d2c71548f5f4725caf44d15ed51365006";
    public static final String DEVICE_HID = "211f793a3bfd00244b28f1c519c19bf702e356e6";

    public static final String NODE_TYPE_HID = "43e4b5577e5e3e7200d0c77729b191e4b86480f3";

    public static final String NODE_HID = "c388f8c4bbe15826cf94908dfdf95bad25a981d5";
    public static final String NEW_NODE_HID = "4050add902766a064b0dea19860b3c025e0021dd";

    public static final String ACTION_HID = "36cf857d2c71548f5f4725caf44d15ed51365006";

    public static final String DEVICE_TYPE_HID = "74322e8f42552503de97ec11fffd3f3641e0983f";

    public static final String DEVICE_STATE_HID = "265f6cfe9526826867db6e0653ed56db5914d858";

    public static final String EVENT_HID = "f74892dbffddd323887277b65c1a1b6a5b7d8257";

    private AcnApiService mService;

    private static String getFormattedDateTime(Long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String formattedDate = format.format(date);
        return formattedDate;
    }

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
    public void registerDevice() throws Exception {
        DeviceRegistrationModel model = new DeviceRegistrationModel();
        model.setUid(DEVICE_UID);
        model.setName("AndroidInternal");
        model.setGatewayHid(GATEWAY_HID);
        model.setUserHid(USER_HID);
        model.setType("android");
        model.setEnabled(true);
        mService.registerDevice(model, new RegisterDeviceListener() {
            @Override
            public void onDeviceRegistered(DeviceRegistrationResponse response) {
                assertNotNull(response);
                assertNotNull(response.getHid());
                assertFalse(response.getHid().isEmpty());
            }

            @Override
            public void onDeviceRegistrationFailed(ApiError error) {
                assertNull(error);
            }
        });
    }

    @Test
    public void sendGatewayCommand() throws Exception {
        GatewayCommand command = new GatewayCommand();
        command.setCommand("Command");
        command.setDeviceHid(DEVICE_HID);
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

    @Test
    public void getGatewayLogs() throws Exception {
        AuditLogsQuery query = new AuditLogsQuery();
        query.setUserHids(Arrays.asList(new String[]{USER_HID}));
        query.setPage(0);
        long currentTime = System.currentTimeMillis();
        query.setCreatedDateFrom(getFormattedDateTime(currentTime - 1000 * 3600 * 24));
        query.setSize(200);
        query.setCreatedDateTo(getFormattedDateTime(currentTime));
        mService.getGatewayLogs(GATEWAY_HID, query, new PagingResultListener<AuditLogModel>() {
            @Override
            public void onRequestSuccess(PagingResultModel<AuditLogModel> list) {
                assertNotNull(list);
            }

            @Override
            public void onRequestError(ApiError error) {
                assertNull(error);
            }
        });
    }

    @Test
    public void findTelemetryByAppHid() throws Exception {
        FindTelemetryRequest request = new FindTelemetryRequest();
        request.setHid(APPLICATION_HID);
        request.setPage(0);
        request.setSize(200);
        long currentTime = System.currentTimeMillis();
        request.setFromTimestamp(getFormattedDateTime(currentTime - 1000 * 3600 * 24));
        request.setToTimestamp(getFormattedDateTime(currentTime));
        mService.findTelemetryByApplicationHid(request, new PagingResultListener<TelemetryItemModel>() {
            @Override
            public void onRequestSuccess(PagingResultModel<TelemetryItemModel> list) {
                assertNotNull(list);
            }

            @Override
            public void onRequestError(ApiError error) {
                assertNull(error);
            }
        });
    }

    @Test
    public void findTelemetryByDeviceHid() throws Exception {
        FindTelemetryRequest request = new FindTelemetryRequest();
        request.setHid(DEVICE_HID);
        request.setPage(0);
        request.setSize(200);
        long currentTime = System.currentTimeMillis();
        request.setFromTimestamp(getFormattedDateTime(currentTime - 1000 * 3600 * 24));
        request.setToTimestamp(getFormattedDateTime(currentTime));
        mService.findTelemetryByDeviceHid(request, new PagingResultListener<TelemetryItemModel>() {
            @Override
            public void onRequestSuccess(PagingResultModel<TelemetryItemModel> list) {
                assertNotNull(list);
            }

            @Override
            public void onRequestError(ApiError error) {
                assertNull(error);
            }
        });
    }

    @Test
    public void findTelemetryByNodeHid() throws Exception {
        FindTelemetryRequest request = new FindTelemetryRequest();
        request.setHid(NODE_TYPE_HID);
        request.setPage(0);
        request.setSize(200);
        long currentTime = System.currentTimeMillis();
        request.setFromTimestamp(getFormattedDateTime(currentTime - 1000 * 3600 * 24));
        request.setToTimestamp(getFormattedDateTime(currentTime));
        mService.findTelemetryByNodeHid(request, new PagingResultListener<TelemetryItemModel>() {
            @Override
            public void onRequestSuccess(PagingResultModel<TelemetryItemModel> list) {
                assertNotNull(list);
            }

            @Override
            public void onRequestError(ApiError error) {
                assertNull(error);
            }
        });
    }

    @Test
    public void getTelemetryItemsCount() throws Exception {
        TelemetryCountRequest request = new TelemetryCountRequest();
        request.setDeviceHid(DEVICE_HID);
        long currentTime = System.currentTimeMillis();
        request.setFromTimestamp(getFormattedDateTime(currentTime - 1000 * 3600 * 24));
        request.setToTimestamp(getFormattedDateTime(currentTime));
        final String telemetryName = "light";
        request.setTelemetryName(telemetryName);
        mService.getTelemetryItemsCount(request, new TelemetryCountListener() {
            @Override
            public void onTelemetryItemsCountSuccess(TelemetryCountResponse response) {
                assertNotNull(response);
                assertEquals(DEVICE_HID, response.getDeviceHid());
                assertEquals(telemetryName, response.getName());
            }

            @Override
            public void onTelemetryItemsCountError(ApiError error) {
                assertNull(error);
            }
        });
    }

    @Test
    public void getLastTelemetry() throws Exception {
        mService.getLastTelemetry(DEVICE_HID, new ListResultListener<TelemetryItemModel>() {
            @Override
            public void onRequestSuccess(List<TelemetryItemModel> list) {
                assertNotNull(list);
            }

            @Override
            public void onRequestError(ApiError error) {
                assertNull(error);
            }
        });
    }

    @Test
    public void getHistoricalEvents() throws Exception {
        HistoricalEventsRequest request = new HistoricalEventsRequest();
        request.setHid(DEVICE_HID);
        request.setSize(200);
        request.setPage(0);
        long currentTime = System.currentTimeMillis();
        request.setCreatedDateFrom((getFormattedDateTime(currentTime - 1000 * 3600 * 24)));
        request.setCreatedDateTo(getFormattedDateTime(currentTime));
        mService.getDeviceHistoricalEvents(request, new PagingResultListener<DeviceEventModel>() {
            @Override
            public void onRequestSuccess(PagingResultModel<DeviceEventModel> list) {
                assertNotNull(list);
            }

            @Override
            public void onRequestError(ApiError error) {
                assertNull(error);
            }
        });
    }

    @Test
    public void getActionTypes() throws Exception {
        mService.getDeviceActionTypes(new ListResultListener<DeviceActionTypeModel>() {
            @Override
            public void onRequestSuccess(List<DeviceActionTypeModel> list) {
                assertNotNull(list);
            }

            @Override
            public void onRequestError(ApiError error) {
                assertNull(error);
            }
        });
    }

    @Test
    public void getActions() throws Exception {
        mService.getDeviceActions(DEVICE_HID, new ListResultListener<DeviceActionModel>() {
            @Override
            public void onRequestSuccess(List<DeviceActionModel> list) {
                assertNotNull(list);
            }

            @Override
            public void onRequestError(ApiError error) {
                assertNull(error);
            }
        });
    }

    @Test
    public void postAction() throws Exception {
        DeviceActionModel model = new DeviceActionModel();
        model.setCriteria("light > 50");
        model.setDescription("description");
        model.setEnabled(true);
        model.setExpiration(1000);
        model.setIndex(0);
        model.setSystemName("InitiateSkypeCall");
        mService.postDeviceAction(DEVICE_HID, model, new PostDeviceActionListener() {
            @Override
            public void postActionSucceed(CommonResponse response) {
                assertNotNull(response);
            }

            @Override
            public void postActionFailed(ApiError error) {
                assertNull(error);
            }
        });
    }

    @Test
    public void updateAction() throws Exception {
        DeviceActionModel model = new DeviceActionModel();
        model.setCriteria("light > 100");
        model.setDescription("description new");
        model.setEnabled(true);
        model.setExpiration(1000);
        model.setIndex(0);
        model.setSystemName("InitiateSkypeCall");
        mService.updateDeviceAction(DEVICE_HID, 0, model, new UpdateDeviceActionListener() {
            @Override
            public void onDeviceActionUpdated(CommonResponse response) {
                assertNotNull(response);
            }

            @Override
            public void onDeviceActionUpdateFailed(ApiError error) {
                assertNull(error);
            }
        });
    }

    @Test
    public void deleteAction() throws Exception {
        mService.deleteDeviceAction(DEVICE_HID, 0, new DeleteDeviceActionListener() {
            @Override
            public void onDeviceActionDeleted(CommonResponse response) {
                assertNotNull(response);
            }

            @Override
            public void onDeviceActionDeleteFailed(ApiError error) {
                assertNull(error);
            }
        });

    }

    @Test
    public void findAllDevices() throws Exception {
        FindDevicesRequest request = new FindDevicesRequest();
        request.set_size(200);
        request.set_page(0);
        request.setUserHid(USER_HID);
        request.setGatewayHid(GATEWAY_HID);
        request.setUid(DEVICE_UID);
        mService.findAllDevices(request, new PagingResultListener<DeviceModel>() {
            @Override
            public void onRequestSuccess(PagingResultModel<DeviceModel> list) {
                assertNotNull(list);
            }

            @Override
            public void onRequestError(ApiError error) {
                assertNull(error);
            }
        });
    }

    @Test
    public void findDeviceByHid() throws Exception {
        mService.findDeviceByHid(DEVICE_HID, new FindDeviceListener() {
            @Override
            public void onDeviceFindSuccess(DeviceModel device) {
                assertNotNull(device);
                assertEquals(DEVICE_HID, device.getHid());
            }

            @Override
            public void onDeviceFindFailed(ApiError error) {
                assertNull(error);
            }
        });
    }

    @Test
    public void updateExistingDevice() throws Exception {
        DeviceRegistrationModel model = new DeviceRegistrationModel();
        model.setUid(DEVICE_UID);
        model.setName("AndroidInternal");
        model.setGatewayHid(GATEWAY_HID);
        model.setUserHid(USER_HID);
        model.setType("android");
        model.setEnabled(true);
        mService.updateDevice(DEVICE_HID, model, new CommonRequestListener() {
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
    public void listDeviceAuditLogs() throws Exception {
        AuditLogsQuery query = new AuditLogsQuery();
        query.setSize(200);
        query.setPage(0);
        query.setUserHids(Arrays.asList(new String[]{USER_HID}));
        mService.getDeviceAuditLogs(DEVICE_HID, query, new PagingResultListener<AuditLogModel>() {
            @Override
            public void onRequestSuccess(PagingResultModel<AuditLogModel> list) {
                assertNotNull(list);
            }

            @Override
            public void onRequestError(ApiError error) {
                assertNull(error);
            }
        });
    }

    @Test
    public void getListExistingNodes() throws Exception {
        mService.getNodesList(new ListResultListener<NodeModel>() {
            @Override
            public void onRequestSuccess(List<NodeModel> list) {
                assertNotNull(list);
            }

            @Override
            public void onRequestError(ApiError error) {
                assertNull(error);
            }
        });
    }

    @Test
    public void createNewNode() throws Exception {
        NodeRegistrationModel model = new NodeRegistrationModel();
        model.setName("TestNode");
        model.setDescription("test description");
        model.setEnabled(true);
        model.setParentNodeHid(NODE_HID);
        model.setNodeTypeHid(NODE_TYPE_HID);
        mService.createNewNode(model, new CommonRequestListener() {
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
    public void updateExistingNode() throws Exception {
        NodeRegistrationModel model = new NodeRegistrationModel();
        model.setName("TestNode");
        model.setDescription("test description 2");
        model.setEnabled(true);
        model.setParentNodeHid(NODE_HID);
        model.setNodeTypeHid(NODE_TYPE_HID);
        mService.updateExistingNode(NEW_NODE_HID, model, new CommonRequestListener() {
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
    public void getListNodeTypes() throws Exception {
        mService.getListNodeTypes(new ListNodeTypesListener() {
            @Override
            public void onListNodeTypesSuccess(ListResultModel<NodeTypeModel> result) {
                assertNotNull(result);
            }

            @Override
            public void onListNodeTypesFiled(ApiError error) {
                assertNull(error);
            }
        });
    }

    @Test
    public void createNewNodeType() throws Exception {
        NodeTypeRegistrationModel model = new NodeTypeRegistrationModel();
        model.setName("TestNodeType");
        model.setDescription("TestNodeDescription");
        model.setEnabled(true);
        mService.createNewNodeType(model, new CommonRequestListener() {
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
    public void updateExistingNodeType() throws Exception {
        NodeTypeRegistrationModel model = new NodeTypeRegistrationModel();
        model.setName("TestNodeType");
        model.setDescription("TestNodeDescription");
        model.setEnabled(true);
        mService.updateExistingNodeType(NODE_TYPE_HID, model, new CommonRequestListener() {
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
    public void getListDeviceTypes() throws Exception {
        mService.getListDeviceTypes(new ListResultListener<DeviceTypeModel>() {
            @Override
            public void onRequestSuccess(List<DeviceTypeModel> list) {
                assertNotNull(list);
            }

            @Override
            public void onRequestError(ApiError error) {
                assertNull(error);
            }
        });
    }

    @Test
    public void createNewDeviceType() throws Exception {
        DeviceTypeRegistrationModel model = new DeviceTypeRegistrationModel();
        model.setName("TestDeviceType");
        model.setDescription("TestDeviceTypeDescription");
        model.setEnabled(true);
        mService.createNewDeviceType(model, new CommonRequestListener() {
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
    public void updateExistingDeviceType() throws Exception {
        DeviceTypeRegistrationModel model = new DeviceTypeRegistrationModel();
        model.setName("TestDeviceTypeUpdated");
        model.setDescription("TestDeviceTypeDescription");
        model.setEnabled(true);
        mService.updateExistingDeviceType(DEVICE_TYPE_HID, model, new CommonRequestListener() {
            @Override
            public void onRequestSuccess(CommonResponse response) {
                assertNotNull(response);
                assertEquals(DEVICE_TYPE_HID, response.getHid());
            }

            @Override
            public void onRequestError(ApiError error) {
                assertNull(error);
            }
        });
    }

    //TODO:
    //@Test
    public void findDeviceState() throws Exception {
        mService.findDeviceState(DEVICE_HID, new FindDeviceStateListener() {
            @Override
            public void onDeviceStateSuccess(FindDeviceStateResponse response) {
                assertNotNull(response);
            }

            @Override
            public void onDeviceStateError(ApiError apiError) {
                assertNull(apiError);
            }
        });
    }

    @Test
    public void updateDeviceStateTransaction() throws Exception {
        NewDeviceStateTransactionRequest request = new NewDeviceStateTransactionRequest();
        long currentTime = System.currentTimeMillis();
        request.setTimestamp(getFormattedDateTime(currentTime));
        request.setStates(new JsonObject());
        mService.updateDeviceStateTransaction(DEVICE_HID, request, new CommonRequestListener() {
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
    public void createNewDeviceStateTransaction() throws Exception {
        NewDeviceStateTransactionRequest request = new NewDeviceStateTransactionRequest();
        long currentTime = System.currentTimeMillis();
        request.setTimestamp(getFormattedDateTime(currentTime));
        request.addState("someKey", "someValue");
        mService.createNewDeviceStateTransaction(DEVICE_HID, request, new CommonRequestListener() {
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
    public void deviceStateTransactionSucceeded() throws Exception {
        mService.deviceStateTransactionSucceeded(DEVICE_HID, DEVICE_STATE_HID, new MessageStatusListener() {
            @Override
            public void onResponse(MessageStatusResponse response) {
                assertNotNull(response);
            }

            @Override
            public void onError(ApiError error) {
                assertNull(error);
            }
        });
    }

    @Test
    public void deviceStateTransactionReceived() throws Exception {
        mService.deviceStateTransactionReceived(DEVICE_HID, DEVICE_STATE_HID, new MessageStatusListener() {
            @Override
            public void onResponse(MessageStatusResponse response) {
                assertNotNull(response);
            }

            @Override
            public void onError(ApiError error) {
                assertNull(error);
            }
        });
    }

    @Test
    public void deviceStateTransactionFailed() throws Exception {
        ErrorBodyModel model = new ErrorBodyModel();
        model.setError("testError");
        mService.deviceStateTransactionFailed(DEVICE_HID, DEVICE_STATE_HID, model, new MessageStatusListener() {
            @Override
            public void onResponse(MessageStatusResponse response) {
                assertNotNull(response);
            }

            @Override
            public void onError(ApiError error) {
                assertNull(error);
            }
        });
    }

    @Test
    public void putReceivedEvent() throws Exception {
        mService.registerReceivedEvent(EVENT_HID, new CommonRequestListener() {
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
    public void putSucceededEvent() throws Exception {
        mService.eventHandlingSucceed(EVENT_HID, new CommonRequestListener() {
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
    public void putFailedEvent() throws Exception {
        mService.eventHandlingFailed(EVENT_HID, new CommonRequestListener() {
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
    public void sendGatewayError() throws Exception {
        ErrorBodyModel error = new ErrorBodyModel();
        error.setError("Some error");
        mService.sendGatewayError(GATEWAY_HID, error, new CommonRequestListener() {
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
    public void sendDeviceError() throws Exception {
        ErrorBodyModel error = new ErrorBodyModel();
        error.setError("Some error");
        mService.sendDeviceError(DEVICE_HID, error, new CommonRequestListener() {
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
}
