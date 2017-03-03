package com.arrow.acn.api.listeners;


import com.arrow.acn.api.models.ApiError;
import com.arrow.acn.api.models.DeviceRegistrationResponse;

/**
 * Created by osminin on 9/22/2016.
 */

public interface RegisterDeviceListener {
    void onDeviceRegistered(DeviceRegistrationResponse response);
    void onDeviceRegistrationFailed(ApiError error);
}
