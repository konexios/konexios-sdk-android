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

        if (uid != null ? !uid.equals(that.uid) : that.uid != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (type != that.type) return false;
        if (userHid != null ? !userHid.equals(that.userHid) : that.userHid != null) return false;
        if (osName != null ? !osName.equals(that.osName) : that.osName != null) return false;
        if (softwareName != null ? !softwareName.equals(that.softwareName) : that.softwareName != null)
            return false;
        if (softwareVersion != null ? !softwareVersion.equals(that.softwareVersion) : that.softwareVersion != null)
            return false;
        if (applicationHid != null ? !applicationHid.equals(that.applicationHid) : that.applicationHid != null)
            return false;
        return sdkVersion != null ? sdkVersion.equals(that.sdkVersion) : that.sdkVersion == null;

    }

    @Override
    public int hashCode() {
        int result = uid != null ? uid.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (userHid != null ? userHid.hashCode() : 0);
        result = 31 * result + (osName != null ? osName.hashCode() : 0);
        result = 31 * result + (softwareName != null ? softwareName.hashCode() : 0);
        result = 31 * result + (softwareVersion != null ? softwareVersion.hashCode() : 0);
        result = 31 * result + (applicationHid != null ? applicationHid.hashCode() : 0);
        result = 31 * result + (sdkVersion != null ? sdkVersion.hashCode() : 0);
        return result;
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