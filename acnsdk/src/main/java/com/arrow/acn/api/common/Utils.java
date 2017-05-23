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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import timber.log.Timber;

/**
 * Created by osminin on 5/22/2017.
 */

final class Utils {
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    @Nullable
    public static String hash(@NonNull String value) {
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

    public static String bytesToHex(@NonNull byte[] bytes) {
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
    public static String hmacSha256Hex(@NonNull String key, @NonNull String data) {
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
}
