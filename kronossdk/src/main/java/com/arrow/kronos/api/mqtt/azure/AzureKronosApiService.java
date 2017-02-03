package com.arrow.kronos.api.mqtt.azure;

import android.util.Log;

import com.arrow.kronos.api.mqtt.AbstractMqttKronosApiService;
import com.google.firebase.crash.FirebaseCrash;
import com.microsoft.azure.sdk.iot.device.DeviceClientConfig;
import com.microsoft.azure.sdk.iot.device.auth.IotHubSasToken;
import com.microsoft.azure.sdk.iot.device.transport.TransportUtils;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;

/**
 * Created by osminin on 2/2/2017.
 */

public final class AzureKronosApiService extends AbstractMqttKronosApiService {
    private static final String TAG = AzureKronosApiService.class.getName();

    private static String sslPrefix = "ssl://";
    private static String sslPortSuffix = ":8883";

    private final String mAccessKey;
    private final String mHost;

    public AzureKronosApiService(String gatewayUid, String accessKey, String host) {
        super(gatewayUid);
        mAccessKey = accessKey;
        mHost = host;
    }

    @Override
    protected String getPublisherTopic(String deviceType, String externalId) {
        return "devices/" + mGatewayId + "/messages/events/";
    }

    @Override
    protected String getHost() {
        return sslPrefix + mHost + sslPortSuffix;
    }

    @Override
    public boolean hasBatchMode() {
        return true;
    }

    @Override
    protected MqttConnectOptions getMqttOptions() {
        MqttConnectOptions options = super.getMqttOptions();
        try {
            DeviceClientConfig config = new DeviceClientConfig(mHost, mGatewayId, mAccessKey, null);
            IotHubSasToken sasToken = new IotHubSasToken(config, System.currentTimeMillis() / 1000l +
                    config.getTokenValidSecs() + 1l);
            options.setCleanSession(false);
            String clientIdentifier = "DeviceClientType="
                    + URLEncoder.encode(TransportUtils.javaDeviceClientIdentifier
                    + TransportUtils.clientVersion, "UTF-8");
            String iotHubUserName = config.getIotHubHostname() + "/" + config.getDeviceId() + "/" + clientIdentifier;
            options.setUserName(iotHubUserName);
            options.setPassword(sasToken.toString().toCharArray());
        } catch (URISyntaxException e) {
            reportError(e);
        } catch (UnsupportedEncodingException e) {
            reportError(e);
        }
        return options;
    }

    @Override
    protected String getClientId() {
        return mGatewayId;
    }

    @Override
    protected String getSubscribeTopic() {
        return "devices/" + mGatewayId + "/messages/devicebound/#";
    }

    private void reportError(Exception e) {
        FirebaseCrash.logcat(Log.ERROR, TAG, e.getClass().getName() + " " + e.getMessage());
        FirebaseCrash.report(e);
    }
}
