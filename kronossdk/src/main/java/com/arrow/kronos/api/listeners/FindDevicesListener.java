package com.arrow.kronos.api.listeners;

import com.arrow.kronos.api.models.FindAllDevicesResponse;

/**
 * Created by osminin on 10.10.2016.
 */

public interface FindDevicesListener {
    void onDevicesFond(FindAllDevicesResponse response);
    void onDevicesFindFailed();
}
