package com.arrow.kronos.api;

import com.arrow.kronos.api.listeners.ServerCommandsListener;
import com.arrow.kronos.api.models.ConfigResponse;
import com.arrow.kronos.api.models.TelemetryModel;

import java.util.List;

/**
 * Created by osminin on 10/26/2016.
 */

public interface TelemetrySenderInterface {
    void connect();

    void disconnect();

    void sendSingleTelemetry(TelemetryModel telemetry);

    void sendBatchTelemetry(List<TelemetryModel> telemetry);

    boolean hasBatchMode();
}
