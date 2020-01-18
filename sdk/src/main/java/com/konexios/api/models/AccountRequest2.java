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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class AccountRequest2 implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AccountRequest2> CREATOR = new Parcelable.Creator<AccountRequest2>() {
        @Override
        public AccountRequest2 createFromParcel(Parcel in) {
            return new AccountRequest2(in);
        }

        @Override
        public AccountRequest2[] newArray(int size) {
            return new AccountRequest2[size];
        }
    };
    @SerializedName("applicationCode")
    @Expose
    private String applicationCode;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("username")
    @Expose
    private String username;

    public AccountRequest2() {
    }

    protected AccountRequest2(Parcel in) {
        applicationCode = in.readString();
        password = in.readString();
        username = in.readString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountRequest2 that = (AccountRequest2) o;
        return Objects.equals(applicationCode, that.applicationCode) &&
                Objects.equals(password, that.password) &&
                Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationCode, password, username);
    }

    public String getApplicationCode() {
        return applicationCode;
    }

    public void setApplicationCode(String applicationCode) {
        this.applicationCode = applicationCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(applicationCode);
        dest.writeString(password);
        dest.writeString(username);
    }
}