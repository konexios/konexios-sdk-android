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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * Model which is representing found Telemetry item
 */

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
    /**
     * Device hid
     */
    @SerializedName("deviceHid")
    @Expose
    private String deviceHid;
    /**
     * Telemetry name
     */
    @SerializedName("name")
    @Expose
    private String name;
    /**
     * the type of telemetry
     */
    @SerializedName("type")
    @Expose
    private String type;
    /**
     * timestamp telemetry has been pushed to cloud
     */
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    /**
     * value of telemetry
     */
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
        return Objects.equals(links, that.links) &&
                Objects.equals(deviceHid, that.deviceHid) &&
                Objects.equals(name, that.name) &&
                Objects.equals(type, that.type) &&
                Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(floatValue, that.floatValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(links, deviceHid, name, type, timestamp, floatValue);
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