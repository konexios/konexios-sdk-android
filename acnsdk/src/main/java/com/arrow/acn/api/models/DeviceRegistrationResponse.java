package com.arrow.acn.api.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Created by osminin on 14.10.2016.
 */

public final class DeviceRegistrationResponse extends CommonResponse implements Parcelable {
    @SerializedName("externalId")
    private String externalId;

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    protected DeviceRegistrationResponse(@NonNull Parcel in) {
        super(in);
        externalId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(externalId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DeviceRegistrationResponse> CREATOR = new Parcelable.Creator<DeviceRegistrationResponse>() {
        @NonNull
        @Override
        public DeviceRegistrationResponse createFromParcel(@NonNull Parcel in) {
            return new DeviceRegistrationResponse(in);
        }

        @NonNull
        @Override
        public DeviceRegistrationResponse[] newArray(int size) {
            return new DeviceRegistrationResponse[size];
        }
    };
}