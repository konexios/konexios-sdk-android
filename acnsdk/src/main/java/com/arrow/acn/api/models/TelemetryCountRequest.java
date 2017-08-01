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
 *  Request model for finding count of sent telemetry
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
     *  name of exact telemetry (* for any)
     */
    @SerializedName("telemetryName")
    private String telemetryName;

    /**
     *  start time in "yyyy-MM-dd'T'HH:mm:ss'Z'" format
     */
    @SerializedName("fromTimestamp")
    private String fromTimestamp;

    /**
     *  end time in "yyyy-MM-dd'T'HH:mm:ss'Z'" format
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

        TelemetryCountRequest request = (TelemetryCountRequest) o;

        if (deviceHid != null ? !deviceHid.equals(request.deviceHid) : request.deviceHid != null)
            return false;
        if (telemetryName != null ? !telemetryName.equals(request.telemetryName) : request.telemetryName != null)
            return false;
        if (fromTimestamp != null ? !fromTimestamp.equals(request.fromTimestamp) : request.fromTimestamp != null)
            return false;
        return toTimestamp != null ? toTimestamp.equals(request.toTimestamp) : request.toTimestamp == null;

    }

    @Override
    public int hashCode() {
        int result = deviceHid != null ? deviceHid.hashCode() : 0;
        result = 31 * result + (telemetryName != null ? telemetryName.hashCode() : 0);
        result = 31 * result + (fromTimestamp != null ? fromTimestamp.hashCode() : 0);
        result = 31 * result + (toTimestamp != null ? toTimestamp.hashCode() : 0);
        return result;
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
