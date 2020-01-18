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

package com.konexios.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    @SerializedName("signature")
    private String signature;
    @SerializedName("signatureVersion")
    private String signatureVersion;

    public GatewayEventModel() {
    }

    public GatewayEventModel(String hid, String name, boolean encrypted, Map<String, String> parameters,
                             String signature, String signatureVersion) {
        this.hid = hid;
        this.name = name;
        this.encrypted = encrypted;
        this.parameters = parameters;
        this.signature = signature;
        this.signatureVersion = signatureVersion;
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
        signature = in.readString();
        signatureVersion = in.readString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GatewayEventModel that = (GatewayEventModel) o;
        return encrypted == that.encrypted &&
                Objects.equals(hid, that.hid) &&
                Objects.equals(name, that.name) &&
                Objects.equals(parameters, that.parameters) &&
                Objects.equals(signature, that.signature) &&
                Objects.equals(signatureVersion, that.signatureVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hid, name, encrypted, parameters, signature, signatureVersion);
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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSignatureVersion() {
        return signatureVersion;
    }

    public void setSignatureVersion(String signatureVersion) {
        this.signatureVersion = signatureVersion;
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
        dest.writeString(signature);
        dest.writeString(signatureVersion);
    }
}

