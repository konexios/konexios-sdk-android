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
 * Response model for Account request
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
    /**
     * account hid
     */
    @SerializedName("hid")
    private String hid;
    /**
     * server message
     */
    @SerializedName("message")
    private String message;
    /**
     * user name
     */
    @SerializedName("name")
    private String name;
    /**
     * user email
     */
    @SerializedName("email")
    private String email;
    @SerializedName("code")
    private String code;
    /**
     * application hid
     */
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
        AccountResponse that = (AccountResponse) o;
        return Objects.equals(hid, that.hid) &&
                Objects.equals(message, that.message) &&
                Objects.equals(name, that.name) &&
                Objects.equals(email, that.email) &&
                Objects.equals(code, that.code) &&
                Objects.equals(applicationHid, that.applicationHid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hid, message, name, email, code, applicationHid);
    }
}