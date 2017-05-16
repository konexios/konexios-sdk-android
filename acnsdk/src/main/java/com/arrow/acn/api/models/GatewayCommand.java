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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

        if (command != null ? !command.equals(that.command) : that.command != null) return false;
        if (deviceHid != null ? !deviceHid.equals(that.deviceHid) : that.deviceHid != null)
            return false;
        return payload != null ? payload.equals(that.payload) : that.payload == null;

    }

    @Override
    public int hashCode() {
        int result = command != null ? command.hashCode() : 0;
        result = 31 * result + (deviceHid != null ? deviceHid.hashCode() : 0);
        result = 31 * result + (payload != null ? payload.hashCode() : 0);
        return result;
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
