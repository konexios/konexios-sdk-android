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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * New Device model
 */

public class DeviceModel implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DeviceModel> CREATOR = new Parcelable.Creator<DeviceModel>() {
        @NonNull
        @Override
        public DeviceModel createFromParcel(@NonNull Parcel in) {
            return new DeviceModel(in);
        }

        @NonNull
        @Override
        public DeviceModel[] newArray(int size) {
            return new DeviceModel[size];
        }
    };
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    /**
     * is device enabled flag
     */
    @SerializedName("enabled")
    @Expose
    private boolean enabled;
    /**
     * hid of gateway
     */
    @SerializedName("gatewayHid")
    @Expose
    private String gatewayHid;
    @SerializedName("hid")
    @Expose
    private String hid;
    /**
     * device info Json string like:
     *
     * {
     *    "type": "android",
     *    "name": "AndroidInternal",
     *    "bleAddress": "",
     *    "uid": "android-bded-1030-0033-c5870033c587"
     * }
     */
    @SerializedName("info")
    @Expose
    private JsonElement info;
    @SerializedName("links")
    @Expose
    private JsonElement links;
    /**
     * device name
     */
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("pri")
    @Expose
    private String pri;
    /**
     * Json string represents enabled/disabled sensors like:
     * {
     *    "HumiditySensor/enabled": true,
     *    "HeartRateSensor/enabled": true,
     *    "GyroscopeSensor/enabled": true,
     *    "PedometerSensor/enabled": true,
     *    "AccelerometerSensor/enabled": true,
     *    "TemperatureSensor/enabled": true,
     *    "PressureSensor/enabled": true,
     *    "LightSensor/enabled": true,
     *    "Magnetometer/enabled": true
     *}
     */
    @SerializedName("properties")
    @Expose
    private JsonElement properties;
    /**
     * device type
     */
    @SerializedName("type")
    @Expose
    private String type;
    /**
     * unique id for this device
     */
    @SerializedName("uid")
    @Expose
    private String uid;
    /**
     * user's hid
     */
    @SerializedName("userHid")
    @Expose
    private String userHid;

    public DeviceModel() {
    }

    protected DeviceModel(@NonNull Parcel in) {
        createdDate = in.readString();
        enabled = in.readByte() != 0x00;
        gatewayHid = in.readString();
        hid = in.readString();
        name = in.readString();
        pri = in.readString();
        type = in.readString();
        uid = in.readString();
        userHid = in.readString();
        JsonParser parser = new JsonParser();
        info = parser.parse(in.readString()).getAsJsonObject();
        links = parser.parse(in.readString()).getAsJsonObject();
        properties = parser.parse(in.readString()).getAsJsonObject();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(createdDate);
        dest.writeByte((byte) (enabled ? 0x01 : 0x00));
        dest.writeString(gatewayHid);
        dest.writeString(hid);
        dest.writeString(name);
        dest.writeString(pri);
        dest.writeString(type);
        dest.writeString(uid);
        dest.writeString(userHid);
        Gson gson = new Gson();
        String str = gson.toJson(getInfo());
        dest.writeString(str);
        str = gson.toJson(getLinks());
        dest.writeString(str);
        str = gson.toJson(getProperties());
        dest.writeString(str);
    }

    /**
     * @return The createdDate
     */
    public String getCreatedDate() {
        return createdDate;
    }

    /**
     * @param date The date
     */
    public void setCreatedDate(String date) {
        this.createdDate = date;
    }

    /**
     * @return The enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @param enabled The enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @return The gatewayHid
     */
    public String getGatewayHid() {
        return gatewayHid;
    }

    /**
     * @param gatewayHid The gatewayHid
     */
    public void setGatewayHid(String gatewayHid) {
        this.gatewayHid = gatewayHid;
    }

    /**
     * @return The hid
     */
    public String getHid() {
        return hid;
    }

    /**
     * @param hid The hid
     */
    public void setHid(String hid) {
        this.hid = hid;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The pri
     */
    public String getPri() {
        return pri;
    }

    /**
     * @param pri The pri
     */
    public void setPri(String pri) {
        this.pri = pri;
    }

    public JsonElement getInfo() {
        if (info == null) {
            info = new JsonObject();
        }
        return info;
    }

    public void setInfo(JsonElement info) {
        this.info = info;
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

    public JsonElement getProperties() {
        if (properties == null) {
            properties = new JsonObject();
        }
        return properties;
    }

    public void setProperties(JsonElement properties) {
        this.properties = properties;
    }

    /**
     * @return The type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return The uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * @param uid The uid
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * @return The userHid
     */
    public String getUserHid() {
        return userHid;
    }

    /**
     * @param userHid The userHid
     */
    public void setUserHid(String userHid) {
        this.userHid = userHid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceModel that = (DeviceModel) o;

        if (enabled != that.enabled) return false;
        if (createdDate != null ? !createdDate.equals(that.createdDate) : that.createdDate != null)
            return false;
        if (gatewayHid != null ? !gatewayHid.equals(that.gatewayHid) : that.gatewayHid != null)
            return false;
        if (hid != null ? !hid.equals(that.hid) : that.hid != null) return false;
        if (!getInfo().equals(that.getInfo())) return false;
        if (!getLinks().equals(that.getLinks())) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (pri != null ? !pri.equals(that.pri) : that.pri != null) return false;
        if (!getProperties().equals(that.getProperties()))
            return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (uid != null ? !uid.equals(that.uid) : that.uid != null) return false;
        return userHid != null ? userHid.equals(that.userHid) : that.userHid == null;

    }

    @Override
    public int hashCode() {
        int result = createdDate != null ? createdDate.hashCode() : 0;
        result = 31 * result + (enabled ? 1 : 0);
        result = 31 * result + (gatewayHid != null ? gatewayHid.hashCode() : 0);
        result = 31 * result + (hid != null ? hid.hashCode() : 0);
        result = 31 * result + (info != null ? info.hashCode() : 0);
        result = 31 * result + (links != null ? links.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (pri != null ? pri.hashCode() : 0);
        result = 31 * result + (properties != null ? properties.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (uid != null ? uid.hashCode() : 0);
        result = 31 * result + (userHid != null ? userHid.hashCode() : 0);
        return result;
    }
}