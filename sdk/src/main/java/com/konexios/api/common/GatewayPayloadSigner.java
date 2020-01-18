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

package com.konexios.api.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import timber.log.Timber;

import static com.konexios.api.Constants.Api.X_ARROW_VERSION_1;
import static com.konexios.api.Constants.Api.X_ARROW_VERSION_2;

/**
 * class for signing payload received as a command from cloud
 */

public final class GatewayPayloadSigner {
    private String secretKey;
    private String hid;
    private String name;
    private boolean encrypted;
    private String apiKey;
    private List<String> parameters = new ArrayList<>();

    private GatewayPayloadSigner(String secretKey) {
        this.secretKey = secretKey;
    }

    public static GatewayPayloadSigner create(String secretKey) {
        Timber.v("create: secretKey = %s", secretKey);
        return new GatewayPayloadSigner(secretKey);
    }

    public GatewayPayloadSigner withHid(String hid) {
        Timber.v("withHid: hid = %s", hid);
        this.hid = hid;
        return this;
    }

    public GatewayPayloadSigner withName(String name) {
        Timber.v("withName: name = %s", name);
        this.name = name;
        return this;
    }

    public GatewayPayloadSigner withEncrypted(boolean encrypted) {
        Timber.v("withEncrypted: encrypted = %s", encrypted);
        this.encrypted = encrypted;
        return this;
    }

    public GatewayPayloadSigner withApiKey(String apiKey) {
        Timber.v("withApiKey: apiKey = %s", apiKey);
        this.apiKey = apiKey;
        return this;
    }

    public GatewayPayloadSigner withParameter(String name, String value) {
        Timber.v("withParameter: name = %, value = %s", name, value);
        parameters.add(String.format("%s=%s", name.toLowerCase(), value));
        return this;
    }

    public String signV1() {
        Timber.v("signV1: ");
        StringBuilder stringToSign = new StringBuilder();
        stringToSign.append(Utils.hash(buildCanonicalRequest())).append('\n');
        stringToSign.append(apiKey).append('\n');
        stringToSign.append(X_ARROW_VERSION_1);

        String signingKey = Utils.hmacSha256Hex(X_ARROW_VERSION_1, Utils.hmacSha256Hex(apiKey, secretKey));

        String signature = Utils.hmacSha256Hex(signingKey, stringToSign.toString());

        return signature;
    }

    public String signV2() {
        Timber.v("signV2: ");
        StringBuilder stringToSign = new StringBuilder();
        stringToSign.append(Utils.hash(buildCanonicalRequest())).append('\n');
        stringToSign.append(apiKey).append('\n');
        stringToSign.append(X_ARROW_VERSION_2);

        String signingKey = Utils.hmacSha256Hex(X_ARROW_VERSION_2, Utils.hmacSha256Hex(secretKey, apiKey));

        String signature = Utils.hmacSha256Hex(signingKey, stringToSign.toString());

        return signature;
    }

    private String buildCanonicalRequest() {
        Timber.v("buildCanonicalRequest: ");
        StringBuilder builder = new StringBuilder();
        builder.append(hid).append('\n').append(name).append('\n').append(encrypted).append('\n');

        // append parameters
        if (parameters.size() > 0) {
            Collections.sort(parameters);
            for (String p : parameters) {
                builder.append(p).append('\n');
            }
        }
        return builder.toString();
    }
}
