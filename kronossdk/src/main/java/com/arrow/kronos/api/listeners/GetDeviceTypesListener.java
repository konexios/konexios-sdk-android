package com.arrow.kronos.api.listeners;

import com.arrow.kronos.api.models.DeviceTypeModel;

import java.util.List;

/**
 * Created by osminin on 12.10.2016.
 */

public interface GetDeviceTypesListener {
    void onDeviceTypeListReceived(List<DeviceTypeModel> list);
    void onDeviceTypeListError();
}
