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
 * Model which is representing state
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
        return Objects.equals(value, that.value) &&
                Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, timestamp);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(value);
        dest.writeString(timestamp);
    }
}
