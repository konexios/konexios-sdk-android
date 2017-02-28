package com.arrow.kronos.api.listeners;

import com.arrow.kronos.api.models.ApiError;

/**
 * Created by osminin on 2/28/2017.
 */

public interface ConnectionListener {
    void onConnectionSuccess();
    void onConnectionError(ApiError error);
}
