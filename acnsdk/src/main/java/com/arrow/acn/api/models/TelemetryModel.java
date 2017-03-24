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

/**
 * Created by osminin on 10/17/2016.
 */

public final class TelemetryModel implements Parcelable {
    private String telemetry;
    private String deviceType;
    private String deviceExternalId;

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

    public TelemetryModel() {
    }

    protected TelemetryModel(Parcel in) {
        telemetry = in.readString();
        deviceType = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(telemetry);
        dest.writeString(deviceType);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TelemetryModel> CREATOR = new Parcelable.Creator<TelemetryModel>() {
        @Override
        public TelemetryModel createFromParcel(Parcel in) {
            return new TelemetryModel(in);
        }

        @Override
        public TelemetryModel[] newArray(int size) {
            return new TelemetryModel[size];
        }
    };
}