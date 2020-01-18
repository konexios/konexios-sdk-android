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
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * New Action model
 */

public final class DeviceActionModel implements Parcelable {
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
    @SerializedName("criteria")
    String criteria;
    @SerializedName("description")
    String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceActionModel that = (DeviceActionModel) o;
        return Objects.equals(criteria, that.criteria) &&
                Objects.equals(description, that.description) &&
                Objects.equals(enabled, that.enabled) &&
                Objects.equals(expiration, that.expiration) &&
                Objects.equals(index, that.index) &&
                Objects.equals(parameters, that.parameters) &&
                Objects.equals(systemName, that.systemName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(criteria, description, enabled, expiration, index, parameters, systemName);
    }

    @Nullable
    @SerializedName("enabled")
    Boolean enabled;
    @Nullable
    @SerializedName("expiration")
    Integer expiration;
    @Nullable
    @SerializedName("index")
    Integer index;
    @SerializedName("parameters")
    ActionParametersModel parameters;
    @SerializedName("systemName")
    String systemName;

    protected DeviceActionModel(@NonNull Parcel in) {
        criteria = in.readString();
        description = in.readString();
        byte mEnabledVal = in.readByte();
        enabled = mEnabledVal == 0x02 ? null : mEnabledVal != 0x00;
        expiration = in.readByte() == 0x00 ? null : in.readInt();
        index = in.readByte() == 0x00 ? null : in.readInt();
        parameters = (ActionParametersModel) in.readValue(ActionParametersModel.class.getClassLoader());
        systemName = in.readString();
    }

    public DeviceActionModel() {
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Nullable
    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Nullable
    public Integer getExpiration() {
        return expiration;
    }

    public void setExpiration(Integer expiration) {
        this.expiration = expiration;
    }

    @Nullable
    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public ActionParametersModel getParameters() {
        return parameters;
    }

    public void setParameters(ActionParametersModel parameters) {
        this.parameters = parameters;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(criteria);
        dest.writeString(description);
        if (enabled == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (enabled ? 0x01 : 0x00));
        }
        if (expiration == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(expiration);
        }
        if (index == null) {
            dest.writeByte((byte) (0x00));

        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(index);
        }
        dest.writeValue(parameters);
        dest.writeString(systemName);
    }
}