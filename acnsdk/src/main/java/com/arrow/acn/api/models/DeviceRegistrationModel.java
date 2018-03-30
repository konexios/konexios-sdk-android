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
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 *  Model for registering new device or updating existing one
 */

public class DeviceRegistrationModel implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DeviceRegistrationModel> CREATOR = new Parcelable.Creator<DeviceRegistrationModel>() {
        @NonNull
        @Override
        public DeviceRegistrationModel createFromParcel(@NonNull Parcel in) {
            return new DeviceRegistrationModel(in);
        }

        @NonNull
        @Override
        public DeviceRegistrationModel[] newArray(int size) {
            return new DeviceRegistrationModel[size];
        }
    };
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
    /**
     * device name
     */
    @SerializedName("name")
    @Expose
    private String name;
    /**
     * node hid
     */
    @SerializedName("nodeHid")
    @Expose
    private String nodeHid;
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
    @Nullable
    @SerializedName("tags")
    @Expose
    private List<String> tags = new ArrayList<>();
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
    /**
     * device firmware info
     */
    @SerializedName("softwareName")
    @Expose
    private String softwareName;
    @SerializedName("softwareVersion")
    @Expose
    private String softwareVersion;

    public DeviceRegistrationModel() {
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
     * @return The info
     */
    public JsonElement getInfo() {
        if (info == null) {
            info = new JsonObject();
        }
        return info;
    }

    /**
     * @param info The info
     */
    public void setInfo(JsonElement info) {
        this.info = info;
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
     * @return The nodeHid
     */
    public String getNodeHid() {
        return nodeHid;
    }

    /**
     * @param nodeHid The nodeHid
     */
    public void setNodeHid(String nodeHid) {
        this.nodeHid = nodeHid;
    }

    /**
     * @return The properties
     */
    public JsonElement getProperties() {
        if (properties == null) {
            properties = new JsonObject();
        }
        return properties;
    }

    /**
     * @param properties The properties
     */
    public void setProperties(JsonElement properties) {
        this.properties = properties;
    }

    /**
     * @return The tags
     */
    @Nullable
    public List<String> getTags() {
        return tags;
    }

    /**
     * @param tags The tags
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
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

    protected DeviceRegistrationModel(@NonNull Parcel in) {
        enabled = in.readByte() != 0x00;
        gatewayHid = in.readString();
        JsonParser parser = new JsonParser();
        info = parser.parse(in.readString()).getAsJsonObject();
        name = in.readString();
        nodeHid = in.readString();
        properties = parser.parse(in.readString()).getAsJsonObject();
        if (in.readByte() == 0x01) {
            tags = new ArrayList<>();
            in.readList(tags, String.class.getClassLoader());
        } else {
            tags = null;
        }
        type = in.readString();
        uid = in.readString();
        userHid = in.readString();
        softwareName = in.readString();
        softwareVersion = in.readString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceRegistrationModel that = (DeviceRegistrationModel) o;

        if (enabled != that.enabled) return false;
        if (gatewayHid != null ? !gatewayHid.equals(that.gatewayHid) : that.gatewayHid != null)
            return false;
        if (info != null ? !info.equals(that.info) : that.info != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (nodeHid != null ? !nodeHid.equals(that.nodeHid) : that.nodeHid != null) return false;
        if (properties != null ? !properties.equals(that.properties) : that.properties != null)
            return false;
        if (tags != null ? !tags.equals(that.tags) : that.tags != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (uid != null ? !uid.equals(that.uid) : that.uid != null) return false;
        if (userHid != null ? !userHid.equals(that.userHid) : that.userHid != null) return false;
        if (softwareName != null ? !softwareName.equals(that.softwareName) : that.softwareName != null)
            return false;
        return softwareVersion != null ? softwareVersion.equals(that.softwareVersion) : that.softwareVersion == null;
    }

    @Override
    public int hashCode() {
        int result = (enabled ? 1 : 0);
        result = 31 * result + (gatewayHid != null ? gatewayHid.hashCode() : 0);
        result = 31 * result + (info != null ? info.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (nodeHid != null ? nodeHid.hashCode() : 0);
        result = 31 * result + (properties != null ? properties.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (uid != null ? uid.hashCode() : 0);
        result = 31 * result + (userHid != null ? userHid.hashCode() : 0);
        result = 31 * result + (softwareName != null ? softwareName.hashCode() : 0);
        result = 31 * result + (softwareVersion != null ? softwareVersion.hashCode() : 0);
        return result;
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeByte((byte) (enabled ? 0x01 : 0x00));
        dest.writeString(gatewayHid);
        Gson gson = new Gson();
        String str = gson.toJson(getInfo());
        dest.writeString(str);
        dest.writeString(name);
        dest.writeString(nodeHid);
        str = gson.toJson(getProperties());
        dest.writeString(str);
        if (tags == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(tags);
        }
        dest.writeString(type);
        dest.writeString(uid);
        dest.writeString(userHid);
        dest.writeString(softwareName);
        dest.writeString(softwareVersion);
    }

}