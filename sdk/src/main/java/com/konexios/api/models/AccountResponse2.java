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

public final class AccountResponse2 implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AccountResponse2> CREATOR = new Parcelable.Creator<AccountResponse2>() {
        @Override
        public AccountResponse2 createFromParcel(Parcel in) {
            return new AccountResponse2(in);
        }

        @Override
        public AccountResponse2[] newArray(int size) {
            return new AccountResponse2[size];
        }
    };
    @SerializedName("applicationHid")
    private String applicationHid;
    @SerializedName("companyHid")
    private String companyHid;
    @SerializedName("userHid")
    private String userHid;
    @SerializedName("zoneSystemName")
    private String zoneSystemName;

    protected AccountResponse2(Parcel in) {
        applicationHid = in.readString();
        companyHid = in.readString();
        userHid = in.readString();
        zoneSystemName = in.readString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountResponse2 that = (AccountResponse2) o;
        return Objects.equals(applicationHid, that.applicationHid) &&
                Objects.equals(companyHid, that.companyHid) &&
                Objects.equals(userHid, that.userHid) &&
                Objects.equals(zoneSystemName, that.zoneSystemName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationHid, companyHid, userHid, zoneSystemName);
    }

    public String getApplicationHid() {

        return applicationHid;
    }

    public void setApplicationHid(String applicationHid) {
        this.applicationHid = applicationHid;
    }

    public String getCompanyHid() {
        return companyHid;
    }

    public void setCompanyHid(String companyHid) {
        this.companyHid = companyHid;
    }

    public String getUserHid() {
        return userHid;
    }

    public void setUserHid(String userHid) {
        this.userHid = userHid;
    }

    public String getZoneSystemName() {
        return zoneSystemName;
    }

    public void setZoneSystemName(String zoneSystemName) {
        this.zoneSystemName = zoneSystemName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(applicationHid);
        dest.writeString(companyHid);
        dest.writeString(userHid);
        dest.writeString(zoneSystemName);
    }
}