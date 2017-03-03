package com.arrow.acn.api;

import com.arrow.acn.api.listeners.ConnectionListener;
import com.arrow.acn.api.models.TelemetryModel;

import java.util.List;

/**
 * Created by osminin on 10/26/2016.
 */

public interface TelemetrySenderInterface {
    void connect(ConnectionListener listener);

    void disconnect();

    void sendSingleTelemetry(TelemetryModel telemetry);

    void sendBatchTelemetry(List<TelemetryModel> telemetry);

    boolean hasBatchMode();

    boolean isConnected();
}
