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

import com.google.gson.annotations.SerializedName;

/**
 * Created by osminin on 12/23/2016.
 */

public final class HistoricalTelemetryModel implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<HistoricalTelemetryModel> CREATOR = new Parcelable.Creator<HistoricalTelemetryModel>() {
        @Override
        public HistoricalTelemetryModel createFromParcel(Parcel in) {
            return new HistoricalTelemetryModel(in);
        }

        @Override
        public HistoricalTelemetryModel[] newArray(int size) {
            return new HistoricalTelemetryModel[size];
        }
    };
    @SerializedName("id")
    private String id;
    @SerializedName("hid")
    private String hid;
    @SerializedName("name")
    private String name;
    @SerializedName("timestamp")
    private String timestamp;
    @SerializedName("type")
    private String type;
    @SerializedName("value")
    private String value;

    public HistoricalTelemetryModel() {
    }

    protected HistoricalTelemetryModel(Parcel in) {
        id = in.readString();
        hid = in.readString();
        name = in.readString();
        timestamp = in.readString();
        type = in.readString();
        value = in.readString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HistoricalTelemetryModel that = (HistoricalTelemetryModel) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (hid != null ? !hid.equals(that.hid) : that.hid != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null)
            return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        return value != null ? value.equals(that.value) : that.value == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (hid != null ? hid.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(hid);
        dest.writeString(name);
        dest.writeString(timestamp);
        dest.writeString(type);
        dest.writeString(value);
    }
}
