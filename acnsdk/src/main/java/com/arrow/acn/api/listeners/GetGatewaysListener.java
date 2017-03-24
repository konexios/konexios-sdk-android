/*
 * Copyright (c) 2017 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors: Arrow Electronics, Inc.
 */

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
