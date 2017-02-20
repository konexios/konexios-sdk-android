package com.arrow.acn.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by osminin on 8/8/2016.
 */

public final class ActionParametersModel implements Parcelable {
    @SerializedName("Email")
    String mEmail;
    @SerializedName("Message")
    String mMessage;
    @SerializedName("Phone")
    String mPhone;
    @SerializedName("SipAddress")
    String mSipAddress;
    @SerializedName("Severity")
    String mSeverity;
    @SerializedName("Location")
    String mLocation;
    @SerializedName("URL")
    String mUrl;

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getSipAddress() {
        return mSipAddress;
    }

    public void setSipAddress(String sipAddress) {
        mSipAddress = sipAddress;
    }

    public String getSeverity() {
        return mSeverity;
    }

    public void setSeverity(String severity) {
        mSeverity = severity;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    protected ActionParametersModel(Parcel in) {
        mEmail = in.readString();
        mMessage = in.readString();
        mPhone = in.readString();
        mSipAddress = in.readString();
        mSeverity = in.readString();
        mLocation = in.readString();
        mUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mEmail);
        dest.writeString(mMessage);
        dest.writeString(mPhone);
        dest.writeString(mSipAddress);
        dest.writeString(mSeverity);
        dest.writeString(mLocation);
        dest.writeString(mUrl);
    }

    public ActionParametersModel() {
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ActionParametersModel> CREATOR = new Parcelable.Creator<ActionParametersModel>() {
        @Override
        public ActionParametersModel createFromParcel(Parcel in) {
            return new ActionParametersModel(in);
        }

        @Override
        public ActionParametersModel[] newArray(int size) {
            return new ActionParametersModel[size];
        }
    };
}