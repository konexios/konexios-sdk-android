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

import com.google.gson.annotations.SerializedName;

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

        if (CREATOR != null ? !CREATOR.equals(that.CREATOR) : that.CREATOR != null) return false;
        if (deviceActionTypeName != null ? !deviceActionTypeName.equals(that.deviceActionTypeName) : that.deviceActionTypeName != null)
            return false;
        if (criteria != null ? !criteria.equals(that.criteria) : that.criteria != null)
            return false;
        if (createdDate != null ? !createdDate.equals(that.createdDate) : that.createdDate != null)
            return false;
        return status != null ? status.equals(that.status) : that.status == null;

    }

    @Override
    public int hashCode() {
        int result = CREATOR != null ? CREATOR.hashCode() : 0;
        result = 31 * result + (deviceActionTypeName != null ? deviceActionTypeName.hashCode() : 0);
        result = 31 * result + (criteria != null ? criteria.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
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