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
import com.google.gson.JsonParser;
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
    private JsonElement links;
    @SerializedName("pri")
    @Expose
    private String pri;
    @SerializedName("states")
    @Expose
    private JsonElement states;

    public FindDeviceStateResponse() {
    }

    protected FindDeviceStateResponse(Parcel in) {
        deviceHid = in.readString();
        hid = in.readString();
        pri = in.readString();
        JsonParser parser = new JsonParser();
        links = parser.parse(in.readString()).getAsJsonObject();
        states = parser.parse(in.readString()).getAsJsonObject();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FindDeviceStateResponse that = (FindDeviceStateResponse) o;

        if (deviceHid != null ? !deviceHid.equals(that.deviceHid) : that.deviceHid != null)
            return false;
        if (hid != null ? !hid.equals(that.hid) : that.hid != null) return false;
        if (!getLinks().equals(that.links)) return false;
        if (pri != null ? !pri.equals(that.pri) : that.pri != null) return false;
        return getStates().equals(that.getStates());

    }

    @Override
    public int hashCode() {
        int result = deviceHid != null ? deviceHid.hashCode() : 0;
        result = 31 * result + (hid != null ? hid.hashCode() : 0);
        result = 31 * result + (links != null ? links.hashCode() : 0);
        result = 31 * result + (pri != null ? pri.hashCode() : 0);
        result = 31 * result + (states != null ? states.hashCode() : 0);
        return result;
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

    public JsonElement getLinks() {
        if (links == null) {
            links = new JsonObject();
        }
        return links;
    }

    public void setLinks(JsonElement links) {
        this.links = links;
    }

    public String getPri() {
        return pri;
    }

    public void setPri(String pri) {
        this.pri = pri;
    }

    public JsonElement getStates() {
        if (states == null) {
            states = new JsonObject();
        }
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
        dest.writeString(pri);
        Gson gson = new Gson();
        String str = gson.toJson(getLinks());
        dest.writeString(str);
        str = gson.toJson(getStates());
        dest.writeString(str);
    }
}
