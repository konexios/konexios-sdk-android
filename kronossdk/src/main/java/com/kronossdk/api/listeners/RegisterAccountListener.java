package com.kronossdk.api.listeners;

import com.kronossdk.api.models.AccountResponse;

/**
 * Created by osminin on 9/22/2016.
 */

public interface RegisterAccountListener {
    void onAccountRegistered(AccountResponse response);
    void onAccountRegisterFailed(String error);
}
