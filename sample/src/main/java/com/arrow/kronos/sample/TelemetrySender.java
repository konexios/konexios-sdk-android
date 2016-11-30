package com.arrow.kronos.sample;

import com.arrow.kronos.api.listeners.RegisterDeviceListener;
import com.arrow.kronos.api.models.DeviceRegistrationModel;
import com.arrow.kronos.api.models.TelemetryModel;

/**
 * Created by osminin on 11/28/2016.
 */

public interface TelemetrySender {
    void sendTelemetry(TelemetryModel model);
    void registerDevice(DeviceRegistrationModel model, RegisterDeviceListener listener);
}
