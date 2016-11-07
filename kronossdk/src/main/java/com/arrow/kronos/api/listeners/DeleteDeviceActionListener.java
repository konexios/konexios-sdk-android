package com.arrow.kronos.api.listeners;

import com.arrow.kronos.api.models.ApiError;

/**
 * Created by osminin on 10.10.2016.
 */

public interface DeleteDeviceActionListener {
    void onDeviceActionDeleted();
    void onDeviceActionDeleteFailed(ApiError error);
}
