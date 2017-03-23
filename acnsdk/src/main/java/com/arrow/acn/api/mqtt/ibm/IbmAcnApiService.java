package com.arrow.acn.api.mqtt.ibm;

import android.support.annotation.NonNull;
import android.util.Log;

import com.arrow.acn.api.models.ConfigResponse;
import com.arrow.acn.api.mqtt.AbstractMqttAcnApiService;
import com.arrow.acn.api.mqtt.common.NoSSLv3SocketFactory;
import com.google.firebase.crash.FirebaseCrash;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

/**
 * Created by osminin on 9/2/2016.
 */

public final class IbmAcnApiService extends AbstractMqttAcnApiService {
    private static final String TAG = IbmAcnApiService.class.getName();

    private static final String IOT_ORGANIZATION_SSL = ".messaging.internetofthings.ibmcloud.com:8883";
    private static final String IOT_DEVICE_USERNAME = "use-token-auth";

    public IbmAcnApiService(String gatewayId, ConfigResponse configResponse) {
        super(gatewayId, configResponse);
    }

    @Override
    protected String getPublisherTopic(String deviceType, String externalId) {
        return String.format("iot-2/type/%s/id/%s/evt/telemetry/fmt/json", deviceType, externalId);
    }

    @Override
    protected MqttConnectOptions getMqttOptions() {
        FirebaseCrash.logcat(Log.DEBUG, TAG, "getMqttOptions");
        MqttConnectOptions options = super.getMqttOptions();
        options.setCleanSession(true);
        options.setUserName(IOT_DEVICE_USERNAME);
        options.setPassword(this.mConfigResponse.getIbm().getAuthToken().toCharArray());
        try {
            options.setSocketFactory(new NoSSLv3SocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrash.report(e);
        }
        return options;
    }

    @NonNull
    @Override
    protected String getHost() {
        FirebaseCrash.logcat(Log.DEBUG, TAG, "getHost");
        return "ssl://" + this.mConfigResponse.getIbm().getOrganicationId() + IOT_ORGANIZATION_SSL;
    }

    @NonNull
    @Override
    protected String getClientId() {
        ConfigResponse.Ibm ibm = this.mConfigResponse.getIbm();
        return "g:" + ibm.getOrganicationId() + ":" + ibm.getGatewayType() + ":" + ibm.getGatewayId();
    }

    @Override
    public boolean hasBatchMode() {
        return false;
    }
}
