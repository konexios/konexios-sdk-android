/*
 * Copyright (c) 2017-2019 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *     Arrow Electronics, Inc.
 *     Konexios, Inc.
 */

package com.konexios.api.fakes;

import androidx.annotation.NonNull;

import com.konexios.api.models.AccountRequest;
import com.konexios.api.models.AccountRequest2;
import com.konexios.api.models.AccountResponse;
import com.konexios.api.models.AccountResponse2;
import com.konexios.api.models.AuditLogModel;
import com.konexios.api.models.AvailableFirmwareResponse;
import com.konexios.api.models.CommonResponse;
import com.konexios.api.models.ConfigResponse;
import com.konexios.api.models.CreateAndStartSoftwareReleaseScheduleRequest;
import com.konexios.api.models.DeviceActionModel;
import com.konexios.api.models.DeviceActionTypeModel;
import com.konexios.api.models.DeviceEventModel;
import com.konexios.api.models.DeviceModel;
import com.konexios.api.models.DeviceRegistrationModel;
import com.konexios.api.models.DeviceRegistrationResponse;
import com.konexios.api.models.DeviceTypeModel;
import com.konexios.api.models.DeviceTypeRegistrationModel;
import com.konexios.api.models.ErrorBodyModel;
import com.konexios.api.models.FindDeviceStateResponse;
import com.konexios.api.models.FirmwareVersionModel;
import com.konexios.api.models.GatewayCommand;
import com.konexios.api.models.GatewayModel;
import com.konexios.api.models.GatewayResponse;
import com.konexios.api.models.ListResultModel;
import com.konexios.api.models.MessageStatusResponse;
import com.konexios.api.models.NewDeviceStateTransactionRequest;
import com.konexios.api.models.NodeModel;
import com.konexios.api.models.NodeRegistrationModel;
import com.konexios.api.models.NodeTypeModel;
import com.konexios.api.models.NodeTypeRegistrationModel;
import com.konexios.api.models.PagingResultModel;
import com.konexios.api.models.RequestedFirmwareResponse;
import com.konexios.api.models.TelemetryCountResponse;
import com.konexios.api.models.TelemetryItemModel;
import com.konexios.api.rest.RestApiService;

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

public final class FakeRestService implements RestApiService {

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
    public Call<AccountResponse2> registerAccount2(AccountRequest2 accountRequest) {
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

    @NonNull
    @Override
    public Call<MessageStatusResponse> markSoftwareReleaseTransFailed(String hid) {
        return null;
    }

    @NonNull
    @Override
    public Call<MessageStatusResponse> markSoftwareReleaseTransReceived(String hid) {
        return null;
    }

    @NonNull
    @Override
    public Call<MessageStatusResponse> markSoftwareReleaseTransSecceeded(String hid) {
        return null;
    }

    @NonNull
    @Override
    public Call<MessageStatusResponse> startSoftwareReleaseTrans(String hid) {
        return null;
    }

    @NonNull
    @Override
    public Call<ResponseBody> downloadSoftwareReleaseFile(String hid, String token) {
        return null;
    }

}
