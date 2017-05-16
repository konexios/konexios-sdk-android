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
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NodeTypeModel implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<NodeTypeModel> CREATOR = new Parcelable.Creator<NodeTypeModel>() {
        @NonNull
        @Override
        public NodeTypeModel createFromParcel(@NonNull Parcel in) {
            return new NodeTypeModel(in);
        }

        @NonNull
        @Override
        public NodeTypeModel[] newArray(int size) {
            return new NodeTypeModel[size];
        }
    };
    @SerializedName("createdBy")
    @Expose
    private String createdBy;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("enabled")
    @Expose
    private boolean enabled;
    @SerializedName("hid")
    @Expose
    private String hid;
    @SerializedName("lastModifiedBy")
    @Expose
    private String lastModifiedBy;
    @SerializedName("lastModifiedDate")
    @Expose
    private String lastModifiedDate;
    @SerializedName("links")
    @Expose
    private JsonElement links;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("pri")
    @Expose
    private String pri;

    public NodeTypeModel() {
    }

    protected NodeTypeModel(@NonNull Parcel in) {
        createdBy = in.readString();
        createdDate = in.readString();
        description = in.readString();
        enabled = in.readByte() != 0x00;
        hid = in.readString();
        lastModifiedBy = in.readString();
        lastModifiedDate = in.readString();
        JsonParser parser = new JsonParser();
        links = parser.parse(in.readString()).getAsJsonObject();
        name = in.readString();
        pri = in.readString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodeTypeModel that = (NodeTypeModel) o;

        if (enabled != that.enabled) return false;
        if (createdBy != null ? !createdBy.equals(that.createdBy) : that.createdBy != null)
            return false;
        if (createdDate != null ? !createdDate.equals(that.createdDate) : that.createdDate != null)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (hid != null ? !hid.equals(that.hid) : that.hid != null) return false;
        if (lastModifiedBy != null ? !lastModifiedBy.equals(that.lastModifiedBy) : that.lastModifiedBy != null)
            return false;
        if (lastModifiedDate != null ? !lastModifiedDate.equals(that.lastModifiedDate) : that.lastModifiedDate != null)
            return false;
        if (links != null ? !links.equals(that.links) : that.links != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return pri != null ? pri.equals(that.pri) : that.pri == null;

    }

    @Override
    public int hashCode() {
        int result = createdBy != null ? createdBy.hashCode() : 0;
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (enabled ? 1 : 0);
        result = 31 * result + (hid != null ? hid.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (lastModifiedDate != null ? lastModifiedDate.hashCode() : 0);
        result = 31 * result + (links != null ? links.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (pri != null ? pri.hashCode() : 0);
        return result;
    }

    /**
     * @return The createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy The createdBy
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return The createdDate
     */
    public String getCreatedDate() {
        return createdDate;
    }

    /**
     * @param createdDate The createdDate
     */
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

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
     * @return The hid
     */
    public String getHid() {
        return hid;
    }

    /**
     * @param hid The hid
     */
    public void setHid(String hid) {
        this.hid = hid;
    }

    /**
     * @return The lastModifiedBy
     */
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    /**
     * @param lastModifiedBy The lastModifiedBy
     */
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    /**
     * @return The lastModifiedDate
     */
    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * @param lastModifiedDate The lastModifiedDate
     */
    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * @return The links
     */
    public JsonElement getLinks() {
        if (links == null) {
            links = new JsonObject();
        }
        return links;
    }

    /**
     * @param links The links
     */
    public void setLinks(JsonElement links) {
        this.links = links;
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
     * @return The pri
     */
    public String getPri() {
        return pri;
    }

    /**
     * @param pri The pri
     */
    public void setPri(String pri) {
        this.pri = pri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(createdBy);
        dest.writeString(createdDate);
        dest.writeString(description);
        dest.writeByte((byte) (enabled ? 0x01 : 0x00));
        dest.writeString(hid);
        dest.writeString(lastModifiedBy);
        dest.writeString(lastModifiedDate);
        Gson gson = new Gson();
        dest.writeString(gson.toJson(getLinks()));
        dest.writeString(name);
        dest.writeString(pri);
    }
}