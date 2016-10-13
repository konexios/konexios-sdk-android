package com.arrow.kronos.api.listeners;

import com.arrow.kronos.api.models.CommonResponse;

/**
 * Created by osminin on 10/11/2016.
 */

public interface CommonRequestListener {
    void onRequestSuccess(CommonResponse response);
    void onRequestError();
}
