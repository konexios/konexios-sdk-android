package com.arrow.kronos.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.arrow.kronos.api.common.ApiRequestSigner;
import com.arrow.kronos.api.common.RetrofitHolder;
import com.arrow.kronos.api.listeners.CheckinGatewayListener;
import com.arrow.kronos.api.listeners.CommonRequestListener;
import com.arrow.kronos.api.listeners.DeleteDeviceActionListener;
import com.arrow.kronos.api.listeners.FindDeviceListener;
import com.arrow.kronos.api.listeners.FindDevicesListener;
import com.arrow.kronos.api.listeners.FindGatewayListener;
import com.arrow.kronos.api.listeners.GatewayCommandsListener;
import com.arrow.kronos.api.listeners.GatewayHeartbeatListener;
import com.arrow.kronos.api.listeners.GatewayUpdateListener;
import com.arrow.kronos.api.listeners.GetGatewayConfigListener;
import com.arrow.kronos.api.listeners.GetAuditLogsListener;
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
import com.arrow.kronos.api.models.GatewayCommand;
import com.arrow.kronos.api.models.AuditLogsQuery;
import com.arrow.kronos.api.models.PagingResultModel;
import com.arrow.kronos.api.models.GatewayModel;
import com.arrow.kronos.api.models.GatewayResponse;
import com.arrow.kronos.api.models.GatewayType;
import com.arrow.kronos.api.models.DeviceRegistrationModel;
import com.arrow.kronos.api.models.ListResultModel;
import com.arrow.kronos.api.models.NodeModel;
import com.arrow.kronos.api.models.NodeRegistrationModel;
import com.arrow.kronos.api.models.NodeTypeModel;
import com.arrow.kronos.api.models.NodeTypeRegistrationModel;
import com.arrow.kronos.api.rest.IotConnectAPIService;
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

public abstract class AbstractKronosApiService implements KronosApiService {
    private static final String TAG = AbstractKronosApiService.class.getName();

    private Context mContext;
    private IotConnectAPIService mService;
    private Gson mGson = new Gson();
    protected Handler mServiceThreadHandler;
    private InternalGatewayRegisterListener mGatewayRegisterListener;
    private String mGatewayId;
    protected ServerCommandsListener mServerCommandsListener;
    private String mApiKey;
    private String mApiSecret;

    private Callback<GatewayResponse> mGatewayResponseCallback = new Callback<GatewayResponse>() {
        @Override
        public void onResponse(Call<GatewayResponse> call, final Response<GatewayResponse> response) {
            mServiceThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    onGatewayResponse(response);
                }
            });
        }

        @Override
        public void onFailure(Call<GatewayResponse> call, Throwable t) {
            FirebaseCrash.report(t);
        }
    };

    private Callback<ConfigResponse> mConfigResponseCallback = new Callback<ConfigResponse>() {
        @Override
        public void onResponse(Call<ConfigResponse> call, final Response<ConfigResponse> response) {
            mServiceThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    onConfigResponse(response);
                }
            });
        }

        @Override
        public void onFailure(Call<ConfigResponse> call, Throwable t) {
            FirebaseCrash.report(t);
        }
    };

    private Runnable mHeartBeatTask = new Runnable() {

        @Override
        public void run() {
            Call<ResponseBody> request = mService.heartBeat(mGatewayId);
            request.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    FirebaseCrash.logcat(Log.VERBOSE, TAG, "heartBeat onResponse: " + response.code());
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    FirebaseCrash.logcat(Log.VERBOSE, TAG, "heartBeat onFailure");
                    FirebaseCrash.report(t);
                }
            });
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
            int heartBeatInterval = sp.getInt(Constants.SP_CLOUD_HERATBEAT_INTERVAL, Constants.HEART_BEAT_INTERVAL);
            mServiceThreadHandler.postDelayed(mHeartBeatTask, heartBeatInterval * 1000L);
        }
    };

    protected IotConnectAPIService getService() {
        return mService;
    }

    protected Gson getGson() {
        return mGson;
    }

    protected String getApiKey() {
        return mApiKey;
    }

    protected String getApiSecret() {
        return mApiSecret;
    }

    @Override
    public void setRestEndpoint(String endpoint, String apiKey, String apiSecret) {
        mApiKey = apiKey;
        mApiSecret = apiSecret;
        RetrofitHolder.setApiKey(apiKey);
        RetrofitHolder.setApiSecret(apiSecret);
        mService = RetrofitHolder.getIotConnectAPIService(endpoint);
    }

    @Override
    public void initialize(Context context) {
        mContext = context;
        mServiceThreadHandler = new Handler();
    }

    protected final void registerGateway(final InternalGatewayRegisterListener listener) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        mGatewayRegisterListener = listener;
        String gatewayHid = prefs.getString(Constants.Preference.KEY_GATEWAY_ID, null);
        if (TextUtils.isEmpty(gatewayHid)) {
            String uid = Settings.Secure.getString(mContext.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            FirebaseCrash.logcat(Log.DEBUG, TAG, "registerGateway() UID: " + uid);

            String name = String.format("%s %s", Build.MANUFACTURER, Build.MODEL);
            String osName = String.format("Android %s", Build.VERSION.RELEASE);
            String swName = Constants.SOFTWARE_NAME;
            String userHid = prefs.getString(Constants.Preference.KEY_ACCOUNT_USER_ID, null);

            GatewayModel gatewayModel = new GatewayModel();
            gatewayModel.setName(name);
            gatewayModel.setOsName(osName);
            gatewayModel.setSoftwareName(swName);
            gatewayModel.setUid(uid);
            gatewayModel.setType(GatewayType.Mobile);
            gatewayModel.setUserHid(userHid);
            gatewayModel.setSoftwareVersion(
                    String.format("%d.%d", Constants.MAJOR, Constants.MINOR));

            Call<GatewayResponse> call = mService.registerGateway(gatewayModel);
            call.enqueue(mGatewayResponseCallback);
        } else {
            Call<Void> call = mService.checkin(gatewayHid);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    FirebaseCrash.logcat(Log.VERBOSE, TAG, "checkin onResponse: " + response.code());
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    FirebaseCrash.logcat(Log.VERBOSE, TAG, "checkin onFailure");
                    FirebaseCrash.report(t);
                }
            });
            getConfig(gatewayHid, listener);
        }
    }

    private void getConfig(final String gatewayId, final InternalGatewayRegisterListener listener) {
        Call<ConfigResponse> call = mService.getConfig(gatewayId);
        mGatewayRegisterListener = listener;
        mGatewayId = gatewayId;
        call.enqueue(mConfigResponseCallback);
    }

    private void onGatewayResponse(Response<GatewayResponse> response) {
        FirebaseCrash.logcat(Log.VERBOSE, TAG, "registerGateway() onResponse: " + response.code());
        if (response.body() != null && response.code() == HttpURLConnection.HTTP_OK) {
            final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
            prefs.edit().putString(Constants.Preference.KEY_GATEWAY_ID, response.body().getHid()).commit();
            String gatewayHid = response.body().getHid();
            getConfig(gatewayHid, mGatewayRegisterListener);
            Call<Void> call = mService.checkin(gatewayHid);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    FirebaseCrash.logcat(Log.VERBOSE, TAG, "checkin onResponse: " + response.code());
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    FirebaseCrash.logcat(Log.VERBOSE, TAG, "checkin onFailure");
                    FirebaseCrash.report(t);
                }
            });
        } else {
            FirebaseCrash.logcat(Log.ERROR, TAG, "Gateway registration failed: " + response.code());
        }
    }

    private void onConfigResponse(Response<ConfigResponse> response) {
        FirebaseCrash.logcat(Log.VERBOSE, TAG, "getConfig onResponse: " + response.code());
        if (response.body() != null && response.code() == HttpURLConnection.HTTP_OK) {
            ConfigResponse.Key keys = response.body().getKey();
            if (keys != null) {
                ApiRequestSigner.getInstance().setSecretKey(keys.getSecretKey());
                ApiRequestSigner.getInstance().apiKey(keys.getApiKey());
                if (mGatewayRegisterListener != null) {
                    mGatewayRegisterListener.onGatewayRegistered(mGatewayId);
                }
            }
            ConfigResponse.Aws awsData = response.body().getAws();
            ConfigResponse.Ibm ibmData = response.body().getIbm();
            if (awsData != null || ibmData != null) {
                if (mGatewayRegisterListener != null) {
                    mGatewayRegisterListener.onGatewayRegistered(response.body());
                }
            }
            startSendHeartBeat();
        }
    }

    private void startSendHeartBeat() {
        mServiceThreadHandler.post(mHeartBeatTask);
    }

    protected String formatBatchPayload(List<Bundle> telemetry) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (Bundle bundle : telemetry) {
            String json = bundle.getString(Constants.EXTRA_DATA_LABEL_TELEMETRY);
            builder.append(json).append(",");
        }
        builder.replace(builder.length() - 1, builder.length(), "").append("]");
        return builder.toString();
    }

    @Override
    public void setServerCommandsListener(ServerCommandsListener listener) {
        mServerCommandsListener = listener;
    }

    @Override
    public void registerAccount(AccountRequest accountRequest, final RegisterAccountListener listener) {
        Call<AccountResponse> call = mService.registerAccount(accountRequest);
        call.enqueue(new Callback<AccountResponse>() {
            @Override
            public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                Log.v(TAG, "onResponse: " + response.code());
                try {
                    if (response.body() != null && response.code() == HttpURLConnection.HTTP_OK) {
                        listener.onAccountRegistered(response.body());
                    } else {
                        String code = Integer.toString(response.code());
                        listener.onAccountRegisterFailed(code);
                        Log.v(TAG, "data sent to cloud: " + code);
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
        mService.getActionTypes().enqueue(new Callback<ListResultModel<DeviceActionTypeModel>>() {
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
        mService.getActions(deviceHid).enqueue(new Callback<ListResultModel<DeviceActionModel>>() {
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
        mService.postAction(deviceHid, action).enqueue(new Callback<ResponseBody>() {
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
        mService.updateAction(deviceHid, index, model).enqueue(new Callback<ResponseBody>() {
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
        mService.getHistoricalEvents(deviceHid).enqueue(new Callback<PagingResultModel<DeviceEventModel>>() {
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
        mService.createOrUpdateDevice(req).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "createOrUpdateDevice response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onDeviceRegistered(response.body());
                } else {
                    listener.onDeviceRegistrationFailed();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "createOrUpdateDevice error");
                listener.onDeviceRegistrationFailed();
            }
        });
    }

    @Override
    public void registerReceivedEvent(String eventHid) {
        mService.putReceived(eventHid).enqueue(new Callback<ResponseBody>() {
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
        mService.putSucceeded(eventHid).enqueue(new Callback<ResponseBody>() {
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
        mService.putFailed(eventHid).enqueue(new Callback<ResponseBody>() {
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
        mService.findAllGateways().enqueue(new Callback<List<GatewayModel>>() {
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
    public void registerGateway(GatewayModel gatewayModel, final com.arrow.kronos.api.listeners.GatewayRegisterListener listener) {
        mService.registerGateway(gatewayModel).enqueue(new Callback<GatewayResponse>() {
            @Override
            public void onResponse(Call<GatewayResponse> call, Response<GatewayResponse> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "registerGateway response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onGatewayRegistered(response.body());
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
        mService.findGateway(hid).enqueue(new Callback<GatewayModel>() {
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
        mService.updateGateway(hid, gatewayModel).enqueue(new Callback<GatewayResponse>() {
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
        mService.checkin(hid).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "checkin response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onCheckinGatewaySuccess();
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "checkin error");
                    listener.onCheckinGatewayError();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "checkin error");
                listener.onCheckinGatewayError();
            }
        });
    }

    @Override
    public void sendCommandGateway(String hid, GatewayCommand command, final GatewayCommandsListener listener) {
        mService.sendGatewayCommand(hid, command).enqueue(new Callback<GatewayResponse>() {
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
    public void getGatewayConfig(String hid, final GetGatewayConfigListener listener) {
        mService.getConfig(hid).enqueue(new Callback<ConfigResponse>() {
            @Override
            public void onResponse(Call<ConfigResponse> call, Response<ConfigResponse> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "getConfig response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
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
    public void gatewayHeartbeat(String hid, final GatewayHeartbeatListener listener) {
        mService.heartBeat(hid).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "heartBeat response");
                if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    listener.onGatewayHeartbeatSuccess();
                } else {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "heartBeat error");
                    listener.onGatewayHeartbeatFailed();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "heartBeat error");
                listener.onGatewayHeartbeatFailed();
            }
        });
    }

    @Override
    public void getGatewayLogs(String hid, AuditLogsQuery query, final PagingResultListener<AuditLogModel> listener) {
        mService.getGatewayLogs(hid, query.getCreatedDateFrom(), query.getCreatedDateTo(),
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
        mService.deleteAction(deviceHid, index).enqueue(new Callback<ResponseBody>() {
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
        mService.findAllDevices(userHid, uid, type, gatewayHid, enabled, page, size).
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
        mService.findDeviceByHid(deviceHid).enqueue(new Callback<DeviceModel>() {
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
        mService.updateExistingDevice(deviceHid, device).enqueue(new Callback<CommonResponse>() {
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
        mService.listDeviceAuditLogs(deviceHid, query.getCreatedDateFrom(), query.getCreatedDateTo(),
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
        mService.getListExistingNodes().enqueue(new Callback<ListResultModel<NodeModel>>() {
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
        mService.createNewNode(node).enqueue(new Callback<CommonResponse>() {
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
        mService.updateExistingNode(nodeHid, node).enqueue(new Callback<CommonResponse>() {
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
        mService.getListNodeTypes().enqueue(new Callback<ListResultModel<NodeTypeModel>>() {
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
        mService.createNewNodeType(nodeType).enqueue(new Callback<CommonResponse>() {
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
        mService.updateExistingNodeType(hid, nodeType).enqueue(new Callback<CommonResponse>() {
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
        mService.getListDeviceTypes().enqueue(new Callback<ListResultModel<DeviceTypeModel>>() {
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
        mService.createNewDeviceType(deviceType).enqueue(new Callback<CommonResponse>() {
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
        mService.updateExistingDeviceType(hid, deviceType).enqueue(new Callback<CommonResponse>() {
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

    public interface InternalGatewayRegisterListener {
        void onGatewayRegistered(String gatewayHid);

        void onGatewayRegistered(ConfigResponse aws);
    }
}
