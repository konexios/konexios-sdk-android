package com.arrow.kronos.api.listeners;


import com.arrow.kronos.api.models.ApiError;
import com.arrow.kronos.api.models.DeviceRegistrationResponse;

/**
 * Created by osminin on 9/22/2016.
 */

public interface RegisterDeviceListener {
    void onDeviceRegistered(DeviceRegistrationResponse response);
    void onDeviceRegistrationFailed(ApiError error);
}
