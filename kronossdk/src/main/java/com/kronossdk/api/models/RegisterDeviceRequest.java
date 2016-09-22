package com.kronossdk.api.models;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

/**
 * Created by osminin on 4/21/2016.
 */
public final class RegisterDeviceRequest {
    @SerializedName("uid")
    private String mUid;
    @SerializedName("name")
    private String mName;
    @SerializedName("type")
    private String mType;
    @SerializedName("userHid")
    private String mUserHid;
    @SerializedName("gatewayHid")
    private String mGatewayHid;
    @SerializedName("info")
    private JsonObject mInfo;
    @SerializedName("properties")
    private JsonObject mProperties;

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        mUid = uid;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getUserHid() {
        return mUserHid;
    }

    public void setUserHid(String userHid) {
        mUserHid = userHid;
    }

    public String getGatewayHid() {
        return mGatewayHid;
    }

    public void setGatewayHid(String gatewayHid) {
        mGatewayHid = gatewayHid;
    }

    public JsonObject getInfo() {
        return mInfo;
    }

    public void setInfo(JsonObject info) {
        mInfo = info;
    }

    public JsonObject getProperties() {
        return mProperties;
    }

    public void setProperties(JsonObject properties) {
        mProperties = properties;
    }
}
