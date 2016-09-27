package com.arrow.kronos.api.common;

import android.util.Log;

import com.arrow.kronos.api.Constants;
import com.google.firebase.crash.FirebaseCrash;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import static android.content.ContentValues.TAG;

/**
 * Created by osminin on 4/8/2016.
 */
public class ApiRequestSigner {
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    private static class Holder {
        private static final ApiRequestSigner instance = new ApiRequestSigner();
    }

    private String secretKey;
    private String method;
    private String uri;
    private String apiKey;
    private String timestamp;
    private String payload;
    private List<String> parameters;

    public static ApiRequestSigner getInstance() {
        return Holder.instance;
    }

    private ApiRequestSigner() {
        this.parameters = new ArrayList<>();
        this.payload = "";
    }

    public ApiRequestSigner payload(String payload) {
        if (payload != null)
            this.payload = payload;
        return this;
    }

    public ApiRequestSigner method(String method) {
        this.method = method.toUpperCase();
        return this;
    }

    public ApiRequestSigner canonicalUri(String uri) {
        this.uri = uri;
        return this;
    }

    public ApiRequestSigner apiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public ApiRequestSigner setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    public ApiRequestSigner timestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String signV1() {
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

    public String encode(String key, String data) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            byte[] raw = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
            String result = bytesToHex(raw);
            return result;
        } catch (Exception e) {
            FirebaseCrash.logcat(Log.ERROR, TAG, "encode");
            FirebaseCrash.report(e);
        }
        return "";
    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars).toLowerCase();
    }

    private String buildCanonicalRequest() {
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

    private String hash(String value) {
        MessageDigest digest;
        String result = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(value.getBytes("UTF-8"));
            result = bytesToHex(hash);
        } catch (Exception e) {
            FirebaseCrash.logcat(Log.ERROR, TAG, "hash");
            FirebaseCrash.report(e);
        }
        return result;
    }
}
