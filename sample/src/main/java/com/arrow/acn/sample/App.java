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

import android.app.Application;

import com.arrow.acn.api.AcnApi;
import com.arrow.acn.api.AcnApiService;

/**
 * Created by osminin on 4/20/2017.
 */

public final class App extends Application {
    // DEV
    public static final String BASE_IOT_CONNECT_URL_DEV = "http://pgsdev01.arrowconnect.io:12001";
    //TODO: replace with real keys
    public static final String DEFAULT_API_KEY = "api key goes here";
    public static final String DEFAULT_API_SECRET = "api secret goes here";
    public static final String MQTT_CONNECT_URL_DEV = "tcp://pgsdev01.arrowconnect.io:1883";
    public static final String MQTT_CLIENT_PREFIX_DEV = "/themis.dev";

    private static AcnApiService mAcnApiService;

    @Override
    public void onCreate() {
        super.onCreate();
        mAcnApiService = new AcnApi.Builder()
                //TODO: replace DEFAULT_API_KEY and DEFAULT_API_SECRET with valid keys
                .setRestEndpoint(BASE_IOT_CONNECT_URL_DEV, DEFAULT_API_KEY, DEFAULT_API_SECRET)
                .setMqttEndpoint(MQTT_CONNECT_URL_DEV, MQTT_CLIENT_PREFIX_DEV)
                .setDebug(true)
                .build();
    }

    public static AcnApiService getAcnApiService() {
        return mAcnApiService;
    }
}
