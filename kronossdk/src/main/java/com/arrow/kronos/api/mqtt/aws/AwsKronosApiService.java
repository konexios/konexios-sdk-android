package com.arrow.kronos.api.mqtt.aws;

import android.util.Log;

import com.arrow.kronos.api.models.ConfigResponse;
import com.arrow.kronos.api.mqtt.AbstractMqttKronosApiService;
import com.arrow.kronos.api.mqtt.common.SslUtil;
import com.google.firebase.crash.FirebaseCrash;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

import java.net.HttpURLConnection;

import retrofit2.Response;

/**
 * Created by osminin on 6/20/2016.
 */

public final class AwsKronosApiService extends AbstractMqttKronosApiService {
    private static final String TAG = AwsKronosApiService.class.getName();

    private ConfigResponse.Aws mAws;

    @Override
    protected MqttConnectOptions getMqttOptions() {
        FirebaseCrash.logcat(Log.DEBUG, TAG, "getMqttOptions");
        MqttConnectOptions options = super.getMqttOptions();
        String rootCert = mAws.getCaCert();
        String clientCert = mAws.getClientCert();
        String privateKey = mAws.getPrivateKey();
        try {
            options.setSocketFactory(SslUtil.getSocketFactory(rootCert, clientCert, privateKey));
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrash.report(e);
        }
        return options;
    }

    @Override
    protected String getPublisherTopic() {
        FirebaseCrash.logcat(Log.DEBUG, TAG, "getPublisherTopic");
        return String.format("telemetries/devices/%s", mGatewayHid);
    }

    @Override
    protected String getHost() {
        FirebaseCrash.logcat(Log.DEBUG, TAG, "getHost");
        return "ssl://".concat(mAws.getHost());
    }

    @Override
    public boolean hasBatchMode() {
        return false;
    }

    @Override
    protected void onConfigResponse(ConfigResponse response) {
        super.onConfigResponse(response);
        if (response.getAws() != null) {
            mAws = response.getAws();
        }
        connectMqtt();
    }
}
