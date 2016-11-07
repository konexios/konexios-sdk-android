package com.arrow.kronos.api.listeners;

import com.arrow.kronos.api.models.ApiError;

/**
 * Created by osminin on 10/6/2016.
 */

public interface CheckinGatewayListener {
    void onCheckinGatewaySuccess();
    void onCheckinGatewayError(ApiError error);
}
