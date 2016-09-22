package com.kronossdk.api.mqtt.aws;

import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.kronossdk.api.models.ConfigResponse;
import com.kronossdk.api.mqtt.AbstractMqttKronosApiService;
import com.kronossdk.api.mqtt.common.SslUtil;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

/**
 * Created by osminin on 6/20/2016.
 */

public final class AwsKronosApiService extends AbstractMqttKronosApiService {
    private static final String TAG = AwsKronosApiService.class.getName();

    private ConfigResponse.Aws mAws;

    @Override
    public void connect() {
        FirebaseCrash.logcat(Log.DEBUG, TAG, "connect");
        registerGateway(this);
    }

    @Override
    public void onGatewayRegistered(String gatewayHid) {
        FirebaseCrash.logcat(Log.DEBUG, TAG, "onGatewayRegistered gatewayHid: " + gatewayHid);
        mGatewayHid = gatewayHid;
    }

    @Override
    public void onGatewayRegistered(ConfigResponse response) {
        FirebaseCrash.logcat(Log.DEBUG, TAG, "onGatewayRegistered");
        if (response.getAws() != null) {
            mAws = response.getAws();
        }
        connectMqtt();
    }

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
}
