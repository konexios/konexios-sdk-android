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

import java.util.List;

/**
 * Created by osminin on 12/23/2016.
 */

public final class TelemetryStatsModel {
    @SerializedName("telemetryItemCount")
    private Integer telemetryItemCount;
    @SerializedName("deviceEventCount")
    private Integer deviceEventCount;
    @SerializedName("telemetryEventCounts")
    private List<TelemetryEventCount> telemetryEventCounts;

    public Integer getTelemetryItemCount() {
        return telemetryItemCount;
    }

    public void setTelemetryItemCount(Integer telemetryItemCount) {
        this.telemetryItemCount = telemetryItemCount;
    }

    public Integer getDeviceEventCount() {
        return deviceEventCount;
    }

    public void setDeviceEventCount(Integer deviceEventCount) {
        this.deviceEventCount = deviceEventCount;
    }

    public List<TelemetryEventCount> getTelemetryEventCounts() {
        return telemetryEventCounts;
    }

    public void setTelemetryEventCounts(List<TelemetryEventCount> telemetryEventCounts) {
        this.telemetryEventCounts = telemetryEventCounts;
    }
}
