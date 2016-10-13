package com.arrow.kronos.api.listeners;

import com.arrow.kronos.api.models.ConfigResponse;

/**
 * Created by osminin on 10/6/2016.
 */

public interface GetGatewayConfigListener {
    void onGatewayConfigReceived(ConfigResponse response);
    void onGatewayConfigFailed();
}
