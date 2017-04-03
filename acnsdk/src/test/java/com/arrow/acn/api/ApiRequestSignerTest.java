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
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;

/**
 * Created by osminin on 4/3/2017.
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApiRequestSignerTest {

    public ApiRequestSigner mRequestSigner;

    @Before
    public void init() {
        mRequestSigner = new ApiRequestSigner();
    }

    @Test
    public void test1_setSecretKey() {
        //random key
        String testKey = "AK40L17HD/G72HLKHD2314/SAGdLAFRMC452DASF4h=";
        mRequestSigner.setSecretKey(testKey);
        assertEquals(testKey, mRequestSigner.getSecretKey());
    }

    @Test
    public void test2_setApiKey() {
        //random key
        String testKey = "kjg23asd123g456nbjhf6123kdlfghfg908jh43jh11234dfliyasfk376nx2n9b";
        mRequestSigner.setApiKey(testKey);
        assertEquals(testKey, mRequestSigner.getApiKey());
    }

    @Test
    public void test3_signingTest() {

    }

}
