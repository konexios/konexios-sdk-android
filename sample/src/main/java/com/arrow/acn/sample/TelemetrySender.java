package com.arrow.acn.sample;

import com.arrow.acn.api.listeners.RegisterDeviceListener;
import com.arrow.acn.api.models.DeviceRegistrationModel;
import com.arrow.acn.api.models.TelemetryModel;

/**
 * Created by osminin on 11/28/2016.
 */

public interface TelemetrySender {
    void sendTelemetry(TelemetryModel model);
    void registerDevice(DeviceRegistrationModel model, RegisterDeviceListener listener);
}
