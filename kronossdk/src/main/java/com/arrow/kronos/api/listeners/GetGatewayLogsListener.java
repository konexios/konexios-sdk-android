package com.arrow.kronos.api.listeners;

/**
 * Created by osminin on 10/6/2016.
 */

public interface GetGatewayLogsListener {
    void onGatewayLogsReceived();
    void onGatewayLogsFailed();
}
