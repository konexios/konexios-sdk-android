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

public class ApiError implements Parcelable {
    public static final String NETWORK_ERROR_MESSAGE = "Network error";
    public static final int NETWORK_ERROR_CODE = 1;
    public static final String COMMON_ERROR_MESSAGE = "Common error";
    public static final int COMMON_ERROR_CODE = 11;
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ApiError> CREATOR = new Parcelable.Creator<ApiError>() {
        @Override
        public ApiError createFromParcel(Parcel in) {
            return new ApiError(in);
        }

        @Override
        public ApiError[] newArray(int size) {
            return new ApiError[size];
        }
    };
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;

    public ApiError(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    protected ApiError(Parcel in) {
        status = in.readByte() == 0x00 ? null : in.readInt();
        message = in.readString();
    }

    /**
     * @return The status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiError apiError = (ApiError) o;
        return Objects.equals(status, apiError.status) &&
                Objects.equals(message, apiError.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, message);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (status == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(status);
        }
        dest.writeString(message);

    }
}