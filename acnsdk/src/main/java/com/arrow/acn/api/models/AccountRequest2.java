package com.arrow.acn.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountRequest2 implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AccountRequest2> CREATOR = new Parcelable.Creator<AccountRequest2>() {
        @Override
        public AccountRequest2 createFromParcel(Parcel in) {
            return new AccountRequest2(in);
        }

        @Override
        public AccountRequest2[] newArray(int size) {
            return new AccountRequest2[size];
        }
    };
    @SerializedName("applicationCode")
    @Expose
    private String applicationCode;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("username")
    @Expose
    private String username;

    public AccountRequest2() {
    }

    protected AccountRequest2(Parcel in) {
        applicationCode = in.readString();
        password = in.readString();
        username = in.readString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountRequest2 that = (AccountRequest2) o;

        if (getApplicationCode() != null ? !getApplicationCode().equals(that.getApplicationCode()) : that.getApplicationCode() != null)
            return false;
        if (getPassword() != null ? !getPassword().equals(that.getPassword()) : that.getPassword() != null)
            return false;
        return getUsername() != null ? getUsername().equals(that.getUsername()) : that.getUsername() == null;
    }

    @Override
    public int hashCode() {
        int result = getApplicationCode() != null ? getApplicationCode().hashCode() : 0;
        result = 31 * result + (getPassword() != null ? getPassword().hashCode() : 0);
        result = 31 * result + (getUsername() != null ? getUsername().hashCode() : 0);
        return result;
    }

    public String getApplicationCode() {
        return applicationCode;
    }

    public void setApplicationCode(String applicationCode) {
        this.applicationCode = applicationCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(applicationCode);
        dest.writeString(password);
        dest.writeString(username);
    }
}