package com.arrow.kronos.api.listeners;


import com.arrow.kronos.api.models.DeviceActionTypeModel;

import java.util.List;

/**
 * Created by osminin on 9/22/2016.
 */

public interface DeviceActionTypesListener {

    void onActionTypesReceived(List<DeviceActionTypeModel> actionTypes);
    void onActionTypesFailed(String error);
}
