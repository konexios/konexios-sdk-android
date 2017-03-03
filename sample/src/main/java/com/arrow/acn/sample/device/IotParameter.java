package com.arrow.acn.sample.device;

import com.google.gson.annotations.SerializedName;

public class IotParameter {
    @SerializedName("Key")
    private String key;

    @SerializedName("Value")
    private String value;

    public IotParameter(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }
}