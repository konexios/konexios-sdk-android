package com.arrow.acn.api.common;

import android.text.TextUtils;

import com.arrow.acn.api.models.ApiError;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by osminin on 11/7/2016.
 */

public final class ErrorUtils {
    public static final String NETWORK_ERROR_MESSAGE = "Network error";
    public static final int NETWORK_ERROR_CODE = 1;
    public static final String COMMON_ERROR_MESSAGE = "Common error";
    public static final int COMMON_ERROR_CODE = 11;

    public static ApiError parseError(Response<?> response) {
        Retrofit retrofit = RetrofitHolder.getRetrofit();
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
        ApiError error;
        if (t instanceof IOException) {
            error = new ApiError(NETWORK_ERROR_CODE, NETWORK_ERROR_MESSAGE);
        } else {
            error = new ApiError(COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE);
        }
        return error;
    }
}
