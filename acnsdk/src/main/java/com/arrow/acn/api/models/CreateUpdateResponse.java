/*
 * Copyright (c) 2017 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors: Arrow Electronics, Inc.
 */

package com.arrow.acn.api.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

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

    protected CreateUpdateResponse(@NonNull Parcel in) {
        id = in.readString();
        message = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(message);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CreateUpdateResponse> CREATOR = new Parcelable.Creator<CreateUpdateResponse>() {
        @NonNull
        @Override
        public CreateUpdateResponse createFromParcel(@NonNull Parcel in) {
            return new CreateUpdateResponse(in);
        }

        @NonNull
        @Override
        public CreateUpdateResponse[] newArray(int size) {
            return new CreateUpdateResponse[size];
        }
    };
}