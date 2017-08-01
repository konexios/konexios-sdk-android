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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *  Model which is representing state
 */

public class StateModel implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<StateModel> CREATOR = new Parcelable.Creator<StateModel>() {
        @Override
        public StateModel createFromParcel(Parcel in) {
            return new StateModel(in);
        }

        @Override
        public StateModel[] newArray(int size) {
            return new StateModel[size];
        }
    };
    @SerializedName("value")
    @Expose
    private String value;

    public StateModel() {
    }

    @SerializedName("timestamp")
    @Expose

    private String timestamp;

    protected StateModel(Parcel in) {
        value = in.readString();
        timestamp = in.readString();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StateModel that = (StateModel) o;

        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        return timestamp != null ? timestamp.equals(that.timestamp) : that.timestamp == null;

    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        return result;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(value);
        dest.writeString(timestamp);
    }
}
