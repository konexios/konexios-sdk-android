package com.arrow.kronos.api.listeners;

import com.arrow.kronos.api.models.GatewayModel;
import com.arrow.kronos.api.models.GatewayResponse;

import java.util.List;

/**
 * Created by osminin on 10/6/2016.
 */

public interface GetGatewaysListener {
    void onGatewaysReceived(List<GatewayModel> response);
    void onGatewaysFailed();
}
