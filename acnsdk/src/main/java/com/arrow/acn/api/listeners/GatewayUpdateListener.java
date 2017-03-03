package com.arrow.acn.api.listeners;

import com.arrow.acn.api.models.ApiError;
import com.arrow.acn.api.models.GatewayResponse;

/**
 * Created by osminin on 10/6/2016.
 */

public interface GatewayUpdateListener {
    void onGatewayUpdated(GatewayResponse response);
    void onGatewayUpdateFailed(ApiError error);
}
