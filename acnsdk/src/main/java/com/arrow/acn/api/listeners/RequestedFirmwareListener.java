package com.arrow.acn.api.listeners;

import com.arrow.acn.api.models.ApiError;
import com.arrow.acn.api.models.RequestedFirmwareResponse;

/**
 * Created by batrakov on 23.01.18.
 */

public interface RequestedFirmwareListener {
    void onListRequestedFirmwareSuccess(RequestedFirmwareResponse requestedFirmwareResponse);
    void onListRequestedFirmwareFailure(ApiError error);
}
