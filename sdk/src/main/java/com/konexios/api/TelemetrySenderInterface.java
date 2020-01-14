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

package com.konexios.api;

import androidx.annotation.Keep;

import com.konexios.api.listeners.ConnectionListener;
import com.konexios.api.listeners.TelemetryRequestListener;
import com.konexios.api.models.TelemetryModel;

import java.util.List;

@Keep
public interface TelemetrySenderInterface {
    void connect(ConnectionListener listener);

    void disconnect();

    void sendSingleTelemetry(TelemetryModel telemetry);

    void sendBatchTelemetry(List<TelemetryModel> telemetry);

    boolean hasBatchMode();

    boolean isConnected();

    void addTelemetryRequestListener(TelemetryRequestListener listener);
}
