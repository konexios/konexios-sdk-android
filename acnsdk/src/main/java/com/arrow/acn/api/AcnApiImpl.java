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

import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.arrow.acn.api.common.ErrorUtils;
import com.arrow.acn.api.common.RetrofitHolder;
import com.arrow.acn.api.listeners.CheckinGatewayListener;
import com.arrow.acn.api.listeners.CommonRequestListener;
import com.arrow.acn.api.listeners.ConnectionListener;
import com.arrow.acn.api.listeners.DeleteDeviceActionListener;
import com.arrow.acn.api.listeners.FindDeviceListener;
import com.arrow.acn.api.listeners.FindGatewayListener;
import com.arrow.acn.api.listeners.GatewayCommandsListener;
import com.arrow.acn.api.listeners.GatewayRegisterListener;
import com.arrow.acn.api.listeners.GatewayUpdateListener;
import com.arrow.acn.api.listeners.GetGatewayConfigListener;
import com.arrow.acn.api.listeners.GetGatewaysListener;
import com.arrow.acn.api.listeners.ListNodeTypesListener;
import com.arrow.acn.api.listeners.ListResultListener;
import com.arrow.acn.api.listeners.PagingResultListener;
import com.arrow.acn.api.listeners.PostDeviceActionListener;
import com.arrow.acn.api.listeners.RegisterAccountListener;
import com.arrow.acn.api.listeners.RegisterDeviceListener;
import com.arrow.acn.api.listeners.ServerCommandsListener;
import com.arrow.acn.api.listeners.UpdateDeviceActionListener;
import com.arrow.acn.api.models.AccountRequest;
import com.arrow.acn.api.models.AccountResponse;
import com.arrow.acn.api.models.ApiError;
import com.arrow.acn.api.models.AuditLogModel;
import com.arrow.acn.api.models.AuditLogsQuery;
import com.arrow.acn.api.models.CommonResponse;
import com.arrow.acn.api.models.ConfigResponse;
import com.arrow.acn.api.models.DeviceActionModel;
import com.arrow.acn.api.models.DeviceActionTypeModel;
import com.arrow.acn.api.models.DeviceEventModel;
import com.arrow.acn.api.models.DeviceModel;
import com.arrow.acn.api.models.DeviceRegistrationModel;
import com.arrow.acn.api.models.DeviceRegistrationResponse;
import com.arrow.acn.api.models.DeviceTypeModel;
import com.arrow.acn.api.models.DeviceTypeRegistrationModel;
import com.arrow.acn.api.models.FindTelemetryRequest;
import com.arrow.acn.api.models.GatewayCommand;
import com.arrow.acn.api.models.GatewayModel;
import com.arrow.acn.api.models.GatewayResponse;
import com.arrow.acn.api.models.ListResultModel;
import com.arrow.acn.api.models.NodeModel;
import com.arrow.acn.api.models.NodeRegistrationModel;
import com.arrow.acn.api.models.NodeTypeModel;
import com.arrow.acn.api.models.NodeTypeRegistrationModel;
import com.arrow.acn.api.models.PagingResultModel;
import com.arrow.acn.api.models.TelemetryItemModel;
import com.arrow.acn.api.models.TelemetryModel;
import com.arrow.acn.api.rest.IotConnectAPIService;
import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;

import java.net.HttpURLConnection;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.arrow.acn.api.models.ApiError.COMMON_ERROR_CODE;

/**
 * Created by osminin on 6/17/2016.
 */

class AcnApiImpl implements AcnApiService {
    private static final String TAG = AcnApiImpl.class.getName();

    protected String mGatewayId;
    private String mGatewayUid;
    private IotConnectAPIService mRestService;
    @NonNull
    private Gson mGson = new Gson();
    private TelemetrySenderInterface mSenderService;

    private ServerCommandsListener mServerCommandsListener;
    private String mMqttHost;
    private String mMqttPrefix;
    private ConfigResponse mConfigResponse;

    private final RetrofitHolder mRetrofitHolder;
    private final SenderServiceFactory mSenderServiceFactory;

    AcnApiImpl(RetrofitHolder retrofitHolder,
               SenderServiceFactory senderServiceFactory) {
        mRetrofitHolder = retrofitHolder;
        mSenderServiceFactory = senderServiceFactory;
    }

    @NonNull
    protected Gson getGson() {
        return mGson;
    }

    @Override
    public void setRestEndpoint(@NonNull String endpoint, @NonNull String apiKey, @NonNull String apiSecret) {
        mRetrofitHolder.setDefaultApiKey(apiKey);
        mRetrofitHolder.setDefaultApiSecret(apiSecret);
        mRetrofitHolder.setSecretKey(null);
        mRetrofitHolder.setApiKey(null);
        mRestService = mRetrofitHolder.getIotConnectAPIService(endpoint);
    }

    @Override
    public void setMqttEndpoint(String host, String prefix) {
        mMqttHost = host;
        mMqttPrefix = prefix;
    }

    @Override
    public void connect(@NonNull ConnectionListener listener) {
        if (mConfigResponse == null) {
            FirebaseCrash.logcat(Log.ERROR, TAG, "connect() mConfigResponse is NULL");
            ApiError error = new ApiError(COMMON_ERROR_CODE, "config() method must be called first!");
            listener.onConnectionError(error);
            return;
        } else if (mGatewayUid == null) {
            FirebaseCrash.logcat(Log.ERROR, TAG, "connect() mConfigResponse is NULL");
            ApiError error = new ApiError(COMMON_ERROR_CODE, "registerGateway or checkinGateway" +
                    "method must be called first!");
            listener.onConnectionError(error);
            return;
        }
        if (mSenderService != null && mSenderService.isConnected()) {
            mSenderService.disconnect();
            FirebaseCrash.logcat(Log.DEBUG, TAG, "connect(), old service is disconnected");
        }
        ConfigResponse.CloudPlatform cloud = mConfigResponse.getCloudPlatform();
        FirebaseCrash.logcat(Log.DEBUG, TAG, "connect() cloudPlatform: " + cloud);
        mSenderService = mSenderServiceFactory.createTelemetrySender(mRetrofitHolder,
                mConfigResponse,
                mGatewayUid,
                mGatewayId,
                mMqttHost,
                mMqttPrefix,
                mServerCommandsListener);
        if (mSenderService == null) {
            FirebaseCrash.logcat(Log.ERROR, TAG, "connect() invalid cloud platform: " + cloud);
            ApiError error = new ApiError(COMMON_ERROR_CODE, "invalid cloud platform: " + cloud);
            listener.onConnectionError(error);
            return;
        }
        mSenderService.connect(listener);
        FirebaseCrash.logcat(Log.DEBUG, TAG, "connect() done!");
    }

    @Override
    public void disconnect() {
        mSenderService.disconnect();
    }

    @Override
    public void sendSingleTelemetry(TelemetryModel telemetry) {
        mSenderService.sendSingleTelemetry(telemetry);
    }

    @Override
    public void sendBatchTelemetry(List<TelemetryModel> telemetry) {
        mSenderService.sendBatchTelemetry(telemetry);
    }

    protected void onGatewayResponse(@NonNull GatewayResponse response) {
        mGatewayId = response.getHid();
    }

    protected void onConfigResponse(@NonNull ConfigResponse response) {
        ConfigResponse.Key keys = response.getKey();
        if (keys != null) {
            mRetrofitHolder.setSecretKey(keys.getSecretKey());
            mRetrofitHolder.setApiKey(keys.getApiKey());
        }
        mConfigResponse = response;
        FirebaseCrash.logcat(Log.DEBUG, TAG, "onConfigResponse() cloudPlatform: " + mConfigResponse.getCloudPlatform());
    }

    @Override
    public void setServerCommandsListener(ServerCommandsListener listener) {
        mServerCommandsListener = listener;
    }

    @Override
    public void registerAccount(@NonNull AccountRequest accountRequest, @NonNull final RegisterAccountListener listener) {
        FirebaseCrash.logcat(Log.DEBUG, TAG, "registerAccount() email: " + accountRequest.getEmail()
                + ", code: " + accountRequest.getCode());
        Call<AccountResponse> call = mRestService.registerAccount(accountRequest);
        call.enqueue(new Callback<AccountResponse>() {
            @Override
            public void onResponse(Call<AccountResponse> call, @NonNull Response<AccountResponse> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "registerAccount: " + response.code());
                try {
                    if (response.body() != null && response.code() == HttpURLConnection.HTTP_OK) {
                        listener.onAccountRegistered(response.body());
                    } else {
                        ApiError error = mRetrofitHolder.convertToApiError(response);
                        listener.onAccountRegisterFailed(error);
                    }
                } catch (Exception e) {
                    listener.onAccountRegisterFailed(ErrorUtils.parseError(e));
                    e.printStackTrace();
                    FirebaseCrash.report(e);
                }
            }

            @Override
            public void onFailure(Call<AccountResponse> call, Throwable t) {
                listener.onAccountRegisterFailed(ErrorUtils.parseError(t));
                FirebaseCrash.logcat(Log.ERROR, TAG, "registerAccount() failed");
                FirebaseCrash.report(t);
            }
        });
    }

    @Override
    public void getDeviceActionTypes(@NonNull final ListResultListener<DeviceActionTypeModel> listener) {
        mRestService.getActionTypes().enqueue(new Callback<ListResultModel<DeviceActionTypeModel>>() {
            @Override
            public void onResponse(Call<ListResultModel<DeviceActionTypeModel>> call, @NonNull Response<ListResultModel<DeviceActionTypeModel>> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "getActionTypes response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body().getData());
                } else {
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<ListResultModel<DeviceActionTypeModel>> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "getActionTypes error");
                listener.onRequestError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void getDeviceActions(@NonNull String deviceHid, @NonNull final ListResultListener<DeviceActionModel> listener) {
        mRestService.getActions(deviceHid).enqueue(new Callback<ListResultModel<DeviceActionModel>>() {
            @Override
            public void onResponse(Call<ListResultModel<DeviceActionModel>> call, @NonNull Response<ListResultModel<DeviceActionModel>> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "getActions response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body().getData());
                } else {
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<ListResultModel<DeviceActionModel>> call, Throwable t) {
                listener.onRequestError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void postDeviceAction(@NonNull String deviceHid, @NonNull DeviceActionModel action, @NonNull final PostDeviceActionListener listener) {
        mRestService.postAction(deviceHid, action).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "getActionTypes response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.postActionSucceed();
                } else {
                    listener.postActionFailed(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.postActionFailed(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void updateDeviceAction(@NonNull String deviceHid, int index, @NonNull DeviceActionModel model, @NonNull final UpdateDeviceActionListener listener) {
        mRestService.updateAction(deviceHid, index, model).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "getActionTypes response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onDeviceActionUpdated();
                } else {
                    listener.onDeviceActionUpdateFailed(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onDeviceActionUpdateFailed(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void getDeviceHistoricalEvents(@NonNull String deviceHid, @NonNull final PagingResultListener<DeviceEventModel> listener) {
        mRestService.getHistoricalEvents(deviceHid).enqueue(new Callback<PagingResultModel<DeviceEventModel>>() {
            @Override
            public void onResponse(Call<PagingResultModel<DeviceEventModel>> call, @NonNull Response<PagingResultModel<DeviceEventModel>> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "getHistoricalEvents response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body().getData());
                } else {
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<PagingResultModel<DeviceEventModel>> call, Throwable t) {
                listener.onRequestError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void registerDevice(@NonNull DeviceRegistrationModel req, @NonNull final RegisterDeviceListener listener) {
        FirebaseCrash.logcat(Log.DEBUG, TAG, "regiterDevice() type: " + req.getType() + ", uid: " + req.getUid());
        mRestService.createOrUpdateDevice(req).enqueue(new Callback<DeviceRegistrationResponse>() {
            @Override
            public void onResponse(Call<DeviceRegistrationResponse> call, @NonNull final Response<DeviceRegistrationResponse> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "createOrUpdateDevice response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onDeviceRegistered(response.body());
                } else {
                    listener.onDeviceRegistrationFailed(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<DeviceRegistrationResponse> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "createOrUpdateDevice error");
                listener.onDeviceRegistrationFailed(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void registerReceivedEvent(@NonNull String eventHid) {
        mRestService.putReceived(eventHid).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "registerReceivedEvent response");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "registerReceivedEvent error");
            }
        });
    }

    @Override
    public void eventHandlingSucceed(@NonNull String eventHid) {
        mRestService.putSucceeded(eventHid).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "eventHandlingSucceed response");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "eventHandlingSucceed error");
            }
        });
    }

    @Override
    public void eventHandlingFailed(@NonNull String eventHid) {
        mRestService.putFailed(eventHid).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "eventHandlingSucceed response");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "eventHandlingSucceed error");
            }
        });
    }

    @Override
    public void findAllGateways(@NonNull final GetGatewaysListener listener) {
        mRestService.findAllGateways().enqueue(new Callback<List<GatewayModel>>() {
            @Override
            public void onResponse(Call<List<GatewayModel>> call, @NonNull Response<List<GatewayModel>> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "findAllGateways response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onGatewaysReceived(response.body());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "findAllGateways error");
                    listener.onGatewaysFailed(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<List<GatewayModel>> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "findAllGateways error");
                listener.onGatewaysFailed(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void registerGateway(@NonNull final GatewayModel gatewayModel, @NonNull final GatewayRegisterListener listener) {
        FirebaseCrash.logcat(Log.DEBUG, TAG, "registerGateway(), uid: " + gatewayModel.getUid() +
                ", applicationHid: " + gatewayModel.getApplicationHid());
        mRestService.registerGateway(gatewayModel).enqueue(new Callback<GatewayResponse>() {
            @Override
            public void onResponse(Call<GatewayResponse> call, @NonNull final Response<GatewayResponse> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "registerGateway response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    onGatewayResponse(response.body());
                    mGatewayUid = gatewayModel.getUid();
                    listener.onGatewayRegistered(response.body());

                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "registerGateway error");
                    listener.onGatewayRegisterFailed(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<GatewayResponse> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "registerGateway error");
                listener.onGatewayRegisterFailed(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void findGateway(@NonNull String hid, @NonNull final FindGatewayListener listener) {
        mRestService.findGateway(hid).enqueue(new Callback<GatewayModel>() {
            @Override
            public void onResponse(Call<GatewayModel> call, @NonNull Response<GatewayModel> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "findGateway response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onGatewayFound(response.body());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "findGateway error");
                    listener.onGatewayFindError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<GatewayModel> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "findGateway error");
                listener.onGatewayFindError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void updateGateway(@NonNull String hid, @NonNull GatewayModel gatewayModel, @NonNull final GatewayUpdateListener listener) {
        mRestService.updateGateway(hid, gatewayModel).enqueue(new Callback<GatewayResponse>() {
            @Override
            public void onResponse(Call<GatewayResponse> call, @NonNull Response<GatewayResponse> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "updateGateway response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onGatewayUpdated(response.body());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "updateGateway error");
                    listener.onGatewayUpdateFailed(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<GatewayResponse> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "updateGateway error");
                listener.onGatewayUpdateFailed(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void checkinGateway(@NonNull String hid, @NonNull String gatewayUid, @NonNull final CheckinGatewayListener listener) {
        FirebaseCrash.logcat(Log.DEBUG, TAG, "checkinGateway(), hid: " + hid);
        mGatewayUid = gatewayUid;
        mRestService.checkin(hid).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "checkin response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onCheckinGatewaySuccess();
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "checkin error");
                    ApiError error = mRetrofitHolder.convertToApiError(response);
                    listener.onCheckinGatewayError(error);
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "checkin error");
                listener.onCheckinGatewayError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void sendCommandGateway(@NonNull String hid, @NonNull GatewayCommand command, @NonNull final GatewayCommandsListener listener) {
        mRestService.sendGatewayCommand(hid, command).enqueue(new Callback<GatewayResponse>() {
            @Override
            public void onResponse(Call<GatewayResponse> call, @NonNull Response<GatewayResponse> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "sendGatewayCommand response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onGatewayCommandSent(response.body());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "sendGatewayCommand error");
                    listener.onGatewayCommandFailed(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<GatewayResponse> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "sendGatewayCommand error");
                listener.onGatewayCommandFailed(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void getGatewayConfig(@NonNull final String hid, @NonNull final GetGatewayConfigListener listener) {
        FirebaseCrash.logcat(Log.DEBUG, TAG, "getGatewayConfig() hid: " + hid);
        mRestService.getConfig(hid).enqueue(new Callback<ConfigResponse>() {
            @Override
            public void onResponse(Call<ConfigResponse> call, @NonNull final Response<ConfigResponse> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "getConfig response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    if (mGatewayId == null) {
                        //this means that gateway id has been stored on a client side and we have
                        //to set it here
                        mGatewayId = hid;
                    }
                    onConfigResponse(response.body());
                    listener.onGatewayConfigReceived(response.body());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "getConfig error");
                    listener.onGatewayConfigFailed(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<ConfigResponse> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "getConfig error");
                listener.onGatewayConfigFailed(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void getDevicesList(@NonNull String gatewayHid, @NonNull final ListResultListener<DeviceModel> listener) {
        FirebaseCrash.logcat(Log.DEBUG, TAG, "getDevicesList() hid: " + gatewayHid);
        mRestService.getDevicesByGatewayHid(gatewayHid).enqueue(new Callback<ListResultModel<DeviceModel>>() {
            @Override
            public void onResponse(Call<ListResultModel<DeviceModel>> call, @NonNull Response<ListResultModel<DeviceModel>> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "getDevicesList response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body().getData());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "getDevicesList error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<ListResultModel<DeviceModel>> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "getDevicesList error");
                listener.onRequestError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void gatewayHeartbeat(@NonNull String hid, @NonNull final CommonRequestListener listener) {
        mRestService.heartBeat(hid).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "heartBeat response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "heartBeat error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "heartBeat error");
                listener.onRequestError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void getGatewayLogs(@NonNull String hid, @NonNull AuditLogsQuery query, @NonNull final PagingResultListener<AuditLogModel> listener) {
        mRestService.getGatewayLogs(hid, query.getCreatedDateFrom(), query.getCreatedDateTo(),
                query.getUserHids(), query.getTypes(), query.getSortField(), query.getSortDirection(),
                query.getPage(), query.getSize()).enqueue(new Callback<PagingResultModel<AuditLogModel>>() {
            @Override
            public void onResponse(Call<PagingResultModel<AuditLogModel>> call, @NonNull Response<PagingResultModel<AuditLogModel>> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "getGatewayLogs response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body().getData());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "getGatewayLogs error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<PagingResultModel<AuditLogModel>> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "getGatewayLogs error");
                listener.onRequestError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void deleteDeviceAction(@NonNull String deviceHid, int index, @NonNull final DeleteDeviceActionListener listener) {
        mRestService.deleteAction(deviceHid, index).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "deleteAction response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onDeviceActionDeleted();
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "deleteAction error");
                    listener.onDeviceActionDeleteFailed(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "deleteAction error");
                listener.onDeviceActionDeleteFailed(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void findAllDevices(String userHid, String uid, String type, String gatewayHid,
                               String enabled, int page, int size, @NonNull final PagingResultListener<DeviceModel> listener) {
        mRestService.findAllDevices(userHid, uid, type, gatewayHid, enabled, page, size).
                enqueue(new Callback<PagingResultModel<DeviceModel>>() {
                    @Override
                    public void onResponse(Call<PagingResultModel<DeviceModel>> call, @NonNull Response<PagingResultModel<DeviceModel>> response) {
                        FirebaseCrash.logcat(Log.DEBUG, TAG, "deleteAction response");
                        if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                            listener.onRequestSuccess(response.body().getData());
                        } else {
                            FirebaseCrash.logcat(Log.ERROR, TAG, "deleteAction error");
                            listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                        }
                    }

                    @Override
                    public void onFailure(Call<PagingResultModel<DeviceModel>> call, Throwable t) {
                        FirebaseCrash.logcat(Log.ERROR, TAG, "deleteAction error");
                        listener.onRequestError(ErrorUtils.parseError(t));
                    }
                });
    }

    @Override
    public void findDeviceByHid(@NonNull String deviceHid, @NonNull final FindDeviceListener listener) {
        mRestService.findDeviceByHid(deviceHid).enqueue(new Callback<DeviceModel>() {
            @Override
            public void onResponse(Call<DeviceModel> call, @NonNull Response<DeviceModel> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "findDeviceByHid response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onDeviceFindSuccess(response.body());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "findDeviceByHid error");
                    listener.onDeviceFindFailed(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<DeviceModel> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "findDeviceByHid error");
                listener.onDeviceFindFailed(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void updateDevice(@NonNull String deviceHid, @NonNull DeviceRegistrationModel device, @NonNull final CommonRequestListener listener) {
        mRestService.updateExistingDevice(deviceHid, device).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "updateExistingDevice response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "updateExistingDevice error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "updateExistingDevice error");
                listener.onRequestError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void getDeviceAuditLogs(@NonNull String deviceHid, @NonNull AuditLogsQuery query, @NonNull final PagingResultListener<AuditLogModel> listener) {
        mRestService.listDeviceAuditLogs(deviceHid, query.getCreatedDateFrom(), query.getCreatedDateTo(),
                query.getUserHids(), query.getTypes(), query.getSortField(), query.getSortDirection(),
                query.getPage(), query.getSize()).enqueue(new Callback<PagingResultModel<AuditLogModel>>() {
            @Override
            public void onResponse(Call<PagingResultModel<AuditLogModel>> call, @NonNull Response<PagingResultModel<AuditLogModel>> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "getDeviceAuditLogs response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body().getData());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "getDeviceAuditLogs error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<PagingResultModel<AuditLogModel>> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "getDeviceAuditLogs error");
                listener.onRequestError(ErrorUtils.parseError(t));
            }
        });
    }

    //node api


    @Override
    public void getNodesList(@NonNull final ListResultListener<NodeModel> listener) {
        mRestService.getListExistingNodes().enqueue(new Callback<ListResultModel<NodeModel>>() {
            @Override
            public void onResponse(Call<ListResultModel<NodeModel>> call, @NonNull Response<ListResultModel<NodeModel>> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "getNodesList response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body().getData());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "getNodesList error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<ListResultModel<NodeModel>> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "getNodesList error");
                listener.onRequestError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void createNewNode(@NonNull NodeRegistrationModel node, @NonNull final CommonRequestListener listener) {
        mRestService.createNewNode(node).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "createNewNode response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "createNewNode error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "createNewNode error");
                listener.onRequestError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void updateExistingNode(@NonNull String nodeHid, NodeRegistrationModel node, @NonNull final CommonRequestListener listener) {
        mRestService.updateExistingNode(nodeHid, node).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "updateExistingNode response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "updateExistingNode error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "updateExistingNode error");
                listener.onRequestError(ErrorUtils.parseError(t));
            }
        });
    }

    // node - type api


    @Override
    public void getListNodeTypes(@NonNull final ListNodeTypesListener listener) {
        mRestService.getListNodeTypes().enqueue(new Callback<ListResultModel<NodeTypeModel>>() {
            @Override
            public void onResponse(Call<ListResultModel<NodeTypeModel>> call, @NonNull Response<ListResultModel<NodeTypeModel>> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "getListNodeTypes response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onListNodeTypesSuccess(response.body());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "getListNodeTypes error");
                    listener.onListNodeTypesFiled(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<ListResultModel<NodeTypeModel>> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "getListNodeTypes error");
                listener.onListNodeTypesFiled(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void createNewNodeType(@NonNull NodeTypeRegistrationModel nodeType, @NonNull final CommonRequestListener listener) {
        mRestService.createNewNodeType(nodeType).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "createNewNodeType response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "createNewNodeType error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "createNewNodeType error");
                listener.onRequestError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void updateExistingNodeType(@NonNull String hid, @NonNull NodeTypeRegistrationModel nodeType, @NonNull final CommonRequestListener listener) {
        mRestService.updateExistingNodeType(hid, nodeType).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "updateExistingNodeType response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "updateExistingNodeType error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "updateExistingNodeType error");
                listener.onRequestError(ErrorUtils.parseError(t));
            }
        });
    }

    //device - type api

    @Override
    public void getListDeviceTypes(@NonNull final ListResultListener<DeviceTypeModel> listener) {
        mRestService.getListDeviceTypes().enqueue(new Callback<ListResultModel<DeviceTypeModel>>() {
            @Override
            public void onResponse(Call<ListResultModel<DeviceTypeModel>> call, @NonNull Response<ListResultModel<DeviceTypeModel>> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "getListDeviceTypes response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body().getData());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "getListDeviceTypes error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<ListResultModel<DeviceTypeModel>> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "getListDeviceTypes error");
                listener.onRequestError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void createNewDeviceType(@NonNull DeviceTypeRegistrationModel deviceType, @NonNull final CommonRequestListener listener) {
        mRestService.createNewDeviceType(deviceType).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "createNewDeviceType response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "createNewDeviceType error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "createNewDeviceType error");
                listener.onRequestError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void updateExistingDeviceType(String hid, DeviceTypeRegistrationModel deviceType,
                                         @NonNull final CommonRequestListener listener) {
        mRestService.updateExistingDeviceType(hid, deviceType).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "updateExistingDeviceType response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "updateExistingDeviceType error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "updateExistingDeviceType error");
                listener.onRequestError(ErrorUtils.parseError(t));
            }
        });
    }

    //telemetry api
    @Override
    public void findTelemetryByApplicationHid(@NonNull FindTelemetryRequest request, @NonNull final PagingResultListener<TelemetryItemModel> listener) {
        mRestService.findTelemetryByAppHid(request.getHid(), request.getFromTimestamp(), request.getToTimestamp(),
                request.getTelemetryNames(), request.getPage(), request.getSize()).enqueue(new Callback<PagingResultModel<TelemetryItemModel>>() {
            @Override
            public void onResponse(Call<PagingResultModel<TelemetryItemModel>> call, @NonNull Response<PagingResultModel<TelemetryItemModel>> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "findTelemetryByApplicationHid response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body().getData());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "findTelemetryByApplicationHid error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<PagingResultModel<TelemetryItemModel>> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "findTelemetryByApplicationHid error");
                listener.onRequestError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void findTelemetryByDeviceHid(@NonNull FindTelemetryRequest request, @NonNull final PagingResultListener<TelemetryItemModel> listener) {
        mRestService.findTelemetryByDeviceHid(request.getHid(), request.getFromTimestamp(), request.getToTimestamp(),
                request.getTelemetryNames(), request.getPage(), request.getSize()).enqueue(new Callback<PagingResultModel<TelemetryItemModel>>() {
            @Override
            public void onResponse(Call<PagingResultModel<TelemetryItemModel>> call, @NonNull Response<PagingResultModel<TelemetryItemModel>> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "findTelemetryByDeviceHid response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body().getData());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "findTelemetryByDeviceHid error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<PagingResultModel<TelemetryItemModel>> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "findTelemetryByDeviceHid error");
                listener.onRequestError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void findTelemetryByNodeHid(@NonNull FindTelemetryRequest request, @NonNull final PagingResultListener<TelemetryItemModel> listener) {
        mRestService.findTelemetryByNodeHid(request.getHid(), request.getFromTimestamp(), request.getToTimestamp(),
                request.getTelemetryNames(), request.getPage(), request.getSize()).enqueue(new Callback<PagingResultModel<TelemetryItemModel>>() {
            @Override
            public void onResponse(Call<PagingResultModel<TelemetryItemModel>> call, @NonNull Response<PagingResultModel<TelemetryItemModel>> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "findTelemetryByNodeHid response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body().getData());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "findTelemetryByNodeHid error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<PagingResultModel<TelemetryItemModel>> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "findTelemetryByNodeHid error");
                listener.onRequestError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void getLastTelemetry(String deviceHid, @NonNull final ListResultListener<TelemetryItemModel> listener) {
        mRestService.getLastTelemetry(deviceHid).enqueue(new Callback<ListResultModel<TelemetryItemModel>>() {
            @Override
            public void onResponse(Call<ListResultModel<TelemetryItemModel>> call, @NonNull Response<ListResultModel<TelemetryItemModel>> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "getLastTelemetry response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body().getData());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "getLastTelemetry error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<ListResultModel<TelemetryItemModel>> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "getLastTelemetry error");
                listener.onRequestError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public boolean hasBatchMode() {
        return mSenderService.hasBatchMode();
    }

    @Override
    public boolean isConnected() {
        return mSenderService != null && mSenderService.isConnected();
    }
}
