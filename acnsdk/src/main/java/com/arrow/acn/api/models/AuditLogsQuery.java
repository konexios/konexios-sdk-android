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
 * Created by osminin on 10/6/2016.
 */

public final class AuditLogsQuery implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AuditLogsQuery> CREATOR = new Parcelable.Creator<AuditLogsQuery>() {
        @Override
        public AuditLogsQuery createFromParcel(Parcel in) {
            return new AuditLogsQuery(in);
        }

        @Override
        public AuditLogsQuery[] newArray(int size) {
            return new AuditLogsQuery[size];
        }
    };
    private String createdDateFrom;
    private String createdDateTo;
    private List<String> userHids;
    private List<String> types;
    private String sortField;
    private String sortDirection;
    private int page;
    private int size;

    public AuditLogsQuery(String createdDateFrom, String createdDateTo, List<String> userHids, List<String> types, String sortField, String sortDirection, int page, int size) {
        this.createdDateFrom = createdDateFrom;
        this.createdDateTo = createdDateTo;
        this.userHids = userHids;
        this.types = types;
        this.sortField = sortField;
        this.sortDirection = sortDirection;
        this.page = page;
        this.size = size;
    }

    protected AuditLogsQuery(Parcel in) {
        createdDateFrom = in.readString();
        createdDateTo = in.readString();
        if (in.readByte() == 0x01) {
            userHids = new ArrayList<String>();
            in.readList(userHids, String.class.getClassLoader());
        } else {
            userHids = null;
        }
        if (in.readByte() == 0x01) {
            types = new ArrayList<String>();
            in.readList(types, String.class.getClassLoader());
        } else {
            types = null;
        }
        sortField = in.readString();
        sortDirection = in.readString();
        page = in.readInt();
        size = in.readInt();
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

    public List<String> getUserHids() {
        return userHids;
    }

    public void setUserHids(List<String> userHids) {
        this.userHids = userHids;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
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
        dest.writeString(createdDateFrom);
        dest.writeString(createdDateTo);
        if (userHids == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(userHids);
        }
        if (types == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(types);
        }
        dest.writeString(sortField);
        dest.writeString(sortDirection);
        dest.writeInt(page);
        dest.writeInt(size);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuditLogsQuery that = (AuditLogsQuery) o;

        if (page != that.page) return false;
        if (size != that.size) return false;
        if (createdDateFrom != null ? !createdDateFrom.equals(that.createdDateFrom) : that.createdDateFrom != null)
            return false;
        if (createdDateTo != null ? !createdDateTo.equals(that.createdDateTo) : that.createdDateTo != null)
            return false;
        if (userHids != null ? !userHids.equals(that.userHids) : that.userHids != null)
            return false;
        if (types != null ? !types.equals(that.types) : that.types != null) return false;
        if (sortField != null ? !sortField.equals(that.sortField) : that.sortField != null)
            return false;
        return sortDirection != null ? sortDirection.equals(that.sortDirection) : that.sortDirection == null;

    }

    @Override
    public int hashCode() {
        int result = createdDateFrom != null ? createdDateFrom.hashCode() : 0;
        result = 31 * result + (createdDateTo != null ? createdDateTo.hashCode() : 0);
        result = 31 * result + (userHids != null ? userHids.hashCode() : 0);
        result = 31 * result + (types != null ? types.hashCode() : 0);
        result = 31 * result + (sortField != null ? sortField.hashCode() : 0);
        result = 31 * result + (sortDirection != null ? sortDirection.hashCode() : 0);
        result = 31 * result + page;
        result = 31 * result + size;
        return result;
    }
}
