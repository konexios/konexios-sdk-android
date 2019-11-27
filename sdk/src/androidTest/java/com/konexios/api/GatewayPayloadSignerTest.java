/*
 * Copyright (c) 2017-2019 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *     Arrow Electronics, Inc.
 *     Konexios, Inc.
 */

package com.konexios.api;

import android.support.test.runner.AndroidJUnit4;

import com.konexios.api.common.GatewayPayloadSigner;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by osminin on 5/22/2017.
 */

@RunWith(AndroidJUnit4.class)
public class GatewayPayloadSignerTest {
    private static final String SECRET_KEY = "ARAzUzRzekFwRTNACBQYUx89LlZyImhKFVloHUVMDw8EGRxxSCckFgdFPysAAWJCLDgMdkstZzw3GGVqNHxXcno5Iz54LRBSKy0TaCBwNndkfQNdD38KAA==";
    private static final String API_KEY = "5501f50fdc62aee5d04dbd6a58b68b781ee2aaade8ad1eb24b1e4e77cb282ae2";
    private static final String NAME = "update-configuration";
    private static final String HID = "05c2d78dee6798025e6e3f83f79256914b7c3664";
    private static final String FINAL_RESULT = "2bcc72adcef72780dfd436d4de46054a49f6bcb832dc2bd3ec05a54da275b8b5";

    private GatewayPayloadSigner mSigner;

    @Before
    public void setUp() throws Exception {
        mSigner = GatewayPayloadSigner.create(SECRET_KEY);
        mSigner.withApiKey(API_KEY)
                .withEncrypted(false)
                .withName(NAME)
                .withHid(HID)
                .withParameter("Key1", "Value 1")
                .withParameter("Key2", "Value 2");
    }

    @Test
    public void test1() throws Exception {
        String result = mSigner.signV1();
        Assert.assertEquals(FINAL_RESULT, result);
    }
}
