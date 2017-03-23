package com.arrow.acn.api.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by osminin on 12.10.2016.
 */

final public class DeviceTypeTelemetryModel implements Parcelable {
    @SerializedName("controllable")
    @Expose
    private boolean controllable;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private String type;

    /**
     * @return The controllable
     */
    public boolean isControllable() {
        return controllable;
    }

    /**
     * @param controllable The controllable
     */
    public void setControllable(boolean controllable) {
        this.controllable = controllable;
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


    protected DeviceTypeTelemetryModel(@NonNull Parcel in) {
        controllable = in.readByte() != 0x00;
        description = in.readString();
        name = in.readString();
        type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeByte((byte) (controllable ? 0x01 : 0x00));
        dest.writeString(description);
        dest.writeString(name);
        dest.writeString(type);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DeviceTypeTelemetryModel> CREATOR = new Parcelable.Creator<DeviceTypeTelemetryModel>() {
        @NonNull
        @Override
        public DeviceTypeTelemetryModel createFromParcel(@NonNull Parcel in) {
            return new DeviceTypeTelemetryModel(in);
        }

        @NonNull
        @Override
        public DeviceTypeTelemetryModel[] newArray(int size) {
            return new DeviceTypeTelemetryModel[size];
        }
    };
}