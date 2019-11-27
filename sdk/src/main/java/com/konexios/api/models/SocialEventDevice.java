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

public class SocialEventDevice implements Parcelable {

    @SerializedName("hid")
    @Expose
    private String hid;
    @SerializedName("links")
    @Expose
    private JsonElement links;
    @SerializedName("pinCode")
    @Expose
    private String pinCode;
    @SerializedName("macAddress")
    @Expose
    private String macAddress;
    @SerializedName("deviceTypeHid")
    @Expose
    private String deviceTypeHid;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("createdBy")
    @Expose
    private String createdBy;
    @SerializedName("lastModifiedDate")
    @Expose
    private String lastModifiedDate;
    @SerializedName("lastModifiedBy")
    @Expose
    private String lastModifiedBy;

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

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getDeviceTypeHid() {
        return deviceTypeHid;
    }

    public void setDeviceTypeHid(String deviceTypeHid) {
        this.deviceTypeHid = deviceTypeHid;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }


    protected SocialEventDevice(Parcel in) {
        hid = in.readString();
        JsonParser parser = new JsonParser();
        links = parser.parse(in.readString()).getAsJsonObject();
        pinCode = in.readString();
        macAddress = in.readString();
        deviceTypeHid = in.readString();
        createdDate = in.readString();
        createdBy = in.readString();
        lastModifiedDate = in.readString();
        lastModifiedBy = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hid);
        String str = new Gson().toJson(getLinks());
        dest.writeString(str);
        dest.writeString(pinCode);
        dest.writeString(macAddress);
        dest.writeString(deviceTypeHid);
        dest.writeString(createdDate);
        dest.writeString(createdBy);
        dest.writeString(lastModifiedDate);
        dest.writeString(lastModifiedBy);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SocialEventDevice> CREATOR = new Parcelable.Creator<SocialEventDevice>() {
        @Override
        public SocialEventDevice createFromParcel(Parcel in) {
            return new SocialEventDevice(in);
        }

        @Override
        public SocialEventDevice[] newArray(int size) {
            return new SocialEventDevice[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SocialEventDevice that = (SocialEventDevice) o;
        return Objects.equals(hid, that.hid) &&
                Objects.equals(links, that.links) &&
                Objects.equals(pinCode, that.pinCode) &&
                Objects.equals(macAddress, that.macAddress) &&
                Objects.equals(deviceTypeHid, that.deviceTypeHid) &&
                Objects.equals(createdDate, that.createdDate) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
                Objects.equals(lastModifiedBy, that.lastModifiedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hid, links, pinCode, macAddress, deviceTypeHid, createdDate, createdBy, lastModifiedDate, lastModifiedBy);
    }
}