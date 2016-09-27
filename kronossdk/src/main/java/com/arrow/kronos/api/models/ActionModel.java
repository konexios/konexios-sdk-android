package com.arrow.kronos.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by osminin on 8/2/2016.
 */

public final class ActionModel implements Parcelable {
    @SerializedName("criteria")
    String mCriteria;
    @SerializedName("description")
    String mDescription;
    @SerializedName("enabled")
    Boolean mEnabled;
    @SerializedName("expiration")
    Integer mExpiration;
    @SerializedName("index")
    Integer mIndex;
    @SerializedName("parameters")
    ActionParametersModel mParameters;
    @SerializedName("systemName")
    String mSystemName;

    public String getCriteria() {
        return mCriteria;
    }

    public void setCriteria(String criteria) {
        mCriteria = criteria;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Boolean getEnabled() {
        return mEnabled;
    }

    public void setEnabled(Boolean enabled) {
        mEnabled = enabled;
    }

    public Integer getExpiration() {
        return mExpiration;
    }

    public void setExpiration(Integer expiration) {
        mExpiration = expiration;
    }

    public Integer getIndex() {
        return mIndex;
    }

    public void setIndex(Integer index) {
        mIndex = index;
    }

    public ActionParametersModel getParameters() {
        return mParameters;
    }

    public void setParameters(ActionParametersModel parameters) {
        mParameters = parameters;
    }

    public String getSystemName() {
        return mSystemName;
    }

    public void setSystemName(String systemName) {
        mSystemName = systemName;
    }

    protected ActionModel(Parcel in) {
        mCriteria = in.readString();
        mDescription = in.readString();
        byte mEnabledVal = in.readByte();
        mEnabled = mEnabledVal == 0x02 ? null : mEnabledVal != 0x00;
        mExpiration = in.readByte() == 0x00 ? null : in.readInt();
        mIndex = in.readByte() == 0x00 ? null : in.readInt();
        mParameters = (ActionParametersModel) in.readValue(ActionParametersModel.class.getClassLoader());
        mSystemName = in.readString();
    }

    public ActionModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCriteria);
        dest.writeString(mDescription);
        if (mEnabled == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (mEnabled ? 0x01 : 0x00));
        }
        if (mExpiration == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(mExpiration);
        }
        if (mIndex == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(mIndex);
        }
        dest.writeValue(mParameters);
        dest.writeString(mSystemName);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ActionModel> CREATOR = new Parcelable.Creator<ActionModel>() {
        @Override
        public ActionModel createFromParcel(Parcel in) {
            return new ActionModel(in);
        }

        @Override
        public ActionModel[] newArray(int size) {
            return new ActionModel[size];
        }
    };
}