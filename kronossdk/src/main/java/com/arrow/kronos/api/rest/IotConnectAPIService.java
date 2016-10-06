package com.arrow.kronos.api.rest;


import com.arrow.kronos.api.models.AccountRequest;
import com.arrow.kronos.api.models.AccountResponse;
import com.arrow.kronos.api.models.ActionModel;
import com.arrow.kronos.api.models.ActionResponseModel;
import com.arrow.kronos.api.models.ActionTypeResponseModel;
import com.arrow.kronos.api.models.ConfigResponse;
import com.arrow.kronos.api.models.GatewayCommand;
import com.arrow.kronos.api.models.GatewayLogsResponse;
import com.arrow.kronos.api.models.GatewayModel;
import com.arrow.kronos.api.models.GatewayResponse;
import com.arrow.kronos.api.models.HistoricalEventResponse;
import com.arrow.kronos.api.models.RegisterDeviceRequest;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by osminin on 3/15/2016.
 */
public interface IotConnectAPIService {

    @POST("/api/v1/kronos/accounts")
    Call<AccountResponse> registerAccount(@Body AccountRequest accountRequest);

    //GatewayApi
    @POST("/api/v1/kronos/gateways")
    Call<GatewayResponse> registerGateway(@Body GatewayModel gatewayModel);

    @PUT("/api/v1/kronos/gateways/{hid}/checkin")
    Call<Void> checkin(@Path("hid") String hid);

    @GET("/api/v1/kronos/gateways/{hid}/config")
    Call<ConfigResponse> getConfig(@Path("hid") String hid);

    @PUT("/api/v1/kronos/gateways/{hid}")
    Call<GatewayResponse> updateGateway(@Path("hid") String hid, @Body GatewayModel gatewayModel);

    @PUT("/api/v1/kronos/gateways/{hid}/heartbeat")
    Call<ResponseBody> heartBeat(@Path("hid") String hid);

    @GET("/api/v1/kronos/gateways")
    Call<List<GatewayModel>> findAllGateways();

    @GET("/api/v1/kronos/gateways/{hid}")
    Call<GatewayModel> findGateway(@Path("hid") String hid);

    @POST("/api/v1/kronos/gateways/{hid}/commands/device-command")
    Call<ResponseBody> sendGatewayCommand(@Path("hid") String hid, GatewayCommand command);

    @GET("/api/v1/kronos/gateways/{hid}/logs")
    Call<GatewayLogsResponse> getGatewayLogs(@Path("hid") String hid,
                                             @Query("createdDateFrom") String createdDateFrom,
                                             @Query("createdDateTo") String createdDateTo,
                                             @Query("userHids") String[] userHids,
                                             @Query("types") String[] types,
                                             @Query("sortField") String sortField,
                                             @Query("sortDirection") String sortDirection,
                                             @Query("_page") int page,
                                             @Query("_size") int size);

    @POST("/api/v1/kronos/devices")
    Call<GatewayResponse> registerDevice(@Body RegisterDeviceRequest deviceRequest);

    @POST("/api/v1/kronos/telemetries")
    Call<ResponseBody> sendTelemetry(@Body RequestBody body);

    @POST("/api/v1/kronos/telemetries/batch")
    Call<ResponseBody> sendBatchTelemetry(@Body RequestBody body);

    @PUT("/api/v1/core/events/{hid}/received")
    Call<ResponseBody> putReceived(@Path("hid") String hid);

    @PUT("/api/v1/core/events/{hid}/succeeded")
    Call<ResponseBody> putSucceeded(@Path("hid") String hid);

    @PUT("/api/v1/core/events/{hid}/failed")
    Call<ResponseBody> putFailed(@Path("hid") String hid);

    //device-action api
    @GET("/api/v1/kronos/devices/actions/types")
    Call<ActionTypeResponseModel> getActionTypes();

    @GET("/api/v1/kronos/devices/{hid}/actions")
    Call<ActionResponseModel> getActions(@Path("hid") String hid);

    @POST("/api/v1/kronos/devices/{hid}/actions")
    Call<ResponseBody> postAction(@Path("hid") String hid, @Body ActionModel action);

    @PUT("/api/v1/kronos/devices/{hid}/actions/{index}")
    Call<ResponseBody> updateAction(@Path("hid") String hid, @Path("index") int index,
                                    @Body ActionModel action);

    @GET("/api/v1/kronos/devices/{hid}/events")
    Call<HistoricalEventResponse> getHistoricalEvents(@Path("hid") String hid);

}
