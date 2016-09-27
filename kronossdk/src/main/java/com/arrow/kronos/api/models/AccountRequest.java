package com.arrow.kronos.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by osminin on 4/14/2016.
 */
public final class AccountRequest implements Parcelable {
    @SerializedName("name")
    private String mName;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("password")
    private String mPassword;
    @SerializedName("code")
    private String mCode;

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
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

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    protected AccountRequest(Parcel in) {
        mName = in.readString();
        mEmail = in.readString();
        mPassword = in.readString();
        mCode = in.readString();
    }

    public AccountRequest() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mEmail);
        dest.writeString(mPassword);
        dest.writeString(mCode);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AccountRequest> CREATOR = new Parcelable.Creator<AccountRequest>() {
        @Override
        public AccountRequest createFromParcel(Parcel in) {
            return new AccountRequest(in);
        }

        @Override
        public AccountRequest[] newArray(int size) {
            return new AccountRequest[size];
        }
    };
}