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

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by batrakov on 23.01.18.
 */

public class RequestedFirmwareResponse implements Parcelable {

    @SerializedName("size")
    private int size;

    @SerializedName("data")
    private ArrayList<RequestedFirmware> data;

    @SerializedName("page")
    private int page;

    @SerializedName("totalSize")
    private int totalSize;

    @SerializedName("totalPages")
    private int totalPages;

    protected RequestedFirmwareResponse(Parcel in) {
        size = in.readInt();
        data = in.createTypedArrayList(RequestedFirmware.CREATOR);
        page = in.readInt();
        totalSize = in.readInt();
        totalPages = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(size);
        dest.writeTypedList(data);
        dest.writeInt(page);
        dest.writeInt(totalSize);
        dest.writeInt(totalPages);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @SuppressWarnings("unused")
    public static final Creator<RequestedFirmwareResponse> CREATOR = new Creator<RequestedFirmwareResponse>() {
        @Override
        public RequestedFirmwareResponse createFromParcel(Parcel in) {
            return new RequestedFirmwareResponse(in);
        }

        @Override
        public RequestedFirmwareResponse[] newArray(int size) {
            return new RequestedFirmwareResponse[size];
        }
    };

    private static class RequestedFirmware implements Parcelable {
        @SerializedName("softwareReleaseHid")
        private String softwareReleaseHid;

        @SerializedName("softwareReleaseName")
        private String softwareReleaseName;

        protected RequestedFirmware(Parcel in) {
            softwareReleaseHid = in.readString();
            softwareReleaseName = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(softwareReleaseHid);
            dest.writeString(softwareReleaseName);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @SuppressWarnings("unused")
        public static final Creator<RequestedFirmware> CREATOR = new Creator<RequestedFirmware>() {
            @Override
            public RequestedFirmware createFromParcel(Parcel in) {
                return new RequestedFirmware(in);
            }

            @Override
            public RequestedFirmware[] newArray(int size) {
                return new RequestedFirmware[size];
            }
        };

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RequestedFirmware that = (RequestedFirmware) o;
            return Objects.equals(softwareReleaseHid, that.softwareReleaseHid) &&
                    Objects.equals(softwareReleaseName, that.softwareReleaseName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(softwareReleaseHid, softwareReleaseName);
        }
    }

    @Override
    public boolean equals(Object aO) {
        if (this == aO) return true;
        if (aO == null || getClass() != aO.getClass()) return false;

        RequestedFirmwareResponse that = (RequestedFirmwareResponse) aO;

        if (size != that.size) return false;
        if (page != that.page) return false;
        if (totalSize != that.totalSize) return false;
        if (totalPages != that.totalPages) return false;
        return data != null ? data.equals(that.data) : that.data == null;
    }

    @Override
    public int hashCode() {
        int result = size;
        result = 31 * result + (data != null ? data.hashCode() : 0);
        result = 31 * result + page;
        result = 31 * result + totalSize;
        result = 31 * result + totalPages;
        return result;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int aSize) {
        size = aSize;
    }

    public ArrayList<RequestedFirmware> getData() {
        return data;
    }

    public void setData(ArrayList<RequestedFirmware> aData) {
        data = aData;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int aPage) {
        page = aPage;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int aTotalSize) {
        totalSize = aTotalSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int aTotalPages) {
        totalPages = aTotalPages;
    }
}
