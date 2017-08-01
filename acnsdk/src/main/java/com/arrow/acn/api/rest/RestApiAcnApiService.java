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

import android.support.annotation.NonNull;

import com.arrow.acn.api.AbstractTelemetrySenderService;
import com.arrow.acn.api.Constants;
import com.arrow.acn.api.common.ErrorUtils;
import com.arrow.acn.api.listeners.ConnectionListener;
import com.arrow.acn.api.listeners.TelemetryRequestListener;
import com.arrow.acn.api.models.TelemetryModel;

import java.net.HttpURLConnection;
import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * rest telemetry sender
 */

public final class RestApiAcnApiService extends AbstractTelemetrySenderService {
    private IotConnectAPIService mService;

    private CallbackHandler mCallbackHandler = new CallbackHandler();

    public RestApiAcnApiService(IotConnectAPIService service) {
        mService = service;
    }

    @Override
    public void connect(@NonNull ConnectionListener listener) {
        listener.onConnectionSuccess();
    }

    @Override
    public void disconnect() {

    }

    @Override
    public void sendSingleTelemetry(@NonNull TelemetryModel telemetry, TelemetryRequestListener listener) {
        String json = telemetry.getTelemetry();
        RequestBody body = RequestBody.create(Constants.JSON, json);
        Call<ResponseBody> call = mService.sendTelemetry(body);
        call.enqueue(mCallbackHandler.setListener(listener));
    }

    @Override
    public void sendBatchTelemetry(List<TelemetryModel> telemetry, TelemetryRequestListener listener) {
        String json = formatBatchPayload(telemetry);
        RequestBody body = RequestBody.create(Constants.JSON, json);
        Call<ResponseBody> call = mService.sendBatchTelemetry(body);
        call.enqueue(mCallbackHandler.setListener(listener));
    }

    @Override
    public boolean hasBatchMode() {
        return true;
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    private class CallbackHandler implements Callback<ResponseBody> {
        TelemetryRequestListener mListener;

        CallbackHandler setListener(TelemetryRequestListener listener) {
            CallbackHandler callbackHandler;
            //comparing references
            if (listener == mListener) {
                callbackHandler = this;
            } else {
                callbackHandler = new CallbackHandler();
            }
            callbackHandler.mListener = listener;
            return callbackHandler;
        }

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            Timber.v("data sent to cloud: " + response.code());
            if (mListener != null &&
                    response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                mListener.onTelemetrySendSuccess();
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            Timber.e("data sent to cloud failed: " + t.toString());
            mListener.onTelemetrySendError(ErrorUtils.parseError(t));
        }
    }
}
