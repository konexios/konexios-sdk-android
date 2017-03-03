package com.arrow.acn.api.listeners;


import com.arrow.acn.api.models.GatewayEventModel;

/**
 * Created by osminin on 9/21/2016.
 */

public interface ServerCommandsListener {
    void onServerCommand(GatewayEventModel model);
}
