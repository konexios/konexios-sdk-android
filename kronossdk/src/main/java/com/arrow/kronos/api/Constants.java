package com.arrow.kronos.api;

import okhttp3.MediaType;

/**
 * Created by osminin on 9/20/2016.
 */

public final class Constants {
    public final static String SOFTWARE_NAME = "JMyIotGateway";
    public final static int MAJOR = 0;
    public final static int MINOR = 8;

    public final static String SP_CLOUD_HERATBEAT_INTERVAL = "com.arrow.jmyiotgateway.cloud_service_heartbeat";

    // DEV
    public static final String BASE_IOT_CONNECT_URL_DEV = "http://pegasuskronos01-dev.cloudapp.net:28880";
    public static final String MQTT_CONNECT_URL_DEV = "tcp://pegasusqueue01-dev.cloudapp.net:46953";
    public static final String MQTT_CLIENT_PREFIX_DEV = "/themis.dev";

    // DEMO
    public static final String BASE_IOT_CONNECT_URL_DEMO = "https://api.arrowconnect.io";
    public static final String MQTT_CONNECT_URL_DEV_DEMO = "tcp://pegasus.arrowconnect.io:1883";
    public static final String MQTT_CLIENT_PREFIX_DEMO = "/pegasus";

    public static final String DEFAULT_API_KEY = "***REMOVED***";
    public static final String DEFAULT_API_SECRET = "***REMOVED***";

    public final static int HEART_BEAT_INTERVAL = 60; //1 minute

    public static class Preference {
        public final static String KEY_ACCOUNT_USER_ID = "account-user-id";
        public final static String KEY_GATEWAY_ID = "gateway-id";
    }

    public interface Api {
        String X_ARROW_APIKEY = "x-arrow-apikey";
        String X_ARROW_DATE = "x-arrow-date";
        String X_ARROW_SIGNATURE = "x-arrow-signature";
        String X_ARROW_VERSION = "x-arrow-version";
        String X_ARROW_VERSION_1 = "1";
    }

    public static final String EXTRA_DATA_LABEL_TELEMETRY = "telemetry_extra_data";
    public static final String DEVICE_ID_KEY = "SynapS3_IotConnect_DeviceId";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
}
