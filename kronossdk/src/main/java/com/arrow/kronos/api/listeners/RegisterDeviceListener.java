package com.arrow.kronos.api.listeners;


import com.arrow.kronos.api.models.CommonResponse;

/**
 * Created by osminin on 9/22/2016.
 */

public interface RegisterDeviceListener {
    void onDeviceRegistered(CommonResponse response);
    void onDeviceRegistrationFailed();
}
