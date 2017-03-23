package com.arrow.acn.api.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by osminin on 10/17/2016.
 */

public final class TelemetryModel implements Parcelable {
    private String telemetry;
    private String deviceType;
    private String deviceExternalId;

    public String getDeviceExternalId() {
        return deviceExternalId;
    }

    public void setDeviceExternalId(String deviceExternalId) {
        this.deviceExternalId = deviceExternalId;
    }

    public String getTelemetry() {
        return telemetry;

    }

    public void setTelemetry(String telemetry) {
        this.telemetry = telemetry;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public TelemetryModel() {
    }

    protected TelemetryModel(@NonNull Parcel in) {
        telemetry = in.readString();
        deviceType = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(telemetry);
        dest.writeString(deviceType);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TelemetryModel> CREATOR = new Parcelable.Creator<TelemetryModel>() {
        @NonNull
        @Override
        public TelemetryModel createFromParcel(@NonNull Parcel in) {
            return new TelemetryModel(in);
        }

        @NonNull
        @Override
        public TelemetryModel[] newArray(int size) {
            return new TelemetryModel[size];
        }
    };
}