package com.arrow.acn.api.listeners;

import com.arrow.acn.api.models.ApiError;

/**
 * Created by osminin on 9/22/2016.
 */

public interface PostDeviceActionListener {
    void postActionSucceed();
    void postActionFailed(ApiError error);
}
