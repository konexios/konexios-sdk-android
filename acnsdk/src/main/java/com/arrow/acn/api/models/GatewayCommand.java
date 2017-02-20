package com.arrow.acn.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GatewayCommand {

    @SerializedName("command")
    @Expose
    private String command;
    @SerializedName("deviceHid")
    @Expose
    private String deviceHid;
    @SerializedName("payload")
    @Expose
    private String payload;

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

}
