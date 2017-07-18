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
import android.support.annotation.NonNull;

import com.arrow.acn.api.common.ErrorUtils;
import com.arrow.acn.api.common.RetrofitHolder;
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
import com.arrow.acn.api.listeners.ServerCommandsListener;
import com.arrow.acn.api.listeners.TelemetryCountListener;
import com.arrow.acn.api.listeners.TelemetryRequestListener;
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
import com.arrow.acn.api.models.ErrorBodyModel;
import com.arrow.acn.api.models.FindDeviceStateResponse;
import com.arrow.acn.api.models.FindDevicesRequest;
import com.arrow.acn.api.models.FindTelemetryRequest;
import com.arrow.acn.api.models.GatewayCommand;
import com.arrow.acn.api.models.GatewayModel;
import com.arrow.acn.api.models.GatewayResponse;
import com.arrow.acn.api.models.HistoricalEventsRequest;
import com.arrow.acn.api.models.ListResultModel;
import com.arrow.acn.api.models.MessageStatusResponse;
import com.arrow.acn.api.models.NewDeviceStateTransactionRequest;
import com.arrow.acn.api.models.NodeModel;
import com.arrow.acn.api.models.NodeRegistrationModel;
import com.arrow.acn.api.models.NodeTypeModel;
import com.arrow.acn.api.models.NodeTypeRegistrationModel;
import com.arrow.acn.api.models.PagingResultModel;
import com.arrow.acn.api.models.TelemetryCountRequest;
import com.arrow.acn.api.models.TelemetryCountResponse;
import com.arrow.acn.api.models.TelemetryItemModel;
import com.arrow.acn.api.models.TelemetryModel;
import com.arrow.acn.api.rest.IotConnectAPIService;

import java.net.HttpURLConnection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.arrow.acn.api.models.ApiError.COMMON_ERROR_CODE;

/**
 * Created by osminin on 6/17/2016.
 */

@Keep
final class AcnApiImpl implements AcnApiService, SenderServiceArgsProvider {
    private final RetrofitHolder mRetrofitHolder;
    private final SenderServiceFactory mSenderServiceFactory;
    private String mGatewayId;
    private String mGatewayUid;
    private IotConnectAPIService mRestService;
    private TelemetrySenderInterface mSenderService;
    private ServerCommandsListener mServerCommandsListener;
    private String mMqttHost;
    private String mMqttPrefix;
    private ConfigResponse mConfigResponse;

    AcnApiImpl(RetrofitHolder retrofitHolder,
               SenderServiceFactory senderServiceFactory,
               boolean isDebug) {
        mRetrofitHolder = retrofitHolder;
        mSenderServiceFactory = senderServiceFactory;
        if ((BuildConfig.DEBUG || isDebug) && Timber.forest().isEmpty()) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    void setRestEndpoint(@NonNull String endpoint, @NonNull String apiKey, @NonNull String apiSecret) {
        Timber.d("setRestEndpoint");
        mRetrofitHolder.setDefaultApiKey(apiKey);
        mRetrofitHolder.setDefaultApiSecret(apiSecret);
        mRetrofitHolder.setSecretKey(null);
        mRetrofitHolder.setApiKey(null);
        mRestService = mRetrofitHolder.getIotConnectAPIService(endpoint);
    }

    void setMqttEndpoint(String host, String prefix) {
        Timber.d("setMqttEndpoint");
        mMqttHost = host;
        mMqttPrefix = prefix;
    }

    @Override
    public void connect(@NonNull ConnectionListener listener) {
        if (mConfigResponse == null) {
            Timber.e("connect() mConfigResponse is NULL");
            ApiError error = new ApiError(COMMON_ERROR_CODE, "config() method must be called first!");
            listener.onConnectionError(error);
            return;
        } else if (mGatewayUid == null) {
            Timber.e("connect() mConfigResponse is NULL");
            ApiError error = new ApiError(COMMON_ERROR_CODE, "registerGateway or checkinGateway" +
                    "method must be called first!");
            listener.onConnectionError(error);
            return;
        }
        if (mSenderService != null && mSenderService.isConnected()) {
            mSenderService.disconnect();
            Timber.d("connect(), old service is disconnected");
        }
        ConfigResponse.CloudPlatform cloud = mConfigResponse.getCloudPlatform();
        Timber.d("connect() cloudPlatform: " + cloud);
        mSenderService = mSenderServiceFactory.createTelemetrySender(this);
        if (mSenderService == null) {
            Timber.e("connect() invalid cloud platform: " + cloud);
            ApiError error = new ApiError(COMMON_ERROR_CODE, "invalid cloud platform: " + cloud);
            listener.onConnectionError(error);
            return;
        }
        mSenderService.connect(listener);
        Timber.d("connect() done!");
    }

    @Override
    public void disconnect() {
        mSenderService.disconnect();
    }

    @Override
    public void sendSingleTelemetry(TelemetryModel telemetry, TelemetryRequestListener listener) {
        mSenderService.sendSingleTelemetry(telemetry, listener);
    }

    @Override
    public void sendBatchTelemetry(List<TelemetryModel> telemetry, TelemetryRequestListener listener) {
        mSenderService.sendBatchTelemetry(telemetry, listener);
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
        Timber.d("onConfigResponse() cloudPlatform: " + mConfigResponse.getCloudPlatform());
    }

    @Override
    public void setServerCommandsListener(ServerCommandsListener listener) {
        mServerCommandsListener = listener;
    }

    @Override
    public void registerAccount(@NonNull AccountRequest accountRequest, @NonNull final RegisterAccountListener listener) {
        Timber.d("registerAccount() email: " + accountRequest.getEmail()
                + ", code: " + accountRequest.getCode());
        Call<AccountResponse> call = mRestService.registerAccount(accountRequest);
        call.enqueue(new Callback<AccountResponse>() {
            @Override
            public void onResponse(Call<AccountResponse> call, @NonNull Response<AccountResponse> response) {
                Timber.d("registerAccount: " + response.code());
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
                    Timber.e(e);
                }
            }

            @Override
            public void onFailure(Call<AccountResponse> call, Throwable t) {
                listener.onAccountRegisterFailed(ErrorUtils.parseError(t));
                Timber.e("registerAccount() failed");
                Timber.e(t);
            }
        });
    }

    @Override
    public void getDeviceActionTypes(@NonNull final ListResultListener<DeviceActionTypeModel> listener) {
        Call<ListResultModel<DeviceActionTypeModel>> call =  mRestService.getActionTypes();
        call.enqueue(new Callback<ListResultModel<DeviceActionTypeModel>>() {
            @Override
            public void onResponse(Call<ListResultModel<DeviceActionTypeModel>> call, @NonNull Response<ListResultModel<DeviceActionTypeModel>> response) {
                Timber.d("getActionTypes response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body().getData());
                } else {
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<ListResultModel<DeviceActionTypeModel>> call, Throwable t) {
                Timber.e("getActionTypes error");
                listener.onRequestError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void getDeviceActions(@NonNull String deviceHid, @NonNull final ListResultListener<DeviceActionModel> listener) {
        mRestService.getActions(deviceHid).enqueue(new Callback<ListResultModel<DeviceActionModel>>() {
            @Override
            public void onResponse(Call<ListResultModel<DeviceActionModel>> call, @NonNull Response<ListResultModel<DeviceActionModel>> response) {
                Timber.d("getActions response");
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
        mRestService.postAction(deviceHid, action).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                Timber.d("getActionTypes response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.postActionSucceed(response.body());
                } else {
                    listener.postActionFailed(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                listener.postActionFailed(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void updateDeviceAction(@NonNull String deviceHid, int index, @NonNull DeviceActionModel model, @NonNull final UpdateDeviceActionListener listener) {
        mRestService.updateAction(deviceHid, index, model).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                Timber.d("getActionTypes response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onDeviceActionUpdated(response.body());
                } else {
                    listener.onDeviceActionUpdateFailed(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                listener.onDeviceActionUpdateFailed(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void getDeviceHistoricalEvents(@NonNull HistoricalEventsRequest request,
                                          @NonNull final PagingResultListener<DeviceEventModel> listener) {
        mRestService.getHistoricalEvents(request.getHid(),
                request.getCreatedDateFrom(),
                request.getCreatedDateTo(),
                request.getSortField(),
                request.getSortDirection(),
                request.getStatuses(),
                request.getSystemNames(),
                request.getPage(),
                request.getSize()).enqueue(new Callback<PagingResultModel<DeviceEventModel>>() {
            @Override
            public void onResponse(Call<PagingResultModel<DeviceEventModel>> call, @NonNull Response<PagingResultModel<DeviceEventModel>> response) {
                Timber.d("getHistoricalEvents response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body());
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
        Timber.d("regiterDevice() type: " + req.getType() + ", uid: " + req.getUid());
        mRestService.createOrUpdateDevice(req).enqueue(new Callback<DeviceRegistrationResponse>() {
            @Override
            public void onResponse(Call<DeviceRegistrationResponse> call, @NonNull final Response<DeviceRegistrationResponse> response) {
                Timber.d("createOrUpdateDevice response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onDeviceRegistered(response.body());
                } else {
                    listener.onDeviceRegistrationFailed(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<DeviceRegistrationResponse> call, Throwable t) {
                Timber.e("createOrUpdateDevice error");
                listener.onDeviceRegistrationFailed(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void registerReceivedEvent(String eventHid, final CommonRequestListener listener) {
        mRestService.putReceived(eventHid).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                Timber.d("registerReceivedEvent response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body());
                } else {
                    Timber.e("heartBeat error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Timber.e("registerReceivedEvent error");
                listener.onRequestError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void eventHandlingSucceed(@NonNull String eventHid, final CommonRequestListener listener) {
        mRestService.putSucceeded(eventHid).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                Timber.d("eventHandlingSucceed response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body());
                } else {
                    Timber.e("heartBeat error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Timber.e("eventHandlingSucceed error");
                listener.onRequestError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void eventHandlingFailed(@NonNull String eventHid, final CommonRequestListener listener) {
        mRestService.putFailed(eventHid).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                Timber.d("eventHandlingSucceed response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body());
                } else {
                    Timber.e("heartBeat error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Timber.e("eventHandlingSucceed error");
                listener.onRequestError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void findAllGateways(@NonNull final GetGatewaysListener listener) {
        mRestService.findAllGateways().enqueue(new Callback<List<GatewayModel>>() {
            @Override
            public void onResponse(Call<List<GatewayModel>> call, @NonNull Response<List<GatewayModel>> response) {
                Timber.d("findAllGateways response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onGatewaysReceived(response.body());
                } else {
                    Timber.e("findAllGateways error");
                    listener.onGatewaysFailed(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<List<GatewayModel>> call, Throwable t) {
                Timber.e("findAllGateways error");
                listener.onGatewaysFailed(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void registerGateway(@NonNull final GatewayModel gatewayModel, @NonNull final GatewayRegisterListener listener) {
        Timber.d("registerGateway(), uid: " + gatewayModel.getUid() +
                ", applicationHid: " + gatewayModel.getApplicationHid());
        mRestService.registerGateway(gatewayModel).enqueue(new Callback<GatewayResponse>() {
            @Override
            public void onResponse(Call<GatewayResponse> call, @NonNull final Response<GatewayResponse> response) {
                Timber.d("registerGateway response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    onGatewayResponse(response.body());
                    mGatewayUid = gatewayModel.getUid();
                    listener.onGatewayRegistered(response.body());

                } else {
                    Timber.e("registerGateway error");
                    listener.onGatewayRegisterFailed(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<GatewayResponse> call, Throwable t) {
                Timber.e("registerGateway error");
                listener.onGatewayRegisterFailed(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void findGateway(@NonNull String hid, @NonNull final FindGatewayListener listener) {
        mRestService.findGateway(hid).enqueue(new Callback<GatewayModel>() {
            @Override
            public void onResponse(Call<GatewayModel> call, @NonNull Response<GatewayModel> response) {
                Timber.d("findGateway response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onGatewayFound(response.body());
                } else {
                    Timber.e("findGateway error");
                    listener.onGatewayFindError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<GatewayModel> call, Throwable t) {
                Timber.e("findGateway error");
                listener.onGatewayFindError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void updateGateway(@NonNull String hid, @NonNull GatewayModel gatewayModel, @NonNull final GatewayUpdateListener listener) {
        mRestService.updateGateway(hid, gatewayModel).enqueue(new Callback<GatewayResponse>() {
            @Override
            public void onResponse(Call<GatewayResponse> call, @NonNull Response<GatewayResponse> response) {
                Timber.d("updateGateway response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onGatewayUpdated(response.body());
                } else {
                    Timber.e("updateGateway error");
                    listener.onGatewayUpdateFailed(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<GatewayResponse> call, Throwable t) {
                Timber.e("updateGateway error");
                listener.onGatewayUpdateFailed(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void checkinGateway(@NonNull String hid, @NonNull String gatewayUid, @NonNull final CheckinGatewayListener listener) {
        Timber.d("checkinGateway(), hid: " + hid);
        mGatewayUid = gatewayUid;
        mRestService.checkin(hid).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                Timber.d("checkin response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onCheckinGatewaySuccess();
                } else {
                    Timber.e("checkin error");
                    ApiError error = mRetrofitHolder.convertToApiError(response);
                    listener.onCheckinGatewayError(error);
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Timber.e("checkin error");
                listener.onCheckinGatewayError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void sendGatewayError(String hid, ErrorBodyModel error, final CommonRequestListener listener) {
        mRestService.sendGatewayError(hid, error).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                Timber.d("sendGatewayError response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body());
                } else {
                    Timber.e("sendGatewayError error");
                    ApiError error = mRetrofitHolder.convertToApiError(response);
                    listener.onRequestError(error);
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Timber.e("sendGatewayError error");
                listener.onRequestError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void sendCommandGateway(@NonNull String hid, @NonNull GatewayCommand command, @NonNull final GatewayCommandsListener listener) {
        mRestService.sendGatewayCommand(hid, command).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                Timber.d("sendGatewayCommand response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onGatewayCommandSent(response.body());
                } else {
                    Timber.e("sendGatewayCommand error");
                    listener.onGatewayCommandFailed(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Timber.e("sendGatewayCommand error");
                listener.onGatewayCommandFailed(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void getGatewayConfig(@NonNull final String hid, @NonNull final GetGatewayConfigListener listener) {
        Timber.d("getGatewayConfig() hid: " + hid);
        mRestService.getConfig(hid).enqueue(new Callback<ConfigResponse>() {
            @Override
            public void onResponse(Call<ConfigResponse> call, @NonNull final Response<ConfigResponse> response) {
                Timber.d("getConfig response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    if (mGatewayId == null) {
                        //this means that gateway id has been stored on a client side and we have
                        //to set it here
                        mGatewayId = hid;
                    }
                    onConfigResponse(response.body());
                    listener.onGatewayConfigReceived(response.body());
                } else {
                    Timber.e("getConfig error");
                    listener.onGatewayConfigFailed(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<ConfigResponse> call, Throwable t) {
                Timber.e("getConfig error");
                listener.onGatewayConfigFailed(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void getDevicesList(@NonNull String gatewayHid, @NonNull final ListResultListener<DeviceModel> listener) {
        Timber.d("getDevicesList() hid: " + gatewayHid);
        mRestService.getDevicesByGatewayHid(gatewayHid).enqueue(new Callback<ListResultModel<DeviceModel>>() {
            @Override
            public void onResponse(Call<ListResultModel<DeviceModel>> call, @NonNull Response<ListResultModel<DeviceModel>> response) {
                Timber.d("getDevicesList response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body().getData());
                } else {
                    Timber.e("getDevicesList error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<ListResultModel<DeviceModel>> call, Throwable t) {
                Timber.e("getDevicesList error");
                listener.onRequestError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void gatewayHeartbeat(@NonNull String hid, @NonNull final CommonRequestListener listener) {
        mRestService.heartBeat(hid).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                Timber.d("heartBeat response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body());
                } else {
                    Timber.e("heartBeat error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Timber.e("heartBeat error");
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
                Timber.d("getGatewayLogs response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body());
                } else {
                    Timber.e("getGatewayLogs error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<PagingResultModel<AuditLogModel>> call, Throwable t) {
                Timber.e("getGatewayLogs error");
                listener.onRequestError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void deleteDeviceAction(@NonNull String deviceHid, int index, @NonNull final DeleteDeviceActionListener listener) {
        mRestService.deleteAction(deviceHid, index).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                Timber.d("deleteAction response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onDeviceActionDeleted(response.body());
                } else {
                    Timber.e("deleteAction error");
                    listener.onDeviceActionDeleteFailed(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Timber.e("deleteAction error");
                listener.onDeviceActionDeleteFailed(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void findAllDevices(FindDevicesRequest request, @NonNull final PagingResultListener<DeviceModel> listener) {
        mRestService.findAllDevices(request.getUserHid(),
                request.getUid(),
                request.getType(),
                request.getGatewayHid(),
                request.getCreatedBefore(),
                request.getCreatedAfter(),
                request.getUpdatedBefore(),
                request.getUpdatedAfter(),
                request.getEnabled(),
                request.get_page(), request.get_size()).
                enqueue(new Callback<PagingResultModel<DeviceModel>>() {
                    @Override
                    public void onResponse(Call<PagingResultModel<DeviceModel>> call, @NonNull Response<PagingResultModel<DeviceModel>> response) {
                        Timber.d("deleteAction response");
                        if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                            listener.onRequestSuccess(response.body());
                        } else {
                            Timber.e("deleteAction error");
                            listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                        }
                    }

                    @Override
                    public void onFailure(Call<PagingResultModel<DeviceModel>> call, Throwable t) {
                        Timber.e("deleteAction error");
                        listener.onRequestError(ErrorUtils.parseError(t));
                    }
                });
    }

    @Override
    public void findDeviceByHid(@NonNull String deviceHid, @NonNull final FindDeviceListener listener) {
        mRestService.findDeviceByHid(deviceHid).enqueue(new Callback<DeviceModel>() {
            @Override
            public void onResponse(Call<DeviceModel> call, @NonNull Response<DeviceModel> response) {
                Timber.d("findDeviceByHid response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onDeviceFindSuccess(response.body());
                } else {
                    Timber.e("findDeviceByHid error");
                    listener.onDeviceFindFailed(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<DeviceModel> call, Throwable t) {
                Timber.e("findDeviceByHid error");
                listener.onDeviceFindFailed(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void updateDevice(@NonNull String deviceHid, @NonNull DeviceRegistrationModel device, @NonNull final CommonRequestListener listener) {
        mRestService.updateExistingDevice(deviceHid, device).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                Timber.d("updateExistingDevice response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body());
                } else {
                    Timber.e("updateExistingDevice error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Timber.e("updateExistingDevice error");
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
                Timber.d("getDeviceAuditLogs response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body());
                } else {
                    Timber.e("getDeviceAuditLogs error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<PagingResultModel<AuditLogModel>> call, Throwable t) {
                Timber.e("getDeviceAuditLogs error");
                listener.onRequestError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void sendDeviceError(String deviceHid, ErrorBodyModel error, final CommonRequestListener listener) {
        mRestService.sendDeviceError(deviceHid, error).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                Timber.d("sendDeviceError response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body());
                } else {
                    Timber.e("sendDeviceError error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Timber.e("sendDeviceError error");
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
                Timber.d("getNodesList response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body().getData());
                } else {
                    Timber.e("getNodesList error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<ListResultModel<NodeModel>> call, Throwable t) {
                Timber.e("getNodesList error");
                listener.onRequestError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void createNewNode(@NonNull NodeRegistrationModel node, @NonNull final CommonRequestListener listener) {
        mRestService.createNewNode(node).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                Timber.d("createNewNode response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body());
                } else {
                    Timber.e("createNewNode error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Timber.e("createNewNode error");
                listener.onRequestError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void updateExistingNode(@NonNull String nodeHid, NodeRegistrationModel node, @NonNull final CommonRequestListener listener) {
        mRestService.updateExistingNode(nodeHid, node).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                Timber.d("updateExistingNode response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body());
                } else {
                    Timber.e("updateExistingNode error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Timber.e("updateExistingNode error");
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
                Timber.d("getListNodeTypes response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onListNodeTypesSuccess(response.body());
                } else {
                    Timber.e("getListNodeTypes error");
                    listener.onListNodeTypesFiled(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<ListResultModel<NodeTypeModel>> call, Throwable t) {
                Timber.e("getListNodeTypes error");
                listener.onListNodeTypesFiled(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void createNewNodeType(@NonNull NodeTypeRegistrationModel nodeType, @NonNull final CommonRequestListener listener) {
        mRestService.createNewNodeType(nodeType).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                Timber.d("createNewNodeType response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body());
                } else {
                    Timber.e("createNewNodeType error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Timber.e("createNewNodeType error");
                listener.onRequestError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void updateExistingNodeType(@NonNull String hid, @NonNull NodeTypeRegistrationModel nodeType, @NonNull final CommonRequestListener listener) {
        mRestService.updateExistingNodeType(hid, nodeType).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                Timber.d("updateExistingNodeType response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body());
                } else {
                    Timber.e("updateExistingNodeType error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Timber.e("updateExistingNodeType error");
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
                Timber.d("getListDeviceTypes response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body().getData());
                } else {
                    Timber.e("getListDeviceTypes error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<ListResultModel<DeviceTypeModel>> call, Throwable t) {
                Timber.e("getListDeviceTypes error");
                listener.onRequestError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void createNewDeviceType(@NonNull DeviceTypeRegistrationModel deviceType, @NonNull final CommonRequestListener listener) {
        mRestService.createNewDeviceType(deviceType).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                Timber.d("createNewDeviceType response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body());
                } else {
                    Timber.e("createNewDeviceType error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Timber.e("createNewDeviceType error");
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
                Timber.d("updateExistingDeviceType response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body());
                } else {
                    Timber.e("updateExistingDeviceType error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Timber.e("updateExistingDeviceType error");
                listener.onRequestError(ErrorUtils.parseError(t));
            }
        });
    }

    //device-state-api

    @Override
    public void findDeviceState(String deviceHid, final FindDeviceStateListener listener) {
        mRestService.findDeviceState(deviceHid).enqueue(new Callback<FindDeviceStateResponse>() {
            @Override
            public void onResponse(Call<FindDeviceStateResponse> call, Response<FindDeviceStateResponse> response) {
                Timber.d("findDeviceState: ");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onDeviceStateSuccess(response.body());
                } else {
                    Timber.e("findDeviceState error");
                    listener.onDeviceStateError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<FindDeviceStateResponse> call, Throwable t) {
                Timber.e("findDeviceState error");
                listener.onDeviceStateError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void createNewDeviceStateTransaction(String hid,
                                                NewDeviceStateTransactionRequest request,
                                                final CommonRequestListener listener) {
        mRestService.createNewDeviceStateTransaction(hid, request).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                Timber.d("createNewDeviceStateTransaction: ok");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body());
                } else {
                    Timber.e("createNewDeviceStateTransaction error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Timber.e("createNewDeviceStateTransaction error");
                listener.onRequestError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void deviceStateTransactionSucceeded(String hid, String transHid, final MessageStatusListener listener) {
        mRestService.deviceStateTransactionSucceeded(hid, transHid).enqueue(new Callback<MessageStatusResponse>() {
            @Override
            public void onResponse(Call<MessageStatusResponse> call, Response<MessageStatusResponse> response) {
                Timber.d("deviceStateTransactionSucceeded: ok");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onResponse(response.body());
                } else {
                    Timber.e("deviceStateTransactionSucceeded error");
                    listener.onError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<MessageStatusResponse> call, Throwable t) {
                Timber.e("deviceStateTransactionSucceeded error");
                listener.onError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void deviceStateTransactionFailed(String hid, String transHid, ErrorBodyModel error,
                                             final MessageStatusListener listener) {
        mRestService.deviceStateTransactionFailed(hid, transHid, error).enqueue(new Callback<MessageStatusResponse>() {
            @Override
            public void onResponse(Call<MessageStatusResponse> call, Response<MessageStatusResponse> response) {
                Timber.d("deviceStateTransactionFailed: ok");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onResponse(response.body());
                } else {
                    Timber.e("deviceStateTransactionFailed error");
                    listener.onError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<MessageStatusResponse> call, Throwable t) {
                Timber.e("deviceStateTransactionFailed error");
                listener.onError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void deviceStateTransactionReceived(String hid, String transHid, final MessageStatusListener listener) {
        mRestService.deviceStateTransactionReceived(hid, transHid).enqueue(new Callback<MessageStatusResponse>() {
            @Override
            public void onResponse(Call<MessageStatusResponse> call, Response<MessageStatusResponse> response) {
                Timber.d("deviceStateTransactionReceived: ok");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onResponse(response.body());
                } else {
                    Timber.e("deviceStateTransactionReceived error");
                    listener.onError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<MessageStatusResponse> call, Throwable t) {
                Timber.e("deviceStateTransactionReceived error");
                listener.onError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void updateDeviceStateTransaction(String hid,
                                             NewDeviceStateTransactionRequest request,
                                             final CommonRequestListener listener) {
        mRestService.updateDeviceStateTransaction(hid, request).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                Timber.d("updateDeviceStateTransaction: ok");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body());
                } else {
                    Timber.e("updateDeviceStateTransaction error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Timber.e("updateDeviceStateTransaction error");
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
                Timber.d("findTelemetryByApplicationHid response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body());
                } else {
                    Timber.e("findTelemetryByApplicationHid error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<PagingResultModel<TelemetryItemModel>> call, Throwable t) {
                Timber.e("findTelemetryByApplicationHid error");
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
                Timber.d("findTelemetryByDeviceHid response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body());
                } else {
                    Timber.e("findTelemetryByDeviceHid error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<PagingResultModel<TelemetryItemModel>> call, Throwable t) {
                Timber.e("findTelemetryByDeviceHid error");
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
                Timber.d("findTelemetryByNodeHid response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body());
                } else {
                    Timber.e("findTelemetryByNodeHid error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<PagingResultModel<TelemetryItemModel>> call, Throwable t) {
                Timber.e("findTelemetryByNodeHid error");
                listener.onRequestError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void getLastTelemetry(String deviceHid, @NonNull final ListResultListener<TelemetryItemModel> listener) {
        mRestService.getLastTelemetry(deviceHid).enqueue(new Callback<ListResultModel<TelemetryItemModel>>() {
            @Override
            public void onResponse(Call<ListResultModel<TelemetryItemModel>> call, @NonNull Response<ListResultModel<TelemetryItemModel>> response) {
                Timber.d("getLastTelemetry response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body().getData());
                } else {
                    Timber.e("getLastTelemetry error");
                    listener.onRequestError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<ListResultModel<TelemetryItemModel>> call, Throwable t) {
                Timber.e("getLastTelemetry error");
                listener.onRequestError(ErrorUtils.parseError(t));
            }
        });
    }

    @Override
    public void getTelemetryItemsCount(TelemetryCountRequest request, final TelemetryCountListener listener) {
        mRestService.getTelemetryItemsCount(request.getDeviceHid(),
                request.getTelemetryName(),
                request.getFromTimestamp(),
                request.getToTimestamp()).enqueue(new Callback<TelemetryCountResponse>() {
            @Override
            public void onResponse(Call<TelemetryCountResponse> call, Response<TelemetryCountResponse> response) {
                Timber.d("getTelemetryItemsCount");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onTelemetryItemsCountSuccess(response.body());
                } else {
                    Timber.e("getTelemetryItemsCount error");
                    listener.onTelemetryItemsCountError(mRetrofitHolder.convertToApiError(response));
                }
            }

            @Override
            public void onFailure(Call<TelemetryCountResponse> call, Throwable t) {
                Timber.e("getTelemetryItemsCount error");
                listener.onTelemetryItemsCountError(ErrorUtils.parseError(t));
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


    @Override
    public RetrofitHolder getRetrofitHolder() {
        return mRetrofitHolder;
    }

    @Override
    public ConfigResponse getConfigResponse() {
        return mConfigResponse;
    }

    @Override
    public ServerCommandsListener getServerCommandsListener() {
        return mServerCommandsListener;
    }

    @Override
    public String getGatewayUid() {
        return mGatewayUid;
    }

    @Override
    public String getGatewayId() {
        return mGatewayId;
    }

    @Override
    public String getMqttHost() {
        return mMqttHost;
    }

    @Override
    public String getMqttPrefix() {
        return mMqttPrefix;
    }

    @Override
    public IotConnectAPIService getIotConnectApiService() {
        return mRestService;
    }
}
