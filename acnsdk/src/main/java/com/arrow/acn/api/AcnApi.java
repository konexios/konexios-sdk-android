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

import okhttp3.Dispatcher;

/**
 * Created by osminin on 9/21/2016.
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

        public Builder setDebug(boolean debug) {
            isDebug = debug;
            return this;
        }

        public Builder setCallbackExecutor(Executor executor) {
            mCallbackExecutor = executor;
            return this;
        }

        public Builder setHttpExecutorService(ExecutorService service) {
            mHttpExecutorService = service;
            return this;
        }

        public AcnApiService build() {
            AcnApiImpl service = new AcnApiImpl(new RetrofitHolderImpl(mCallbackExecutor, mHttpExecutorService),
                    new SenderServiceFactoryImpl(), isDebug);
            service.setRestEndpoint(mEndpoint, mApiKey, mApiSecret);
            service.setMqttEndpoint(mMqttHost, mMqttPrefix);
            return service;
        }
    }
}
