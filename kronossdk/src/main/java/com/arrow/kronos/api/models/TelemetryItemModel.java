package com.arrow.kronos.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TelemetryItemModel {

    @SerializedName("binaryValue")
    @Expose
    private String binaryValue;
    @SerializedName("boolValue")
    @Expose
    private boolean boolValue;
    @SerializedName("dateValue")
    @Expose
    private String dateValue;
    @SerializedName("datetimeValue")
    @Expose
    private String datetimeValue;
    @SerializedName("deviceHid")
    @Expose
    private String deviceHid;
    @SerializedName("floatCubeValue")
    @Expose
    private String floatCubeValue;
    @SerializedName("floatSqrValue")
    @Expose
    private String floatSqrValue;
    @SerializedName("floatValue")
    @Expose
    private int floatValue;
    @SerializedName("hid")
    @Expose
    private String hid;
    @SerializedName("intCubeValue")
    @Expose
    private String intCubeValue;
    @SerializedName("intSqrValue")
    @Expose
    private String intSqrValue;
    @SerializedName("intValue")
    @Expose
    private int intValue;
    @SerializedName("links")
    @Expose
    private Links links;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("pri")
    @Expose
    private String pri;
    @SerializedName("strValue")
    @Expose
    private String strValue;
    @SerializedName("timestamp")
    @Expose
    private int timestamp;
    @SerializedName("type")
    @Expose
    private String type;

    /**
     * @return The binaryValue
     */
    public String getBinaryValue() {
        return binaryValue;
    }

    /**
     * @param binaryValue The binaryValue
     */
    public void setBinaryValue(String binaryValue) {
        this.binaryValue = binaryValue;
    }

    /**
     * @return The boolValue
     */
    public boolean isBoolValue() {
        return boolValue;
    }

    /**
     * @param boolValue The boolValue
     */
    public void setBoolValue(boolean boolValue) {
        this.boolValue = boolValue;
    }

    /**
     * @return The dateValue
     */
    public String getDateValue() {
        return dateValue;
    }

    /**
     * @param dateValue The dateValue
     */
    public void setDateValue(String dateValue) {
        this.dateValue = dateValue;
    }

    /**
     * @return The datetimeValue
     */
    public String getDatetimeValue() {
        return datetimeValue;
    }

    /**
     * @param datetimeValue The datetimeValue
     */
    public void setDatetimeValue(String datetimeValue) {
        this.datetimeValue = datetimeValue;
    }

    /**
     * @return The deviceHid
     */
    public String getDeviceHid() {
        return deviceHid;
    }

    /**
     * @param deviceHid The deviceHid
     */
    public void setDeviceHid(String deviceHid) {
        this.deviceHid = deviceHid;
    }

    /**
     * @return The floatCubeValue
     */
    public String getFloatCubeValue() {
        return floatCubeValue;
    }

    /**
     * @param floatCubeValue The floatCubeValue
     */
    public void setFloatCubeValue(String floatCubeValue) {
        this.floatCubeValue = floatCubeValue;
    }

    /**
     * @return The floatSqrValue
     */
    public String getFloatSqrValue() {
        return floatSqrValue;
    }

    /**
     * @param floatSqrValue The floatSqrValue
     */
    public void setFloatSqrValue(String floatSqrValue) {
        this.floatSqrValue = floatSqrValue;
    }

    /**
     * @return The floatValue
     */
    public int getFloatValue() {
        return floatValue;
    }

    /**
     * @param floatValue The floatValue
     */
    public void setFloatValue(int floatValue) {
        this.floatValue = floatValue;
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
     * @return The intCubeValue
     */
    public String getIntCubeValue() {
        return intCubeValue;
    }

    /**
     * @param intCubeValue The intCubeValue
     */
    public void setIntCubeValue(String intCubeValue) {
        this.intCubeValue = intCubeValue;
    }

    /**
     * @return The intSqrValue
     */
    public String getIntSqrValue() {
        return intSqrValue;
    }

    /**
     * @param intSqrValue The intSqrValue
     */
    public void setIntSqrValue(String intSqrValue) {
        this.intSqrValue = intSqrValue;
    }

    /**
     * @return The intValue
     */
    public int getIntValue() {
        return intValue;
    }

    /**
     * @param intValue The intValue
     */
    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    /**
     * @return The links
     */
    public Links getLinks() {
        return links;
    }

    /**
     * @param links The links
     */
    public void setLinks(Links links) {
        this.links = links;
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

    /**
     * @return The strValue
     */
    public String getStrValue() {
        return strValue;
    }

    /**
     * @param strValue The strValue
     */
    public void setStrValue(String strValue) {
        this.strValue = strValue;
    }

    /**
     * @return The timestamp
     */
    public int getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp The timestamp
     */
    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
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

}