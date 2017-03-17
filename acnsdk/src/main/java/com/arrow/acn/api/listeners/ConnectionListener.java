package com.arrow.acn.api.listeners;

import com.arrow.acn.api.models.ApiError;

/**
 * Created by osminin on 3/3/2017.
 */

public interface ConnectionListener {
    void onConnectionSuccess();
    void onConnectionError(ApiError error);
}
