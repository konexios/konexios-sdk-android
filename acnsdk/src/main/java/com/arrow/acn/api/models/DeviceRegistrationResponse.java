package com.arrow.acn.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by osminin on 14.10.2016.
 */

public final class DeviceRegistrationResponse extends CommonResponse implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DeviceRegistrationResponse> CREATOR = new Parcelable.Creator<DeviceRegistrationResponse>() {
        @Override
        public DeviceRegistrationResponse createFromParcel(Parcel in) {
            return new DeviceRegistrationResponse(in);
        }

        @Override
        public DeviceRegistrationResponse[] newArray(int size) {
            return new DeviceRegistrationResponse[size];
        }
    };
    @SerializedName("externalId")
    private String externalId;

    protected DeviceRegistrationResponse(Parcel in) {
        super(in);
        externalId = in.readString();
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(externalId);
    }
}