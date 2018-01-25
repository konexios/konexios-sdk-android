package com.arrow.acn.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by batrakov on 23.01.18.
 */

public class AvailableFirmwareResponse implements Parcelable {
    @SerializedName("availableFirmwareVersion")
    private FirmwareVersionModel availableFirmwareVersion;

    @SerializedName("currentFirmwareVersion")
    private FirmwareVersionModel currentFirmwareVersion;

    @SerializedName("deviceTypeHid")
    private String deviceTypeHid;

    @SerializedName("deviceTypeName")
    private String deviceTypeName;

    @SerializedName("hardwareVersionName")
    private String hardwareVersionName;

    @SerializedName("numberOfAssets")
    private int numberOfAssets;

    protected AvailableFirmwareResponse(Parcel in) {
        availableFirmwareVersion = in.readParcelable(FirmwareVersionModel.class.getClassLoader());
        currentFirmwareVersion = in.readParcelable(FirmwareVersionModel.class.getClassLoader());
        deviceTypeHid = in.readString();
        deviceTypeName = in.readString();
        hardwareVersionName = in.readString();
        numberOfAssets = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(availableFirmwareVersion, flags);
        dest.writeParcelable(currentFirmwareVersion, flags);
        dest.writeString(deviceTypeHid);
        dest.writeString(deviceTypeName);
        dest.writeString(hardwareVersionName);
        dest.writeInt(numberOfAssets);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AvailableFirmwareResponse> CREATOR = new Creator<AvailableFirmwareResponse>() {
        @Override
        public AvailableFirmwareResponse createFromParcel(Parcel in) {
            return new AvailableFirmwareResponse(in);
        }

        @Override
        public AvailableFirmwareResponse[] newArray(int size) {
            return new AvailableFirmwareResponse[size];
        }
    };

    @Override
    public boolean equals(Object aO) {
        if (this == aO) return true;
        if (aO == null || getClass() != aO.getClass()) return false;

        AvailableFirmwareResponse that = (AvailableFirmwareResponse) aO;

        if (numberOfAssets != that.numberOfAssets) return false;
        if (availableFirmwareVersion != null ? !availableFirmwareVersion.equals(that.availableFirmwareVersion) : that.availableFirmwareVersion != null)
            return false;
        if (currentFirmwareVersion != null ? !currentFirmwareVersion.equals(that.currentFirmwareVersion) : that.currentFirmwareVersion != null)
            return false;
        if (deviceTypeHid != null ? !deviceTypeHid.equals(that.deviceTypeHid) : that.deviceTypeHid != null)
            return false;
        if (deviceTypeName != null ? !deviceTypeName.equals(that.deviceTypeName) : that.deviceTypeName != null)
            return false;
        return hardwareVersionName != null ? hardwareVersionName.equals(that.hardwareVersionName) : that.hardwareVersionName == null;
    }

    @Override
    public int hashCode() {
        int result = availableFirmwareVersion != null ? availableFirmwareVersion.hashCode() : 0;
        result = 31 * result + (currentFirmwareVersion != null ? currentFirmwareVersion.hashCode() : 0);
        result = 31 * result + (deviceTypeHid != null ? deviceTypeHid.hashCode() : 0);
        result = 31 * result + (deviceTypeName != null ? deviceTypeName.hashCode() : 0);
        result = 31 * result + (hardwareVersionName != null ? hardwareVersionName.hashCode() : 0);
        result = 31 * result + numberOfAssets;
        return result;
    }

    public FirmwareVersionModel getAvailableFirmwareVersion() {
        return availableFirmwareVersion;
    }

    public void setAvailableFirmwareVersion(FirmwareVersionModel aAvailableFirmwareVersion) {
        availableFirmwareVersion = aAvailableFirmwareVersion;
    }

    public FirmwareVersionModel getCurrentFirmwareVersion() {
        return currentFirmwareVersion;
    }

    public void setCurrentFirmwareVersion(FirmwareVersionModel aCurrentFirmwareVersion) {
        currentFirmwareVersion = aCurrentFirmwareVersion;
    }

    public String getDeviceTypeHid() {
        return deviceTypeHid;
    }

    public void setDeviceTypeHid(String aDeviceTypeHid) {
        deviceTypeHid = aDeviceTypeHid;
    }

    public String getDeviceTypeName() {
        return deviceTypeName;
    }

    public void setDeviceTypeName(String aDeviceTypeName) {
        deviceTypeName = aDeviceTypeName;
    }

    public String getHardwareVersionName() {
        return hardwareVersionName;
    }

    public void setHardwareVersionName(String aHardwareVersionName) {
        hardwareVersionName = aHardwareVersionName;
    }

    public int getNumberOfAssets() {
        return numberOfAssets;
    }

    public void setNumberOfAssets(int aNumberOfAssets) {
        numberOfAssets = aNumberOfAssets;
    }
}
