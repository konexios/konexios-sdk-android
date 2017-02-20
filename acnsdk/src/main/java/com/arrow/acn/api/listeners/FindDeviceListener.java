package com.arrow.acn.api.listeners;

import com.arrow.acn.api.models.ApiError;
import com.arrow.acn.api.models.DeviceModel;

/**
 * Created by osminin on 10/11/2016.
 */

public interface FindDeviceListener {
    void onDeviceFindSuccess(DeviceModel device);
    void onDeviceFindFailed(ApiError error);
}
