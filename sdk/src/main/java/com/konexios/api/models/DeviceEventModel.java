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

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * Created by osminin on 12.10.2016.
 */

public class DeviceEventModel implements Parcelable {
    @SuppressWarnings("unused")
    public final Parcelable.Creator<DeviceEventModel> CREATOR = new Parcelable.Creator<DeviceEventModel>() {
        @NonNull
        @Override
        public DeviceEventModel createFromParcel(@NonNull Parcel in) {
            return new DeviceEventModel(in);
        }

        @NonNull
        @Override
        public DeviceEventModel[] newArray(int size) {
            return new DeviceEventModel[size];
        }
    };
    @SerializedName("deviceActionTypeName")
    private String deviceActionTypeName;
    @SerializedName("criteria")
    private String criteria;
    @SerializedName("createdDate")
    private String createdDate;
    @SerializedName("status")
    private String status;

    protected DeviceEventModel(@NonNull Parcel in) {
        deviceActionTypeName = in.readString();
        criteria = in.readString();
        createdDate = in.readString();
        status = in.readString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceEventModel that = (DeviceEventModel) o;
        return Objects.equals(CREATOR, that.CREATOR) &&
                Objects.equals(deviceActionTypeName, that.deviceActionTypeName) &&
                Objects.equals(criteria, that.criteria) &&
                Objects.equals(createdDate, that.createdDate) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(CREATOR, deviceActionTypeName, criteria, createdDate, status);
    }

    public String getDeviceActionTypeName() {
        return deviceActionTypeName;
    }

    public void setDeviceActionTypeName(String deviceActionTypeName) {
        this.deviceActionTypeName = deviceActionTypeName;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(deviceActionTypeName);
        dest.writeString(criteria);
        if (createdDate == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeString(createdDate);
        }
        dest.writeString(status);
    }
}