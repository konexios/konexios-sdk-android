package com.arrow.acn.api.listeners;

import com.arrow.acn.api.models.ApiError;

/**
 * Created by osminin on 10/6/2016.
 */

public interface CheckinGatewayListener {
    void onCheckinGatewaySuccess();
    void onCheckinGatewayError(ApiError error);
}
