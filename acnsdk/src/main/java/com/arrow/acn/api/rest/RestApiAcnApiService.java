/*
 * Copyright (c) 2017 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors: Arrow Electronics, Inc.
 */

package com.arrow.acn.api.rest;

import android.util.Log;

import com.arrow.acn.api.AbstractTelemetrySenderService;
import com.arrow.acn.api.Constants;
import com.arrow.acn.api.listeners.ConnectionListener;
import com.arrow.acn.api.models.TelemetryModel;
import com.google.firebase.crash.FirebaseCrash;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by osminin on 6/17/2016.
 */

public final class RestApiAcnApiService extends AbstractTelemetrySenderService {
    private static final String TAG = RestApiAcnApiService.class.getName();
    private final retrofit2.Callback<ResponseBody> mRestApiCallback = new retrofit2.Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            FirebaseCrash.logcat(Log.VERBOSE, TAG, "data sent to cloud: " + response.code());
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            FirebaseCrash.logcat(Log.ERROR, TAG, "data sent to cloud failed: " + t.toString());
        }
    };
    private IotConnectAPIService mService;

    public RestApiAcnApiService(IotConnectAPIService service) {
        mService = service;
    }

    @Override
    public void connect(ConnectionListener listener) {
        listener.onConnectionSuccess();
    }

    @Override
    public void disconnect() {

    }

    @Override
    public void sendSingleTelemetry(TelemetryModel telemetry) {
        String json = telemetry.getTelemetry();
        RequestBody body = RequestBody.create(Constants.JSON, json);
        Call<ResponseBody> call = mService.sendTelemetry(body);
        call.enqueue(mRestApiCallback);
    }

    @Override
    public void sendBatchTelemetry(List<TelemetryModel> telemetry) {
        String json = formatBatchPayload(telemetry);
        RequestBody body = RequestBody.create(Constants.JSON, json);
        Call<ResponseBody> call = mService.sendBatchTelemetry(body);
        call.enqueue(mRestApiCallback);
    }

    @Override
    public boolean hasBatchMode() {
        return true;
    }

    @Override
    public boolean isConnected() {
        return true;
    }
}
