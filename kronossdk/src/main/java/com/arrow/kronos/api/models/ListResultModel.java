package com.arrow.kronos.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by osminin on 10/11/2016.
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
