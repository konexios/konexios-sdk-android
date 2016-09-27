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

    /**
     * sets endpoint server environment demo or dev
     * @param endpoint - enum {DEMO, DEV}
     */
    void setRestEndpoint(ServerEndpoint endpoint);

    /**
     *initialize service with a context and bind it with activity's lifecycle
     * @param context - Activity or Service, it should be valid while using kronos lib
     */
    void initialize(Context context);

    /**
     * register new gateway and initiate persistent connection (it makes sense only in case when
     * some of {ConnectionType.MQTT, ConnectionType.AWS,
     */
    void connect();

    /**
     * destroy persistent connection
     */
    void disconnect();

    /**
     *  sends single telemetry request
     * @param bundle - should contain String as json with telemetry using the key EXTRA_DATA_LABEL_TELEMETRY
     */
    void sendSingleTelemetry(Bundle bundle);

    /**
     * sends a scope of bundles with telemetry data
     * @param telemetry - list of bundles, each bundle should be like in sendSingleTelemetry
     */
    void sendBatchTelemetry(List<Bundle> telemetry);

    /**
     * check whether if current service supports sending batch telemetry
     * @return true if supports, false otherwise
     */
    boolean hasBatchMode();

    /**
     * set listener for commands from server (only for mqtt)
     * @param listener - listener interface
     */
    void setServerCommandsListener(ServerCommandsListener listener);

    /**
     * register new account or sign in
     * @param accountRequest user's account data
     * @param listener - listener to get the result
     */
    void registerAccount(AccountRequest accountRequest, RegisterAccountListener listener);

    /**
     * register new device
     * @param req - device data
     * @param listener - listener to get the result
     */
    void registerDevice(RegisterDeviceRequest req, RegisterDeviceListener listener);

    void getDeviceActionTypes(DeviceActionTypesListener listener);

    void getDeviceActions(String deviceHid, DeviceActionsListener listener);

    void postDeviceAction(String deviceHid, ActionModel action, PostDeviceActionListener listener);

    void updateDeviceAction(String deviceHid, int index, ActionModel model, UpdateDeviceActionListener listener);

    void getDeviceHistoricalEvents(String deviceHid, DeviceHistoricalEventsListener listener);

    void registerReceivedEvent(String eventHid);

    void eventHandlingSucceed(String eventHid);

    void eventHandlingFailed(String eventHid);
}
