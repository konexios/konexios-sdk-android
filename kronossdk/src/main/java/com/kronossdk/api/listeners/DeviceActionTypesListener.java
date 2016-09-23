package com.kronossdk.api.listeners;

import com.kronossdk.api.models.ActionTypeModel;
import com.kronossdk.api.models.ActionTypeResponseModel;

import java.util.List;

import okhttp3.ResponseBody;

/**
 * Created by osminin on 9/22/2016.
 */

public interface DeviceActionTypesListener {

    void onActionTypesReceived(ActionTypeResponseModel actionTypes);
    void onActionTypesFailed(String error);
}
