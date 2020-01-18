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

package com.konexios.api;


import androidx.annotation.Keep;

import com.konexios.api.common.RetrofitHolderImpl;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * Main entry point for ApiService creation
 */

@Keep
public final class Api {

    @Keep
    public static final class Builder {
        private String mEndpoint = Constants.Api.DEFAULT_GATEWAY_API_URL;
        private String mApiKey;
        private String mApiSecret;

        private String mMqttHost = Constants.Api.DEFAULT_MQTT_URL;
        private String mMqttVirtualHost = Constants.Api.DEFAULT_MQTT_VIRTUAL_HOST;

        private boolean isDebug;

        private Executor mCallbackExecutor;
        private ExecutorService mHttpExecutorService;
        private static ApiService mService;

        /**
         * sets endpoint server environment url and user credentials. Should be called before any other calls
         *
         * @param endpoint - String url like "https://api.konexios.io"
         */
        public Builder setRestEndpoint(String endpoint) {
            mEndpoint = endpoint;
            return this;
        }

        public Builder setKeys(String apiKey, String apiSecret) {
            mApiKey = apiKey;
            mApiSecret = apiSecret;
            return this;
        }

        /**
         * sets mqtt server host and userName virtualHost. It's used only for connection to simple mqtt
         * server type (aka ArrowConnect ot IotConnect types). It's useless for Azure, Aws and Ibm types
         * of connection because for them credentials are returned from getGatewayConfig call.
         *
         * @param host        String contains host like "ssl://mqtt.konexios.io"
         * @param virtualHost String contains virtualHost like "/pegasus"
         */
        public Builder setMqttEndpoint(String host, String virtualHost) {
            mMqttHost = host;
            mMqttVirtualHost = virtualHost;
            return this;
        }

        /**
         * enables debug mode
         *
         * @param debug - true if you need debug information like logs, false otherwise
         */
        public Builder setDebug(boolean debug) {
            isDebug = debug;
            return this;
        }

        /**
         * sets the Executor which will be used for executing callbacks
         *
         * @param executor - Executor
         */
        public Builder setCallbackExecutor(Executor executor) {
            mCallbackExecutor = executor;
            return this;
        }

        /**
         * sets the Executor which will be used for performing http requests
         *
         * @param service - ExecutorService
         */
        public Builder setHttpExecutorService(ExecutorService service) {
            mHttpExecutorService = service;
            return this;
        }

        public static ApiService resetRestEndpoint(String endpoint) {
            ((ApiImpl) mService).resetRestEndpoint(endpoint);
            return mService;
        }

        public static ApiService resetDefaultAdminEndpoint() {
            return resetRestEndpoint(Constants.Api.DEFAULT_ADMIN_API_URL);
        }

        public static ApiService resetDefaultGatewayEndpoint() {
            return resetRestEndpoint(Constants.Api.DEFAULT_GATEWAY_API_URL);
        }

        public ApiService build() {
            mService = new ApiImpl(new RetrofitHolderImpl(mCallbackExecutor, mHttpExecutorService),
                    new SenderServiceFactoryImpl(), isDebug);
            ((ApiImpl) mService).setRestEndpoint(mEndpoint, mApiKey, mApiSecret);
            ((ApiImpl) mService).setMqttEndpoint(mMqttHost, mMqttVirtualHost);
            return mService;
        }
    }
}
