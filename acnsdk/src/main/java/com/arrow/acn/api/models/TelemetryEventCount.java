package com.arrow.acn.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by osminin on 12/23/2016.
 */

public final class TelemetryEventCount {
    @SerializedName("timestamp")
    private Long timestamp;
    @SerializedName("telemetryCount")
    private Integer telemetryCount;
    @SerializedName("deviceEventCount")
    private Integer deviceEventCount;

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getTelemetryCount() {
        return telemetryCount;
    }

    public void setTelemetryCount(Integer telemetryCount) {
        this.telemetryCount = telemetryCount;
    }

    public Integer getDeviceEventCount() {
        return deviceEventCount;
    }

    public void setDeviceEventCount(Integer deviceEventCount) {
        this.deviceEventCount = deviceEventCount;
    }
}
