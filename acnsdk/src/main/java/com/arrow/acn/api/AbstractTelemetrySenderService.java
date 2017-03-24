/*
 * Copyright (c) 2017 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors: Arrow Electronics, Inc.
 */

package com.arrow.acn.api;

import com.arrow.acn.api.models.TelemetryModel;

import java.util.List;

/**
 * Created by osminin on 10/26/2016.
 */

public abstract class AbstractTelemetrySenderService implements TelemetrySenderInterface {

    protected String formatBatchPayload(List<TelemetryModel> telemetry) {
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
