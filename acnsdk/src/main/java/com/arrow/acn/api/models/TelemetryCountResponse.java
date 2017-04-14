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
 * Created by osminin on 3/31/2017.
 */

public class TelemetryCountResponse {
    @SerializedName("deviceHid")
    private String deviceHid;
    @SerializedName("name")
    private String name;
    @SerializedName("value")
    private String value;

    public String getDeviceHid() {
        return deviceHid;
    }

    public void setDeviceHid(String deviceHid) {
        this.deviceHid = deviceHid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
