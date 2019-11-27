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
import androidx.annotation.NonNull;

import com.konexios.api.models.TelemetryModel;

import java.util.List;

/**
 * Common class for different types of telemetry senders like aws, azure, ibm and mqtt
 */

@Keep
public abstract class AbstractTelemetrySenderService implements TelemetrySenderInterface {

    @NonNull
    protected String formatBatchPayload(@NonNull List<TelemetryModel> telemetry) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (TelemetryModel model : telemetry) {
            String json = model.getTelemetry();
            builder.append(json).append(",");
        }
        builder.replace(builder.length() - 1, builder.length(), "").append("]");
        return builder.toString();
    }
}
