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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by osminin on 12/23/2016.
 */

public final class TelemetryStatsModel implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TelemetryStatsModel> CREATOR = new Parcelable.Creator<TelemetryStatsModel>() {
        @Override
        public TelemetryStatsModel createFromParcel(Parcel in) {
            return new TelemetryStatsModel(in);
        }

        @Override
        public TelemetryStatsModel[] newArray(int size) {
            return new TelemetryStatsModel[size];
        }
    };
    @SerializedName("telemetryItemCount")
    private Integer telemetryItemCount;
    @SerializedName("deviceEventCount")
    private Integer deviceEventCount;
    @SerializedName("telemetryEventCounts")
    private List<TelemetryEventCount> telemetryEventCounts;

    public TelemetryStatsModel() {
    }

    protected TelemetryStatsModel(Parcel in) {
        telemetryItemCount = in.readByte() == 0x00 ? null : in.readInt();
        deviceEventCount = in.readByte() == 0x00 ? null : in.readInt();
        if (in.readByte() == 0x01) {
            telemetryEventCounts = new ArrayList<TelemetryEventCount>();
            in.readList(telemetryEventCounts, TelemetryEventCount.class.getClassLoader());
        } else {
            telemetryEventCounts = null;
        }
    }

    public Integer getTelemetryItemCount() {
        return telemetryItemCount;
    }

    public void setTelemetryItemCount(Integer telemetryItemCount) {
        this.telemetryItemCount = telemetryItemCount;
    }

    public Integer getDeviceEventCount() {
        return deviceEventCount;
    }

    public void setDeviceEventCount(Integer deviceEventCount) {
        this.deviceEventCount = deviceEventCount;
    }

    public List<TelemetryEventCount> getTelemetryEventCounts() {
        return telemetryEventCounts;
    }

    public void setTelemetryEventCounts(List<TelemetryEventCount> telemetryEventCounts) {
        this.telemetryEventCounts = telemetryEventCounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TelemetryStatsModel that = (TelemetryStatsModel) o;

        if (telemetryItemCount != null ? !telemetryItemCount.equals(that.telemetryItemCount) : that.telemetryItemCount != null)
            return false;
        if (deviceEventCount != null ? !deviceEventCount.equals(that.deviceEventCount) : that.deviceEventCount != null)
            return false;
        return telemetryEventCounts != null ? telemetryEventCounts.equals(that.telemetryEventCounts) : that.telemetryEventCounts == null;

    }

    @Override
    public int hashCode() {
        int result = telemetryItemCount != null ? telemetryItemCount.hashCode() : 0;
        result = 31 * result + (deviceEventCount != null ? deviceEventCount.hashCode() : 0);
        result = 31 * result + (telemetryEventCounts != null ? telemetryEventCounts.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (telemetryItemCount == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(telemetryItemCount);
        }
        if (deviceEventCount == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(deviceEventCount);
        }
        if (telemetryEventCounts == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(telemetryEventCounts);
        }
    }
}
