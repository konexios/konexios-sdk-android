package com.kronossdk.api.listeners;

import com.kronossdk.api.models.HistoricalEventResponse;

/**
 * Created by osminin on 9/22/2016.
 */

public interface DeviceHistoricalEventsListener {
    void onHistoricalEventsReceived(HistoricalEventResponse response);
    void onHistoricalEventsFailed(String error);
}
