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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ErrorBodyModel implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ErrorBodyModel> CREATOR = new Parcelable.Creator<ErrorBodyModel>() {
        @Override
        public ErrorBodyModel createFromParcel(Parcel in) {
            return new ErrorBodyModel(in);
        }

        @Override
        public ErrorBodyModel[] newArray(int size) {
            return new ErrorBodyModel[size];
        }
    };
    @SerializedName("error")
    @Expose
    private String error;

    public ErrorBodyModel() {

    }

    protected ErrorBodyModel(Parcel in) {
        error = in.readString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ErrorBodyModel that = (ErrorBodyModel) o;

        return error != null ? error.equals(that.error) : that.error == null;

    }

    @Override
    public int hashCode() {
        return error != null ? error.hashCode() : 0;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(error);
    }
}
