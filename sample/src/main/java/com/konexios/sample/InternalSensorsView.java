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

import java.util.Map;

/**
 * Created by osminin on 11/28/2016.
 */

public interface InternalSensorsView {
    void update(Map<String, String> telemetryData);
    void setDeviceId(String deviceId);
}
