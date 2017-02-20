package com.arrow.acn.api.listeners;

import com.arrow.acn.api.models.ApiError;
import com.arrow.acn.api.models.CommonResponse;

/**
 * Created by osminin on 10/11/2016.
 */

public interface CommonRequestListener {
    void onRequestSuccess(CommonResponse response);
    void onRequestError(ApiError error);
}
