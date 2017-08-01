/*
 * Copyright (c) 2017 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors: Arrow Electronics, Inc.
 */

package com.arrow.acn.api.common;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.arrow.acn.api.models.ApiError;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

import static com.arrow.acn.api.models.ApiError.COMMON_ERROR_CODE;
import static com.arrow.acn.api.models.ApiError.COMMON_ERROR_MESSAGE;
import static com.arrow.acn.api.models.ApiError.NETWORK_ERROR_CODE;
import static com.arrow.acn.api.models.ApiError.NETWORK_ERROR_MESSAGE;

public final class ErrorUtils {
    static ApiError parseError(@NonNull Response<?> response, Retrofit retrofit) {
        Timber.v("parseError: ");
        Converter<ResponseBody, ApiError> converter = retrofit.responseBodyConverter(ApiError.class, new Annotation[0]);
        ApiError error;
        try {
            error = converter.convert(response.errorBody());
            if (TextUtils.isEmpty(error.getMessage())) {
                error.setMessage(response.message());
            }
        } catch (IOException e) {
            return new ApiError(COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE);
        }
        return error;
    }

    public static ApiError parseError(Throwable t) {
        Timber.v("parseError: ");
        ApiError error;
        if (t instanceof IOException) {
            error = new ApiError(NETWORK_ERROR_CODE, NETWORK_ERROR_MESSAGE);
        } else {
            error = new ApiError(COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE);
        }
        return error;
    }
}
