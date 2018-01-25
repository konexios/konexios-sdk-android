package com.arrow.acn.api.listeners;

import com.arrow.acn.api.models.ApiError;
import com.arrow.acn.api.models.AvailableFirmwareResponse;

/**
 * Created by batrakov on 23.01.18.
 */

public interface AvailableFirmwareListener {
    void onAvailableFirmwareSuccess(AvailableFirmwareResponse availableFirmwareResponse);
    void onAvailableFirmwareFailure(ApiError error);
}
