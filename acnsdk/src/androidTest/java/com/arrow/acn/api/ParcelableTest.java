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

import android.os.Parcel;
import android.os.Parcelable;
import android.support.test.runner.AndroidJUnit4;

import com.arrow.acn.api.models.AccountRequest;
import com.arrow.acn.api.models.AccountResponse;
import com.arrow.acn.api.models.ActionParametersModel;
import com.arrow.acn.api.models.ApiError;
import com.arrow.acn.api.models.AuditLogModel;
import com.arrow.acn.api.models.CommonResponse;
import com.arrow.acn.api.models.ConfigResponse;
import com.arrow.acn.api.models.CreateUpdateResponse;
import com.arrow.acn.api.models.DeviceActionModel;
import com.arrow.acn.api.models.DeviceActionTypeModel;
import com.arrow.acn.api.models.DeviceModel;
import com.arrow.acn.api.models.DeviceRegistrationResponse;
import com.arrow.acn.api.models.DeviceTypeTelemetryModel;
import com.arrow.acn.api.models.ErrorBodyModel;
import com.arrow.acn.api.models.FindDeviceStateResponse;
import com.arrow.acn.api.models.FindTelemetryRequest;
import com.arrow.acn.api.models.GatewayCommand;
import com.arrow.acn.api.models.GatewayModel;
import com.arrow.acn.api.models.HistoricalTelemetryModel;
import com.arrow.acn.api.models.MessageStatusResponse;
import com.arrow.acn.api.models.NodeModel;
import com.arrow.acn.api.models.NodeRegistrationModel;
import com.arrow.acn.api.models.NodeTypeModel;
import com.arrow.acn.api.models.NodeTypeRegistrationModel;
import com.arrow.acn.api.models.TelemetryModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

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
    public void pojosTest() throws Exception {
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
                DeviceTypeTelemetryModel.class,
                ErrorBodyModel.class,
                FindDeviceStateResponse.class,
                FindTelemetryRequest.class,
                GatewayCommand.class,
                GatewayModel.class,
                HistoricalTelemetryModel.class,
                MessageStatusResponse.class,
                NodeModel.class,
                NodeRegistrationModel.class,
                NodeTypeModel.class,
                NodeTypeRegistrationModel.class,
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
