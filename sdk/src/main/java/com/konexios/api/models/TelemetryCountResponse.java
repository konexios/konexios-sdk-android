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

import java.util.Objects;

/**
 * Response model for TelemetryCountRequest
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
    /**
     * device hid
     */
    @SerializedName("deviceHid")
    private String deviceHid;
    /**
     * the name of telemetry
     */
    @SerializedName("name")
    private String name;
    /**
     * count of telemetries
     */
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
        return Objects.equals(deviceHid, that.deviceHid) &&
                Objects.equals(name, that.name) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceHid, name, value);
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
