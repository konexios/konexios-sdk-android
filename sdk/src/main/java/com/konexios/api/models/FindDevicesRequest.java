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
 * Created by osminin on 5/16/2017.
 */

public final class FindDevicesRequest implements Parcelable {
    private String userHid;
    private String uid;
    private String type;
    private String gatewayHid;
    private String createdBefore;
    private String createdAfter;
    private String updatedBefore;
    private String updatedAfter;
    private String enabled;
    private int _page;
    private int _size;

    public FindDevicesRequest() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FindDevicesRequest that = (FindDevicesRequest) o;
        return _page == that._page &&
                _size == that._size &&
                Objects.equals(userHid, that.userHid) &&
                Objects.equals(uid, that.uid) &&
                Objects.equals(type, that.type) &&
                Objects.equals(gatewayHid, that.gatewayHid) &&
                Objects.equals(createdBefore, that.createdBefore) &&
                Objects.equals(createdAfter, that.createdAfter) &&
                Objects.equals(updatedBefore, that.updatedBefore) &&
                Objects.equals(updatedAfter, that.updatedAfter) &&
                Objects.equals(enabled, that.enabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userHid, uid, type, gatewayHid, createdBefore, createdAfter, updatedBefore, updatedAfter, enabled, _page, _size);
    }

    public String getUserHid() {

        return userHid;
    }

    public void setUserHid(String userHid) {
        this.userHid = userHid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGatewayHid() {
        return gatewayHid;
    }

    public void setGatewayHid(String gatewayHid) {
        this.gatewayHid = gatewayHid;
    }

    public String getCreatedBefore() {
        return createdBefore;
    }

    public void setCreatedBefore(String createdBefore) {
        this.createdBefore = createdBefore;
    }

    public String getCreatedAfter() {
        return createdAfter;
    }

    public void setCreatedAfter(String createdAfter) {
        this.createdAfter = createdAfter;
    }

    public String getUpdatedBefore() {
        return updatedBefore;
    }

    public void setUpdatedBefore(String updatedBefore) {
        this.updatedBefore = updatedBefore;
    }

    public String getUpdatedAfter() {
        return updatedAfter;
    }

    public void setUpdatedAfter(String updatedAfter) {
        this.updatedAfter = updatedAfter;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public int get_page() {
        return _page;
    }

    public void set_page(int _page) {
        this._page = _page;
    }

    public int get_size() {
        return _size;
    }

    public void set_size(int _size) {
        this._size = _size;
    }

    protected FindDevicesRequest(Parcel in) {
        userHid = in.readString();
        uid = in.readString();
        type = in.readString();
        gatewayHid = in.readString();
        createdBefore = in.readString();
        createdAfter = in.readString();
        updatedBefore = in.readString();
        updatedAfter = in.readString();
        enabled = in.readString();
        _page = in.readInt();
        _size = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userHid);
        dest.writeString(uid);
        dest.writeString(type);
        dest.writeString(gatewayHid);
        dest.writeString(createdBefore);
        dest.writeString(createdAfter);
        dest.writeString(updatedBefore);
        dest.writeString(updatedAfter);
        dest.writeString(enabled);
        dest.writeInt(_page);
        dest.writeInt(_size);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FindDevicesRequest> CREATOR = new Parcelable.Creator<FindDevicesRequest>() {
        @Override
        public FindDevicesRequest createFromParcel(Parcel in) {
            return new FindDevicesRequest(in);
        }

        @Override
        public FindDevicesRequest[] newArray(int size) {
            return new FindDevicesRequest[size];
        }
    };
}
