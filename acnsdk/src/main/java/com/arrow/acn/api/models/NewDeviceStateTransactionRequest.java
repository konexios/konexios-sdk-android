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

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by osminin on 4/24/2017.
 */

public class NewDeviceStateTransactionRequest implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<NewDeviceStateTransactionRequest> CREATOR = new Parcelable.Creator<NewDeviceStateTransactionRequest>() {
        @Override
        public NewDeviceStateTransactionRequest createFromParcel(Parcel in) {
            return new NewDeviceStateTransactionRequest(in);
        }

        @Override
        public NewDeviceStateTransactionRequest[] newArray(int size) {
            return new NewDeviceStateTransactionRequest[size];
        }
    };
    @SerializedName("states")
    @Expose
    private JsonElement states;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    public NewDeviceStateTransactionRequest() {
    }

    protected NewDeviceStateTransactionRequest(Parcel in) {
        JsonParser parser = new JsonParser();
        states = parser.parse(in.readString()).getAsJsonObject();
        timestamp = in.readString();
    }

    public JsonElement getStates() {
        if (states == null) {
            states = new JsonObject();
        }
        return states;
    }

    public void setStates(JsonElement states) {
        this.states = states;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, String> getMappedStates() {
        if (states == null) {
            return null;
        }
        Gson gson = new Gson();
        Type stringStringMap = new TypeToken<Map<String, String>>(){}.getType();
        return gson.fromJson(states, stringStringMap);
    }

    public NewDeviceStateTransactionRequest addState(String key, String value) {
        if (states == null) {
            states = new JsonObject();
        }
        states.getAsJsonObject().addProperty(key, value);
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Gson gson = new Gson();
        String str = gson.toJson(getStates());
        dest.writeString(str);
        dest.writeString(timestamp);
    }
}
