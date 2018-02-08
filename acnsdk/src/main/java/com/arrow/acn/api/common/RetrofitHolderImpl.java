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


import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.arrow.acn.api.Constants;
import com.arrow.acn.api.models.ApiError;
import com.arrow.acn.api.mqtt.common.NoSSLv3SocketFactory;
import com.arrow.acn.api.rest.IotConnectAPIService;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import okhttp3.Dispatcher;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * holds retrofit stuff
 */
public class RetrofitHolderImpl implements RetrofitHolder {
    @NonNull
    private final ApiRequestSigner mRequestSigner = new ApiRequestSigner();
    private Retrofit mRetrofit;
    private String mApiKey;
    private String mApiSecret;
    private final Executor mExecutor;
    final private OkHttpClient okHttpClient;


    public RetrofitHolderImpl(Executor executor, ExecutorService httpRequestsExecutor) {
        Timber.v("RetrofitHolderImpl: ");
        mExecutor = executor;
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new RequestInterceptor())
                .sslSocketFactory(new NoSSLv3SocketFactory())
                .connectTimeout(20000, TimeUnit.MILLISECONDS)
                .dispatcher(new Dispatcher(httpRequestsExecutor))
                .build();
    }

    @Override
    public void setDefaultApiKey(String apiKey) {
        Timber.v("setDefaultApiKey: ");
        mApiKey = apiKey;
    }

    @Override
    public void setDefaultApiSecret(String apiSecret) {
        Timber.v("setDefaultApiSecret: ");
        mApiSecret = apiSecret;
    }

    @Override
    public String getSecretKey() {
        Timber.v("getSecretKey: ");
        return mRequestSigner.getSecretKey();
    }

    @Override
    public void setSecretKey(String secretKey) {
        Timber.v("setSecretKey: ");
        mRequestSigner.setSecretKey(secretKey);
    }

    @Override
    public String getApiKey() {
        Timber.v("getApiKey: ");
        return mRequestSigner.getApiKey();
    }

    @Override
    public void setApiKey(String apiKey) {
        Timber.v("setApiKey: ");
        mRequestSigner.setApiKey(apiKey);
    }

    @Override
    public IotConnectAPIService getIotConnectAPIService(@NonNull String endpoint) {
        Timber.v("getIotConnectAPIService: ");
        if (mRetrofit == null || !mRetrofit.baseUrl().toString().equals(endpoint)) {
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(endpoint)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(okHttpClient);
            if (mExecutor != null) {
                builder.callbackExecutor(mExecutor);
            }
            mRetrofit = builder.build();

        }
        return mRetrofit.create(IotConnectAPIService.class);
    }

    @Override
    public ApiError convertToApiError(@NonNull Response<?> response) {
        Timber.v("convertToApiError: ");
        return ErrorUtils.parseError(response, mRetrofit);
    }

    private String bodyToString(final RequestBody request) {
        Timber.v("bodyToString: ");
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (@NonNull final IOException e) {
            Timber.e(e);
            return "did not work";
        }
    }

    private class RequestInterceptor implements Interceptor {

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            TimeZone tz = TimeZone.getTimeZone("UTC");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS'Z'");
            df.setTimeZone(tz);
            String nowAsISO = df.format(new Date()).replace(" ", "T");

            String body = bodyToString(chain.request().body());
            if (TextUtils.isEmpty(mRequestSigner.getSecretKey()) &&
                    !TextUtils.isEmpty(mApiSecret)) {
                mRequestSigner.setSecretKey(mApiSecret);
            }
            String apiKey = TextUtils.isEmpty(mRequestSigner.getApiKey()) ?
                    mApiKey : mRequestSigner.getApiKey();
            String signature = mRequestSigner.method(chain.request().method())
                    .canonicalUri(chain.request().url().uri().getPath())
                    .setApiKey(apiKey).timestamp(nowAsISO).payload(body).signV1();
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
    }
}
