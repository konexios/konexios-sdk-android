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

package com.konexios.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class GatewayCommand implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<GatewayCommand> CREATOR = new Parcelable.Creator<GatewayCommand>() {
        @Override
        public GatewayCommand createFromParcel(Parcel in) {
            return new GatewayCommand(in);
        }

        @Override
        public GatewayCommand[] newArray(int size) {
            return new GatewayCommand[size];
        }
    };
    @SerializedName("command")
    @Expose
    private String command;
    @SerializedName("deviceHid")
    @Expose
    private String deviceHid;
    @SerializedName("payload")
    @Expose
    private String payload;

    public GatewayCommand() {
    }

    protected GatewayCommand(Parcel in) {
        command = in.readString();
        deviceHid = in.readString();
        payload = in.readString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GatewayCommand that = (GatewayCommand) o;
        return Objects.equals(command, that.command) &&
                Objects.equals(deviceHid, that.deviceHid) &&
                Objects.equals(payload, that.payload);
    }

    @Override
    public int hashCode() {
        return Objects.hash(command, deviceHid, payload);
    }

    /**
     * @return The command
     */
    public String getCommand() {
        return command;
    }

    /**
     * @param command The command
     */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
     * @return The deviceHid
     */
    public String getDeviceHid() {
        return deviceHid;
    }

    /**
     * @param deviceHid The deviceHid
     */
    public void setDeviceHid(String deviceHid) {
        this.deviceHid = deviceHid;
    }

    /**
     * @return The payload
     */
    public String getPayload() {
        return payload;
    }

    /**
     * @param payload The payload
     */
    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(command);
        dest.writeString(deviceHid);
        dest.writeString(payload);
    }
}
