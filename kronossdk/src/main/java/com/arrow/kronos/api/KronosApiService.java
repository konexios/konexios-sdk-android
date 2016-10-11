package com.arrow.kronos.api;

import android.content.Context;
import android.os.Bundle;

import com.arrow.kronos.api.listeners.CheckinGatewayListener;
import com.arrow.kronos.api.listeners.CommonRequestListener;
import com.arrow.kronos.api.listeners.DeleteDeviceActionListener;
import com.arrow.kronos.api.listeners.DeviceActionTypesListener;
import com.arrow.kronos.api.listeners.DeviceActionsListener;
import com.arrow.kronos.api.listeners.DeviceHistoricalEventsListener;
import com.arrow.kronos.api.listeners.FindDeviceListener;
import com.arrow.kronos.api.listeners.FindDevicesListener;
import com.arrow.kronos.api.listeners.FindGatewayListener;
import com.arrow.kronos.api.listeners.GatewayCommandsListener;
import com.arrow.kronos.api.listeners.GatewayHeartbeatListener;
import com.arrow.kronos.api.listeners.GatewayRegisterListener;
import com.arrow.kronos.api.listeners.GatewayUpdateListener;
import com.arrow.kronos.api.listeners.GetGatewayConfigListener;
import com.arrow.kronos.api.listeners.GetAuditLogsListener;
import com.arrow.kronos.api.listeners.GetGatewaysListener;
import com.arrow.kronos.api.listeners.GetNodesListListener;
import com.arrow.kronos.api.listeners.PostDeviceActionListener;
import com.arrow.kronos.api.listeners.RegisterAccountListener;
import com.arrow.kronos.api.listeners.RegisterDeviceListener;
import com.arrow.kronos.api.listeners.ServerCommandsListener;
import com.arrow.kronos.api.listeners.UpdateDeviceActionListener;
import com.arrow.kronos.api.models.AccountRequest;
import com.arrow.kronos.api.models.ActionModel;
import com.arrow.kronos.api.models.GatewayCommand;
import com.arrow.kronos.api.models.AuditLogsQuery;
import com.arrow.kronos.api.models.GatewayModel;
import com.arrow.kronos.api.models.DeviceRegistrationModel;
import com.arrow.kronos.api.models.NodeRegistrationModel;

import java.util.List;

/**
 * Created by osminin on 6/17/2016.
 */

public interface KronosApiService {

    /**
     * sets endpoint server environment url
     * @param endpoint - String url like "http://pegasuskronos01-dev.cloudapp.net:28880"
     * @param apiKey
     * @param apiSecret
     */
    void setRestEndpoint(String endpoint, String apiKey, String apiSecret);

    /**
     *  sets mqtt server host and userName prefix
     * @param host String contains host like "tcp://pegasusqueue01-dev.cloudapp.net:46953"
     * @param prefix String contains prefix like "/themis.dev"
     */
    void setMqttEndpoint(String host, String prefix);

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

    //Action api

    void getDeviceActionTypes(DeviceActionTypesListener listener);

    void getDeviceActions(String deviceHid, DeviceActionsListener listener);

    void postDeviceAction(String deviceHid, ActionModel action, PostDeviceActionListener listener);

    void updateDeviceAction(String deviceHid, int index, ActionModel model, UpdateDeviceActionListener listener);

    void deleteDeviceAction(String deviceHid, int index, DeleteDeviceActionListener listener);

    //Device api

    /**
     * register new device
     * @param req - device data
     * @param listener - listener to get the result
     */
    void registerDevice(DeviceRegistrationModel req, RegisterDeviceListener listener);

    void findAllDevices(String userHid, String uid, String type, String gatewayHid, String enabled,
                        int page, int size, FindDevicesListener listener);

    void getDeviceHistoricalEvents(String deviceHid, DeviceHistoricalEventsListener listener);

    void findDeviceByHid(String deviceHid, FindDeviceListener listener);

    void updateDevice(String deviceHid, DeviceRegistrationModel device, CommonRequestListener listener);

    void getDeviceAuditLogs(String deviceHid, AuditLogsQuery query, GetAuditLogsListener listener);

    //Core-event api

    void registerReceivedEvent(String eventHid);

    void eventHandlingSucceed(String eventHid);

    void eventHandlingFailed(String eventHid);

    //Gateways api

    void findAllGateways(GetGatewaysListener listener);

    void registerGateway(GatewayModel gatewayModel, GatewayRegisterListener listener);

    void findGateway(String hid, FindGatewayListener listener);

    void updateGateway(String hid, GatewayModel gatewayModel, GatewayUpdateListener listener);

    void checkinGateway(String hid, CheckinGatewayListener listener);

    void sendCommandGateway(String hid, GatewayCommand command, GatewayCommandsListener listener);

    void getGatewayConfig(String hid, GetGatewayConfigListener listener);

    void gatewayHeartbeat(String hid, GatewayHeartbeatListener listener);

    void getGatewayLogs(String hid, AuditLogsQuery query, GetAuditLogsListener listener);

    //Node api

    void getNodesList(GetNodesListListener listener);

    void createNewNode(NodeRegistrationModel node, CommonRequestListener listener);

    void updateExistingNode(String nodeHid, NodeRegistrationModel node, CommonRequestListener listener);
}
