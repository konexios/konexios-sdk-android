package com.arrow.kronos.api.listeners;


import com.arrow.kronos.api.models.GatewayResponse;

/**
 * Created by osminin on 9/22/2016.
 */

public interface RegisterDeviceListener {
    void onDeviceRegistered(GatewayResponse response);
    void onDeviceRegistrationFailed();
}
