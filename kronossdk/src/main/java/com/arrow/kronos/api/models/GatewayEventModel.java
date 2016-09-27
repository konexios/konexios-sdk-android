package com.arrow.kronos.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by osminin on 4/29/2016.
 */
public final class GatewayEventModel {
    public static final String DEVICE_HID_KEY = "deviceHid";

    @SerializedName("hid")
    private String mHid;
    @SerializedName("name")
    private String mName;
    @SerializedName("encrypted")
    private boolean mEncrypted = false;
    @SerializedName("parameters")
    private Map<String, String> mParameters = new HashMap<>();

    public GatewayEventModel() {
    }

    public GatewayEventModel(String hid, String name, boolean encrypted, Map<String, String> parameters) {
        this.mHid = hid;
        this.mName = name;
        this.mEncrypted = encrypted;
        this.mParameters = parameters;
    }

    public String getHid() {
        return mHid;
    }

    public void setHid(String hid) {
        this.mHid = hid;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public boolean isEncrypted() {
        return mEncrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.mEncrypted = encrypted;
    }

    public Map<String, String> getParameters() {
        return mParameters;
    }
}

