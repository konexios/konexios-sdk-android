package com.arrow.kronos.api.listeners;


import com.arrow.kronos.api.models.DeviceActionModel;

import java.util.List;

/**
 * Created by osminin on 9/22/2016.
 */

public interface DeviceActionsListener {
    void onDeviceActionsReceived(List<DeviceActionModel> response);
    void onDeviceActionsFailed(String error);
}
