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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

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
    private JsonElement states;

    protected FindDeviceStateResponse(Parcel in) {
        deviceHid = in.readString();
        hid = in.readString();
        links = (Links) in.readValue(Links.class.getClassLoader());
        pri = in.readString();
        states = (JsonElement) in.readValue(JsonElement.class.getClassLoader());
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

    public JsonElement getStates() {
        return states;
    }

    public void setStates(JsonElement states) {
        this.states = states;
    }

    public Map<String, Map<String, String>> getMappedStates() {
        if (states == null) {
            return null;
        }
        Gson gson = new Gson();
        Type stringStringMap = new TypeToken<Map<String, Map<String, String>>>(){}.getType();
        return gson.fromJson(states, stringStringMap);
    }

    public FindDeviceStateResponse addState(String key, String value) {
        if (states == null) {
            states = new JsonObject();
        }
        states.getAsJsonObject().addProperty(key, value);
        return this;
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
