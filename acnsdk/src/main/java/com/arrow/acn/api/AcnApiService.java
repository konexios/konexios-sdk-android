/*
 * Copyright (c) 2017 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors: Arrow Electronics, Inc.
 */

package com.arrow.acn.api;

import android.support.annotation.Keep;

import com.arrow.acn.api.listeners.AvailableFirmwareListener;
import com.arrow.acn.api.listeners.AvailableFirmwareVersionListener;
import com.arrow.acn.api.listeners.CheckinGatewayListener;
import com.arrow.acn.api.listeners.CommonRequestListener;
import com.arrow.acn.api.listeners.ConnectionListener;
import com.arrow.acn.api.listeners.DeleteDeviceActionListener;
import com.arrow.acn.api.listeners.FindDeviceListener;
import com.arrow.acn.api.listeners.FindDeviceStateListener;
import com.arrow.acn.api.listeners.FindGatewayListener;
import com.arrow.acn.api.listeners.GatewayCommandsListener;
import com.arrow.acn.api.listeners.GatewayRegisterListener;
import com.arrow.acn.api.listeners.GatewayUpdateListener;
import com.arrow.acn.api.listeners.GetGatewayConfigListener;
import com.arrow.acn.api.listeners.GetGatewaysListener;
import com.arrow.acn.api.listeners.ListNodeTypesListener;
import com.arrow.acn.api.listeners.ListResultListener;
import com.arrow.acn.api.listeners.MessageStatusListener;
import com.arrow.acn.api.listeners.PagingResultListener;
import com.arrow.acn.api.listeners.PostDeviceActionListener;
import com.arrow.acn.api.listeners.RegisterAccountListener;
import com.arrow.acn.api.listeners.RegisterDeviceListener;
import com.arrow.acn.api.listeners.RequestedFirmwareListener;
import com.arrow.acn.api.listeners.ServerCommandsListener;
import com.arrow.acn.api.listeners.TelemetryCountListener;
import com.arrow.acn.api.listeners.TelemetryRequestListener;
import com.arrow.acn.api.listeners.UpdateDeviceActionListener;
import com.arrow.acn.api.models.AccountRequest;
import com.arrow.acn.api.models.AuditLogModel;
import com.arrow.acn.api.models.AuditLogsQuery;
import com.arrow.acn.api.models.CreateAndStartSoftwareReleaseScheduleRequest;
import com.arrow.acn.api.models.DeviceActionModel;
import com.arrow.acn.api.models.DeviceActionTypeModel;
import com.arrow.acn.api.models.DeviceEventModel;
import com.arrow.acn.api.models.DeviceModel;
import com.arrow.acn.api.models.DeviceRegistrationModel;
import com.arrow.acn.api.models.DeviceTypeModel;
import com.arrow.acn.api.models.DeviceTypeRegistrationModel;
import com.arrow.acn.api.models.ErrorBodyModel;
import com.arrow.acn.api.models.FindDevicesRequest;
import com.arrow.acn.api.models.FindTelemetryRequest;
import com.arrow.acn.api.models.GatewayCommand;
import com.arrow.acn.api.models.GatewayModel;
import com.arrow.acn.api.models.HistoricalEventsRequest;
import com.arrow.acn.api.models.NewDeviceStateTransactionRequest;
import com.arrow.acn.api.models.NodeModel;
import com.arrow.acn.api.models.NodeRegistrationModel;
import com.arrow.acn.api.models.NodeTypeRegistrationModel;
import com.arrow.acn.api.models.TelemetryCountRequest;
import com.arrow.acn.api.models.TelemetryItemModel;
import com.arrow.acn.api.models.TelemetryModel;

import java.util.List;

/**
 * Created by osminin on 6/17/2016.
 */

@Keep
public interface AcnApiService {

    /**
     * make persistent connection to the cloud, should be called only after gateway registration
     * (connection require gateway hid to be performed)
     */

    void connect(ConnectionListener listener);

    /**
     * destroy persistent connection
     */
    void disconnect();

    /**
     * check if persistent connection is
     *
     * @return
     */
    boolean isConnected();

    /**
     * sends single telemetry request
     *
     * @param telemetry - telemetry model object
     */
    void sendSingleTelemetry(TelemetryModel telemetry, TelemetryRequestListener listener);

    /**
     * sends a scope of bundles with telemetry data
     *
     * @param telemetry - list of telemetries
     */
    void sendBatchTelemetry(List<TelemetryModel> telemetry, TelemetryRequestListener listener);

    /**
     * Finds and returns the list of telemetry by application hid
     * @param request FindTelemetryRequest with application hid, fromTimestamp and toTimestamp should
     *                have "yyyy-MM-dd'T'HH:mm:ss'Z'" format
     * @param listener listener interface implementation, should be not null
     */
    void findTelemetryByApplicationHid(FindTelemetryRequest request,
                                       PagingResultListener<TelemetryItemModel> listener);

    /**
     * Finds and returns the list of telemetry by device hid
     * @param request FindTelemetryRequest with device hid, fromTimestamp and toTimestamp should
     *                have "yyyy-MM-dd'T'HH:mm:ss'Z'" format
     * @param listener listener interface implementation, should be not null
     */
    void findTelemetryByDeviceHid(FindTelemetryRequest request,
                                  PagingResultListener<TelemetryItemModel> listener);

    /**
     * Finds and returns the list of telemetry by node hid
     * @param request FindTelemetryRequest with node hid, fromTimestamp and toTimestamp should
     *                have "yyyy-MM-dd'T'HH:mm:ss'Z'" format
     * @param listener listener interface implementation, should be not null
     */
    void findTelemetryByNodeHid(FindTelemetryRequest request,
                                PagingResultListener<TelemetryItemModel> listener);

    /**
     * returns the list of last sent telemetry
     * @param deviceHid device Hid
     * @param listener listener interface implementation, should be not null
     */
    void getLastTelemetry(String deviceHid, ListResultListener<TelemetryItemModel> listener);

    /**
     *  returns count of sent telemetries for the period from 'fromTimestamp' to 'toTimestamp'
     * @param request should contain device hid, fromTimestamp and toTimestamp should have
     *                "yyyy-MM-dd'T'HH:mm:ss'Z'" format, telemetryName like 'light' or some other. To
     *                get all sent telemetry use '*' as telemetryName
     * @param listener listener interface implementation, should be not null
     */
    void getTelemetryItemsCount(TelemetryCountRequest request, TelemetryCountListener listener);

    /**
     * check whether if current service supports sending batch telemetry
     *
     * @return true if supports, false otherwise
     */
    boolean hasBatchMode();

    /**
     * set listener for commands from server (only for mqtt)
     *
     * @param listener - listener interface
     */
    void setServerCommandsListener(ServerCommandsListener listener);

    /**
     * register new account or sign in
     *
     * @param accountRequest user's account data
     * @param listener       - listener to get the result
     */
    void registerAccount(AccountRequest accountRequest, RegisterAccountListener listener);

    //Action api

    /**
     *  Returns a list of available action types
     * @param listener listener interface implementation, should be not null
     */
    void getDeviceActionTypes(ListResultListener<DeviceActionTypeModel> listener);

    /**
     * Returns the list existing device actions for a specific device type
     * @param deviceHid hid of device
     * @param listener listener interface implementation, should be not null
     */
    void getDeviceActions(String deviceHid, ListResultListener<DeviceActionModel> listener);

    /**
     * Creates a new device action for a specific device type
     * @param deviceHid hid of device
     * @param action action model
     * @param listener listener interface implementation, should be not null
     */
    void postDeviceAction(String deviceHid, DeviceActionModel action, PostDeviceActionListener listener);

    /**
     * Updates an existing device action for a specific device type
     * @param deviceHid hid of device
     * @param index index of action to be updated
     * @param model action model
     * @param listener listener interface implementation, should be not null
     */
    void updateDeviceAction(String deviceHid, int index, DeviceActionModel model, UpdateDeviceActionListener listener);

    /**
     * Delete a device action from a specific device type
     * @param deviceHid hid of device
     * @param index index of action to be deleted
     * @param listener listener interface implementation, should be not null
     */
    void deleteDeviceAction(String deviceHid, int index, DeleteDeviceActionListener listener);

    //Device api

    /**
     * register new device
     * @param req      - device data
     * @param listener - listener to get the result
     */
    void registerDevice(DeviceRegistrationModel req, RegisterDeviceListener listener);

    /**
     * Find all devices
     * @param request - request model to find
     * @param listener listener interface implementation, should be not null
     */
    void findAllDevices(FindDevicesRequest request, PagingResultListener<DeviceModel> listener);

    /**
     * List historical device events
     * @param request model to find, device Hid is required
     * @param listener listener interface implementation, should be not null
     */
    void getDeviceHistoricalEvents(HistoricalEventsRequest request, PagingResultListener<DeviceEventModel> listener);

    /**
     * Find device by device's Hid
     * @param deviceHid - hid of device
     * @param listener listener interface implementation, should be not null
     */
    void findDeviceByHid(String deviceHid, FindDeviceListener listener);

    /**
     * Update existing device
     * @param deviceHid - hid of device to be updated
     * @param device - device model with updated parameters
     * @param listener listener interface implementation, should be not null
     */
    void updateDevice(String deviceHid, DeviceRegistrationModel device, CommonRequestListener listener);

    /**
     * List device audit logs
     * @param deviceHid - hid of device
     * @param query - model to find appropriate logs
     * @param listener - listener interface implementation, should be not null
     */
    void getDeviceAuditLogs(String deviceHid, AuditLogsQuery query, PagingResultListener<AuditLogModel> listener);

    /**
     *
     * @param deviceHid
     * @param error
     * @param listener - listener interface implementation, should be not null
     */
    void sendDeviceError(String deviceHid, ErrorBodyModel error, CommonRequestListener listener);

    /**
     * Get available firmware for device by hid.
     * @param hid - device hid
     * @param listener - listener interface implementation, should be not null
     */
    void getAvailableFirmwareForDeviceByHid(String hid, AvailableFirmwareVersionListener listener);

    //Core-event api

    /**
     * Notify cloud that event has been received
     * @param eventHid - hid of event
     * @param listener - listener interface implementation, should be not null
     */
    void registerReceivedEvent(String eventHid, CommonRequestListener listener);

    /**
     * Notify cloud that event has been handled properly
     * @param eventHid - hid of event
     * @param listener - listener interface implementation, should be not null
     */
    void eventHandlingSucceed(String eventHid, CommonRequestListener listener);

    /***
     * Notify cloud that event hasn't been handled properly
     * @param eventHid - hid of event
     * @param listener - listener interface implementation, should be not null
     */
    void eventHandlingFailed(String eventHid, CommonRequestListener listener);

    //Gateways api

    /**
     *  Find all gateways
     * @param listener - listener interface implementation, should be not null
     */
    void findAllGateways(GetGatewaysListener listener);

    /**
     * Register new gateway
     * @param gatewayModel - new gateway model
     * @param listener - listener interface implementation, should be not null
     */
    void registerGateway(GatewayModel gatewayModel, GatewayRegisterListener listener);

    /**
     * Find gateway by hid
     * @param hid - hid of gateway
     * @param listener - listener interface implementation, should be not null
     */
    void findGateway(String hid, FindGatewayListener listener);

    /**
     * Update existing gateway
     * @param hid - hid of gateway
     * @param gatewayModel updated gateway's model
     * @param listener - listener interface implementation, should be not null
     */
    void updateGateway(String hid, GatewayModel gatewayModel, GatewayUpdateListener listener);

    /**
     * Checkin gateway to notify cloud
     * @param hid - gateway hid
     * @param gatewayUid - gateway uid
     * @param listener - listener interface implementation, should be not null
     */
    void checkinGateway(String hid, String gatewayUid, CheckinGatewayListener listener);

    /**
     *
     * @param hid
     * @param error
     * @param listener - listener interface implementation, should be not null
     */
    void sendGatewayError(String hid, ErrorBodyModel error, CommonRequestListener listener);

    /**
     * Send command and payload to gateway and device
     * @param hid - gateway hid
     * @param command - command model
     * @param listener - listener interface implementation, should be not null
     */
    @Deprecated
    void sendCommandGateway(String hid, GatewayCommand command, GatewayCommandsListener listener);

    /**
     * List gateway devices
     * @param gatewayHid - gateway hid
     * @param listener - listener interface implementation, should be not null
     */
    void getDevicesList(String gatewayHid, ListResultListener<DeviceModel> listener);

    /**
     * Download gateway configuration
     * @param hid - gateway hid
     * @param listener - listener interface implementation, should be not null
     */
    void getGatewayConfig(String hid, GetGatewayConfigListener listener);

    /**
     * Send gateway heartbeat
     * @param hid - gateway hid
     * @param listener - listener interface implementation, should be not null
     */
    void gatewayHeartbeat(String hid, CommonRequestListener listener);

    /**
     * List gateway audit logs
     * @param hid - hid of gateway
     * @param query - model to find exact logs
     * @param listener - listener interface implementation, should be not null
     */
    void getGatewayLogs(String hid, AuditLogsQuery query, PagingResultListener<AuditLogModel> listener);

    /**
     * Get available firmware for gateway by hid.
     * @param hid gateway hid
     * @param listener listener - listener interface implementation, should be not null
     */
    void getAvailableFirmwareForGatewayByHid(String hid, AvailableFirmwareVersionListener listener);

    //Node api

    /**
     * List existing nodes
     * @param listener - listener interface implementation, should be not null
     */
    void getNodesList(ListResultListener<NodeModel> listener);

    /**
     * Create new node
     * @param node - node model
     * @param listener - listener interface implementation, should be not null
     */
    void createNewNode(NodeRegistrationModel node, CommonRequestListener listener);

    /**
     * Update existing node
     * @param nodeHid - node hid
     * @param node - updated node model
     * @param listener - listener interface implementation, should be not null
     */
    void updateExistingNode(String nodeHid, NodeRegistrationModel node, CommonRequestListener listener);

    //Node-type api

    /**
     * List existing node types
     * @param listener - listener interface implementation, should be not null
     */
    void getListNodeTypes(ListNodeTypesListener listener);

    /**
     * Create new node type
     * @param nodeType - new node type
     * @param listener - listener interface implementation, should be not null
     */
    void createNewNodeType(NodeTypeRegistrationModel nodeType, CommonRequestListener listener);

    /**
     * Update existing node type
     * @param hid - node type hid
     * @param nodeType - updated node type model
     * @param listener - listener interface implementation, should be not null
     */
    void updateExistingNodeType(String hid, NodeTypeRegistrationModel nodeType, CommonRequestListener listener);

    //device-type api

    /**
     * List existing device types
     * @param listener - listener interface implementation, should be not null
     */
    void getListDeviceTypes(ListResultListener<DeviceTypeModel> listener);

    /**
     * Create new device type
     * @param deviceType - new device type
     * @param listener - listener interface implementation, should be not null
     */
    void createNewDeviceType(DeviceTypeRegistrationModel deviceType, CommonRequestListener listener);

    /**
     * Update existing device type
     * @param hid - device type hid
     * @param deviceType - device type model
     * @param listener - listener interface implementation, should be not null
     */
    void updateExistingDeviceType(String hid,
                                  DeviceTypeRegistrationModel deviceType, CommonRequestListener listener);

    //device-state-api

    /**
     * Find device state
     * @param deviceHid - device hid
     * @param listener - listener interface implementation, should be not null
     */
    void findDeviceState(String deviceHid, FindDeviceStateListener listener);

    /**
     * Create new device state request transaction
     * @param hid device state hid
     * @param request - state request model
     * @param listener - listener interface implementation, should be not null
     */
    void createNewDeviceStateTransaction(String hid,
                                         NewDeviceStateTransactionRequest request,
                                         CommonRequestListener listener);

    /**
     * Mark device state transaction as succeeded
     * @param hid - device state hid
     * @param transHid - transaction hid
     * @param listener - listener interface implementation, should be not null
     */
    void deviceStateTransactionSucceeded(String hid, String transHid, MessageStatusListener listener);

    /**
     * Mark device state transaction as failed
     * @param hid - device state hid
     * @param transHid - transaction hid
     * @param error - error model
     * @param listener - listener interface implementation, should be not null
     */
    void deviceStateTransactionFailed(String hid, String transHid, ErrorBodyModel error,
                                      MessageStatusListener listener);

    /**
     * Mark device state transaction as received
     * @param hid - device state hid
     * @param transHid - transaction hid
     * @param listener - listener interface implementation, should be not null
     */
    void deviceStateTransactionReceived(String hid, String transHid, MessageStatusListener listener);

    /**
     * Create new device state update transaction
     * @param hid - device state hid
     * @param request - updated device state
     * @param listener - listener interface implementation, should be not null
     */
    void updateDeviceStateTransaction(String hid,
                                      NewDeviceStateTransactionRequest request,
                                      CommonRequestListener listener);

    // RTU FIRMWARE API

    /**
     * Get list of requested firmware.
     * @param status - status should be "Requested", "Approved", "Declined", "Revoked" or empty
     * @param page - num of page
     * @param size
     * @param listener - listener interface implementation, should be not null
     */
    void getListRequestedFirmware(String status, int page, int size, RequestedFirmwareListener listener);

    /**
     *  Get list available firmware.
     * @param deviceTypeHid - device type hid
     * @param listener - listener interface implementation, should be not null
     */
    void getListAvailableFirmware(String deviceTypeHid, AvailableFirmwareListener listener);

    /**
     *  Check rights to use firmware.
     * @param softwareReleaseHid
     * @param listener - listener interface implementation, should be not null
     */
    void requireRightToUseFirmware(String softwareReleaseHid, MessageStatusListener listener);

    // Software Release Schedule ApI

    /**
     *
     * @param request
     * @param listener
     */
    void createAndStartNewSoftwareReleaseSchedule(CreateAndStartSoftwareReleaseScheduleRequest request, CommonRequestListener listener);
}
