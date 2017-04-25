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
 * Created by osminin on 4/24/2017.
 */

public class NewDeviceStateTransactionRequest implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<NewDeviceStateTransactionRequest> CREATOR = new Parcelable.Creator<NewDeviceStateTransactionRequest>() {
        @Override
        public NewDeviceStateTransactionRequest createFromParcel(Parcel in) {
            return new NewDeviceStateTransactionRequest(in);
        }

        @Override
        public NewDeviceStateTransactionRequest[] newArray(int size) {
            return new NewDeviceStateTransactionRequest[size];
        }
    };
    @SerializedName("states")
    @Expose
    private States states;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    public NewDeviceStateTransactionRequest() {
    }

    protected NewDeviceStateTransactionRequest(Parcel in) {
        states = (States) in.readValue(States.class.getClassLoader());
        timestamp = in.readString();
    }

    public States getStates() {
        return states;
    }

    public void setStates(States states) {
        this.states = states;
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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(states);
        dest.writeString(timestamp);
    }
}
