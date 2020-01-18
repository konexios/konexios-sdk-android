/*
 * Copyright (c) 2017-2019 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *     Arrow Electronics, Inc.
 *     Konexios, Inc.
 */

package com.konexios.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * Created by osminin on 3/16/2016.
 */
public final class CreateUpdateResponse implements Parcelable {
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
    @SerializedName("id")
    private String id;
    @SerializedName("message")
    private String message;

    protected CreateUpdateResponse(@NonNull Parcel in) {
        id = in.readString();
        message = in.readString();
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateUpdateResponse that = (CreateUpdateResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, message);
    }
}