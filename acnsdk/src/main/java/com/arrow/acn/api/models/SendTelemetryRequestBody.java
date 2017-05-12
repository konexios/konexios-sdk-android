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
import android.support.annotation.Nullable;

import com.arrow.acn.api.Constants;
import com.google.gson.annotations.SerializedName;

/**
 * Created by osminin on 3/16/2016.
 */
public final class SendTelemetryRequestBody implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SendTelemetryRequestBody> CREATOR = new Parcelable.Creator<SendTelemetryRequestBody>() {
        @NonNull
        @Override
        public SendTelemetryRequestBody createFromParcel(@NonNull Parcel in) {
            return new SendTelemetryRequestBody(in);
        }

        @NonNull
        @Override
        public SendTelemetryRequestBody[] newArray(int size) {
            return new SendTelemetryRequestBody[size];
        }
    };
    @SerializedName(Constants.DEVICE_ID_KEY)
    String mDeviceIdKey;
    @SerializedName("long_timestamp")
    Long mTimestamp;
    @SerializedName("double_longitude")
    Double mLongitude;
    @SerializedName("double_latitude")
    Double mLatitude;

    public SendTelemetryRequestBody() {
    }

    public SendTelemetryRequestBody(String deviceIdKey, Long timestamp, Double longitude, Double latitude) {
        mDeviceIdKey = deviceIdKey;
        mTimestamp = timestamp;
        mLongitude = longitude;
        mLatitude = latitude;
    }

    public SendTelemetryRequestBody(@NonNull SendTelemetryRequestBody body) {
        mDeviceIdKey = body.getDeviceIdKey();
        mTimestamp = body.getTimestamp();
        mLongitude = body.getLongitude();
        mLatitude = body.getLatitude();
    }

    protected SendTelemetryRequestBody(@NonNull Parcel in) {
        mDeviceIdKey = in.readString();
        mTimestamp = in.readByte() == 0x00 ? null : in.readLong();
        mLongitude = in.readByte() == 0x00 ? null : in.readDouble();
        mLatitude = in.readByte() == 0x00 ? null : in.readDouble();
    }

    public String getDeviceIdKey() {
        return mDeviceIdKey;
    }

    public void setDeviceIdKey(String deviceIdKey) {
        mDeviceIdKey = deviceIdKey;
    }

    @Nullable
    public Long getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(Long timestamp) {
        mTimestamp = timestamp;
    }

    @Nullable
    public Double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(Double longitude) {
        mLongitude = longitude;
    }

    @Nullable
    public Double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(Double latitude) {
        mLatitude = latitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SendTelemetryRequestBody that = (SendTelemetryRequestBody) o;

        if (mDeviceIdKey != null ? !mDeviceIdKey.equals(that.mDeviceIdKey) : that.mDeviceIdKey != null)
            return false;
        if (mTimestamp != null ? !mTimestamp.equals(that.mTimestamp) : that.mTimestamp != null)
            return false;
        if (mLongitude != null ? !mLongitude.equals(that.mLongitude) : that.mLongitude != null)
            return false;
        return mLatitude != null ? mLatitude.equals(that.mLatitude) : that.mLatitude == null;

    }

    @Override
    public int hashCode() {
        int result = mDeviceIdKey != null ? mDeviceIdKey.hashCode() : 0;
        result = 31 * result + (mTimestamp != null ? mTimestamp.hashCode() : 0);
        result = 31 * result + (mLongitude != null ? mLongitude.hashCode() : 0);
        result = 31 * result + (mLatitude != null ? mLatitude.hashCode() : 0);
        return result;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(mDeviceIdKey);
        if (mTimestamp == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(mTimestamp);
        }
        if (mLongitude == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(mLongitude);
        }
        if (mLatitude == null) {
            dest.writeByte((byte) (0x00));

        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(mLatitude);
        }
    }
}
