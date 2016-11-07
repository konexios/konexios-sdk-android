package com.arrow.kronos.api.listeners;

import com.arrow.kronos.api.models.ApiError;

import java.util.List;

/**
 * Created by osminin on 12.10.2016.
 */

public interface PagingResultListener<T> {
    void onRequestSuccess(List<T> list);
    void onRequestError(ApiError error);
}
