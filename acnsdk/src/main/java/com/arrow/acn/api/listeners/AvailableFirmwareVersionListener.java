package com.arrow.acn.api.listeners;

import com.arrow.acn.api.models.ApiError;
import com.arrow.acn.api.models.FirmwareVersionModel;

import java.util.List;

/**
 * Created by batrakov on 23.01.18.
 */

public interface AvailableFirmwareVersionListener {
    void onAvailableFirmwareVersionSuccess(List<FirmwareVersionModel> firmwareVersionModel);
    void onAvailableFirmwareVersionFailure(ApiError error);
}
