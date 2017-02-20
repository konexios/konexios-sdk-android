package com.arrow.acn.api.models;

/**
 * Created by osminin on 10/19/2016.
 */

public final class FindTelemetryRequest {
    private String hid;
    private String fromTimestamp;
    private String toTimestamp;
    private String telemetryNames;
    private int page;
    private int size;

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
}
