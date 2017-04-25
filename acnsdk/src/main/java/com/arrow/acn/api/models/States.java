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

/**
 * Created by osminin on 4/24/2017.
 */

public class States implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<States> CREATOR = new Parcelable.Creator<States>() {
        @Override
        public States createFromParcel(Parcel in) {
            return new States(in);
        }

        @Override
        public States[] newArray(int size) {
            return new States[size];
        }
    };
    private String state;

    public States(String state) {
        this.state = state;
    }

    protected States(Parcel in) {
        state = in.readString();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(state);
    }
}
