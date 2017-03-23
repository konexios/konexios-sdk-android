package com.arrow.acn.api.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Created by osminin on 12.10.2016.
 */

public class DeviceEventModel implements Parcelable {
    @SerializedName("deviceActionTypeName")
    private String mDeviceActionTypeName;
    @SerializedName("criteria")
    private String mCriteria;
    @SerializedName("createdDate")
    private String mCreatedDate;
    @SerializedName("status")
    private String mStatus;

    public String getDeviceActionTypeName() {
        return mDeviceActionTypeName;
    }

    public void setDeviceActionTypeName(String deviceActionTypeName) {
        mDeviceActionTypeName = deviceActionTypeName;
    }

    public String getCriteria() {
        return mCriteria;
    }

    public void setCriteria(String criteria) {
        mCriteria = criteria;
    }

    public String getCreatedDate() {
        return mCreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        mCreatedDate = createdDate;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    protected DeviceEventModel(@NonNull Parcel in) {
        mDeviceActionTypeName = in.readString();
        mCriteria = in.readString();
        mCreatedDate = in.readString();
        mStatus = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(mDeviceActionTypeName);
        dest.writeString(mCriteria);
        if (mCreatedDate == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeString(mCreatedDate);
        }
        dest.writeString(mStatus);
    }

    @SuppressWarnings("unused")
    public final Parcelable.Creator<DeviceEventModel> CREATOR = new Parcelable.Creator<DeviceEventModel>() {
        @NonNull
        @Override
        public DeviceEventModel createFromParcel(@NonNull Parcel in) {
            return new DeviceEventModel(in);
        }

        @NonNull
        @Override
        public DeviceEventModel[] newArray(int size) {
            return new DeviceEventModel[size];
        }
    };
}
