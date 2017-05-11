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
    @SerializedName("hid")
    private String hid;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @Nullable
    @SerializedName("enabled")
    private Boolean isEnabled;
    @SerializedName("systemName")
    private String systemName;
    @SerializedName("applicationId")
    private String applicationId;
    @SerializedName("parameters")
    private ActionParametersModel parameters;

    protected DeviceActionTypeModel(@NonNull Parcel in) {
        hid = in.readString();
        name = in.readString();
        description = in.readString();
        byte isEnabledVal = in.readByte();
        isEnabled = isEnabledVal == 0x02 ? null : isEnabledVal != 0x00;
        systemName = in.readString();
        applicationId = in.readString();
        parameters = (ActionParametersModel) in.readValue(ActionParametersModel.class.getClassLoader());
    }

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Nullable
    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        this.isEnabled = enabled;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public ActionParametersModel getParameters() {
        return parameters;
    }

    public void setParameters(ActionParametersModel parameters) {
        this.parameters = parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceActionTypeModel that = (DeviceActionTypeModel) o;

        if (hid != null ? !hid.equals(that.hid) : that.hid != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (isEnabled != null ? !isEnabled.equals(that.isEnabled) : that.isEnabled != null)
            return false;
        if (systemName != null ? !systemName.equals(that.systemName) : that.systemName != null)
            return false;
        if (applicationId != null ? !applicationId.equals(that.applicationId) : that.applicationId != null)
            return false;
        return parameters != null ? parameters.equals(that.parameters) : that.parameters == null;

    }

    @Override
    public int hashCode() {
        int result = hid != null ? hid.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (isEnabled != null ? isEnabled.hashCode() : 0);
        result = 31 * result + (systemName != null ? systemName.hashCode() : 0);
        result = 31 * result + (applicationId != null ? applicationId.hashCode() : 0);
        result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(hid);
        dest.writeString(name);
        dest.writeString(description);
        if (isEnabled == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isEnabled ? 0x01 : 0x00));
        }
        dest.writeString(systemName);
        dest.writeString(applicationId);
        dest.writeValue(parameters);
    }
}