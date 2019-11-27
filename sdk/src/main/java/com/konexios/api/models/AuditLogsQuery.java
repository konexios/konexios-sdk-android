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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public AuditLogsQuery() {
    }

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
        return page == that.page &&
                size == that.size &&
                Objects.equals(createdDateFrom, that.createdDateFrom) &&
                Objects.equals(createdDateTo, that.createdDateTo) &&
                Objects.equals(userHids, that.userHids) &&
                Objects.equals(types, that.types) &&
                Objects.equals(sortField, that.sortField) &&
                Objects.equals(sortDirection, that.sortDirection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdDateFrom, createdDateTo, userHids, types, sortField, sortDirection, page, size);
    }
}
