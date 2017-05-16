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
 * Created by osminin on 4/15/2016.
 */
public final class GatewayResponse extends CommonResponse implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<GatewayResponse> CREATOR = new Parcelable.Creator<GatewayResponse>() {
        @NonNull
        @Override
        public GatewayResponse createFromParcel(@NonNull Parcel in) {
            return new GatewayResponse(in);
        }

        @NonNull
        @Override
        public GatewayResponse[] newArray(int size) {
            return new GatewayResponse[size];
        }
    };
    @SerializedName("externalId")
    private String externalId;

    protected GatewayResponse(@NonNull Parcel in) {
        super(in);
        externalId = in.readString();
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(externalId);
    }
}