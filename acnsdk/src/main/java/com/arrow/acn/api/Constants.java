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

import okhttp3.MediaType;

/**
 * Created by osminin on 9/20/2016.
 */

public final class Constants {
    public interface Api {
        String X_ARROW_APIKEY = "x-arrow-apikey";
        String X_ARROW_DATE = "x-arrow-date";
        String X_ARROW_SIGNATURE = "x-arrow-signature";
        String X_ARROW_VERSION = "x-arrow-version";
        String X_ARROW_VERSION_1 = "1";
    }

    public static final String DEVICE_ID_KEY = "SynapS3_IotConnect_DeviceId";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
}
