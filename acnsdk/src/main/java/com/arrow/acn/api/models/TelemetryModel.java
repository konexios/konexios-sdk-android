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

/**
 * Created by osminin on 10/17/2016.
 */

public final class TelemetryModel implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TelemetryModel> CREATOR = new Parcelable.Creator<TelemetryModel>() {
        @NonNull
        @Override
        public TelemetryModel createFromParcel(@NonNull Parcel in) {
            return new TelemetryModel(in);
        }

        @NonNull
        @Override
        public TelemetryModel[] newArray(int size) {
            return new TelemetryModel[size];
        }
    };
    private String telemetry;
    private String deviceType;
    private String deviceExternalId;

    public TelemetryModel() {
    }

    protected TelemetryModel(@NonNull Parcel in) {
        telemetry = in.readString();
        deviceType = in.readString();
        deviceExternalId = in.readString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TelemetryModel that = (TelemetryModel) o;

        if (telemetry != null ? !telemetry.equals(that.telemetry) : that.telemetry != null)
            return false;
        if (deviceType != null ? !deviceType.equals(that.deviceType) : that.deviceType != null)
            return false;
        return deviceExternalId != null ? deviceExternalId.equals(that.deviceExternalId) : that.deviceExternalId == null;

    }

    @Override
    public int hashCode() {
        int result = telemetry != null ? telemetry.hashCode() : 0;
        result = 31 * result + (deviceType != null ? deviceType.hashCode() : 0);
        result = 31 * result + (deviceExternalId != null ? deviceExternalId.hashCode() : 0);
        return result;
    }

    public String getDeviceExternalId() {
        return deviceExternalId;
    }

    public void setDeviceExternalId(String deviceExternalId) {
        this.deviceExternalId = deviceExternalId;
    }

    public String getTelemetry() {
        return telemetry;

    }

    public void setTelemetry(String telemetry) {
        this.telemetry = telemetry;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(telemetry);
        dest.writeString(deviceType);
        dest.writeString(deviceExternalId);
    }
}