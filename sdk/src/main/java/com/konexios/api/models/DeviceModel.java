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

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

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
     * device firmware info
     */
    @SerializedName("softwareName")
    @Expose
    private String softwareName;
    @SerializedName("softwareVersion")
    @Expose
    private String softwareVersion;
    /**
     * device info Json string like:
     * <p>
     * {
     * "type": "android",
     * "name": "AndroidInternal",
     * "bleAddress": "",
     * "uid": "android-bded-1030-0033-c5870033c587"
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
     * "HumiditySensor/enabled": true,
     * "HeartRateSensor/enabled": true,
     * "GyroscopeSensor/enabled": true,
     * "PedometerSensor/enabled": true,
     * "AccelerometerSensor/enabled": true,
     * "TemperatureSensor/enabled": true,
     * "PressureSensor/enabled": true,
     * "LightSensor/enabled": true,
     * "Magnetometer/enabled": true
     * }
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
        softwareName = in.readString();
        softwareVersion = in.readString();
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
        dest.writeString(softwareName);
        dest.writeString(softwareVersion);
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

    public String getSoftwareName() {
        return softwareName;
    }

    public void setSoftwareName(String softwareName) {
        this.softwareName = softwareName;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
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
        return enabled == that.enabled &&
                Objects.equals(createdDate, that.createdDate) &&
                Objects.equals(gatewayHid, that.gatewayHid) &&
                Objects.equals(hid, that.hid) &&
                Objects.equals(softwareName, that.softwareName) &&
                Objects.equals(softwareVersion, that.softwareVersion) &&
                Objects.equals(info, that.info) &&
                Objects.equals(links, that.links) &&
                Objects.equals(name, that.name) &&
                Objects.equals(pri, that.pri) &&
                Objects.equals(properties, that.properties) &&
                Objects.equals(type, that.type) &&
                Objects.equals(uid, that.uid) &&
                Objects.equals(userHid, that.userHid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdDate, enabled, gatewayHid, hid, softwareName, softwareVersion, info, links, name, pri, properties, type, uid, userHid);
    }
}