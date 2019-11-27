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

import androidx.annotation.NonNull;

import java.util.Objects;

/**
 * Model represents Telemetry object
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
    /**
     * Json string contains telemetries keys - values pairs
     */
    private String telemetry;
    /**
     * device type string
     */
    private String deviceType;
    /**
     * external id from DeviceRegistrationResponse model
     */
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
        return Objects.equals(telemetry, that.telemetry) &&
                Objects.equals(deviceType, that.deviceType) &&
                Objects.equals(deviceExternalId, that.deviceExternalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(telemetry, deviceType, deviceExternalId);
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