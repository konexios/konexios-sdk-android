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
 * Model for registering new gateway or updating existing one
 */

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
    /**
     * unique Id for gateway
     */
    @SerializedName("uid")
    private String uid;
    /**
     * gateway name
     */
    @SerializedName("name")
    private String name;
    /**
     * gateway type (Local, Cloud or Mobile)
     */
    @SerializedName("type")
    private GatewayType type;
    /**
     * user's hid
     */
    @SerializedName("userHid")
    private String userHid;
    /**
     * Operating System name
     */
    @SerializedName("osName")
    private String osName;
    /**
     * software name
     */
    @SerializedName("softwareName")
    private String softwareName;
    /**
     * software version
     */
    @SerializedName("softwareVersion")
    private String softwareVersion;
    /**
     * application hid from AccountResponse
     */
    @SerializedName("applicationHid")
    private String applicationHid;
    /**
     * version of sdk
     */
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
        applicationHid = in.readString();
    }

    public GatewayModel() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GatewayModel that = (GatewayModel) o;
        return Objects.equals(uid, that.uid) &&
                Objects.equals(name, that.name) &&
                type == that.type &&
                Objects.equals(userHid, that.userHid) &&
                Objects.equals(osName, that.osName) &&
                Objects.equals(softwareName, that.softwareName) &&
                Objects.equals(softwareVersion, that.softwareVersion) &&
                Objects.equals(applicationHid, that.applicationHid) &&
                Objects.equals(sdkVersion, that.sdkVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, name, type, userHid, osName, softwareName, softwareVersion, applicationHid, sdkVersion);
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
        dest.writeString(applicationHid);
    }
}