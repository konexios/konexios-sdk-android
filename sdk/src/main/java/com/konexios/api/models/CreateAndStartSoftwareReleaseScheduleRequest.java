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

import java.util.Arrays;
import java.util.Objects;

/**
 * Created by batrakov on 23.01.18.
 */

public class CreateAndStartSoftwareReleaseScheduleRequest implements Parcelable {

    @SerializedName("deviceCategory")
    private String deviceCategory = "GATEWAY";

    @SerializedName("objectHids")
    private String[] objectHids;

    @SerializedName("softwareReleaseHid")
    private String softwareReleaseHid;

    @SerializedName("userHid")
    private String userHid;

    private CreateAndStartSoftwareReleaseScheduleRequest(Parcel in) {
        deviceCategory = in.readString();
        objectHids = in.createStringArray();
        softwareReleaseHid = in.readString();
        userHid = in.readString();
    }

    public CreateAndStartSoftwareReleaseScheduleRequest() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(deviceCategory);
        dest.writeStringArray(objectHids);
        dest.writeString(softwareReleaseHid);
        dest.writeString(userHid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @SuppressWarnings("unused")
    public static final Creator<CreateAndStartSoftwareReleaseScheduleRequest> CREATOR = new Creator<CreateAndStartSoftwareReleaseScheduleRequest>() {
        @Override
        public CreateAndStartSoftwareReleaseScheduleRequest createFromParcel(Parcel in) {
            return new CreateAndStartSoftwareReleaseScheduleRequest(in);
        }

        @Override
        public CreateAndStartSoftwareReleaseScheduleRequest[] newArray(int size) {
            return new CreateAndStartSoftwareReleaseScheduleRequest[size];
        }
    };

    public String getDeviceCategory() {
        return deviceCategory;
    }

    public void setDeviceCategory(String aDeviceCategory) {
        deviceCategory = aDeviceCategory;
    }

    public String[] getObjectHids() {
        return objectHids;
    }

    public void setObjectHids(String[] aObjectHids) {
        objectHids = aObjectHids;
    }

    public String getSoftwareReleaseHid() {
        return softwareReleaseHid;
    }

    public void setSoftwareReleaseHid(String aSoftwareReleaseHid) {
        softwareReleaseHid = aSoftwareReleaseHid;
    }

    public String getUserHid() {
        return userHid;
    }

    public void setUserHid(String aUserHid) {
        userHid = aUserHid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateAndStartSoftwareReleaseScheduleRequest that = (CreateAndStartSoftwareReleaseScheduleRequest) o;
        return Objects.equals(deviceCategory, that.deviceCategory) &&
                Arrays.equals(objectHids, that.objectHids) &&
                Objects.equals(softwareReleaseHid, that.softwareReleaseHid) &&
                Objects.equals(userHid, that.userHid);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(deviceCategory, softwareReleaseHid, userHid);
        result = 31 * result + Arrays.hashCode(objectHids);
        return result;
    }
}
