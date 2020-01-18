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

public class FirmwareVersionModel implements Parcelable {
    @SerializedName("softwareReleaseHID")
    private String softwareReleaseHID;

    @SerializedName("softwareReleaseName")
    private String softwareReleaseName;

    protected FirmwareVersionModel(Parcel in) {
        softwareReleaseHID = in.readString();
        softwareReleaseName = in.readString();
    }

    public static final Creator<FirmwareVersionModel> CREATOR = new Creator<FirmwareVersionModel>() {
        @Override
        public FirmwareVersionModel createFromParcel(Parcel in) {
            return new FirmwareVersionModel(in);
        }

        @Override
        public FirmwareVersionModel[] newArray(int size) {
            return new FirmwareVersionModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel aParcel, int aI) {
        aParcel.writeString(softwareReleaseHID);
        aParcel.writeString(softwareReleaseName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FirmwareVersionModel that = (FirmwareVersionModel) o;
        return Objects.equals(softwareReleaseHID, that.softwareReleaseHID) &&
                Objects.equals(softwareReleaseName, that.softwareReleaseName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(softwareReleaseHID, softwareReleaseName);
    }

    public String getSoftwareReleaseHID() {
        return softwareReleaseHID;
    }

    public void setSoftwareReleaseHID(String aSoftwareReleaseHid) {
        softwareReleaseHID = aSoftwareReleaseHid;
    }

    public String getSoftwareReleaseName() {
        return softwareReleaseName;
    }

    public void setSoftwareReleaseName(String aSoftwareReleaseName) {
        softwareReleaseName = aSoftwareReleaseName;
    }
}
