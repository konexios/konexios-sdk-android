package com.arrow.acn.api.listeners;

import com.arrow.acn.api.models.ApiError;

import java.util.List;

/**
 * Created by osminin on 12.10.2016.
 */

public interface ListResultListener<T> {
    void onRequestSuccess(List<T> list);
    void onRequestError(ApiError error);
}
