
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
 *  Pagination model
 * @param <T> - any model to be represented as paging result
 */

public class PagingResultModel<T> {

    /**
     *  list of results
     */
    @SerializedName("data")
    @Expose
    private List<T> data = new ArrayList<>();
    /**
     *  number of page
     */
    @SerializedName("page")
    @Expose
    private Integer page;
    /**
     *  count of items in page
     */
    @SerializedName("size")
    @Expose
    private Integer size;
    /**
     *  total amount of pages
     */
    @SerializedName("totalPages")
    @Expose
    private Integer totalPages;
    /**
     *  total size
     */
    @SerializedName("totalSize")
    @Expose
    private Integer totalSize;

    /**
     * 
     * @return
     *     The data
     */
    public List<T> getData() {
        return data;
    }

    /**
     * 
     * @param data
     *     The data
     */
    public void setData(List<T> data) {
        this.data = data;
    }

    /**
     * 
     * @return
     *     The page
     */
    public Integer getPage() {
        return page;
    }

    /**
     * 
     * @param page
     *     The page
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     * 
     * @return
     *     The size
     */
    public Integer getSize() {
        return size;
    }

    /**
     * 
     * @param size
     *     The size
     */
    public void setSize(Integer size) {
        this.size = size;
    }

    /**
     * 
     * @return
     *     The totalPages
     */
    public Integer getTotalPages() {
        return totalPages;
    }

    /**
     * 
     * @param totalPages
     *     The totalPages
     */
    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    /**
     * 
     * @return
     *     The totalSize
     */
    public Integer getTotalSize() {
        return totalSize;
    }

    /**
     * 
     * @param totalSize
     *     The totalSize
     */
    public void setTotalSize(Integer totalSize) {
        this.totalSize = totalSize;
    }
}
