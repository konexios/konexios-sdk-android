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
public final class GatewayEventModel implements Parcelable{
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

    protected GatewayEventModel(Parcel in) {
        mHid = in.readString();
        mName = in.readString();
        mEncrypted = in.readByte() != 0x00;
        int size = in.readInt();
        mParameters = new HashMap<>(size);
        for(int i = 0; i < size; i++){
            String key = in.readString();
            String value = in.readString();
            mParameters.put(key,value);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mHid);
        dest.writeString(mName);
        dest.writeByte((byte) (mEncrypted ? 0x01 : 0x00));
        dest.writeInt(mParameters.size());
        for(Map.Entry<String,String> entry : mParameters.entrySet()){
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
    }

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
}

