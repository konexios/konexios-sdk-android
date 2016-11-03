package com.arrow.kronos.api;

import android.os.Handler;
import android.util.Log;

import com.arrow.kronos.api.common.ApiRequestSigner;
import com.arrow.kronos.api.common.RetrofitHolder;
import com.arrow.kronos.api.listeners.CheckinGatewayListener;
import com.arrow.kronos.api.listeners.CommonRequestListener;
import com.arrow.kronos.api.listeners.DeleteDeviceActionListener;
import com.arrow.kronos.api.listeners.FindDeviceListener;
import com.arrow.kronos.api.listeners.FindGatewayListener;
import com.arrow.kronos.api.listeners.GatewayCommandsListener;
import com.arrow.kronos.api.listeners.GatewayRegisterListener;
import com.arrow.kronos.api.listeners.GatewayUpdateListener;
import com.arrow.kronos.api.listeners.GetGatewayConfigListener;
import com.arrow.kronos.api.listeners.GetGatewaysListener;
import com.arrow.kronos.api.listeners.ListNodeTypesListener;
import com.arrow.kronos.api.listeners.ListResultListener;
import com.arrow.kronos.api.listeners.PagingResultListener;
import com.arrow.kronos.api.listeners.PostDeviceActionListener;
import com.arrow.kronos.api.listeners.RegisterAccountListener;
import com.arrow.kronos.api.listeners.RegisterDeviceListener;
import com.arrow.kronos.api.listeners.ServerCommandsListener;
import com.arrow.kronos.api.listeners.UpdateDeviceActionListener;
import com.arrow.kronos.api.models.AccountRequest;
import com.arrow.kronos.api.models.AccountResponse;
import com.arrow.kronos.api.models.AuditLogModel;
import com.arrow.kronos.api.models.DeviceActionModel;
import com.arrow.kronos.api.models.CommonResponse;
import com.arrow.kronos.api.models.ConfigResponse;
import com.arrow.kronos.api.models.DeviceActionTypeModel;
import com.arrow.kronos.api.models.DeviceEventModel;
import com.arrow.kronos.api.models.DeviceModel;
import com.arrow.kronos.api.models.DeviceTypeModel;
import com.arrow.kronos.api.models.DeviceTypeRegistrationModel;
import com.arrow.kronos.api.models.FindTelemetryRequest;
import com.arrow.kronos.api.models.GatewayCommand;
import com.arrow.kronos.api.models.AuditLogsQuery;
import com.arrow.kronos.api.models.PagingResultModel;
import com.arrow.kronos.api.models.GatewayModel;
import com.arrow.kronos.api.models.GatewayResponse;
import com.arrow.kronos.api.models.DeviceRegistrationModel;
import com.arrow.kronos.api.models.ListResultModel;
import com.arrow.kronos.api.models.NodeModel;
import com.arrow.kronos.api.models.NodeRegistrationModel;
import com.arrow.kronos.api.models.NodeTypeModel;
import com.arrow.kronos.api.models.NodeTypeRegistrationModel;
import com.arrow.kronos.api.models.DeviceRegistrationResponse;
import com.arrow.kronos.api.models.TelemetryItemModel;
import com.arrow.kronos.api.models.TelemetryModel;
import com.arrow.kronos.api.mqtt.MqttKronosApiService;
import com.arrow.kronos.api.mqtt.aws.AwsKronosApiService;
import com.arrow.kronos.api.mqtt.ibm.IbmKronosApiService;
import com.arrow.kronos.api.rest.IotConnectAPIService;
import com.arrow.kronos.api.rest.RestApiKronosApiService;
import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;

import java.net.HttpURLConnection;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by osminin on 6/17/2016.
 */

class KronosApiImpl implements KronosApiService {
    private static final String TAG = KronosApiImpl.class.getName();

    private IotConnectAPIService mRestService;
    private Gson mGson = new Gson();
    protected Handler mServiceThreadHandler;
    protected String mGatewayId;
    private TelemetrySenderInterface mSenderService;

    private ServerCommandsListener mServerCommandsListener;
    private String mMqttHost;
    private String mMqttPrefix;
    private ConfigResponse mConfigResponse;

    protected Gson getGson() {
        return mGson;
    }

    @Override
    public void setRestEndpoint(String endpoint, String apiKey, String apiSecret) {
        RetrofitHolder.setApiKey(apiKey);
        RetrofitHolder.setApiSecret(apiSecret);
        mRestService = RetrofitHolder.getIotConnectAPIService(endpoint);
    }

    @Override
    public void setMqttEndpoint(String host, String prefix) {
        mMqttHost = host;
        mMqttPrefix = prefix;
    }

    @Override
    public void connect() {
        if (mConfigResponse == null) {
            FirebaseCrash.logcat(Log.ERROR, TAG, "connect() mConfigResponse is NULL");
            throw new RuntimeException("config() method must be called first!");
        }
        String cloud = mConfigResponse.getCloudPlatform();
        FirebaseCrash.logcat(Log.DEBUG, TAG, "connect() cloudPlatform: " + cloud);
        if (cloud.equalsIgnoreCase("IotConnect")) {
            mSenderService = new MqttKronosApiService(mMqttHost, mMqttPrefix, mGatewayId, mServerCommandsListener);
        } else if (cloud.equalsIgnoreCase("IBM")) {
            mSenderService = new IbmKronosApiService(mGatewayId, mConfigResponse);
        } else if (cloud.equalsIgnoreCase("AWS")) {
            mSenderService = new AwsKronosApiService(mGatewayId, mConfigResponse);
        } else {
            FirebaseCrash.logcat(Log.ERROR, TAG, "connect() invalid cloud platform: " + cloud);
            throw new RuntimeException("invalid cloud platform: " + cloud);
        }
        mSenderService.connect();
        FirebaseCrash.logcat(Log.DEBUG, TAG, "connect() done!");
    }

    @Override
    public void initialize(Handler handler) {
        mServiceThreadHandler = handler;
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

    protected void onGatewayResponse(GatewayResponse response) {
        mGatewayId = response.getHid();
    }

    protected void onConfigResponse(ConfigResponse response) {
        ConfigResponse.Key keys = response.getKey();
        if (keys != null) {
            ApiRequestSigner.getInstance().setSecretKey(keys.getSecretKey());
            ApiRequestSigner.getInstance().apiKey(keys.getApiKey());
        }
        mConfigResponse = response;
        FirebaseCrash.logcat(Log.DEBUG, TAG, "onConfigResponse() cloudPlatform: " + mConfigResponse.getCloudPlatform());
    }

    @Override
    public void setServerCommandsListener(ServerCommandsListener listener) {
        mServerCommandsListener = listener;
    }

    @Override
    public void registerAccount(AccountRequest accountRequest, final RegisterAccountListener listener) {
        FirebaseCrash.logcat(Log.DEBUG, TAG, "registerAccount() email: " + accountRequest.getEmail()
            + ", code: " + accountRequest.getCode());
        Call<AccountResponse> call = mRestService.registerAccount(accountRequest);
        call.enqueue(new Callback<AccountResponse>() {
            @Override
            public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                Log.v(TAG, "registerAccount: " + response.code());
                try {
                    if (response.body() != null && response.code() == HttpURLConnection.HTTP_OK) {
                        listener.onAccountRegistered(response.body());
                    } else {
                        String code = Integer.toString(response.code());
                        listener.onAccountRegisterFailed(code);
                        Log.v(TAG, "registerAccount " + code);
                    }
                } catch (Exception e) {
                    listener.onAccountRegisterFailed(e.toString());
                    e.printStackTrace();
                    FirebaseCrash.report(e);
                }
            }

            @Override
            public void onFailure(Call<AccountResponse> call, Throwable t) {
                Log.v(TAG, "onFailure: " + t.toString());
                listener.onAccountRegisterFailed(t.toString());
                FirebaseCrash.logcat(Log.ERROR, TAG, "postDelayed() failed");
                FirebaseCrash.report(t);
            }
        });
    }

    @Override
    public void getDeviceActionTypes(final ListResultListener<DeviceActionTypeModel> listener) {
        mRestService.getActionTypes().enqueue(new Callback<ListResultModel<DeviceActionTypeModel>>() {
            @Override
            public void onResponse(Call<ListResultModel<DeviceActionTypeModel>> call, Response<ListResultModel<DeviceActionTypeModel>> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "getActionTypes response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body().getData());
                } else {
                    listener.onRequestError();
                }
            }

            @Override
            public void onFailure(Call<ListResultModel<DeviceActionTypeModel>> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "getActionTypes error");
                listener.onRequestError();
            }
        });
    }

    @Override
    public void getDeviceActions(String deviceHid, final ListResultListener<DeviceActionModel> listener) {
        mRestService.getActions(deviceHid).enqueue(new Callback<ListResultModel<DeviceActionModel>>() {
            @Override
            public void onResponse(Call<ListResultModel<DeviceActionModel>> call, Response<ListResultModel<DeviceActionModel>> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "getActions response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body().getData());
                } else {
                    listener.onRequestError();
                }
            }

            @Override
            public void onFailure(Call<ListResultModel<DeviceActionModel>> call, Throwable t) {
                listener.onRequestError();
            }
        });
    }

    @Override
    public void postDeviceAction(String deviceHid, DeviceActionModel action, final PostDeviceActionListener listener) {
        mRestService.postAction(deviceHid, action).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "getActionTypes response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.postActionSucceed();
                } else {
                    listener.postActionFailed();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.postActionFailed();
            }
        });
    }

    @Override
    public void updateDeviceAction(String deviceHid, int index, DeviceActionModel model, final UpdateDeviceActionListener listener) {
        mRestService.updateAction(deviceHid, index, model).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "getActionTypes response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onDeviceActionUpdated();
                } else {
                    listener.onDeviceActionUpdateFailed();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onDeviceActionUpdateFailed();
            }
        });
    }

    @Override
    public void getDeviceHistoricalEvents(String deviceHid, final PagingResultListener<DeviceEventModel> listener) {
        mRestService.getHistoricalEvents(deviceHid).enqueue(new Callback<PagingResultModel<DeviceEventModel>>() {
            @Override
            public void onResponse(Call<PagingResultModel<DeviceEventModel>> call, Response<PagingResultModel<DeviceEventModel>> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "getHistoricalEvents response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body().getData());
                } else {
                    listener.onRequestError();
                }
            }

            @Override
            public void onFailure(Call<PagingResultModel<DeviceEventModel>> call, Throwable t) {
                listener.onRequestError();
            }
        });
    }

    @Override
    public void registerDevice(DeviceRegistrationModel req, final RegisterDeviceListener listener) {
        FirebaseCrash.logcat(Log.DEBUG, TAG, "regiterDevice() type: " + req.getType() + ", uid: " + req.getUid());
        mRestService.createOrUpdateDevice(req).enqueue(new Callback<DeviceRegistrationResponse>() {
            @Override
            public void onResponse(Call<DeviceRegistrationResponse> call, final Response<DeviceRegistrationResponse> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "createOrUpdateDevice response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onDeviceRegistered(response.body());
                } else {
                    listener.onDeviceRegistrationFailed();
                }
            }

            @Override
            public void onFailure(Call<DeviceRegistrationResponse> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "createOrUpdateDevice error");
                listener.onDeviceRegistrationFailed();
            }
        });
    }

    @Override
    public void registerReceivedEvent(String eventHid) {
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
    public void eventHandlingSucceed(String eventHid) {
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
    public void eventHandlingFailed(String eventHid) {
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
    public void findAllGateways(final GetGatewaysListener listener) {
        mRestService.findAllGateways().enqueue(new Callback<List<GatewayModel>>() {
            @Override
            public void onResponse(Call<List<GatewayModel>> call, Response<List<GatewayModel>> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "findAllGateways response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onGatewaysReceived(response.body());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "findAllGateways error");
                    listener.onGatewaysFailed();
                }
            }

            @Override
            public void onFailure(Call<List<GatewayModel>> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "findAllGateways error");
                listener.onGatewaysFailed();
            }
        });
    }

    @Override
    public void registerGateway(GatewayModel gatewayModel, final GatewayRegisterListener listener) {
        FirebaseCrash.logcat(Log.DEBUG, TAG, "registerGateway(), uid: " + gatewayModel.getUid() +
            ", applicationHid: " + gatewayModel.getApplicationHid());
        mRestService.registerGateway(gatewayModel).enqueue(new Callback<GatewayResponse>() {
            @Override
            public void onResponse(Call<GatewayResponse> call, final Response<GatewayResponse> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "registerGateway response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    final Handler uiHandler = new Handler();
                    final Runnable handleInUiThread = new Runnable() {
                        @Override
                        public void run() {
                            listener.onGatewayRegistered(response.body());
                        }
                    };
                    Runnable handleInServiceThread = new Runnable() {
                        @Override
                        public void run() {
                            onGatewayResponse(response.body());
                            uiHandler.post(handleInUiThread);
                        }
                    };
                    mServiceThreadHandler.post(handleInServiceThread);

                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "registerGateway error");
                    listener.onGatewayRegisterFailed();
                }
            }

            @Override
            public void onFailure(Call<GatewayResponse> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "registerGateway error");
                listener.onGatewayRegisterFailed();
            }
        });
    }

    @Override
    public void findGateway(String hid, final FindGatewayListener listener) {
        mRestService.findGateway(hid).enqueue(new Callback<GatewayModel>() {
            @Override
            public void onResponse(Call<GatewayModel> call, Response<GatewayModel> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "findGateway response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onGatewayFound(response.body());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "findGateway error");
                    listener.onGatewayFindError();
                }
            }

            @Override
            public void onFailure(Call<GatewayModel> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "findGateway error");
                listener.onGatewayFindError();
            }
        });
    }

    @Override
    public void updateGateway(String hid, GatewayModel gatewayModel, final GatewayUpdateListener listener) {
        mRestService.updateGateway(hid, gatewayModel).enqueue(new Callback<GatewayResponse>() {
            @Override
            public void onResponse(Call<GatewayResponse> call, Response<GatewayResponse> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "updateGateway response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onGatewayUpdated(response.body());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "updateGateway error");
                    listener.onGatewayUpdateFailed();
                }
            }

            @Override
            public void onFailure(Call<GatewayResponse> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "updateGateway error");
                listener.onGatewayUpdateFailed();
            }
        });
    }

    @Override
    public void checkinGateway(String hid, final CheckinGatewayListener listener) {
        FirebaseCrash.logcat(Log.DEBUG, TAG, "checkinGateway(), hid: " + hid);
        mRestService.checkin(hid).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "checkin response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onCheckinGatewaySuccess();
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "checkin error");
                    listener.onCheckinGatewayError();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "checkin error");
                listener.onCheckinGatewayError();
            }
        });
    }

    @Override
    public void sendCommandGateway(String hid, GatewayCommand command, final GatewayCommandsListener listener) {
        mRestService.sendGatewayCommand(hid, command).enqueue(new Callback<GatewayResponse>() {
            @Override
            public void onResponse(Call<GatewayResponse> call, Response<GatewayResponse> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "sendGatewayCommand response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onGatewayCommandSent(response.body());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "sendGatewayCommand error");
                    listener.onGatewayCommandFailed();
                }
            }

            @Override
            public void onFailure(Call<GatewayResponse> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "sendGatewayCommand error");
                listener.onGatewayCommandFailed();
            }
        });
    }

    @Override
    public void getGatewayConfig(final String hid, final GetGatewayConfigListener listener) {
        FirebaseCrash.logcat(Log.DEBUG, TAG, "getGatewayConfig() hid: " + hid);
        mRestService.getConfig(hid).enqueue(new Callback<ConfigResponse>() {
            @Override
            public void onResponse(Call<ConfigResponse> call, final Response<ConfigResponse> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "getConfig response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    if (mGatewayId == null) {
                        mGatewayId = hid;
                    }
                    onConfigResponse(response.body());
                    listener.onGatewayConfigReceived(response.body());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "getConfig error");
                    listener.onGatewayConfigFailed();
                }
            }

            @Override
            public void onFailure(Call<ConfigResponse> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "getConfig error");
                listener.onGatewayConfigFailed();
            }
        });
    }

    @Override
    public void gatewayHeartbeat(String hid, final CommonRequestListener listener) {
        mRestService.heartBeat(hid).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "heartBeat response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "heartBeat error");
                    listener.onRequestError();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "heartBeat error");
                listener.onRequestError();
            }
        });
    }

    @Override
    public void getGatewayLogs(String hid, AuditLogsQuery query, final PagingResultListener<AuditLogModel> listener) {
        mRestService.getGatewayLogs(hid, query.getCreatedDateFrom(), query.getCreatedDateTo(),
                query.getUserHids(), query.getTypes(), query.getSortField(), query.getSortDirection(),
                query.getPage(), query.getSize()).enqueue(new Callback<PagingResultModel<AuditLogModel>>() {
            @Override
            public void onResponse(Call<PagingResultModel<AuditLogModel>> call, Response<PagingResultModel<AuditLogModel>> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "getGatewayLogs response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body().getData());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "getGatewayLogs error");
                    listener.onRequestError();
                }
            }

            @Override
            public void onFailure(Call<PagingResultModel<AuditLogModel>> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "getGatewayLogs error");
                listener.onRequestError();
            }
        });
    }

    @Override
    public void deleteDeviceAction(String deviceHid, int index, final DeleteDeviceActionListener listener) {
        mRestService.deleteAction(deviceHid, index).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "deleteAction response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onDeviceActionDeleted();
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "deleteAction error");
                    listener.onDeviceActionDeleteFailed();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "deleteAction error");
                listener.onDeviceActionDeleteFailed();
            }
        });
    }

    @Override
    public void findAllDevices(String userHid, String uid, String type, String gatewayHid,
                               String enabled, int page, int size, final PagingResultListener<DeviceModel> listener) {
        mRestService.findAllDevices(userHid, uid, type, gatewayHid, enabled, page, size).
                enqueue(new Callback<PagingResultModel<DeviceModel>>() {
                    @Override
                    public void onResponse(Call<PagingResultModel<DeviceModel>> call, Response<PagingResultModel<DeviceModel>> response) {
                        FirebaseCrash.logcat(Log.DEBUG, TAG, "deleteAction response");
                        if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                            listener.onRequestSuccess(response.body().getData());
                        } else {
                            FirebaseCrash.logcat(Log.ERROR, TAG, "deleteAction error");
                            listener.onRequestError();
                        }
                    }

                    @Override
                    public void onFailure(Call<PagingResultModel<DeviceModel>> call, Throwable t) {
                        FirebaseCrash.logcat(Log.ERROR, TAG, "deleteAction error");
                        listener.onRequestError();
                    }
                });
    }

    @Override
    public void findDeviceByHid(String deviceHid, final FindDeviceListener listener) {
        mRestService.findDeviceByHid(deviceHid).enqueue(new Callback<DeviceModel>() {
            @Override
            public void onResponse(Call<DeviceModel> call, Response<DeviceModel> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "findDeviceByHid response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onDeviceFindSuccess(response.body());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "findDeviceByHid error");
                    listener.onDeviceFindFailed();
                }
            }

            @Override
            public void onFailure(Call<DeviceModel> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "findDeviceByHid error");
                listener.onDeviceFindFailed();
            }
        });
    }

    @Override
    public void updateDevice(String deviceHid, DeviceRegistrationModel device, final CommonRequestListener listener) {
        mRestService.updateExistingDevice(deviceHid, device).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "updateExistingDevice response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "updateExistingDevice error");
                    listener.onRequestError();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "updateExistingDevice error");
                listener.onRequestError();
            }
        });
    }

    @Override
    public void getDeviceAuditLogs(String deviceHid, AuditLogsQuery query, final PagingResultListener<AuditLogModel> listener) {
        mRestService.listDeviceAuditLogs(deviceHid, query.getCreatedDateFrom(), query.getCreatedDateTo(),
                query.getUserHids(), query.getTypes(), query.getSortField(), query.getSortDirection(),
                query.getPage(), query.getSize()).enqueue(new Callback<PagingResultModel<AuditLogModel>>() {
            @Override
            public void onResponse(Call<PagingResultModel<AuditLogModel>> call, Response<PagingResultModel<AuditLogModel>> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "getDeviceAuditLogs response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body().getData());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "getDeviceAuditLogs error");
                    listener.onRequestError();
                }
            }

            @Override
            public void onFailure(Call<PagingResultModel<AuditLogModel>> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "getDeviceAuditLogs error");
                listener.onRequestError();
            }
        });
    }

    //node api


    @Override
    public void getNodesList(final ListResultListener<NodeModel> listener) {
        mRestService.getListExistingNodes().enqueue(new Callback<ListResultModel<NodeModel>>() {
            @Override
            public void onResponse(Call<ListResultModel<NodeModel>> call, Response<ListResultModel<NodeModel>> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "getNodesList response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body().getData());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "getNodesList error");
                    listener.onRequestError();
                }
            }

            @Override
            public void onFailure(Call<ListResultModel<NodeModel>> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "getNodesList error");
                listener.onRequestError();
            }
        });
    }

    @Override
    public void createNewNode(NodeRegistrationModel node, final CommonRequestListener listener) {
        mRestService.createNewNode(node).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "createNewNode response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "createNewNode error");
                    listener.onRequestError();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "createNewNode error");
                listener.onRequestError();
            }
        });
    }

    @Override
    public void updateExistingNode(String nodeHid, NodeRegistrationModel node, final CommonRequestListener listener) {
        mRestService.updateExistingNode(nodeHid, node).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "updateExistingNode response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "updateExistingNode error");
                    listener.onRequestError();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "updateExistingNode error");
                listener.onRequestError();
            }
        });
    }

    // node - type api


    @Override
    public void getListNodeTypes(final ListNodeTypesListener listener) {
        mRestService.getListNodeTypes().enqueue(new Callback<ListResultModel<NodeTypeModel>>() {
            @Override
            public void onResponse(Call<ListResultModel<NodeTypeModel>> call, Response<ListResultModel<NodeTypeModel>> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "getListNodeTypes response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onListNodeTypesSuccess(response.body());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "getListNodeTypes error");
                    listener.onListNodeTypesFiled();
                }
            }

            @Override
            public void onFailure(Call<ListResultModel<NodeTypeModel>> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "getListNodeTypes error");
                listener.onListNodeTypesFiled();
            }
        });
    }

    @Override
    public void createNewNodeType(NodeTypeRegistrationModel nodeType, final CommonRequestListener listener) {
        mRestService.createNewNodeType(nodeType).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "createNewNodeType response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "createNewNodeType error");
                    listener.onRequestError();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "createNewNodeType error");
                listener.onRequestError();
            }
        });
    }

    @Override
    public void updateExistingNodeType(String hid, NodeTypeRegistrationModel nodeType, final CommonRequestListener listener) {
        mRestService.updateExistingNodeType(hid, nodeType).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "updateExistingNodeType response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "updateExistingNodeType error");
                    listener.onRequestError();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "updateExistingNodeType error");
                listener.onRequestError();
            }
        });
    }

    //device - type api

    @Override
    public void getListDeviceTypes(final ListResultListener<DeviceTypeModel> listener) {
        mRestService.getListDeviceTypes().enqueue(new Callback<ListResultModel<DeviceTypeModel>>() {
            @Override
            public void onResponse(Call<ListResultModel<DeviceTypeModel>> call, Response<ListResultModel<DeviceTypeModel>> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "getListDeviceTypes response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body().getData());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "getListDeviceTypes error");
                    listener.onRequestError();
                }
            }

            @Override
            public void onFailure(Call<ListResultModel<DeviceTypeModel>> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "getListDeviceTypes error");
                listener.onRequestError();
            }
        });
    }

    @Override
    public void createNewDeviceType(DeviceTypeRegistrationModel deviceType, final CommonRequestListener listener) {
        mRestService.createNewDeviceType(deviceType).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "createNewDeviceType response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "createNewDeviceType error");
                    listener.onRequestError();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "createNewDeviceType error");
                listener.onRequestError();
            }
        });
    }

    @Override
    public void updateExistingDeviceType(String hid, DeviceTypeRegistrationModel deviceType,
                                         final CommonRequestListener listener) {
        mRestService.updateExistingDeviceType(hid, deviceType).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "updateExistingDeviceType response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "updateExistingDeviceType error");
                    listener.onRequestError();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "updateExistingDeviceType error");
                listener.onRequestError();
            }
        });
    }

    //telemetry api

    @Override
    public void findTelemetryByApplicationHid(FindTelemetryRequest request, final PagingResultListener<TelemetryItemModel> listener) {
        mRestService.findTelemetryByAppHid(request.getHid(), request.getFromTimestamp(), request.getToTimestamp(),
                request.getTelemetryNames(), request.getPage(), request.getSize()).enqueue(new Callback<PagingResultModel<TelemetryItemModel>>() {
            @Override
            public void onResponse(Call<PagingResultModel<TelemetryItemModel>> call, Response<PagingResultModel<TelemetryItemModel>> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "findTelemetryByApplicationHid response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body().getData());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "findTelemetryByApplicationHid error");
                    listener.onRequestError();
                }
            }

            @Override
            public void onFailure(Call<PagingResultModel<TelemetryItemModel>> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "findTelemetryByApplicationHid error");
                listener.onRequestError();
            }
        });
    }

    @Override
    public void findTelemetryByDeviceHid(FindTelemetryRequest request, final PagingResultListener<TelemetryItemModel> listener) {
        mRestService.findTelemetryByDeviceHid(request.getHid(), request.getFromTimestamp(), request.getToTimestamp(),
                request.getTelemetryNames(), request.getPage(), request.getSize()).enqueue(new Callback<PagingResultModel<TelemetryItemModel>>() {
            @Override
            public void onResponse(Call<PagingResultModel<TelemetryItemModel>> call, Response<PagingResultModel<TelemetryItemModel>> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "findTelemetryByDeviceHid response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body().getData());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "findTelemetryByDeviceHid error");
                    listener.onRequestError();
                }
            }

            @Override
            public void onFailure(Call<PagingResultModel<TelemetryItemModel>> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "findTelemetryByDeviceHid error");
                listener.onRequestError();
            }
        });
    }

    @Override
    public void findTelemetryByNodeHid(FindTelemetryRequest request, final PagingResultListener<TelemetryItemModel> listener) {
        mRestService.findTelemetryByNodeHid(request.getHid(), request.getFromTimestamp(), request.getToTimestamp(),
                request.getTelemetryNames(), request.getPage(), request.getSize()).enqueue(new Callback<PagingResultModel<TelemetryItemModel>>() {
            @Override
            public void onResponse(Call<PagingResultModel<TelemetryItemModel>> call, Response<PagingResultModel<TelemetryItemModel>> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "findTelemetryByNodeHid response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onRequestSuccess(response.body().getData());
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "findTelemetryByNodeHid error");
                    listener.onRequestError();
                }
            }

            @Override
            public void onFailure(Call<PagingResultModel<TelemetryItemModel>> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "findTelemetryByNodeHid error");
                listener.onRequestError();
            }
        });
    }

    @Override
    public boolean hasBatchMode() {
        return mSenderService.hasBatchMode();
    }
}
