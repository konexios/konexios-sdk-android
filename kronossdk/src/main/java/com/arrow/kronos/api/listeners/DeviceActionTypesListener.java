package com.arrow.kronos.api.listeners;


import com.arrow.kronos.api.models.ActionTypeResponseModel;

/**
 * Created by osminin on 9/22/2016.
 */

public interface DeviceActionTypesListener {

    void onActionTypesReceived(ActionTypeResponseModel actionTypes);
    void onActionTypesFailed(String error);
}
