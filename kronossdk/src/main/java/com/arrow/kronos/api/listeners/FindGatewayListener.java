package com.arrow.kronos.api.listeners;

import com.arrow.kronos.api.models.GatewayModel;

/**
 * Created by osminin on 10/6/2016.
 */

public interface FindGatewayListener {
    void onGatewayFound(GatewayModel gatewayModel);
    void onGatewayFindError();
}
