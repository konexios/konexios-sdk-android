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

package com.konexios.api.common;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.konexios.api.models.ApiError;
import com.konexios.api.rest.RestApiService;

import retrofit2.Response;

@Keep
public interface RetrofitHolder {
    void setDefaultApiKey(String apiKey);

    void setDefaultApiSecret(String apiSecret);

    String getSecretKey();

    void setSecretKey(String secretKey);

    String getApiKey();

    void setApiKey(String apiKey);

    RestApiService getRestApiService(@NonNull String endpoint);

    ApiError convertToApiError(@NonNull Response<?> response);
}
