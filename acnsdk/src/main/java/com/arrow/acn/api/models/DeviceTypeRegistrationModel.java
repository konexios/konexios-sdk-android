package com.arrow.acn.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DeviceTypeRegistrationModel {

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("enabled")
    @Expose
    private boolean enabled;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("telemetries")
    @Expose
    private List<DeviceTypeTelemetryModel> telemetries = new ArrayList<>();

    /**
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return The enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @param enabled The enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The telemetries
     */
    public List<DeviceTypeTelemetryModel> getTelemetries() {
        return telemetries;
    }

    /**
     * @param telemetries The telemetries
     */
    public void setTelemetries(List<DeviceTypeTelemetryModel> telemetries) {
        this.telemetries = telemetries;
    }
}