/*
 * Copyright (c) 2017 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors: Arrow Electronics, Inc.
 */

package com.arrow.acn.api.common;


import android.text.TextUtils;
import android.util.Log;

import com.arrow.acn.api.Constants;
import com.arrow.acn.api.mqtt.common.NoSSLv3SocketFactory;
import com.arrow.acn.api.rest.IotConnectAPIService;
import com.google.firebase.crash.FirebaseCrash;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by osminin on 3/15/2016.
 */
public abstract class RetrofitHolder {
    private static final String TAG = RetrofitHolder.class.getSimpleName();
    private static ApiRequestSigner requestSigner = new ApiRequestSigner();
    private static Retrofit retrofit;
    private static String sApiKey;
    private static String sApiSecret;
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    TimeZone tz = TimeZone.getTimeZone("UTC");
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS'Z'");
                    df.setTimeZone(tz);
                    String nowAsISO = df.format(new Date()).replace(" ", "T");

                    String body = bodyToString(chain.request().body());
                    if (TextUtils.isEmpty(requestSigner.getSecretKey()) &&
                            !TextUtils.isEmpty(sApiSecret)) {
                        requestSigner.setSecretKey(sApiSecret);
                    }
                    String apiKey = TextUtils.isEmpty(requestSigner.getApiKey()) ?
                            sApiKey : requestSigner.getApiKey();
                    String signature = requestSigner.method(chain.request().method())
                            .canonicalUri(chain.request().url().uri().getPath())
                            .apiKey(apiKey).timestamp(nowAsISO).payload(body).signV1();
                    Request request = chain.request().newBuilder()
                            .addHeader("Accept", "application/json")
                            .addHeader("Content-Type", "application/json")
                            .addHeader(Constants.Api.X_ARROW_APIKEY, apiKey)
                            .addHeader(Constants.Api.X_ARROW_DATE, nowAsISO)
                            .addHeader(Constants.Api.X_ARROW_SIGNATURE, signature)
                            .addHeader(Constants.Api.X_ARROW_VERSION, Constants.Api.X_ARROW_VERSION_1)
                            .build();

                    okhttp3.Response response = chain.proceed(request);
                    return response;
                }
            })
            .sslSocketFactory(new NoSSLv3SocketFactory())
            .build();

    public static void setDefaultApiKey(String apiKey) {
        RetrofitHolder.sApiKey = apiKey;
    }

    public static void setDefaultApiSecret(String apiSecret) {
        RetrofitHolder.sApiSecret = apiSecret;
    }

    public static void setSecretKey(String secretKey) {
        requestSigner.setSecretKey(secretKey);
    }

    public static void setApiKey(String apiKey) {
        requestSigner.apiKey(apiKey);
    }

    public static String getApiKey() {
        return requestSigner.getApiKey();
    }

    public static IotConnectAPIService getIotConnectAPIService(String endpoint) {
        if (retrofit == null || !retrofit.baseUrl().toString().equals(endpoint)) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(endpoint)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit.create(IotConnectAPIService.class);
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    private static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            FirebaseCrash.logcat(Log.ERROR, TAG, "bodyToString");
            FirebaseCrash.report(e);
            return "did not work";
        }
    }
}
