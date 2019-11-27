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
 * Request model for finding count of sent telemetry
 */

public class TelemetryCountRequest implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TelemetryCountRequest> CREATOR = new Parcelable.Creator<TelemetryCountRequest>() {
        @Override
        public TelemetryCountRequest createFromParcel(Parcel in) {
            return new TelemetryCountRequest(in);
        }

        @Override
        public TelemetryCountRequest[] newArray(int size) {
            return new TelemetryCountRequest[size];
        }
    };

    /**
     * hid of device
     */
    @SerializedName("deviceHid")
    private String deviceHid;

    /**
     * name of exact telemetry (* for any)
     */
    @SerializedName("telemetryName")
    private String telemetryName;

    /**
     * start time in "yyyy-MM-dd'T'HH:mm:ss'Z'" format
     */
    @SerializedName("fromTimestamp")
    private String fromTimestamp;

    /**
     * end time in "yyyy-MM-dd'T'HH:mm:ss'Z'" format
     */
    @SerializedName("toTimestamp")
    private String toTimestamp;

    public TelemetryCountRequest() {
    }

    protected TelemetryCountRequest(Parcel in) {
        deviceHid = in.readString();
        telemetryName = in.readString();
        fromTimestamp = in.readString();
        toTimestamp = in.readString();
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
        TelemetryCountRequest that = (TelemetryCountRequest) o;
        return Objects.equals(deviceHid, that.deviceHid) &&
                Objects.equals(telemetryName, that.telemetryName) &&
                Objects.equals(fromTimestamp, that.fromTimestamp) &&
                Objects.equals(toTimestamp, that.toTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceHid, telemetryName, fromTimestamp, toTimestamp);
    }

    public String getTelemetryName() {
        return telemetryName;
    }

    public void setTelemetryName(String telemetryName) {
        this.telemetryName = telemetryName;
    }

    public String getFromTimestamp() {
        return fromTimestamp;
    }

    public void setFromTimestamp(String fromTimestamp) {
        this.fromTimestamp = fromTimestamp;
    }

    public String getToTimestamp() {
        return toTimestamp;
    }

    public void setToTimestamp(String toTimestamp) {
        this.toTimestamp = toTimestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(deviceHid);
        dest.writeString(telemetryName);
        dest.writeString(fromTimestamp);
        dest.writeString(toTimestamp);
    }
}
