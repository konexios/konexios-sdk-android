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

public class FindDeviceStateResponse implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FindDeviceStateResponse> CREATOR = new Parcelable.Creator<FindDeviceStateResponse>() {
        @Override
        public FindDeviceStateResponse createFromParcel(Parcel in) {
            return new FindDeviceStateResponse(in);
        }

        @Override
        public FindDeviceStateResponse[] newArray(int size) {
            return new FindDeviceStateResponse[size];
        }
    };
    @SerializedName("deviceHid")
    @Expose
    private String deviceHid;
    @SerializedName("hid")
    @Expose
    private String hid;
    @SerializedName("links")
    @Expose
    private Links links;
    @SerializedName("pri")
    @Expose
    private String pri;
    @SerializedName("states")
    @Expose
    private States states;

    protected FindDeviceStateResponse(Parcel in) {
        deviceHid = in.readString();
        hid = in.readString();
        links = (Links) in.readValue(Links.class.getClassLoader());
        pri = in.readString();
        states = (States) in.readValue(States.class.getClassLoader());
    }

    public String getDeviceHid() {
        return deviceHid;
    }

    public void setDeviceHid(String deviceHid) {
        this.deviceHid = deviceHid;
    }

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public String getPri() {
        return pri;
    }

    public void setPri(String pri) {
        this.pri = pri;
    }

    public States getStates() {
        return states;
    }

    public void setStates(States states) {
        this.states = states;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(deviceHid);
        dest.writeString(hid);
        dest.writeValue(links);
        dest.writeString(pri);
        dest.writeValue(states);
    }
}
