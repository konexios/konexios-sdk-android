package com.arrow.acn.api.listeners;

import com.arrow.acn.api.models.ApiError;

/**
 * Created by osminin on 25.05.2017.
 */

public interface TelemetryRequestListener {
    void onTelemetrySendSuccess();

    void onTelemetrySendError(ApiError error);
}
