/*
 * Copyright (c) 2017 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors: Arrow Electronics, Inc.
 */

package com.arrow.acn.api.fakes;

import com.arrow.acn.api.SenderServiceFactory;
import com.arrow.acn.api.TelemetrySenderInterface;
import com.arrow.acn.api.common.RetrofitHolder;
import com.arrow.acn.api.listeners.ServerCommandsListener;
import com.arrow.acn.api.models.ConfigResponse;

/**
 * Created by osminin on 4/5/2017.
 */

public final class FakeSenderServiceFactory implements SenderServiceFactory {

    @Override
    public TelemetrySenderInterface createTelemetrySender(RetrofitHolder retrofitHolder,
                                                          ConfigResponse configResponse,
                                                          String gatewayUid, String gatewayId,
                                                          String mqttHost, String mqttPrefix,
                                                          ServerCommandsListener serverCommandsListener) {
        TelemetrySenderInterface sender = new FakeTelemetrySender(retrofitHolder, configResponse, gatewayUid, gatewayId,
                mqttHost, mqttPrefix, serverCommandsListener);
        return sender;
    }
}
