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
 * Created by osminin on 4/14/2016.
 */
public final class AccountResponse implements Parcelable {
    @SuppressWarnings("unused")

    public static final Parcelable.Creator<AccountResponse> CREATOR = new Parcelable.Creator<AccountResponse>() {
        @NonNull
        @Override
        public AccountResponse createFromParcel(@NonNull Parcel in) {
            return new AccountResponse(in);
        }

        @NonNull
        @Override
        public AccountResponse[] newArray(int size) {
            return new AccountResponse[size];
        }
    };
    @SerializedName("hid")
    private String hid;
    @SerializedName("message")
    private String message;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("code")
    private String code;
    @SerializedName("applicationHid")
    private String applicationHid;

    public AccountResponse() {
    }

    protected AccountResponse(@NonNull Parcel in) {
        hid = in.readString();
        message = in.readString();
        name = in.readString();
        email = in.readString();
        code = in.readString();
        applicationHid = in.readString();
    }

    public String getApplicationHid() {
        return applicationHid;
    }

    public void setApplicationHid(String applicationHid) {
        this.applicationHid = applicationHid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(hid);
        dest.writeString(message);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(code);
        dest.writeString(applicationHid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountResponse response = (AccountResponse) o;

        if (hid != null ? !hid.equals(response.hid) : response.hid != null) return false;
        if (message != null ? !message.equals(response.message) : response.message != null)
            return false;
        if (name != null ? !name.equals(response.name) : response.name != null) return false;
        if (email != null ? !email.equals(response.email) : response.email != null) return false;
        if (code != null ? !code.equals(response.code) : response.code != null) return false;
        return applicationHid != null ? applicationHid.equals(response.applicationHid) : response.applicationHid == null;

    }

    @Override
    public int hashCode() {
        int result = hid != null ? hid.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (applicationHid != null ? applicationHid.hashCode() : 0);
        return result;
    }
}