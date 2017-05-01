/*
 * Copyright (c) 2017 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors: Arrow Electronics, Inc.
 */

package com.arrow.acn.sample.device;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.arrow.acn.api.AcnApiService;
import com.arrow.acn.api.listeners.CommonRequestListener;
import com.arrow.acn.api.listeners.FindDeviceStateListener;
import com.arrow.acn.api.listeners.MessageStatusListener;
import com.arrow.acn.api.listeners.RegisterDeviceListener;
import com.arrow.acn.api.models.ApiError;
import com.arrow.acn.api.models.CommonResponse;
import com.arrow.acn.api.models.DeviceRegistrationModel;
import com.arrow.acn.api.models.DeviceRegistrationResponse;
import com.arrow.acn.api.models.FindDeviceStateResponse;
import com.arrow.acn.api.models.MessageStatusResponse;
import com.arrow.acn.api.models.NewDeviceStateTransactionRequest;
import com.arrow.acn.api.models.TelemetryModel;
import com.arrow.acn.sample.App;
import com.arrow.acn.sample.InternalSensorsView;
import com.arrow.acn.sample.TelemetrySender;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DeviceAbstract implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private final static String TAG = DeviceAbstract.class.getSimpleName();

    protected final Context mContext;
    @NonNull
    private Map<String, IotParameter> iotParamsMap = new HashMap<>();
    @Nullable
    protected GoogleApiClient mGoogleApiClient;
    private String mDeviceHid;
    private String mGatewayHid;
    private String mUserHid;
    private Handler mUiHandler;

    private InternalSensorsView mView;
    private TelemetrySender mSender;

    public DeviceAbstract(Context context) {
        this.mContext = context;
        mUiHandler = new Handler();
    }

    protected void initializeLocationService() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
    }

    public synchronized void putIotParams(@NonNull IotParameter... params) {
        for (IotParameter param : params) {
            iotParamsMap.put(param.getKey(), param);
        }
    }

    @NonNull
    private synchronized List<IotParameter> getIotParams() {
        if (iotParamsMap.isEmpty())
            return Collections.emptyList();
        List<IotParameter> result = new ArrayList<>(iotParamsMap.values());
        iotParamsMap.clear();
        return result;
    }

    public void setView(InternalSensorsView view) {
        mView = view;
    }

    public void setGatewayHid(String gatewayHid) {
        mGatewayHid = gatewayHid;
    }

    public void setUserHid(String userHid) {
        mUserHid = userHid;
    }

    public void setSender(TelemetrySender sender) {
        mSender = sender;
    }

    @NonNull
    public abstract DeviceType getDeviceType();

    @NonNull
    public abstract String getDeviceUId();

    public abstract void enable();

    public abstract void disable();

    @NonNull
    public abstract String getDeviceTypeName();

    protected DeviceRegistrationModel getRegisterPayload() {
        DeviceRegistrationModel payload = new DeviceRegistrationModel();
        payload.setUid(getDeviceUId());
        payload.setName(getDeviceType().name());
        payload.setGatewayHid(mGatewayHid);
        payload.setUserHid(mUserHid);
        payload.setType(getDeviceTypeName());
        payload.setEnabled(true);
        return payload;
    }

    protected void checkAndRegisterDevice() {
        if (TextUtils.isEmpty(mDeviceHid)) {
            if (!getDeviceUId().contains("null")) {
                FirebaseCrash.logcat(Log.INFO, TAG, "checkAndRegisterDevice() launching registration ...");
                DeviceRegistrationModel payload = getRegisterPayload();
                mSender.registerDevice(payload, new RegisterDeviceListener() {
                    @Override
                    public void onDeviceRegistered(@NonNull DeviceRegistrationResponse response) {
                        mDeviceHid = response.getHid();
                        mView.setDeviceId(getDeviceUId());
                        FirebaseCrash.logcat(Log.INFO, TAG, "device hid: " + mDeviceHid);
                    }

                    @Override
                    public void onDeviceRegistrationFailed(@NonNull ApiError error) {
                        FirebaseCrash.logcat(Log.ERROR, TAG, "checkAndRegisterDevice, code: "
                                + error.getStatus() + ", mesage: " + error.getMessage());
                    }
                });
            }
        }
    }

    private static String getFormattedDateTime(Long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String formattedDate = format.format(date);
        return formattedDate;
    }

    public void pollingTask() {
        try {
            putNewLocation();


            final List<IotParameter> params = getIotParams();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(TelemetriesNames.TIMESTAMP, System.currentTimeMillis());
            jsonObject.addProperty(TelemetriesNames.DEVICE_HID, mDeviceHid);

            Map<String, String> telemetryMap = new HashMap<>();
            for (IotParameter param : params) {
                jsonObject.addProperty(param.getKey(), param.getValue());
                telemetryMap.put(param.getKey(), param.getValue());
            }
            updateUi(telemetryMap);
            TelemetryModel telemetryModel = new TelemetryModel();
            telemetryModel.setTelemetry(jsonObject.toString());
            telemetryModel.setDeviceType(getDeviceType().toString());
            mSender.sendTelemetry(telemetryModel);
            FirebaseCrash.logcat(Log.VERBOSE, TAG, "PollingTask completed");
        } catch (Exception e) {
            FirebaseCrash.logcat(Log.ERROR, TAG, "getPollingTask");
            FirebaseCrash.report(e);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        putNewLocation();
    }

    private void putNewLocation() {
        if (mGoogleApiClient != null) {
            Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (currentLocation != null) {
                putIotParams(new IotParameter("f|longitude", currentLocation.getLongitude() + ""));
                putIotParams(new IotParameter("f|latitude", currentLocation.getLatitude() + ""));
            } else {
                FirebaseCrash.logcat(Log.INFO, TAG, "putNewLocation() no last known location found");
            }
        }
    }

    private void updateUi(final Map<String, String> telemetryMap) {
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                mView.update(telemetryMap);
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {
        //TODO: handle
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //TODO: handle
    }

    @Override
    public void onLocationChanged(Location location) {
        putNewLocation();
    }
}
