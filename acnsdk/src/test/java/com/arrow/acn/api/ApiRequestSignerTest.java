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

import com.arrow.acn.api.common.ApiRequestSigner;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by osminin on 4/3/2017.
 */

public class ApiRequestSignerTest {

    private static final String RND_API_KEY = "kjg23asd123g456nbjhf6123kdlfghfg908jh43jh11234dfliyasfk376nx2n9b";
    private static final String RND_SECRET_KEY = "AK40L17HD/G72HLKHD2314/SAGdLAFRMC452DASF4h=";
    private static final String METHOD = "get";
    private static final String PATH = "http://google.com";
    private static final String RND_TIME_STRING = "2017-04-04T09:10:01.334Z";
    private static final String BODY = "some body";
    private static final String RESULT_SIGNATURE = "84743c7ca35a225af9d69012dfafbff677a2baeeec25dcf62499c7561c97f7f0";


    public ApiRequestSigner mRequestSigner;

    @Before
    public void init() {
        mRequestSigner = new ApiRequestSigner();
        mRequestSigner.setSecretKey(RND_SECRET_KEY);
        mRequestSigner.setApiKey(RND_API_KEY);
    }

    @Test
    public void test_setSecretKey() {
        assertEquals(RND_SECRET_KEY, mRequestSigner.getSecretKey());
    }

    @Test
    public void test_setApiKey() {
        assertEquals(RND_API_KEY, mRequestSigner.getApiKey());
    }

    @Test
    public void test_signing() {
        String signature = mRequestSigner.method(METHOD)
                .canonicalUri(PATH).timestamp(RND_TIME_STRING).payload(BODY).signV1();
        assertEquals(RESULT_SIGNATURE, signature);
    }

}
