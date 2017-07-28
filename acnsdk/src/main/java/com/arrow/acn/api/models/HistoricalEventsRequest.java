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

import java.util.ArrayList;
import java.util.List;

/**
 * Request for historical device events
 */

public class HistoricalEventsRequest implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<HistoricalEventsRequest> CREATOR = new Parcelable.Creator<HistoricalEventsRequest>() {
        @Override
        public HistoricalEventsRequest createFromParcel(Parcel in) {
            return new HistoricalEventsRequest(in);
        }

        @Override
        public HistoricalEventsRequest[] newArray(int size) {
            return new HistoricalEventsRequest[size];
        }
    };
    /**
     *  device hid
     */
    private String hid;
    /**
     *  start date (optional)
     */
    private String createdDateFrom;
    /**
     *  end date (optional)
     */
    private String createdDateTo;
    /**
     *  sorting field (optional)
     */
    private String sortField;
    /**
     *  sorting direction (optional)
     */
    private String sortDirection;
    /**
     *  list of statuses (optional)
     */
    private List<String> statuses;
    private List<String> systemNames;
    /**
     *  page number (required)
     */
    private int _page;
    /**
     *  size (required, max 200)
     */
    private int _size;

    protected HistoricalEventsRequest(Parcel in) {
        hid = in.readString();
        createdDateFrom = in.readString();
        createdDateTo = in.readString();
        sortField = in.readString();
        sortDirection = in.readString();
        if (in.readByte() == 0x01) {
            statuses = new ArrayList<String>();
            in.readList(statuses, String.class.getClassLoader());
        } else {
            statuses = null;
        }
        if (in.readByte() == 0x01) {
            systemNames = new ArrayList<String>();
            in.readList(systemNames, String.class.getClassLoader());
        } else {
            systemNames = null;
        }
        _page = in.readInt();
        _size = in.readInt();
    }

    public HistoricalEventsRequest() {
    }

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public String getCreatedDateFrom() {
        return createdDateFrom;
    }

    public void setCreatedDateFrom(String createdDateFrom) {
        this.createdDateFrom = createdDateFrom;
    }

    public String getCreatedDateTo() {
        return createdDateTo;
    }

    public void setCreatedDateTo(String createdDateTo) {
        this.createdDateTo = createdDateTo;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

    public List<String> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<String> statuses) {
        this.statuses = statuses;
    }

    public List<String> getSystemNames() {
        return systemNames;
    }

    public void setSystemNames(List<String> systemNames) {
        this.systemNames = systemNames;
    }

    public int getPage() {
        return _page;
    }

    public void setPage(int _page) {
        this._page = _page;
    }

    public int getSize() {
        return _size;
    }

    public void setSize(int _size) {
        this._size = _size;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hid);
        dest.writeString(createdDateFrom);
        dest.writeString(createdDateTo);
        dest.writeString(sortField);
        dest.writeString(sortDirection);
        if (statuses == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(statuses);
        }
        if (systemNames == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(systemNames);
        }
        dest.writeInt(_page);
        dest.writeInt(_size);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HistoricalEventsRequest request = (HistoricalEventsRequest) o;

        if (_page != request._page) return false;
        if (_size != request._size) return false;
        if (hid != null ? !hid.equals(request.hid) : request.hid != null) return false;
        if (createdDateFrom != null ? !createdDateFrom.equals(request.createdDateFrom) : request.createdDateFrom != null)
            return false;
        if (createdDateTo != null ? !createdDateTo.equals(request.createdDateTo) : request.createdDateTo != null)
            return false;
        if (sortField != null ? !sortField.equals(request.sortField) : request.sortField != null)
            return false;
        if (sortDirection != null ? !sortDirection.equals(request.sortDirection) : request.sortDirection != null)
            return false;
        if (statuses != null ? !statuses.equals(request.statuses) : request.statuses != null)
            return false;
        return systemNames != null ? systemNames.equals(request.systemNames) : request.systemNames == null;

    }

    @Override
    public int hashCode() {
        int result = hid != null ? hid.hashCode() : 0;
        result = 31 * result + (createdDateFrom != null ? createdDateFrom.hashCode() : 0);
        result = 31 * result + (createdDateTo != null ? createdDateTo.hashCode() : 0);
        result = 31 * result + (sortField != null ? sortField.hashCode() : 0);
        result = 31 * result + (sortDirection != null ? sortDirection.hashCode() : 0);
        result = 31 * result + (statuses != null ? statuses.hashCode() : 0);
        result = 31 * result + (systemNames != null ? systemNames.hashCode() : 0);
        result = 31 * result + _page;
        result = 31 * result + _size;
        return result;
    }
}
