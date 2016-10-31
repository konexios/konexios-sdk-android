package com.arrow.kronos.api;

import com.arrow.kronos.api.listeners.ServerCommandsListener;
import com.arrow.kronos.api.models.ConfigResponse;
import com.arrow.kronos.api.models.TelemetryModel;
import com.google.gson.Gson;

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
