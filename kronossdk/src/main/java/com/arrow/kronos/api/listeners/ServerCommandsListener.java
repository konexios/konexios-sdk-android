package com.arrow.kronos.api.listeners;


import com.arrow.kronos.api.models.GatewayEventModel;

/**
 * Created by osminin on 9/21/2016.
 */

public interface ServerCommandsListener {
    void onServerCommand(GatewayEventModel model);
}
