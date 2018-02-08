/*
 * Copyright (c) 2017 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors: Arrow Electronics, Inc.
 */

package com.arrow.acn.sample;

import android.support.multidex.MultiDexApplication;

import com.arrow.acn.api.AcnApi;
import com.arrow.acn.api.AcnApiService;

/**
 * Created by osminin on 4/20/2017.
 */

public final class App extends MultiDexApplication {
    // DEV
    public static final String BASE_IOT_CONNECT_URL_DEV = "http://pgsdev01.arrowconnect.io:11003";
    //TODO: define ApiKey and ApiSecret variables in gradle.properties file
    public static final String DEFAULT_API_KEY = BuildConfig.API_KEY;
    public static final String DEFAULT_API_SECRET = BuildConfig.API_SECRET;
    public static final String MQTT_CONNECT_URL_DEV = "ssl://mqtt-a01.arrowconnect.io";
    public static final String MQTT_CLIENT_PREFIX_DEV = "/pegasus";

    private static AcnApiService mAcnApiService;

    public static AcnApiService getAcnApiService() {
        return mAcnApiService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAcnApiService = new AcnApi.Builder()
                .setRestEndpoint(BASE_IOT_CONNECT_URL_DEV, DEFAULT_API_KEY, DEFAULT_API_SECRET)
                .setMqttEndpoint(MQTT_CONNECT_URL_DEV, MQTT_CLIENT_PREFIX_DEV)
                .setDebug(true)
                .build();
    }
}
