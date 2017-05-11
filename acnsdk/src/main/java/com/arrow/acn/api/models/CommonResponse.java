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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by osminin on 10/11/2016.
 */

public class CommonResponse implements Parcelable {

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
    @SerializedName("hid")
    @Expose
    private String hid;
    @SerializedName("links")
    @Expose
    private JsonElement links;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("pri")
    @Expose
    private String pri;

    public CommonResponse() {
    }

    protected CommonResponse(@NonNull Parcel in) {
        hid = in.readString();
        JsonParser parser = new JsonParser();
        links = parser.parse(in.readString()).getAsJsonObject();
        message = in.readString();
        pri = in.readString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommonResponse response = (CommonResponse) o;

        if (hid != null ? !hid.equals(response.hid) : response.hid != null) return false;
        if (!getLinks().equals(response.getLinks())) return false;
        if (message != null ? !message.equals(response.message) : response.message != null)
            return false;
        return pri != null ? pri.equals(response.pri) : response.pri == null;

    }

    @Override
    public int hashCode() {
        int result = hid != null ? hid.hashCode() : 0;
        result = 31 * result + (links != null ? links.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (pri != null ? pri.hashCode() : 0);
        return result;
    }

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
    public JsonElement getLinks() {
        if (links == null) {
            links = new JsonObject();
        }
        return links;
    }

    /**
     * @param links The links
     */
    public void setLinks(JsonElement links) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(hid);
        String str = new Gson().toJson(getLinks());
        dest.writeString(str);
        dest.writeString(message);
        dest.writeString(pri);
    }
}