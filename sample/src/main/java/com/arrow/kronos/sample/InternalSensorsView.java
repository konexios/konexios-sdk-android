package com.arrow.kronos.sample;

import com.arrow.kronos.sample.device.IotParameter;

import java.util.List;
import java.util.Map;

/**
 * Created by osminin on 11/28/2016.
 */

public interface InternalSensorsView {
    void update(Map<String, String> telemetryData);
    void setDeviceId(String deviceId);
}
