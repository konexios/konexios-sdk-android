package com.arrow.kronos.api;

import android.content.Context;
import android.os.Bundle;


import com.arrow.kronos.api.listeners.DeviceActionTypesListener;
import com.arrow.kronos.api.listeners.DeviceActionsListener;
import com.arrow.kronos.api.listeners.DeviceHistoricalEventsListener;
import com.arrow.kronos.api.listeners.PostDeviceActionListener;
import com.arrow.kronos.api.listeners.RegisterAccountListener;
import com.arrow.kronos.api.listeners.RegisterDeviceListener;
import com.arrow.kronos.api.listeners.ServerCommandsListener;
import com.arrow.kronos.api.listeners.UpdateDeviceActionListener;
import com.arrow.kronos.api.models.AccountRequest;
import com.arrow.kronos.api.models.ActionModel;
import com.arrow.kronos.api.models.RegisterDeviceRequest;

import java.util.List;

/**
 * Created by osminin on 6/17/2016.
 */

public interface KronosApiService {

    void setRestEndpoint(ServerEndpoint endpoint);

    void initialize(Context context);

    void connect();

    void disconnect();

    void sendSingleTelemetry(Bundle bundle);

    void sendBatchTelemetry(List<Bundle> telemetry);

    boolean hasBatchMode();

    void setServerCommandsListener(ServerCommandsListener listener);

    void registerAccount(AccountRequest accountRequest, RegisterAccountListener listener);

    void registerDevice(RegisterDeviceRequest req, RegisterDeviceListener listener);

    void getDeviceActionTypes(DeviceActionTypesListener listener);

    void getDeviceActions(String deviceHid, DeviceActionsListener listener);

    void postDeviceAction(String deviceHid, ActionModel action, PostDeviceActionListener listener);

    void updateDeviceAction(String deviceHid, int index, ActionModel model, UpdateDeviceActionListener listener);

    void getDeviceHistoricalEvents(String deviceHid, DeviceHistoricalEventsListener listener);
}
