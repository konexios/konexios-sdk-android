/*
 * Copyright (c) 2017 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors: Arrow Electronics, Inc.
 */

package com.arrow.acn.api;


import android.support.annotation.Keep;

import com.arrow.acn.api.common.RetrofitHolderImpl;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * Main entry point for AcnApiService creation
 */

@Keep
public final class AcnApi {

    @Keep
    public static final class Builder {
        private String mEndpoint;
        private String mApiKey;
        private String mApiSecret;

        private String mMqttHost;
        private String mMqttPrefix;

        private boolean isDebug;

        private Executor mCallbackExecutor;
        private ExecutorService mHttpExecutorService;
        private static AcnApiService mService;

        /**
         * sets endpoint server environment url and user credentials. Should be called before any other calls
         *
         * @param endpoint  - String url like "http://pegasuskronos01-dev.cloudapp.net:28880"
         * @param apiKey
         * @param apiSecret
         */
        public Builder setRestEndpoint(String endpoint, String apiKey, String apiSecret) {
            mEndpoint = endpoint;
            mApiKey = apiKey;
            mApiSecret = apiSecret;
            return this;
        }

        /**
         * sets mqtt server host and userName prefix. It's used only for connection to simple mqtt
         * server type (aka ArrowConnect ot IotConnect types). It's useless for Azure, Aws and Ibm types
         * of connection because for them credentials are returned from getGatewayConfig call.
         *
         * @param host   String contains host like "tcp://pegasusqueue01-dev.cloudapp.net:46953"
         * @param prefix String contains prefix like "/themis.dev"
         */
        public Builder setMqttEndpoint(String host, String prefix) {
            mMqttHost = host;
            mMqttPrefix = prefix;
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

        public static AcnApiService resetRestEndpoint(String endpoint) {
            ((AcnApiImpl) mService).resetRestEndpoint(endpoint);
            return mService;
        }

        public AcnApiService build() {
            mService = new AcnApiImpl(new RetrofitHolderImpl(mCallbackExecutor, mHttpExecutorService),
                    new SenderServiceFactoryImpl(), isDebug);
            ((AcnApiImpl) mService).setRestEndpoint(mEndpoint, mApiKey, mApiSecret);
            ((AcnApiImpl) mService).setMqttEndpoint(mMqttHost, mMqttPrefix);
            return mService;
        }
    }
}
