package com.arrow.kronos.api.rest;

import android.os.Bundle;
import android.util.Log;

import com.arrow.kronos.api.AbstractKronosApiService;
import com.arrow.kronos.api.Constants;
import com.google.firebase.crash.FirebaseCrash;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by osminin on 6/17/2016.
 */

public final class RestApiKronosApiService extends AbstractKronosApiService {
    private static final String TAG = RestApiKronosApiService.class.getName();

    private final retrofit2.Callback<ResponseBody> mRestApiCallback = new retrofit2.Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            FirebaseCrash.logcat(Log.VERBOSE, TAG, "data sent to cloud: " + response.code());
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            FirebaseCrash.logcat(Log.ERROR,  TAG, "data sent to cloud failed: " + t.toString());
        }
    };

    @Override
    public void connect(String applicationHid) {
        registerGateway(applicationHid, null);
    }

    @Override
    public void disconnect() {

    }

    @Override
    public void sendSingleTelemetry(Bundle bundle) {
        String json = bundle.getString(Constants.EXTRA_DATA_LABEL_TELEMETRY);
        RequestBody body = RequestBody.create(Constants.JSON, json);
        Call<ResponseBody> call = getService().sendTelemetry(body);
        call.enqueue(mRestApiCallback);
    }

    @Override
    public void sendBatchTelemetry(List<Bundle> telemetry) {
        String json = formatBatchPayload(telemetry);
        RequestBody body = RequestBody.create(Constants.JSON, json);
        Call<ResponseBody> call = getService().sendBatchTelemetry(body);
        call.enqueue(mRestApiCallback);
    }

    @Override
    public boolean hasBatchMode() {
        return true;
    }

    @Override
    public void setMqttEndpoint(String host, String prefix) {
    }
}
