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
 * Created by batrakov on 23.01.18.
 */

public class AvailableFirmwareResponse implements Parcelable {
    @SerializedName("availableFirmwareVersion")
    private FirmwareVersionModel availableFirmwareVersion;

    @SerializedName("currentFirmwareVersion")
    private FirmwareVersionModel currentFirmwareVersion;

    @SerializedName("deviceTypeHid")
    private String deviceTypeHid;

    @SerializedName("deviceTypeName")
    private String deviceTypeName;

    @SerializedName("hardwareVersionName")
    private String hardwareVersionName;

    @SerializedName("numberOfAssets")
    private int numberOfAssets;

    protected AvailableFirmwareResponse(Parcel in) {
        availableFirmwareVersion = in.readParcelable(FirmwareVersionModel.class.getClassLoader());
        currentFirmwareVersion = in.readParcelable(FirmwareVersionModel.class.getClassLoader());
        deviceTypeHid = in.readString();
        deviceTypeName = in.readString();
        hardwareVersionName = in.readString();
        numberOfAssets = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(availableFirmwareVersion, flags);
        dest.writeParcelable(currentFirmwareVersion, flags);
        dest.writeString(deviceTypeHid);
        dest.writeString(deviceTypeName);
        dest.writeString(hardwareVersionName);
        dest.writeInt(numberOfAssets);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AvailableFirmwareResponse> CREATOR = new Creator<AvailableFirmwareResponse>() {
        @Override
        public AvailableFirmwareResponse createFromParcel(Parcel in) {
            return new AvailableFirmwareResponse(in);
        }

        @Override
        public AvailableFirmwareResponse[] newArray(int size) {
            return new AvailableFirmwareResponse[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvailableFirmwareResponse that = (AvailableFirmwareResponse) o;
        return numberOfAssets == that.numberOfAssets &&
                Objects.equals(availableFirmwareVersion, that.availableFirmwareVersion) &&
                Objects.equals(currentFirmwareVersion, that.currentFirmwareVersion) &&
                Objects.equals(deviceTypeHid, that.deviceTypeHid) &&
                Objects.equals(deviceTypeName, that.deviceTypeName) &&
                Objects.equals(hardwareVersionName, that.hardwareVersionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(availableFirmwareVersion, currentFirmwareVersion, deviceTypeHid, deviceTypeName, hardwareVersionName, numberOfAssets);
    }

    public FirmwareVersionModel getAvailableFirmwareVersion() {
        return availableFirmwareVersion;
    }

    public void setAvailableFirmwareVersion(FirmwareVersionModel aAvailableFirmwareVersion) {
        availableFirmwareVersion = aAvailableFirmwareVersion;
    }

    public FirmwareVersionModel getCurrentFirmwareVersion() {
        return currentFirmwareVersion;
    }

    public void setCurrentFirmwareVersion(FirmwareVersionModel aCurrentFirmwareVersion) {
        currentFirmwareVersion = aCurrentFirmwareVersion;
    }

    public String getDeviceTypeHid() {
        return deviceTypeHid;
    }

    public void setDeviceTypeHid(String aDeviceTypeHid) {
        deviceTypeHid = aDeviceTypeHid;
    }

    public String getDeviceTypeName() {
        return deviceTypeName;
    }

    public void setDeviceTypeName(String aDeviceTypeName) {
        deviceTypeName = aDeviceTypeName;
    }

    public String getHardwareVersionName() {
        return hardwareVersionName;
    }

    public void setHardwareVersionName(String aHardwareVersionName) {
        hardwareVersionName = aHardwareVersionName;
    }

    public int getNumberOfAssets() {
        return numberOfAssets;
    }

    public void setNumberOfAssets(int aNumberOfAssets) {
        numberOfAssets = aNumberOfAssets;
    }
}
