package com.kronossdk.api.listeners;

import com.kronossdk.api.models.ActionResponseModel;

/**
 * Created by osminin on 9/22/2016.
 */

public interface DeviceActionsListener {
    void onDeviceActionsReceived(ActionResponseModel responseModel);
    void onDeviceActionsFailed(String error);
}
