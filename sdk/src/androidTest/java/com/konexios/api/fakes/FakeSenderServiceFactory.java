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

package com.konexios.api.fakes;

import com.konexios.api.SenderServiceArgsProvider;
import com.konexios.api.SenderServiceFactory;
import com.konexios.api.TelemetrySenderInterface;

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
