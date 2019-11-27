/*
 * Copyright (c) 2017-2019 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *     Arrow Electronics, Inc.
 *     Konexios, Inc.
 */

package com.konexios.api;

import okhttp3.MediaType;

public final class Constants {
    public interface Api {
        String X_ARROW_APIKEY = "x-arrow-apikey";
        String X_ARROW_DATE = "x-arrow-date";
        String X_ARROW_SIGNATURE = "x-arrow-signature";
        String X_ARROW_VERSION = "x-arrow-version";
        String X_ARROW_VERSION_1 = "1";
        String X_ARROW_VERSION_2 = "2";

        String DEFAULT_ADMIN_API_URL = "https://admin-api.konexios.io";
        String DEFAULT_GATEWAY_API_URL = "https://api.konexios.io";
        String DEFAULT_MQTT_URL = "ssl://mqtt.konexios.io";
        String DEFAULT_MQTT_VIRTUAL_HOST = "/pegasus";
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
}
