package com.arrow.acn.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by batrakov on 23.01.18.
 */

public class CreateAndStartSoftwareReleaseScheduleRequest implements Parcelable {

    @SerializedName("deviceCategory")
    private String deviceCategory;

    @SerializedName("objectHids")
    private List<String> objectHids;

    @SerializedName("softwareReleaseHid")
    private String softwareReleaseHid;

    @SerializedName("userHid")
    private String userHid;

    protected CreateAndStartSoftwareReleaseScheduleRequest(Parcel in) {
        deviceCategory = in.readString();
        objectHids = in.createStringArrayList();
        softwareReleaseHid = in.readString();
        userHid = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(deviceCategory);
        dest.writeStringList(objectHids);
        dest.writeString(softwareReleaseHid);
        dest.writeString(userHid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @SuppressWarnings("unused")
    public static final Creator<CreateAndStartSoftwareReleaseScheduleRequest> CREATOR = new Creator<CreateAndStartSoftwareReleaseScheduleRequest>() {
        @Override
        public CreateAndStartSoftwareReleaseScheduleRequest createFromParcel(Parcel in) {
            return new CreateAndStartSoftwareReleaseScheduleRequest(in);
        }

        @Override
        public CreateAndStartSoftwareReleaseScheduleRequest[] newArray(int size) {
            return new CreateAndStartSoftwareReleaseScheduleRequest[size];
        }
    };

    public String getDeviceCategory() {
        return deviceCategory;
    }

    public void setDeviceCategory(String aDeviceCategory) {
        deviceCategory = aDeviceCategory;
    }

    public List<String> getObjectHids() {
        return objectHids;
    }

    public void setObjectHids(List<String> aObjectHids) {
        objectHids = aObjectHids;
    }

    public String getSoftwareReleaseHid() {
        return softwareReleaseHid;
    }

    public void setSoftwareReleaseHid(String aSoftwareReleaseHid) {
        softwareReleaseHid = aSoftwareReleaseHid;
    }

    public String getUserHid() {
        return userHid;
    }

    public void setUserHid(String aUserHid) {
        userHid = aUserHid;
    }

    @Override
    public boolean equals(Object aO) {
        if (this == aO) return true;
        if (aO == null || getClass() != aO.getClass()) return false;

        CreateAndStartSoftwareReleaseScheduleRequest that = (CreateAndStartSoftwareReleaseScheduleRequest) aO;

        if (deviceCategory != null ? !deviceCategory.equals(that.deviceCategory) : that.deviceCategory != null)
            return false;
        if (objectHids != null ? !objectHids.equals(that.objectHids) : that.objectHids != null)
            return false;
        if (softwareReleaseHid != null ? !softwareReleaseHid.equals(that.softwareReleaseHid) : that.softwareReleaseHid != null)
            return false;
        return userHid != null ? userHid.equals(that.userHid) : that.userHid == null;
    }

    @Override
    public int hashCode() {
        int result = deviceCategory != null ? deviceCategory.hashCode() : 0;
        result = 31 * result + (objectHids != null ? objectHids.hashCode() : 0);
        result = 31 * result + (softwareReleaseHid != null ? softwareReleaseHid.hashCode() : 0);
        result = 31 * result + (userHid != null ? userHid.hashCode() : 0);
        return result;
    }
}
