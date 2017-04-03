/*
 * Copyright (c) 2017 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors: Arrow Electronics, Inc.
 */

package com.arrow.acn.api;

import android.os.Handler;

import com.arrow.acn.api.common.RetrofitHolder;
import com.arrow.acn.api.listeners.RegisterAccountListener;
import com.arrow.acn.api.models.AccountRequest;
import com.arrow.acn.api.models.AccountResponse;
import com.arrow.acn.api.models.ApiError;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import retrofit2.Retrofit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by osminin on 4/3/2017.
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CreateWithRealRetrofitServiceTest {
    //TODO: replace with real
    private static final String TEST_API_URL = "";
    private static final String TEST_API_KEY = "";
    private static final String TEST_API_SECRET = "";

    private AcnApiService apiService;

    @Before
    public void createAcnApiService() {
        apiService = AcnApiServiceFactory.createAcnApiService();
    }

    @Test
    public void test1_setRestEndpoints() {
        assertNotNull(apiService);
        apiService.setRestEndpoint(TEST_API_URL, TEST_API_KEY, TEST_API_SECRET);
        assertEquals(TEST_API_KEY, RetrofitHolder.getDefaultApiKey());
        assertEquals(TEST_API_SECRET, RetrofitHolder.getDefaultApiSecret());
        Retrofit retrofit = RetrofitHolder.getRetrofit();
        assertNotNull(retrofit);
        String baseUrl = retrofit.baseUrl().toString();
        assertEquals(TEST_API_URL, baseUrl);
    }
}
