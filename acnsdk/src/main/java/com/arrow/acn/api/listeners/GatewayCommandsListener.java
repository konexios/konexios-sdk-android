package com.arrow.acn.api.listeners;

import com.arrow.acn.api.models.ApiError;
import com.arrow.acn.api.models.GatewayResponse;

/**
 * Created by osminin on 10/7/2016.
 */

public interface GatewayCommandsListener {
    void onGatewayCommandSent(GatewayResponse response);
    void onGatewayCommandFailed(ApiError error);
}
