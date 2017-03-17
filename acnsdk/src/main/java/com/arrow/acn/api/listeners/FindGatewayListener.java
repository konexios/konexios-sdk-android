package com.arrow.acn.api.listeners;

import com.arrow.acn.api.models.ApiError;
import com.arrow.acn.api.models.GatewayModel;

/**
 * Created by osminin on 10/6/2016.
 */

public interface FindGatewayListener {
    void onGatewayFound(GatewayModel gatewayModel);
    void onGatewayFindError(ApiError error);
}
