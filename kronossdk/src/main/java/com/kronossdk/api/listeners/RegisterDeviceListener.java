package com.kronossdk.api.listeners;

import com.kronossdk.api.models.GatewayResponse;

/**
 * Created by osminin on 9/22/2016.
 */

public interface RegisterDeviceListener {
    void onDeviceRegistered(GatewayResponse response);
    void onDeviceRegistrationFailed();
}
