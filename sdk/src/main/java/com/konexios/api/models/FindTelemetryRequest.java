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

import java.util.Objects;

/**
 * Created by osminin on 10/19/2016.
 */

public final class FindTelemetryRequest implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FindTelemetryRequest> CREATOR = new Parcelable.Creator<FindTelemetryRequest>() {
        @Override
        public FindTelemetryRequest createFromParcel(Parcel in) {
            return new FindTelemetryRequest(in);
        }

        @Override
        public FindTelemetryRequest[] newArray(int size) {
            return new FindTelemetryRequest[size];
        }
    };
    private String hid;
    private String fromTimestamp;
    private String toTimestamp;
    private String telemetryNames;
    private int page;
    private int size;

    public FindTelemetryRequest() {
    }

    protected FindTelemetryRequest(Parcel in) {
        hid = in.readString();
        fromTimestamp = in.readString();
        toTimestamp = in.readString();
        telemetryNames = in.readString();
        page = in.readInt();
        size = in.readInt();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FindTelemetryRequest that = (FindTelemetryRequest) o;
        return page == that.page &&
                size == that.size &&
                Objects.equals(hid, that.hid) &&
                Objects.equals(fromTimestamp, that.fromTimestamp) &&
                Objects.equals(toTimestamp, that.toTimestamp) &&
                Objects.equals(telemetryNames, that.telemetryNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hid, fromTimestamp, toTimestamp, telemetryNames, page, size);
    }

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public String getFromTimestamp() {
        return fromTimestamp;
    }

    public void setFromTimestamp(String fromTimestamp) {
        this.fromTimestamp = fromTimestamp;
    }

    public String getToTimestamp() {
        return toTimestamp;
    }

    public void setToTimestamp(String toTimestamp) {
        this.toTimestamp = toTimestamp;
    }

    public String getTelemetryNames() {
        return telemetryNames;
    }

    public void setTelemetryNames(String telemetryNames) {
        this.telemetryNames = telemetryNames;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hid);
        dest.writeString(fromTimestamp);
        dest.writeString(toTimestamp);
        dest.writeString(telemetryNames);
        dest.writeInt(page);
        dest.writeInt(size);
    }
}
