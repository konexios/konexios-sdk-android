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
 * Created by osminin on 8/8/2016.
 */

public final class DeviceActionTypeModel implements Parcelable {
    @SerializedName("hid")
    private String mHid;
    @SerializedName("name")
    private String mName;
    @SerializedName("description")
    private String mDescription;
    @Nullable
    @SerializedName("enabled")
    private Boolean isEnabled;
    @SerializedName("systemName")
    private String mSystemName;
    @SerializedName("applicationId")
    private String mApplicationId;
    @SerializedName("parameters")
    private ActionParametersModel mParameters;

    public String getHid() {
        return mHid;
    }

    public void setHid(String hid) {
        mHid = hid;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    @Nullable
    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public String getSystemName() {
        return mSystemName;
    }

    public void setSystemName(String systemName) {
        mSystemName = systemName;
    }

    public String getApplicationId() {
        return mApplicationId;
    }

    public void setApplicationId(String applicationId) {
        mApplicationId = applicationId;
    }

    public ActionParametersModel getParameters() {
        return mParameters;
    }

    public void setParameters(ActionParametersModel parameters) {
        mParameters = parameters;
    }

    protected DeviceActionTypeModel(@NonNull Parcel in) {
        mHid = in.readString();
        mName = in.readString();
        mDescription = in.readString();
        byte isEnabledVal = in.readByte();
        isEnabled = isEnabledVal == 0x02 ? null : isEnabledVal != 0x00;
        mSystemName = in.readString();
        mApplicationId = in.readString();
        mParameters = (ActionParametersModel) in.readValue(ActionParametersModel.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(mHid);
        dest.writeString(mName);
        dest.writeString(mDescription);
        if (isEnabled == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isEnabled ? 0x01 : 0x00));
        }
        dest.writeString(mSystemName);
        dest.writeString(mApplicationId);
        dest.writeValue(mParameters);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DeviceActionTypeModel> CREATOR = new Parcelable.Creator<DeviceActionTypeModel>() {
        @NonNull
        @Override
        public DeviceActionTypeModel createFromParcel(@NonNull Parcel in) {
            return new DeviceActionTypeModel(in);
        }

        @NonNull
        @Override
        public DeviceActionTypeModel[] newArray(int size) {
            return new DeviceActionTypeModel[size];
        }
    };
}