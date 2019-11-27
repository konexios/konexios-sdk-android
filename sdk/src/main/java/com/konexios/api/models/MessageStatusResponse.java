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

/**
 * Response model contains only message and status
 */

public class MessageStatusResponse implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MessageStatusResponse> CREATOR = new Parcelable.Creator<MessageStatusResponse>() {
        @Override
        public MessageStatusResponse createFromParcel(Parcel in) {
            return new MessageStatusResponse(in);
        }

        @Override
        public MessageStatusResponse[] newArray(int size) {
            return new MessageStatusResponse[size];
        }
    };
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    protected MessageStatusResponse(Parcel in) {
        message = in.readString();
        status = in.readString();
    }

    public MessageStatusResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(message);
        dest.writeString(status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageStatusResponse that = (MessageStatusResponse) o;
        return Objects.equals(message, that.message) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, status);
    }
}
