package com.arrow.kronos.api.listeners;

import com.arrow.kronos.api.models.GatewayResponse;

import static org.spongycastle.asn1.x500.style.RFC4519Style.o;

/**
 * Created by osminin on 10/7/2016.
 */

public interface GatewayCommandsListener {
    void onGatewayCommandSent(GatewayResponse response);
    void onGatewayCommandFailed();
}
