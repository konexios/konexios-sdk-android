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
 * Created by osminin on 8/8/2016.
 */

public final class ActionParametersModel implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ActionParametersModel> CREATOR = new Parcelable.Creator<ActionParametersModel>() {
        @NonNull
        @Override
        public ActionParametersModel createFromParcel(@NonNull Parcel in) {
            return new ActionParametersModel(in);
        }

        @NonNull
        @Override
        public ActionParametersModel[] newArray(int size) {
            return new ActionParametersModel[size];
        }
    };
    @SerializedName("Email")
    private String email;
    @SerializedName("Message")
    private String message;
    @SerializedName("Phone")
    private String phone;
    @SerializedName("SipAddress")
    private String sipAddress;
    @SerializedName("Severity")
    private String severity;
    @SerializedName("Location")
    private String location;
    @SerializedName("URL")
    private String url;

    protected ActionParametersModel(@NonNull Parcel in) {
        email = in.readString();
        message = in.readString();
        phone = in.readString();
        sipAddress = in.readString();
        severity = in.readString();
        location = in.readString();
        url = in.readString();
    }

    public ActionParametersModel() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSipAddress() {
        return sipAddress;
    }

    public void setSipAddress(String sipAddress) {
        this.sipAddress = sipAddress;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(message);
        dest.writeString(phone);
        dest.writeString(sipAddress);
        dest.writeString(severity);
        dest.writeString(location);
        dest.writeString(url);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActionParametersModel that = (ActionParametersModel) o;
        return Objects.equals(email, that.email) &&
                Objects.equals(message, that.message) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(sipAddress, that.sipAddress) &&
                Objects.equals(severity, that.severity) &&
                Objects.equals(location, that.location) &&
                Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, message, phone, sipAddress, severity, location, url);
    }
}