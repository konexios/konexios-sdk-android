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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by osminin on 10/11/2016.
 */

public class CommonResponse implements Parcelable {

    @SerializedName("hid")
    @Expose
    private String hid;
    @SerializedName("links")
    @Expose
    private Links links;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("pri")
    @Expose
    private String pri;

    /**
     * @return The hid
     */
    public String getHid() {
        return hid;
    }

    /**
     * @param hid The hid
     */
    public void setHid(String hid) {
        this.hid = hid;
    }

    /**
     * @return The links
     */
    public Links getLinks() {
        return links;
    }

    /**
     * @param links The links
     */
    public void setLinks(Links links) {
        this.links = links;
    }

    /**
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return The pri
     */
    public String getPri() {
        return pri;
    }

    /**
     * @param pri The pri
     */
    public void setPri(String pri) {
        this.pri = pri;
    }

    public CommonResponse() {
    }

    protected CommonResponse(@NonNull Parcel in) {
        hid = in.readString();
        links = (Links) in.readValue(Links.class.getClassLoader());
        message = in.readString();
        pri = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(hid);
        dest.writeValue(links);
        dest.writeString(message);
        dest.writeString(pri);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CommonResponse> CREATOR = new Parcelable.Creator<CommonResponse>() {
        @NonNull
        @Override
        public CommonResponse createFromParcel(@NonNull Parcel in) {
            return new CommonResponse(in);
        }

        @NonNull
        @Override
        public CommonResponse[] newArray(int size) {
            return new CommonResponse[size];
        }
    };
}