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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GatewayResponse that = (GatewayResponse) o;
        return Objects.equals(externalId, that.externalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), externalId);
    }
}