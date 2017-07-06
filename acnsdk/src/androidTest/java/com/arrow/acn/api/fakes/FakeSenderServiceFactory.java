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

import com.arrow.acn.api.SenderServiceArgsProvider;
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
    public TelemetrySenderInterface createTelemetrySender(SenderServiceArgsProvider provider) {
        TelemetrySenderInterface sender = new FakeTelemetrySender(provider.getRetrofitHolder(),
                provider.getConfigResponse(),
                provider.getGatewayUid(),
                provider.getGatewayId(),
                provider.getMqttHost(),
                provider.getMqttPrefix(),
                provider.getServerCommandsListener());
        return sender;
    }
}
