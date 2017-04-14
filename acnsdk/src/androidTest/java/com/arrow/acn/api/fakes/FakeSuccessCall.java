/*
 * Copyright (c) 2017 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors: Arrow Electronics, Inc.
 */

package com.arrow.acn.api.fakes;

import java.io.IOException;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by osminin on 4/5/2017.
 */

public class FakeSuccessCall<T> implements Call<T> {

    private final T mResponse;

    public FakeSuccessCall(T response) {
        mResponse = response;
    }

    @Override
    public Response<T> execute() throws IOException {
        return Response.success(mResponse);
    }

    @Override
    public void enqueue(Callback<T> callback) {
        try {
            callback.onResponse(this, execute());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isExecuted() {
        return false;
    }

    @Override
    public void cancel() {

    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public Call<T> clone() {
        return null;
    }

    @Override
    public Request request() {
        return null;
    }
}
