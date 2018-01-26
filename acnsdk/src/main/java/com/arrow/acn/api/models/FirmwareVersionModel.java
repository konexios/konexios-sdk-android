package com.arrow.acn.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by batrakov on 23.01.18.
 */

public class FirmwareVersionModel implements Parcelable {
    @SerializedName("softwareReleaseHid")
    private String softwareReleaseHid;

    @SerializedName("softwareReleaseName")
    private String softwareReleaseName;

    protected FirmwareVersionModel(Parcel in) {
        softwareReleaseHid = in.readString();
        softwareReleaseName = in.readString();
    }

    public static final Creator<FirmwareVersionModel> CREATOR = new Creator<FirmwareVersionModel>() {
        @Override
        public FirmwareVersionModel createFromParcel(Parcel in) {
            return new FirmwareVersionModel(in);
        }

        @Override
        public FirmwareVersionModel[] newArray(int size) {
            return new FirmwareVersionModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel aParcel, int aI) {
        aParcel.writeString(softwareReleaseHid);
        aParcel.writeString(softwareReleaseName);
    }

    @Override
    public boolean equals(Object aO) {
        if (this == aO) return true;
        if (aO == null || getClass() != aO.getClass()) return false;

        FirmwareVersionModel that = (FirmwareVersionModel) aO;

        if (softwareReleaseHid != null ? !softwareReleaseHid.equals(that.softwareReleaseHid) : that.softwareReleaseHid != null)
            return false;
        return softwareReleaseName != null ? softwareReleaseName.equals(that.softwareReleaseName) : that.softwareReleaseName == null;
    }

    @Override
    public int hashCode() {
        int result = softwareReleaseHid != null ? softwareReleaseHid.hashCode() : 0;
        result = 31 * result + (softwareReleaseName != null ? softwareReleaseName.hashCode() : 0);
        return result;
    }

    public String getSoftwareReleaseHid() {
        return softwareReleaseHid;
    }

    public void setSoftwareReleaseHid(String aSoftwareReleaseHid) {
        softwareReleaseHid = aSoftwareReleaseHid;
    }

    public String getSoftwareReleaseName() {
        return softwareReleaseName;
    }

    public void setSoftwareReleaseName(String aSoftwareReleaseName) {
        softwareReleaseName = aSoftwareReleaseName;
    }
}
