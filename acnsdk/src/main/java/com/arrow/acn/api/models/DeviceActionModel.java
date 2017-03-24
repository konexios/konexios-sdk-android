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

import com.google.gson.annotations.SerializedName;

/**
 * Created by osminin on 8/2/2016.
 */

public final class DeviceActionModel implements Parcelable {
    @SerializedName("criteria")
    String mCriteria;
    @SerializedName("description")
    String mDescription;
    @Nullable
    @SerializedName("enabled")
    Boolean mEnabled;
    @Nullable
    @SerializedName("expiration")
    Integer mExpiration;
    @Nullable
    @SerializedName("index")
    Integer mIndex;
    @SerializedName("parameters")
    ActionParametersModel mParameters;
    @SerializedName("systemName")
    String mSystemName;

    public String getCriteria() {
        return mCriteria;
    }

    public void setCriteria(String criteria) {
        mCriteria = criteria;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    @Nullable
    public Boolean getEnabled() {
        return mEnabled;
    }

    public void setEnabled(Boolean enabled) {
        mEnabled = enabled;
    }

    @Nullable
    public Integer getExpiration() {
        return mExpiration;
    }

    public void setExpiration(Integer expiration) {
        mExpiration = expiration;
    }

    @Nullable
    public Integer getIndex() {
        return mIndex;
    }

    public void setIndex(Integer index) {
        mIndex = index;
    }

    public ActionParametersModel getParameters() {
        return mParameters;
    }

    public void setParameters(ActionParametersModel parameters) {
        mParameters = parameters;
    }

    public String getSystemName() {
        return mSystemName;
    }

    public void setSystemName(String systemName) {
        mSystemName = systemName;
    }

    protected DeviceActionModel(@NonNull Parcel in) {
        mCriteria = in.readString();
        mDescription = in.readString();
        byte mEnabledVal = in.readByte();
        mEnabled = mEnabledVal == 0x02 ? null : mEnabledVal != 0x00;
        mExpiration = in.readByte() == 0x00 ? null : in.readInt();
        mIndex = in.readByte() == 0x00 ? null : in.readInt();
        mParameters = (ActionParametersModel) in.readValue(ActionParametersModel.class.getClassLoader());
        mSystemName = in.readString();
    }

    public DeviceActionModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(mCriteria);
        dest.writeString(mDescription);
        if (mEnabled == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (mEnabled ? 0x01 : 0x00));
        }
        if (mExpiration == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(mExpiration);
        }
        if (mIndex == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(mIndex);
        }
        dest.writeValue(mParameters);
        dest.writeString(mSystemName);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DeviceActionModel> CREATOR = new Parcelable.Creator<DeviceActionModel>() {
        @NonNull
        @Override
        public DeviceActionModel createFromParcel(@NonNull Parcel in) {
            return new DeviceActionModel(in);
        }

        @NonNull
        @Override
        public DeviceActionModel[] newArray(int size) {
            return new DeviceActionModel[size];
        }
    };
}