package com.arrow.kronos.api.listeners;


import com.arrow.kronos.api.models.HistoricalEventResponse;

/**
 * Created by osminin on 9/22/2016.
 */

public interface DeviceHistoricalEventsListener {
    void onHistoricalEventsReceived(HistoricalEventResponse response);
    void onHistoricalEventsFailed(String error);
}
