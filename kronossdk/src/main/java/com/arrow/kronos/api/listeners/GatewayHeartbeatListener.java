package com.arrow.kronos.api.listeners;

/**
 * Created by osminin on 10/6/2016.
 */

public interface GatewayHeartbeatListener {
    void onGatewayHeartbeatSuccess();
    void onGatewayHeartbeatFailed();
}


