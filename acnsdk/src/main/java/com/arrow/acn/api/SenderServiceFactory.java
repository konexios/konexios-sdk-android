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

import com.arrow.acn.api.common.RetrofitHolder;
import com.arrow.acn.api.listeners.ServerCommandsListener;
import com.arrow.acn.api.models.ConfigResponse;

/**
 * Created by osminin on 4/5/2017.
 */

public interface SenderServiceFactory {
    TelemetrySenderInterface createTelemetrySender(RetrofitHolder retrofitHolder,
                                                   ConfigResponse configResponse,
                                                   String gatewayUid,
                                                   String gatewayId,
                                                   String mqttHost,
                                                   String mqttPrefix,
                                                   ServerCommandsListener serverCommandsListener);
}
