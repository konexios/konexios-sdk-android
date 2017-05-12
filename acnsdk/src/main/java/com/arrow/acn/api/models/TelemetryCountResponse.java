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

/**
 * Created by osminin on 3/31/2017.
 */

public class TelemetryCountResponse implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TelemetryCountResponse> CREATOR = new Parcelable.Creator<TelemetryCountResponse>() {
        @Override
        public TelemetryCountResponse createFromParcel(Parcel in) {
            return new TelemetryCountResponse(in);
        }

        @Override
        public TelemetryCountResponse[] newArray(int size) {
            return new TelemetryCountResponse[size];
        }
    };
    @SerializedName("deviceHid")
    private String deviceHid;
    @SerializedName("name")
    private String name;
    @SerializedName("value")
    private String value;

    public TelemetryCountResponse() {
    }

    protected TelemetryCountResponse(Parcel in) {
        deviceHid = in.readString();
        name = in.readString();
        value = in.readString();
    }

    public String getDeviceHid() {
        return deviceHid;
    }

    public void setDeviceHid(String deviceHid) {
        this.deviceHid = deviceHid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TelemetryCountResponse that = (TelemetryCountResponse) o;

        if (deviceHid != null ? !deviceHid.equals(that.deviceHid) : that.deviceHid != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return value != null ? value.equals(that.value) : that.value == null;

    }

    @Override
    public int hashCode() {
        int result = deviceHid != null ? deviceHid.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(deviceHid);
        dest.writeString(name);
        dest.writeString(value);
    }
}
