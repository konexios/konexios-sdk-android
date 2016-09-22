package com.kronossdk.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by osminin on 8/8/2016.
 */

public final class ActionTypeModel implements Parcelable {
    @SerializedName("hid")
    private String mHid;
    @SerializedName("name")
    private String mName;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("enabled")
    private Boolean isEnabled;
    @SerializedName("systemName")
    private String mSystemName;
    @SerializedName("applicationId")
    private String mApplicationId;
    @SerializedName("parameters")
    private ActionParametersModel mParameters;

    public String getHid() {
        return mHid;
    }

    public void setHid(String hid) {
        mHid = hid;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public String getSystemName() {
        return mSystemName;
    }

    public void setSystemName(String systemName) {
        mSystemName = systemName;
    }

    public String getApplicationId() {
        return mApplicationId;
    }

    public void setApplicationId(String applicationId) {
        mApplicationId = applicationId;
    }

    public ActionParametersModel getParameters() {
        return mParameters;
    }

    public void setParameters(ActionParametersModel parameters) {
        mParameters = parameters;
    }

    protected ActionTypeModel(Parcel in) {
        mHid = in.readString();
        mName = in.readString();
        mDescription = in.readString();
        byte isEnabledVal = in.readByte();
        isEnabled = isEnabledVal == 0x02 ? null : isEnabledVal != 0x00;
        mSystemName = in.readString();
        mApplicationId = in.readString();
        mParameters = (ActionParametersModel) in.readValue(ActionParametersModel.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mHid);
        dest.writeString(mName);
        dest.writeString(mDescription);
        if (isEnabled == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isEnabled ? 0x01 : 0x00));
        }
        dest.writeString(mSystemName);
        dest.writeString(mApplicationId);
        dest.writeValue(mParameters);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ActionTypeModel> CREATOR = new Parcelable.Creator<ActionTypeModel>() {
        @Override
        public ActionTypeModel createFromParcel(Parcel in) {
            return new ActionTypeModel(in);
        }

        @Override
        public ActionTypeModel[] newArray(int size) {
            return new ActionTypeModel[size];
        }
    };
}