package com.arrow.acn.api.listeners;

import com.arrow.acn.api.models.ApiError;
import com.arrow.acn.api.models.GatewayModel;

import java.util.List;

/**
 * Created by osminin on 10/6/2016.
 */

public interface GetGatewaysListener {
    void onGatewaysReceived(List<GatewayModel> response);
    void onGatewaysFailed(ApiError error);
}
