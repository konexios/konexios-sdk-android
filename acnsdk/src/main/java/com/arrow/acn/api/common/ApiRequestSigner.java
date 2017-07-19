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
import android.support.annotation.Nullable;

import com.arrow.acn.api.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import timber.log.Timber;

/**
 * class for signing requests to cloud
 */
public class ApiRequestSigner {

    private String secretKey;
    private String method;
    private String uri;
    private String apiKey;
    private String timestamp;
    @Nullable
    private String payload;
    private List<String> parameters;

    public ApiRequestSigner() {
        Timber.v("ApiRequestSigner: ");
        this.parameters = new ArrayList<>();
        this.payload = "";
    }

    @NonNull
    public ApiRequestSigner payload(@Nullable String payload) {
        if (payload != null)
            this.payload = payload;
        return this;
    }

    @NonNull
    public ApiRequestSigner method(@NonNull String method) {
        this.method = method.toUpperCase();
        return this;
    }

    @NonNull
    public ApiRequestSigner canonicalUri(String uri) {
        this.uri = uri;
        return this;
    }

    public String getApiKey() {
        return apiKey;
    }

    @NonNull
    public ApiRequestSigner setApiKey(String apiKey) {
        Timber.v("setApiKey: ");
        this.apiKey = apiKey;
        return this;
    }

    public String getSecretKey() {
        return secretKey;
    }

    @NonNull
    public ApiRequestSigner setSecretKey(String secretKey) {
        Timber.v("setSecretKey: ");
        this.secretKey = secretKey;
        return this;
    }

    @NonNull
    public ApiRequestSigner timestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    @NonNull
    public String signV1() {
        Timber.v("signV1: ");
        StringBuffer canonicalRequest = new StringBuffer(buildCanonicalRequest());
        canonicalRequest.append(Utils.hash(payload));

        StringBuffer stringToSign = new StringBuffer();
        stringToSign.append(Utils.hash(canonicalRequest.toString())).append('\n');
        stringToSign.append(this.apiKey).append('\n');
        stringToSign.append(this.timestamp).append('\n');
        stringToSign.append(Constants.Api.X_ARROW_VERSION_1);

        String signingKey = Utils.hmacSha256Hex(apiKey, secretKey);
        signingKey = Utils.hmacSha256Hex(timestamp, signingKey);
        signingKey = Utils.hmacSha256Hex(Constants.Api.X_ARROW_VERSION_1, signingKey);
        String result = Utils.hmacSha256Hex(signingKey, stringToSign.toString());
        return result;
    }

    private String buildCanonicalRequest() {
        Timber.v("buildCanonicalRequest: ");
        StringBuffer buffer = new StringBuffer();

        // append method
        buffer.append(method).append('\n');

        // append uri
        buffer.append(uri).append('\n');

        // append parameters
        if (parameters.size() > 0) {
            Collections.sort(parameters);
            for (String p : parameters) {
                buffer.append(p).append('\n');
            }
        }
        return buffer.toString();
    }
}
