package com.arrow.acn.sample;

import java.util.Map;

/**
 * Created by osminin on 11/28/2016.
 */

public interface InternalSensorsView {
    void update(Map<String, String> telemetryData);
    void setDeviceId(String deviceId);
}
