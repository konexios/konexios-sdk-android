package com.arrow.kronos.api.listeners;

import com.arrow.kronos.api.models.ApiError;
import com.arrow.kronos.api.models.GatewayResponse;

/**
 * Created by osminin on 10/7/2016.
 */

public interface GatewayCommandsListener {
    void onGatewayCommandSent(GatewayResponse response);
    void onGatewayCommandFailed(ApiError error);
}
