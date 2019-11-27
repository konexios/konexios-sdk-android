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

import android.os.Parcel;
import android.os.Parcelable;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.JsonObject;
import com.konexios.api.models.AccountRequest;
import com.konexios.api.models.AccountResponse;
import com.konexios.api.models.ActionParametersModel;
import com.konexios.api.models.ActionResponseModel;
import com.konexios.api.models.ApiError;
import com.konexios.api.models.AuditLogModel;
import com.konexios.api.models.AuditLogsQuery;
import com.konexios.api.models.CommonResponse;
import com.konexios.api.models.ConfigResponse;
import com.konexios.api.models.CreateUpdateResponse;
import com.konexios.api.models.DeviceActionModel;
import com.konexios.api.models.DeviceActionTypeModel;
import com.konexios.api.models.DeviceModel;
import com.konexios.api.models.DeviceRegistrationModel;
import com.konexios.api.models.DeviceRegistrationResponse;
import com.konexios.api.models.DeviceTypeModel;
import com.konexios.api.models.DeviceTypeRegistrationModel;
import com.konexios.api.models.DeviceTypeTelemetryModel;
import com.konexios.api.models.ErrorBodyModel;
import com.konexios.api.models.FindDeviceStateResponse;
import com.konexios.api.models.FindDevicesRequest;
import com.konexios.api.models.FindTelemetryRequest;
import com.konexios.api.models.GatewayCommand;
import com.konexios.api.models.GatewayEventModel;
import com.konexios.api.models.GatewayModel;
import com.konexios.api.models.HistoricalEventsRequest;
import com.konexios.api.models.MessageStatusResponse;
import com.konexios.api.models.NodeModel;
import com.konexios.api.models.NodeRegistrationModel;
import com.konexios.api.models.NodeTypeModel;
import com.konexios.api.models.NodeTypeRegistrationModel;
import com.konexios.api.models.StateModel;
import com.konexios.api.models.TelemetryCountRequest;
import com.konexios.api.models.TelemetryCountResponse;
import com.konexios.api.models.TelemetryItemModel;
import com.konexios.api.models.TelemetryModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import static org.junit.Assert.assertEquals;

/**
 * Created by osminin on 5/10/2017.
 */

@RunWith(AndroidJUnit4.class)
public class ParcelableTest {

    private PodamFactory mFactory;

    @Before
    public void setUp() throws Exception {
        mFactory = new PodamFactoryImpl();
    }

    @Test
    public void auditLogsTest() throws Exception {
        AuditLogsQuery model = new AuditLogsQuery();
        model.setSize(10);
        model.setCreatedDateFrom("CreatedDateFrom");
        model.setCreatedDateTo("CreatedDateTo");
        model.setPage(5);
        model.setSortDirection("direction");
        model.setSortField("sort");
        model.setTypes(Arrays.asList(new String[] {"one", "two", "three"}));
        model.setUserHids(Arrays.asList(new String[] {"four", "five", "six"}));
        Parcel parcel = Parcel.obtain();
        model.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        AuditLogsQuery fromParcel = AuditLogsQuery.CREATOR.createFromParcel(parcel);
        assertEquals(model, fromParcel);
    }

    @Test
    public void actionResponse() throws Exception {
        ActionResponseModel model = new ActionResponseModel();
        ArrayList<DeviceActionModel> list = new ArrayList(10);
        for (int i = 0; i < 10; i++) {
            list.add(mFactory.manufacturePojo(DeviceActionModel.class));
        }
        model.setActions(list);
        Parcel parcel = Parcel.obtain();
        model.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        ActionResponseModel fromParcel = ActionResponseModel.CREATOR.createFromParcel(parcel);
        assertEquals(model, fromParcel);
    }

    @Test
    public void deviceRegistration() throws Exception {
        DeviceRegistrationModel model = new DeviceRegistrationModel();
        model.setName("name");
        model.setEnabled(false);
        model.setGatewayHid("hid");
        model.setInfo(new JsonObject());
        model.setNodeHid("node hid");
        model.setProperties(new JsonObject());
        model.setType("type");
        model.setUid("uid");
        model.setUserHid("user hid");
        model.setTags(Arrays.asList(new String[] {"one", "two", "three"}));
        Parcel parcel = Parcel.obtain();
        model.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        DeviceRegistrationModel fromParcel = DeviceRegistrationModel.CREATOR.createFromParcel(parcel);
        assertEquals(model, fromParcel);
    }

    @Test
    public void deviceType() throws Exception {
        DeviceTypeModel model = new DeviceTypeModel();
        model.setCreatedBy("createdBy");
        model.setName("name");
        model.setCreatedDate("createdDate");
        model.setDescription("direction");
        model.setEnabled(true);
        model.setHid("hid");
        model.setLastModifiedBy("lastModifiedBy");
        model.setLastModifiedDate("lastModifiedDate");
        model.setPri("pri");
        model.setLinks(new JsonObject());
        ArrayList<DeviceTypeTelemetryModel> list = new ArrayList(10);
        for (int i = 0; i < 10; i++) {
            DeviceTypeTelemetryModel telemetryModel = new DeviceTypeTelemetryModel();
            telemetryModel.setName("name");
            telemetryModel.setDescription("description");
            telemetryModel.setType("type");
            Map<String, String> keyMap = new HashMap<>();
            keyMap.put("key", "value");
            telemetryModel.setVariables(keyMap);
            list.add(telemetryModel);
        }
        model.setTelemetries(list);
        Parcel parcel = Parcel.obtain();
        model.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        DeviceTypeModel fromParcel = DeviceTypeModel.CREATOR.createFromParcel(parcel);
        assertEquals(model, fromParcel);
    }

    @Test
    public void deviceTypeRegistration() throws Exception {
        DeviceTypeRegistrationModel model = new DeviceTypeRegistrationModel();
        model.setName("Name");
        model.setEnabled(true);
        model.setDescription("description");
        ArrayList<DeviceTypeTelemetryModel> list = new ArrayList(10);
        for (int i = 0; i < 10; i++) {
            DeviceTypeTelemetryModel telemetryModel = new DeviceTypeTelemetryModel();
            telemetryModel.setName("name");
            telemetryModel.setDescription("description");
            telemetryModel.setType("type");
            Map<String, String> keyMap = new HashMap<>();
            keyMap.put("key", "value");
            telemetryModel.setVariables(keyMap);
            list.add(telemetryModel);
        }
        model.setTelemetries(list);
        Parcel parcel = Parcel.obtain();
        model.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        DeviceTypeRegistrationModel fromParcel = DeviceTypeRegistrationModel.CREATOR.createFromParcel(parcel);
        assertEquals(model, fromParcel);
    }

    @Test
    public void gatewayEvent() throws Exception {
        GatewayEventModel model = new GatewayEventModel();
        model.setName("name");
        model.setHid("hid");
        model.setEncrypted(true);
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        model.setParameters(map);
        Parcel parcel = Parcel.obtain();
        model.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        GatewayEventModel fromParcel = GatewayEventModel.CREATOR.createFromParcel(parcel);
        assertEquals(model, fromParcel);
    }

    @Test
    public void historicalEvent() throws Exception {
        HistoricalEventsRequest model = new HistoricalEventsRequest();
        model.setHid("hid");
        model.setPage(10);
        model.setSize(10);
        model.setCreatedDateFrom("CreatedDateFrom");
        model.setCreatedDateTo("CreatedDateTo");
        model.setSortDirection("direction");
        model.setStatuses(Arrays.asList(new String[] {"one", "two", "three"}));
        model.setSystemNames(Arrays.asList(new String[] {"four", "five", "six"}));
        Parcel parcel = Parcel.obtain();
        model.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        HistoricalEventsRequest fromParcel = HistoricalEventsRequest.CREATOR.createFromParcel(parcel);
        assertEquals(model, fromParcel);
    }

    @Test
    public void commonPojoParcelabelTest() throws Exception {
        Class[] classes = {
                AccountRequest.class,
                AccountResponse.class,
                ActionParametersModel.class,
                ApiError.class,
                AuditLogModel.class,
                CommonResponse.class,
                ConfigResponse.class,
                CreateUpdateResponse.class,
                DeviceActionModel.class,
                DeviceActionTypeModel.class,
                DeviceModel.class,
                DeviceRegistrationResponse.class,
                ErrorBodyModel.class,
                FindDevicesRequest.class,
                FindDeviceStateResponse.class,
                FindTelemetryRequest.class,
                GatewayCommand.class,
                GatewayModel.class,
                MessageStatusResponse.class,
                NodeModel.class,
                NodeRegistrationModel.class,
                NodeTypeModel.class,
                NodeTypeRegistrationModel.class,
                StateModel.class,
                TelemetryCountRequest.class,
                TelemetryCountResponse.class,
                TelemetryItemModel.class,
                TelemetryModel.class};
        for (Class clazz : classes) {
            parcelablePojoTest(clazz);
        }
    }

    private <T extends Parcelable> void parcelablePojoTest(Class<T> clazz) throws NoSuchFieldException, IllegalAccessException {
        T model = mFactory.manufacturePojo(clazz);
        Parcel parcel = Parcel.obtain();
        model.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        Parcelable.Creator<T> creator = (Parcelable.Creator<T>) clazz.getField("CREATOR").get(null);
        T fromParcel = creator.createFromParcel(parcel);
        assertEquals(model, fromParcel);
    }
}
