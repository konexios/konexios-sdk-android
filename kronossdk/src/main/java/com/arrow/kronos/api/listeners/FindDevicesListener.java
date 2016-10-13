package com.arrow.kronos.api.listeners;

import com.arrow.kronos.api.models.DeviceModel;

import java.util.List;

/**
 * Created by osminin on 10.10.2016.
 */

public interface FindDevicesListener {
    void onDevicesFond(List<DeviceModel> response);
    void onDevicesFindFailed();
}
