/*
 * Copyright (c) 2017 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors: Arrow Electronics, Inc.
 */

package com.arrow.acn.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * common model for list results
 */

public class ListResultModel<T> {
    @SerializedName("data")
    @Expose
    private List<T> data = new ArrayList<T>();
    @SerializedName("size")
    @Expose
    private int size;

    /**
     * @return The data
     */
    public List<T> getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(List<T> data) {
        this.data = data;
    }

    /**
     * @return The size
     */
    public int getSize() {
        return size;
    }

    /**
     * @param size The size
     */
    public void setSize(int size) {
        this.size = size;
    }
}
