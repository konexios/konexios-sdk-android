package com.arrow.kronos.api.listeners;

import com.arrow.kronos.api.models.ApiError;
import com.arrow.kronos.api.models.DeviceModel;

/**
 * Created by osminin on 10/11/2016.
 */

public interface FindDeviceListener {
    void onDeviceFindSuccess(DeviceModel device);
    void onDeviceFindFailed(ApiError error);
}
