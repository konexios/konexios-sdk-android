/*
 * Copyright (c) 2017 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors: Arrow Electronics, Inc.
 */

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
