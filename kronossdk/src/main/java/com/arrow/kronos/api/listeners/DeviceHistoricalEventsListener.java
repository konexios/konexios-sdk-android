package com.arrow.kronos.api.listeners;


import com.arrow.kronos.api.models.DeviceEventModel;

import java.util.List;

/**
 * Created by osminin on 9/22/2016.
 */

public interface DeviceHistoricalEventsListener {
    void onHistoricalEventsReceived(List<DeviceEventModel> response);

    void onHistoricalEventsFailed(String error);
}
