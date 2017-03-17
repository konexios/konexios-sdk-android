package com.arrow.acn.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NodeTypeRegistrationModel implements Parcelable {

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("enabled")
    @Expose
    private boolean enabled;
    @SerializedName("name")
    @Expose
    private String name;

    /**
     *
     * @return
     * The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The description
     */
    public void setDescription(String description) {
        this.description = description;
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


    protected NodeTypeRegistrationModel(Parcel in) {
        description = in.readString();
        enabled = in.readByte() != 0x00;
        name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeByte((byte) (enabled ? 0x01 : 0x00));
        dest.writeString(name);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<NodeTypeRegistrationModel> CREATOR = new Parcelable.Creator<NodeTypeRegistrationModel>() {
        @Override
        public NodeTypeRegistrationModel createFromParcel(Parcel in) {
            return new NodeTypeRegistrationModel(in);
        }

        @Override
        public NodeTypeRegistrationModel[] newArray(int size) {
            return new NodeTypeRegistrationModel[size];
        }
    };
}