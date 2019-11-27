/*
 * Copyright (c) 2017-2019 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *     Arrow Electronics, Inc.
 *     Konexios, Inc.
 */

package com.konexios.sample;

import com.konexios.api.listeners.RegisterDeviceListener;
import com.konexios.api.models.DeviceRegistrationModel;
import com.konexios.api.models.TelemetryModel;

/**
 * Created by osminin on 11/28/2016.
 */

public interface TelemetrySender {
    void sendTelemetry(TelemetryModel model);
    void registerDevice(DeviceRegistrationModel model, RegisterDeviceListener listener);
}
