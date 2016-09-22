package com.kronossdk.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by osminin on 3/16/2016.
 */
public final class CreateUpdateResponse implements Parcelable {
    @SerializedName("id")
    private String id;
    @SerializedName("message")
    private String message;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return String.format("[%s | %s]", id, message);
    }

    protected CreateUpdateResponse(Parcel in) {
        id = in.readString();
        message = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(message);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CreateUpdateResponse> CREATOR = new Parcelable.Creator<CreateUpdateResponse>() {
        @Override
        public CreateUpdateResponse createFromParcel(Parcel in) {
            return new CreateUpdateResponse(in);
        }

        @Override
        public CreateUpdateResponse[] newArray(int size) {
            return new CreateUpdateResponse[size];
        }
    };
}