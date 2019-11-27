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

package com.konexios.sample;

import androidx.multidex.MultiDexApplication;

import com.konexios.api.Api;
import com.konexios.api.ApiService;

/**
 * Created by osminin on 4/20/2017.
 */

public final class App extends MultiDexApplication {
    //TODO: define ApiKey and ApiSecret variables in gradle.properties file
    public static final String DEFAULT_API_KEY = BuildConfig.API_KEY;
    public static final String DEFAULT_API_SECRET = BuildConfig.API_SECRET;

    private static ApiService mApiService;

    public static ApiService getAcnApiService() {
        return mApiService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApiService = new Api.Builder()
                .setKeys(DEFAULT_API_KEY, DEFAULT_API_SECRET)
                .setDebug(true)
                .build();
    }
}
