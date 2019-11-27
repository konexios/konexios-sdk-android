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

package com.konexios.api.fakes;

import androidx.annotation.NonNull;

import com.konexios.api.common.RetrofitHolder;
import com.konexios.api.models.ApiError;
import com.konexios.api.rest.RestApiService;

import retrofit2.Response;

/**
 * Created by osminin on 4/4/2017.
 */

public final class FakeRetrofitHolder implements RetrofitHolder {

    private RestApiService mService;
    private String mSecretKey;
    private String mApiKey;

    public FakeRetrofitHolder(RestApiService service) {
        mService = service;
    }

    @Override
    public void setDefaultApiKey(String apiKey) {

    }

    @Override
    public void setDefaultApiSecret(String apiSecret) {

    }

    @Override
    public void setSecretKey(String secretKey) {
        mSecretKey = secretKey;
    }

    @Override
    public String getSecretKey() {
        return mSecretKey;
    }

    @Override
    public String getApiKey() {
        return mApiKey;
    }

    @Override
    public void setApiKey(String apiKey) {
        mApiKey = apiKey;
    }

    @Override
    public RestApiService getRestApiService(@NonNull String endpoint) {
        return mService;
    }

    @Override
    public ApiError convertToApiError(@NonNull Response<?> response) {
        return null;
    }
}
