/*
 * Copyright (c) 2017 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors: Arrow Electronics, Inc.
 */

package com.arrow.acn.api.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Created by osminin on 4/15/2016.
 */
public final class GatewayResponse implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<GatewayResponse> CREATOR = new Parcelable.Creator<GatewayResponse>() {
        @NonNull
        @Override
        public GatewayResponse createFromParcel(@NonNull Parcel in) {
            return new GatewayResponse(in);
        }

        @NonNull
        @Override
        public GatewayResponse[] newArray(int size) {
            return new GatewayResponse[size];
        }
    };
    @SerializedName("hid")
    private String hid;
    @SerializedName("message")
    private String message;
    @SerializedName("type")
    private String type;
    @SerializedName("userHid")
    private String userHid;
    @SerializedName("gatewayHid")
    private String gatewayHid;
    @SerializedName("info")
    private String info;
    @SerializedName("properties")
    private String properties;
    @SerializedName("externalId")
    private String externalId;

    protected GatewayResponse(@NonNull Parcel in) {
        hid = in.readString();
        message = in.readString();
        type = in.readString();
        userHid = in.readString();
        gatewayHid = in.readString();
        info = in.readString();
        properties = in.readString();
        externalId = in.readString();
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserHid() {
        return userHid;
    }

    public void setUserHid(String userHid) {
        this.userHid = userHid;
    }

    public String getGatewayHid() {
        return gatewayHid;
    }

    public void setGatewayHid(String gatewayHid) {
        this.gatewayHid = gatewayHid;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(hid);
        dest.writeString(message);
        dest.writeString(type);
        dest.writeString(userHid);
        dest.writeString(gatewayHid);
        dest.writeString(info);
        dest.writeString(properties);
        dest.writeString(externalId);
    }
}