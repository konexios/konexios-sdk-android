package com.arrow.kronos.api.models;

/**
 * Created by osminin on 10/6/2016.
 */

public final class AuditLogsQuery {
    private String createdDateFrom;
    private String createdDateTo;
    private String[] userHids;
    private String[] types;
    private String sortField;
    private String sortDirection;
    private int page;
    private int size;

    public AuditLogsQuery(String createdDateFrom, String createdDateTo, String[] userHids, String[] types, String sortField, String sortDirection, int page, int size) {
        this.createdDateFrom = createdDateFrom;
        this.createdDateTo = createdDateTo;
        this.userHids = userHids;
        this.types = types;
        this.sortField = sortField;
        this.sortDirection = sortDirection;
        this.page = page;
        this.size = size;
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

    public String[] getUserHids() {
        return userHids;
    }

    public void setUserHids(String[] userHids) {
        this.userHids = userHids;
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
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
}
