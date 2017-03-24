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

public final class GatewayModel implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<GatewayModel> CREATOR = new Parcelable.Creator<GatewayModel>() {
        @NonNull
        @Override
        public GatewayModel createFromParcel(@NonNull Parcel in) {
            return new GatewayModel(in);
        }

        @NonNull
        @Override
        public GatewayModel[] newArray(int size) {
            return new GatewayModel[size];
        }
    };
    @SerializedName("uid")
    private String uid;
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private GatewayType type;
    @SerializedName("userHid")
    private String userHid;
    @SerializedName("osName")
    private String osName;
    @SerializedName("softwareName")
    private String softwareName;
    @SerializedName("softwareVersion")
    private String softwareVersion;
    @SerializedName("applicationHid")
    private String applicationHid;
    @SerializedName("sdkVersion")
    private String sdkVersion;

    protected GatewayModel(@NonNull Parcel in) {

        uid = in.readString();
        name = in.readString();
        type = (GatewayType) in.readValue(GatewayType.class.getClassLoader());
        userHid = in.readString();
        osName = in.readString();
        softwareName = in.readString();
        softwareVersion = in.readString();
        sdkVersion = in.readString();
    }

    public GatewayModel() {
    }

    public String getApplicationHid() {
        return applicationHid;
    }

    public void setApplicationHid(String applicationHid) {
        this.applicationHid = applicationHid;
    }

    public GatewayType getType() {
        return type;
    }

    public void setType(GatewayType type) {
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getSoftwareName() {
        return softwareName;
    }

    public void setSoftwareName(String softwareName) {
        this.softwareName = softwareName;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public String getUserHid() {
        return userHid;
    }

    public void setUserHid(String userHid) {
        this.userHid = userHid;
    }

    public String getSdkVersion() {
        return sdkVersion;
    }

    public void setSdkVersion(String sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(name);
        dest.writeValue(type);
        dest.writeString(userHid);
        dest.writeString(osName);
        dest.writeString(softwareName);
        dest.writeString(softwareVersion);
        dest.writeString(sdkVersion);
    }
}