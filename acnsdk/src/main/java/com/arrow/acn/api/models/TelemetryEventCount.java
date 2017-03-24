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
