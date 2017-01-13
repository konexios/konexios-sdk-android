package com.arrow.kronos.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TelemetryItemModel {

    @SerializedName("links")
    @Expose
    private Links links;
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

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
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

}