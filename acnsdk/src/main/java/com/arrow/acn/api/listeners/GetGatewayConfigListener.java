package com.arrow.acn.api.listeners;

import com.arrow.acn.api.models.ApiError;
import com.arrow.acn.api.models.ConfigResponse;

/**
 * Created by osminin on 10/6/2016.
 */

public interface GetGatewayConfigListener {
    void onGatewayConfigReceived(ConfigResponse response);
    void onGatewayConfigFailed(ApiError error);
}
