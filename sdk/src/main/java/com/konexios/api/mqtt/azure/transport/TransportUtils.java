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

package com.konexios.api.mqtt.azure.transport;

public class TransportUtils {
    /**
     * Version identifier key
     */
    public static final String versionIdentifierKey = "com.microsoft:client-version";
    public static String javaServiceClientIdentifier = "com.microsoft.azure.sdk.iot.iot-service-client/";
    public static String serviceVersion = "1.2.18";

    public static String getJavaServiceClientIdentifier() {
        return javaServiceClientIdentifier;
    }

    public static String getServiceVersion() {
        return serviceVersion;
    }

}
