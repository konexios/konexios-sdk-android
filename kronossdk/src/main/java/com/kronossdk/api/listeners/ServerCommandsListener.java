package com.kronossdk.api.listeners;

import com.kronossdk.api.models.GatewayEventModel;

/**
 * Created by osminin on 9/21/2016.
 */

public interface ServerCommandsListener {
    void onServerCommand(GatewayEventModel model);
}
