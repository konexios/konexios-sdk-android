package com.arrow.acn.api.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DeviceTypeModel implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DeviceTypeModel> CREATOR = new Parcelable.Creator<DeviceTypeModel>() {
        @NonNull
        @Override
        public DeviceTypeModel createFromParcel(@NonNull Parcel in) {
            return new DeviceTypeModel(in);
        }

        @NonNull
        @Override
        public DeviceTypeModel[] newArray(int size) {
            return new DeviceTypeModel[size];
        }
    };
    @SerializedName("createdBy")
    @Expose
    private String createdBy;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("enabled")
    @Expose
    private boolean enabled;
    @SerializedName("hid")
    @Expose
    private String hid;
    @SerializedName("lastModifiedBy")
    @Expose
    private String lastModifiedBy;
    @SerializedName("lastModifiedDate")
    @Expose
    private String lastModifiedDate;
    @SerializedName("links")
    @Expose
    private Links links;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("pri")
    @Expose
    private String pri;
    @Nullable
    @SerializedName("telemetries")
    @Expose
    private List<DeviceTypeTelemetryModel> telemetries = new ArrayList<DeviceTypeTelemetryModel>();

    protected DeviceTypeModel(@NonNull Parcel in) {
        createdBy = in.readString();
        createdDate = in.readString();
        description = in.readString();
        enabled = in.readByte() != 0x00;
        hid = in.readString();
        lastModifiedBy = in.readString();
        lastModifiedDate = in.readString();
        links = (Links) in.readValue(Links.class.getClassLoader());
        name = in.readString();
        pri = in.readString();
        if (in.readByte() == 0x01) {
            telemetries = new ArrayList<DeviceTypeTelemetryModel>();
            in.readList(telemetries, DeviceTypeTelemetryModel.class.getClassLoader());
        } else {
            telemetries = null;
        }
    }

    /**
     * @return The createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy The createdBy
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return The createdDate
     */
    public String getCreatedDate() {
        return createdDate;
    }

    /**
     * @param createdDate The createdDate
     */
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description
     */
    public void setDescription(String description) {
        this.description = description;
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
     * @return The lastModifiedBy
     */
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    /**
     * @param lastModifiedBy The lastModifiedBy
     */
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    /**
     * @return The lastModifiedDate
     */
    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * @param lastModifiedDate The lastModifiedDate
     */
    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
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
     * @return The telemetries
     */
    @Nullable
    public List<DeviceTypeTelemetryModel> getTelemetries() {
        return telemetries;
    }

    /**
     * @param telemetries The telemetries
     */
    public void setTelemetries(List<DeviceTypeTelemetryModel> telemetries) {
        this.telemetries = telemetries;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(createdBy);
        dest.writeString(createdDate);
        dest.writeString(description);
        dest.writeByte((byte) (enabled ? 0x01 : 0x00));
        dest.writeString(hid);
        dest.writeString(lastModifiedBy);
        dest.writeString(lastModifiedDate);
        dest.writeValue(links);
        dest.writeString(name);
        dest.writeString(pri);
        if (telemetries == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(telemetries);
        }
    }
}