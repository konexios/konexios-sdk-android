package com.kronossdk.api.listeners;

import com.kronossdk.api.models.ActionTypeModel;

import java.util.List;

/**
 * Created by osminin on 9/22/2016.
 */

public interface DeviceActionTypesListener {

    void onActionTypesReceived(List<ActionTypeModel> actionTypes);
    void onActionTypesFailed(String error);
}
