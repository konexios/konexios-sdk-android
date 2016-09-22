package com.kronossdk.api.rest;

import com.kronossdk.api.models.*;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by osminin on 3/15/2016.
 */
public interface IotConnectAPIService {

    @POST("/api/v1/kronos/gateways")
    Call<GatewayResponse> registerGateway(@Body GatewayModel gatewayModel);

    @PUT("/api/v1/kronos/gateways/{hid}/checkin")
    Call<Void> checkin(@Path("hid") String hid);

    @POST("/api/v1/kronos/accounts")
    Call<AccountResponse> registerAccount(@Body AccountRequest accountRequest);

    @GET("/api/v1/kronos/gateways/{hid}/config")
    Call<ConfigResponse> getConfig(@Path("hid") String hid);

    @PUT("/api/v1/kronos/gateways/{hid}")
    Call<ResponseBody> updateGateway(@Path("hid") String hid, @Body GatewayModel gatewayModel);

    @PUT("/api/v1/kronos/gateways/{hid}/heartbeat")
    Call<ResponseBody> heartBeat(@Path("hid") String hid);

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
    Call<List<ActionTypeModel>> getActionTypes();

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
