package com.arrow.kronos.api.listeners;


import com.arrow.kronos.api.models.AccountResponse;
import com.arrow.kronos.api.models.ApiError;

/**
 * Created by osminin on 9/22/2016.
 */

public interface RegisterAccountListener {
    void onAccountRegistered(AccountResponse response);
    void onAccountRegisterFailed(ApiError error);
}
