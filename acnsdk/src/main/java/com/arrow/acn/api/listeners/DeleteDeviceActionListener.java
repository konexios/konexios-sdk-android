package com.arrow.acn.api.listeners;

import com.arrow.acn.api.models.ApiError;

/**
 * Created by osminin on 10.10.2016.
 */

public interface DeleteDeviceActionListener {
    void onDeviceActionDeleted();
    void onDeviceActionDeleteFailed(ApiError error);
}
