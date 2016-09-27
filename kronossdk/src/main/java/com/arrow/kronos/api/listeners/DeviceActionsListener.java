package com.arrow.kronos.api.listeners;


import com.arrow.kronos.api.models.ActionResponseModel;

/**
 * Created by osminin on 9/22/2016.
 */

public interface DeviceActionsListener {
    void onDeviceActionsReceived(ActionResponseModel responseModel);
    void onDeviceActionsFailed(String error);
}
