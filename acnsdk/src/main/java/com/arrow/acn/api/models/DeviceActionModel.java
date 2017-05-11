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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceActionModel that = (DeviceActionModel) o;

        if (criteria != null ? !criteria.equals(that.criteria) : that.criteria != null)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (enabled != null ? !enabled.equals(that.enabled) : that.enabled != null) return false;
        if (expiration != null ? !expiration.equals(that.expiration) : that.expiration != null)
            return false;
        if (index != null ? !index.equals(that.index) : that.index != null) return false;
        if (parameters != null ? !parameters.equals(that.parameters) : that.parameters != null)
            return false;
        return systemName != null ? systemName.equals(that.systemName) : that.systemName == null;

    }

    @Override
    public int hashCode() {
        int result = criteria != null ? criteria.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (enabled != null ? enabled.hashCode() : 0);
        result = 31 * result + (expiration != null ? expiration.hashCode() : 0);
        result = 31 * result + (index != null ? index.hashCode() : 0);
        result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
        result = 31 * result + (systemName != null ? systemName.hashCode() : 0);
        return result;
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