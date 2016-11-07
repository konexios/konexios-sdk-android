package com.arrow.kronos.api.listeners;

import com.arrow.kronos.api.models.ApiError;

/**
 * Created by osminin on 9/22/2016.
 */

public interface PostDeviceActionListener {
    void postActionSucceed();
    void postActionFailed(ApiError error);
}
