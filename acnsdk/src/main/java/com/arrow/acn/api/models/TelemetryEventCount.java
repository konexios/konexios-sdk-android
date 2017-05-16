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
 * Created by osminin on 12/23/2016.
 */

public final class TelemetryEventCount implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TelemetryEventCount> CREATOR = new Parcelable.Creator<TelemetryEventCount>() {
        @Override
        public TelemetryEventCount createFromParcel(Parcel in) {
            return new TelemetryEventCount(in);
        }

        @Override
        public TelemetryEventCount[] newArray(int size) {
            return new TelemetryEventCount[size];
        }
    };
    @SerializedName("timestamp")
    private Long timestamp;
    @SerializedName("telemetryCount")
    private Integer telemetryCount;
    @SerializedName("deviceEventCount")
    private Integer deviceEventCount;

    public TelemetryEventCount() {
    }

    protected TelemetryEventCount(Parcel in) {
        timestamp = in.readByte() == 0x00 ? null : in.readLong();
        telemetryCount = in.readByte() == 0x00 ? null : in.readInt();
        deviceEventCount = in.readByte() == 0x00 ? null : in.readInt();
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getTelemetryCount() {
        return telemetryCount;
    }

    public void setTelemetryCount(Integer telemetryCount) {

        this.telemetryCount = telemetryCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TelemetryEventCount that = (TelemetryEventCount) o;

        if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null)
            return false;
        if (telemetryCount != null ? !telemetryCount.equals(that.telemetryCount) : that.telemetryCount != null)
            return false;
        return deviceEventCount != null ? deviceEventCount.equals(that.deviceEventCount) : that.deviceEventCount == null;

    }

    @Override
    public int hashCode() {
        int result = timestamp != null ? timestamp.hashCode() : 0;
        result = 31 * result + (telemetryCount != null ? telemetryCount.hashCode() : 0);
        result = 31 * result + (deviceEventCount != null ? deviceEventCount.hashCode() : 0);
        return result;
    }

    public Integer getDeviceEventCount() {
        return deviceEventCount;
    }

    public void setDeviceEventCount(Integer deviceEventCount) {
        this.deviceEventCount = deviceEventCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (timestamp == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(timestamp);
        }
        if (telemetryCount == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(telemetryCount);
        }
        if (deviceEventCount == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(deviceEventCount);
        }
    }
}
