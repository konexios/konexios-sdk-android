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

public class TelemetryItemModel implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TelemetryItemModel> CREATOR = new Parcelable.Creator<TelemetryItemModel>() {
        @Override
        public TelemetryItemModel createFromParcel(Parcel in) {
            return new TelemetryItemModel(in);
        }

        @Override
        public TelemetryItemModel[] newArray(int size) {
            return new TelemetryItemModel[size];
        }
    };
    @SerializedName("links")
    @Expose
    private JsonElement links;
    @SerializedName("deviceHid")
    @Expose
    private String deviceHid;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("floatValue")
    @Expose
    private Float floatValue;

    protected TelemetryItemModel(Parcel in) {
        JsonParser parser = new JsonParser();
        links = parser.parse(in.readString()).getAsJsonObject();
        deviceHid = in.readString();
        name = in.readString();
        type = in.readString();
        timestamp = in.readString();
        floatValue = in.readByte() == 0x00 ? null : in.readFloat();
    }

    public TelemetryItemModel() {
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

    public String getDeviceHid() {
        return deviceHid;
    }

    public void setDeviceHid(String deviceHid) {
        this.deviceHid = deviceHid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Float getFloatValue() {
        return floatValue;
    }

    public void setFloatValue(Float floatValue) {
        this.floatValue = floatValue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TelemetryItemModel that = (TelemetryItemModel) o;

        if (!getLinks().equals(that.getLinks())) return false;
        if (deviceHid != null ? !deviceHid.equals(that.deviceHid) : that.deviceHid != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null)
            return false;
        return floatValue != null ? floatValue.equals(that.floatValue) : that.floatValue == null;

    }

    @Override
    public int hashCode() {
        int result = links != null ? links.hashCode() : 0;
        result = 31 * result + (deviceHid != null ? deviceHid.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        result = 31 * result + (floatValue != null ? floatValue.hashCode() : 0);
        return result;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(new Gson().toJson(getLinks()));
        dest.writeString(deviceHid);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(timestamp);
        if (floatValue == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeFloat(floatValue);
        }
    }
}