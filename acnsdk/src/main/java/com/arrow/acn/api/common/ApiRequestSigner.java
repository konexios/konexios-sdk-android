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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import timber.log.Timber;

/**
 * Created by osminin on 4/8/2016.
 */
public class ApiRequestSigner {
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

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

    private static String bytesToHex(@NonNull byte[] bytes) {
        Timber.v("bytesToHex: ");
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars).toLowerCase();
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
        canonicalRequest.append(hash(payload));

        StringBuffer stringToSign = new StringBuffer();
        stringToSign.append(hash(canonicalRequest.toString())).append('\n');
        stringToSign.append(this.apiKey).append('\n');
        stringToSign.append(this.timestamp).append('\n');
        stringToSign.append(Constants.Api.X_ARROW_VERSION_1);

        String signingKey = encode(apiKey, secretKey);
        signingKey = encode(timestamp, signingKey);
        signingKey = encode(Constants.Api.X_ARROW_VERSION_1, signingKey);
        String result = encode(signingKey, stringToSign.toString());
        return result;
    }

    @NonNull
    public String encode(@NonNull String key, @NonNull String data) {
        Timber.v("encode: ");
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            byte[] raw = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
            String result = bytesToHex(raw);
            return result;
        } catch (Exception e) {
            Timber.e(e);
        }
        return "";
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

    @Nullable
    private String hash(@NonNull String value) {
        Timber.v("hash: ");
        MessageDigest digest;
        String result = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(value.getBytes("UTF-8"));
            result = bytesToHex(hash);
        } catch (Exception e) {
            Timber.e(e);
        }
        return result;
    }
}
