package com.arrow.acn.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by osminin on 4/14/2016.
 */
public final class AccountResponse implements Parcelable {
    @SerializedName("hid")
    private String mHid;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("name")
    private String mName;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("code")
    private String mCode;
    @SerializedName("applicationHid")
    private String mApplicationHid;

    public String getApplicationHid() {
        return mApplicationHid;
    }

    public void setApplicationHid(String applicationHid) {
        mApplicationHid = applicationHid;
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
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

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    protected AccountResponse(Parcel in) {
        mHid = in.readString();
        mMessage = in.readString();
        mName = in.readString();
        mEmail = in.readString();
        mCode = in.readString();
        mApplicationHid = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mHid);
        dest.writeString(mMessage);
        dest.writeString(mName);
        dest.writeString(mEmail);
        dest.writeString(mCode);
        dest.writeString(mApplicationHid);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AccountResponse> CREATOR = new Parcelable.Creator<AccountResponse>() {
        @Override
        public AccountResponse createFromParcel(Parcel in) {
            return new AccountResponse(in);
        }

        @Override
        public AccountResponse[] newArray(int size) {
            return new AccountResponse[size];
        }
    };
}