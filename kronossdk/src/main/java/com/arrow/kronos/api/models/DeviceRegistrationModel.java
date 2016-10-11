package com.arrow.kronos.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceRegistrationModel implements Parcelable {

    @SerializedName("enabled")
    @Expose
    private boolean enabled;
    @SerializedName("gatewayHid")
    @Expose
    private String gatewayHid;
    @SerializedName("info")
    @Expose
    private Info info;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("nodeHid")
    @Expose
    private String nodeHid;
    @SerializedName("properties")
    @Expose
    private Properties properties;
    @SerializedName("tags")
    @Expose
    private List<String> tags = new ArrayList<String>();
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
     * The nodeHid
     */
    public String getNodeHid() {
        return nodeHid;
    }

    /**
     *
     * @param nodeHid
     * The nodeHid
     */
    public void setNodeHid(String nodeHid) {
        this.nodeHid = nodeHid;
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
     * The tags
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     *
     * @param tags
     * The tags
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
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

    public DeviceRegistrationModel() {
    }

    protected DeviceRegistrationModel(Parcel in) {
        enabled = in.readByte() != 0x00;
        gatewayHid = in.readString();
        info = (Info) in.readValue(Info.class.getClassLoader());
        name = in.readString();
        nodeHid = in.readString();
        properties = (Properties) in.readValue(Properties.class.getClassLoader());
        if (in.readByte() == 0x01) {
            tags = new ArrayList<String>();
            in.readList(tags, String.class.getClassLoader());
        } else {
            tags = null;
        }
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
        dest.writeByte((byte) (enabled ? 0x01 : 0x00));
        dest.writeString(gatewayHid);
        dest.writeValue(info);
        dest.writeString(name);
        dest.writeString(nodeHid);
        dest.writeValue(properties);
        if (tags == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(tags);
        }
        dest.writeString(type);
        dest.writeString(uid);
        dest.writeString(userHid);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CommonResponse> CREATOR = new Parcelable.Creator<CommonResponse>() {
        @Override
        public CommonResponse createFromParcel(Parcel in) {
            return new CommonResponse(in);
        }

        @Override
        public CommonResponse[] newArray(int size) {
            return new CommonResponse[size];
        }
    };
}