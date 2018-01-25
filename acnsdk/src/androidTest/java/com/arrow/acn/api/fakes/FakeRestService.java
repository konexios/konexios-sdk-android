/*
 * Copyright (c) 2017 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors: Arrow Electronics, Inc.
 */

package com.arrow.acn.api.fakes;

import android.support.annotation.NonNull;

import com.arrow.acn.api.models.AccountRequest;
import com.arrow.acn.api.models.AccountResponse;
import com.arrow.acn.api.models.AuditLogModel;
import com.arrow.acn.api.models.AvailableFirmwareResponse;
import com.arrow.acn.api.models.CommonResponse;
import com.arrow.acn.api.models.ConfigResponse;
import com.arrow.acn.api.models.CreateAndStartSoftwareReleaseScheduleRequest;
import com.arrow.acn.api.models.DeviceActionModel;
import com.arrow.acn.api.models.DeviceActionTypeModel;
import com.arrow.acn.api.models.DeviceEventModel;
import com.arrow.acn.api.models.DeviceModel;
import com.arrow.acn.api.models.DeviceRegistrationModel;
import com.arrow.acn.api.models.DeviceRegistrationResponse;
import com.arrow.acn.api.models.DeviceTypeModel;
import com.arrow.acn.api.models.DeviceTypeRegistrationModel;
import com.arrow.acn.api.models.ErrorBodyModel;
import com.arrow.acn.api.models.FindDeviceStateResponse;
import com.arrow.acn.api.models.FirmwareVersionModel;
import com.arrow.acn.api.models.GatewayCommand;
import com.arrow.acn.api.models.GatewayModel;
import com.arrow.acn.api.models.GatewayResponse;
import com.arrow.acn.api.models.ListResultModel;
import com.arrow.acn.api.models.MessageStatusResponse;
import com.arrow.acn.api.models.NewDeviceStateTransactionRequest;
import com.arrow.acn.api.models.NodeModel;
import com.arrow.acn.api.models.NodeRegistrationModel;
import com.arrow.acn.api.models.NodeTypeModel;
import com.arrow.acn.api.models.NodeTypeRegistrationModel;
import com.arrow.acn.api.models.PagingResultModel;
import com.arrow.acn.api.models.RequestedFirmwareResponse;
import com.arrow.acn.api.models.TelemetryCountResponse;
import com.arrow.acn.api.models.TelemetryItemModel;
import com.arrow.acn.api.rest.IotConnectAPIService;

import org.junit.Assert;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by osminin on 4/4/2017.
 */

public final class FakeRestService implements IotConnectAPIService {

    private Object mResponseBody;

    public <T> void setResponse(T responseBody) {
        mResponseBody = responseBody;
    }

    @NonNull
    @Override
    public Call<AccountResponse> registerAccount(@Body AccountRequest accountRequest) {
        return null;
    }

    @NonNull
    @Override
    public Call<GatewayResponse> registerGateway(@Body GatewayModel gatewayModel) {
        return null;
    }

    @NonNull
    @Override
    public Call<CommonResponse> checkin(@Path("hid") String hid) {
        Assert.assertNotNull(hid);
        return new FakeSuccessCall<>((CommonResponse) mResponseBody);
    }

    @NonNull
    @Override
    public Call<ConfigResponse> getConfig(@Path("hid") String hid) {
        Assert.assertNotNull(hid);
        return new FakeSuccessCall<>((ConfigResponse) mResponseBody);
    }

    @NonNull
    @Override
    public Call<GatewayResponse> updateGateway(@Path("hid") String hid, @Body GatewayModel gatewayModel) {
        return null;
    }

    @NonNull
    @Override
    public Call<ListResultModel<DeviceModel>> getDevicesByGatewayHid(@Path("hid") String hid) {
        return null;
    }

    @NonNull
    @Override
    public Call<CommonResponse> heartBeat(@Path("hid") String hid) {
        return null;
    }

    @NonNull
    @Override
    public Call<List<GatewayModel>> findAllGateways() {
        return null;
    }

    @NonNull
    @Override
    public Call<GatewayModel> findGateway(@Path("hid") String hid) {
        return null;
    }

    @NonNull
    @Override
    public Call<CommonResponse> sendGatewayCommand(@Path("hid") String hid, GatewayCommand command) {
        return null;
    }

    @NonNull
    @Override
    public Call<PagingResultModel<AuditLogModel>> getGatewayLogs(@Path("hid") String hid, @Query("createdDateFrom") String createdDateFrom, @Query("createdDateTo") String createdDateTo, @Query("userHids") List<String> userHids, @Query("types") List<String> types, @Query("sortField") String sortField, @Query("sortDirection") String sortDirection, @Query("_page") int page, @Query("_size") int size) {
        return null;
    }

    @Override
    public Call<CommonResponse> sendGatewayError(@Path("hid") String gatewayHid, @Body ErrorBodyModel error) {
        return null;
    }

    @NonNull
    @Override
    public Call<ListResultModel<FirmwareVersionModel>> getAvailableFirmwareForGatewayByHid(String hid) {
        return null;
    }

    @NonNull
    @Override
    public Call<ResponseBody> sendTelemetry(@Body RequestBody body) {
        return null;
    }

    @NonNull
    @Override
    public Call<PagingResultModel<TelemetryItemModel>> findTelemetryByAppHid(@Path("applicationHid") String applicationHid, @Query("fromTimestamp") String fromTimestamp, @Query("toTimestamp") String toTimestamp, @Query("telemetryNames") String telemetryNames, @Query("_page") int page, @Query("_size") int size) {
        return null;
    }

    @NonNull
    @Override
    public Call<ResponseBody> sendBatchTelemetry(@Body RequestBody body) {
        return null;
    }

    @NonNull
    @Override
    public Call<PagingResultModel<TelemetryItemModel>> findTelemetryByDeviceHid(@Path("deviceHid") String deviceHid, @Query("fromTimestamp") String fromTimestamp, @Query("toTimestamp") String toTimestamp, @Query("telemetryNames") String telemetryNames, @Query("_page") int page, @Query("_size") int size) {
        return null;
    }

    @NonNull
    @Override
    public Call<PagingResultModel<TelemetryItemModel>> findTelemetryByNodeHid(@Path("nodeHid") String nodeHid, @Query("fromTimestamp") String fromTimestamp, @Query("toTimestamp") String toTimestamp, @Query("telemetryNames") String telemetryNames, @Query("_page") int page, @Query("_size") int size) {
        return null;
    }

    @NonNull
    @Override
    public Call<TelemetryCountResponse> getTelemetryItemsCount(@Path("deviceHid") String deviceHid, @Query("telemetryName") String telemetryName, @Query("fromTimestamp") String fromTimestamp, @Query("toTimestamp") String toTimestamp) {
        return null;
    }

    @NonNull
    @Override
    public Call<ListResultModel<TelemetryItemModel>> getLastTelemetry(@Path("deviceHid") String deviceHid) {
        return null;
    }

    @Override
    public Call<CommonResponse> sendDeviceError(@Path("hid") String deviceHid, @Body ErrorBodyModel error) {
        return null;
    }

    @NonNull
    @Override
    public Call<CommonResponse> putReceived(@Path("hid") String hid) {
        return null;
    }

    @NonNull
    @Override
    public Call<CommonResponse> putSucceeded(@Path("hid") String hid) {
        return null;
    }

    @NonNull
    @Override
    public Call<CommonResponse> putFailed(@Path("hid") String hid) {
        return null;
    }

    @NonNull
    @Override
    public Call<ListResultModel<DeviceActionTypeModel>> getActionTypes() {
        return null;
    }

    @NonNull
    @Override
    public Call<ListResultModel<DeviceActionModel>> getActions(@Path("hid") String hid) {
        return null;
    }

    @NonNull
    @Override
    public Call<CommonResponse> postAction(@Path("hid") String hid, @Body DeviceActionModel action) {
        return null;
    }

    @NonNull
    @Override
    public Call<CommonResponse> updateAction(@Path("hid") String hid, @Path("index") int index, @Body DeviceActionModel action) {
        return null;
    }

    @NonNull
    @Override
    public Call<CommonResponse> deleteAction(@Path("hid") String hid, @Path("index") int index) {
        return null;
    }

    @NonNull
    @Override
    public Call<PagingResultModel<DeviceEventModel>> getHistoricalEvents(@Path("hid") String hid, @Query("createdDateFrom") String createdDateFrom, @Query("createdDateTo") String createdDateTo, @Query("sortField") String sortField, @Query("sortDirection") String sortDirection, @Query("statuses[]") List<String> statuses, @Query("systemNames[]") List<String> systemNames, @Query("_page") int page, @Query("_size") int size) {
        return null;
    }

    @NonNull
    @Override
    public Call<PagingResultModel<DeviceModel>> findAllDevices(@Query("userHid") String userHid, @Query("uid") String uid, @Query("type") String type, @Query("gatewayHid") String gatewayHid, @Query("createdBefore") String createdBefore, @Query("createdAfter") String createdAfter, @Query("updatedBefore") String updatedBefore, @Query("updatedAfter") String updatedAfter, @Query("enabled") String enabled, @Query("_page") int page, @Query("_size") int size) {
        return null;
    }

    @NonNull
    @Override
    public Call<DeviceRegistrationResponse> createOrUpdateDevice(@Body DeviceRegistrationModel deviceRequest) {
        return null;
    }

    @NonNull
    @Override
    public Call<DeviceModel> findDeviceByHid(@Path("hid") String hid) {
        return null;
    }

    @NonNull
    @Override
    public Call<CommonResponse> updateExistingDevice(@Path("hid") String hid, @Body DeviceRegistrationModel model) {
        return null;
    }

    @NonNull
    @Override
    public Call<PagingResultModel<AuditLogModel>> listDeviceAuditLogs(@Path("hid") String hid, @Query("createdDateFrom") String createdDateFrom, @Query("createdDateTo") String createdDateTo, @Query("userHids") List<String> userHids, @Query("types") List<String> types, @Query("sortField") String sortField, @Query("sortDirection") String sortDirection, @Query("_page") int page, @Query("_size") int size) {
        return null;
    }

    @NonNull
    @Override
    public Call<ListResultModel<FirmwareVersionModel>> getAvailableFirmwareForDeviceByHid(String hid) {
        return null;
    }

    @NonNull
    @Override
    public Call<ListResultModel<NodeModel>> getListExistingNodes() {
        return null;
    }

    @NonNull
    @Override
    public Call<CommonResponse> createNewNode(@Body NodeRegistrationModel model) {
        return null;
    }

    @NonNull
    @Override
    public Call<CommonResponse> updateExistingNode(@Path("hid") String nodeHid, @Body NodeRegistrationModel model) {
        return null;
    }

    @NonNull
    @Override
    public Call<ListResultModel<NodeTypeModel>> getListNodeTypes() {
        return null;
    }

    @NonNull
    @Override
    public Call<CommonResponse> createNewNodeType(NodeTypeRegistrationModel model) {
        return null;
    }

    @NonNull
    @Override
    public Call<CommonResponse> updateExistingNodeType(@Path("hid") String hid, @Body NodeTypeRegistrationModel model) {
        return null;
    }

    @NonNull
    @Override
    public Call<ListResultModel<DeviceTypeModel>> getListDeviceTypes() {
        return null;
    }

    @NonNull
    @Override
    public Call<CommonResponse> createNewDeviceType(@Body DeviceTypeRegistrationModel body) {
        return null;
    }

    @NonNull
    @Override
    public Call<CommonResponse> updateExistingDeviceType(@Path("hid") String hid, @Body DeviceTypeRegistrationModel body) {
        return null;
    }

    @NonNull
    @Override
    public Call<FindDeviceStateResponse> findDeviceState(@Path("hid") String hid) {
        return null;
    }

    @NonNull
    @Override
    public Call<CommonResponse> createNewDeviceStateTransaction(@Path("hid") String hid, @Body NewDeviceStateTransactionRequest body) {
        return null;
    }

    @NonNull
    @Override
    public Call<MessageStatusResponse> deviceStateTransactionSucceeded(@Path("hid") String hid, @Path("transHid") String transId) {
        return null;
    }

    @NonNull
    @Override
    public Call<MessageStatusResponse> deviceStateTransactionFailed(@Path("hid") String hid, @Path("transHid") String transId, @Body ErrorBodyModel error) {
        return null;
    }

    @NonNull
    @Override
    public Call<MessageStatusResponse> deviceStateTransactionReceived(@Path("hid") String hid, @Path("transHid") String transId) {
        return null;
    }

    @NonNull
    @Override
    public Call<CommonResponse> updateDeviceStateTransaction(@Path("hid") String hid, @Body NewDeviceStateTransactionRequest body) {
        return null;
    }

    @NonNull
    @Override
    public Call<RequestedFirmwareResponse> getListRequestedFirmware(String status, int page, int size) {
        return null;
    }

    @NonNull
    @Override
    public Call<AvailableFirmwareResponse> getListAvailableFirmware(String deviceTypeHid) {
        return null;
    }

    @NonNull
    @Override
    public Call<MessageStatusResponse> requireRightToUseFirmware(String softwareReleaseHid) {
        return null;
    }

    @NonNull
    @Override
    public Call<CommonResponse> createAndStartNewSoftwareReleaseSchedule(CreateAndStartSoftwareReleaseScheduleRequest body) {
        return null;
    }
}
