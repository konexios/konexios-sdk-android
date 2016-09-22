package com.kronossdk.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by osminin on 4/15/2016.
 */
public final class GatewayResponse implements Parcelable {
    @SerializedName("hid")
    private String mHid;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("type")
    private String mType;
    @SerializedName("userHid")
    private String mUserHid;
    @SerializedName("gatewayHid")
    private String mGatewayHid;
    @SerializedName("info")
    private String mInfo;
    @SerializedName("properties")
    private String mProperties;

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getUserHid() {
        return mUserHid;
    }

    public void setUserHid(String userHid) {
        mUserHid = userHid;
    }

    public String getGatewayHid() {
        return mGatewayHid;
    }

    public void setGatewayHid(String gatewayHid) {
        mGatewayHid = gatewayHid;
    }

    public String getInfo() {
        return mInfo;
    }

    public void setInfo(String info) {
        mInfo = info;
    }

    public String getProperties() {
        return mProperties;
    }

    public void setProperties(String properties) {
        mProperties = properties;
    }

    public String getHid() {
        return mHid;
    }

    public void setHid(String hid) {
        mHid = hid;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    protected GatewayResponse(Parcel in) {
        mHid = in.readString();
        mMessage = in.readString();
        mType = in.readString();
        mUserHid = in.readString();
        mGatewayHid = in.readString();
        mInfo = in.readString();
        mProperties = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mHid);
        dest.writeString(mMessage);
        dest.writeString(mType);
        dest.writeString(mUserHid);
        dest.writeString(mGatewayHid);
        dest.writeString(mInfo);
        dest.writeString(mProperties);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<GatewayResponse> CREATOR = new Parcelable.Creator<GatewayResponse>() {
        @Override
        public GatewayResponse createFromParcel(Parcel in) {
            return new GatewayResponse(in);
        }

        @Override
        public GatewayResponse[] newArray(int size) {
            return new GatewayResponse[size];
        }
    };
}