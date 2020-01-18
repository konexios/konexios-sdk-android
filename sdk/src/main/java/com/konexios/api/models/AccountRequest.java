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
 * Created by osminin on 4/14/2016.
 */
public final class AccountRequest implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AccountRequest> CREATOR = new Parcelable.Creator<AccountRequest>() {
        @NonNull
        @Override
        public AccountRequest createFromParcel(@NonNull Parcel in) {
            return new AccountRequest(in);
        }

        @NonNull
        @Override
        public AccountRequest[] newArray(int size) {
            return new AccountRequest[size];
        }
    };
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("code")
    private String code;

    protected AccountRequest(@NonNull Parcel in) {
        name = in.readString();
        email = in.readString();
        password = in.readString();
        code = in.readString();
    }

    public AccountRequest() {
    }

    public String getCode() {
        return code;
    }

    /**
     * Sets the code
     *
     * @param code - string to switch between platforms
     */
    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    /**
     * Sets the name
     *
     * @param name - string representing user's name
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    /**
     * email setter
     *
     * @param email - email like "user@someemail.com"
     */
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Sets the password
     *
     * @param password - password string
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(code);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountRequest that = (AccountRequest) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(email, that.email) &&
                Objects.equals(password, that.password) &&
                Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, password, code);
    }
}