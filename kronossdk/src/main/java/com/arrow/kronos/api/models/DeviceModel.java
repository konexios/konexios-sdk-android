package com.arrow.kronos.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceModel implements Parcelable {

    @SerializedName("createdDate")
    @Expose
    private CreatedDate createdDate;
    @SerializedName("enabled")
    @Expose
    private boolean enabled;
    @SerializedName("gatewayHid")
    @Expose
    private String gatewayHid;
    @SerializedName("hid")
    @Expose
    private String hid;
    @SerializedName("info")
    @Expose
    private Info info;
    @SerializedName("links")
    @Expose
    private Links links;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("pri")
    @Expose
    private String pri;
    @SerializedName("properties")
    @Expose
    private Properties properties;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("userHid")
    @Expose
    private String userHid;

    /**
     *
     * @return
     * The createdDate
     */
    public CreatedDate getCreatedDate() {
        return createdDate;
    }

    /**
     *
     * @param createdDate
     * The createdDate
     */
    public void setCreatedDate(CreatedDate createdDate) {
        this.createdDate = createdDate;
    }

    /**
     *
     * @return
     * The enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     *
     * @param enabled
     * The enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     *
     * @return
     * The gatewayHid
     */
    public String getGatewayHid() {
        return gatewayHid;
    }

    /**
     *
     * @param gatewayHid
     * The gatewayHid
     */
    public void setGatewayHid(String gatewayHid) {
        this.gatewayHid = gatewayHid;
    }

    /**
     *
     * @return
     * The hid
     */
    public String getHid() {
        return hid;
    }

    /**
     *
     * @param hid
     * The hid
     */
    public void setHid(String hid) {
        this.hid = hid;
    }

    /**
     *
     * @return
     * The info
     */
    public Info getInfo() {
        return info;
    }

    /**
     *
     * @param info
     * The info
     */
    public void setInfo(Info info) {
        this.info = info;
    }

    /**
     *
     * @return
     * The links
     */
    public Links getLinks() {
        return links;
    }

    /**
     *
     * @param links
     * The links
     */
    public void setLinks(Links links) {
        this.links = links;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The pri
     */
    public String getPri() {
        return pri;
    }

    /**
     *
     * @param pri
     * The pri
     */
    public void setPri(String pri) {
        this.pri = pri;
    }

    /**
     *
     * @return
     * The properties
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     *
     * @param properties
     * The properties
     */
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    /**
     *
     * @return
     * The type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     * The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     * The uid
     */
    public String getUid() {
        return uid;
    }

    /**
     *
     * @param uid
     * The uid
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     *
     * @return
     * The userHid
     */
    public String getUserHid() {
        return userHid;
    }

    /**
     *
     * @param userHid
     * The userHid
     */
    public void setUserHid(String userHid) {
        this.userHid = userHid;
    }

    private class Info {
    }

    private class Links {
    }

    private class Properties {
    }

    protected DeviceModel(Parcel in) {
        createdDate = (CreatedDate) in.readValue(CreatedDate.class.getClassLoader());
        enabled = in.readByte() != 0x00;
        gatewayHid = in.readString();
        hid = in.readString();
        info = (Info) in.readValue(Info.class.getClassLoader());
        links = (Links) in.readValue(Links.class.getClassLoader());
        name = in.readString();
        pri = in.readString();
        properties = (Properties) in.readValue(Properties.class.getClassLoader());
        type = in.readString();
        uid = in.readString();
        userHid = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(createdDate);
        dest.writeByte((byte) (enabled ? 0x01 : 0x00));
        dest.writeString(gatewayHid);
        dest.writeString(hid);
        dest.writeValue(info);
        dest.writeValue(links);
        dest.writeString(name);
        dest.writeString(pri);
        dest.writeValue(properties);
        dest.writeString(type);
        dest.writeString(uid);
        dest.writeString(userHid);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DeviceModel> CREATOR = new Parcelable.Creator<DeviceModel>() {
        @Override
        public DeviceModel createFromParcel(Parcel in) {
            return new DeviceModel(in);
        }

        @Override
        public DeviceModel[] newArray(int size) {
            return new DeviceModel[size];
        }
    };
}