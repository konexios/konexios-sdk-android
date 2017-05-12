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

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by osminin on 4/29/2016.
 */
public final class GatewayEventModel implements Parcelable {
    public static final String DEVICE_HID_KEY = "deviceHid";
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<GatewayEventModel> CREATOR = new Parcelable.Creator<GatewayEventModel>() {
        @Override
        public GatewayEventModel createFromParcel(Parcel in) {
            return new GatewayEventModel(in);
        }

        @Override
        public GatewayEventModel[] newArray(int size) {
            return new GatewayEventModel[size];
        }
    };
    @SerializedName("hid")
    private String hid;
    @SerializedName("name")
    private String name;
    @SerializedName("encrypted")
    private boolean encrypted = false;
    @SerializedName("parameters")
    private Map<String, String> parameters = new HashMap<>();

    public GatewayEventModel() {
    }

    public GatewayEventModel(String hid, String name, boolean encrypted, Map<String, String> parameters) {
        this.hid = hid;
        this.name = name;
        this.encrypted = encrypted;
        this.parameters = parameters;
    }

    protected GatewayEventModel(Parcel in) {
        hid = in.readString();
        name = in.readString();
        encrypted = in.readByte() != 0x00;
        int size = in.readInt();
        parameters = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            String key = in.readString();
            String value = in.readString();
            parameters.put(key, value);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GatewayEventModel that = (GatewayEventModel) o;

        if (encrypted != that.encrypted) return false;
        if (hid != null ? !hid.equals(that.hid) : that.hid != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return parameters != null ? parameters.equals(that.parameters) : that.parameters == null;

    }

    @Override
    public int hashCode() {
        int result = hid != null ? hid.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (encrypted ? 1 : 0);
        result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
        return result;
    }

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hid);
        dest.writeString(name);
        dest.writeByte((byte) (encrypted ? 0x01 : 0x00));
        dest.writeInt(parameters.size());
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
    }
}

