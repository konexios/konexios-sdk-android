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

/**
 * Created by osminin on 10/11/2016.
 */

public final class NodeModel implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<NodeModel> CREATOR = new Parcelable.Creator<NodeModel>() {
        @NonNull
        @Override
        public NodeModel createFromParcel(@NonNull Parcel in) {
            return new NodeModel(in);
        }

        @NonNull
        @Override
        public NodeModel[] newArray(int size) {
            return new NodeModel[size];
        }
    };
    @SerializedName("createdBy")
    @Expose
    private String createdBy;
    @SerializedName("createdString")
    @Expose
    private String createdString;
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
    @SerializedName("lastModifiedString")
    @Expose
    private String lastModifiedString;
    @SerializedName("links")
    @Expose
    private JsonElement links;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("nodeTypeHid")
    @Expose
    private String nodeTypeHid;
    @SerializedName("parentNodeHid")
    @Expose
    private String parentNodeHid;
    @SerializedName("pri")
    @Expose
    private String pri;

    public NodeModel() {
    }
    protected NodeModel(@NonNull Parcel in) {
        createdBy = in.readString();
        createdString = in.readString();
        description = in.readString();
        enabled = in.readByte() != 0x00;
        hid = in.readString();
        lastModifiedBy = in.readString();
        lastModifiedString = in.readString();
        JsonParser parser = new JsonParser();
        links = parser.parse(in.readString()).getAsJsonObject();
        name = in.readString();
        nodeTypeHid = in.readString();
        parentNodeHid = in.readString();
        pri = in.readString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodeModel nodeModel = (NodeModel) o;

        if (enabled != nodeModel.enabled) return false;
        if (createdBy != null ? !createdBy.equals(nodeModel.createdBy) : nodeModel.createdBy != null)
            return false;
        if (createdString != null ? !createdString.equals(nodeModel.createdString) : nodeModel.createdString != null)
            return false;
        if (description != null ? !description.equals(nodeModel.description) : nodeModel.description != null)
            return false;
        if (hid != null ? !hid.equals(nodeModel.hid) : nodeModel.hid != null) return false;
        if (lastModifiedBy != null ? !lastModifiedBy.equals(nodeModel.lastModifiedBy) : nodeModel.lastModifiedBy != null)
            return false;
        if (lastModifiedString != null ? !lastModifiedString.equals(nodeModel.lastModifiedString) : nodeModel.lastModifiedString != null)
            return false;
        if (links != null ? !links.equals(nodeModel.links) : nodeModel.links != null) return false;
        if (name != null ? !name.equals(nodeModel.name) : nodeModel.name != null) return false;
        if (nodeTypeHid != null ? !nodeTypeHid.equals(nodeModel.nodeTypeHid) : nodeModel.nodeTypeHid != null)
            return false;
        if (parentNodeHid != null ? !parentNodeHid.equals(nodeModel.parentNodeHid) : nodeModel.parentNodeHid != null)
            return false;
        return pri != null ? pri.equals(nodeModel.pri) : nodeModel.pri == null;

    }

    @Override
    public int hashCode() {
        int result = createdBy != null ? createdBy.hashCode() : 0;
        result = 31 * result + (createdString != null ? createdString.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (enabled ? 1 : 0);
        result = 31 * result + (hid != null ? hid.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (lastModifiedString != null ? lastModifiedString.hashCode() : 0);
        result = 31 * result + (links != null ? links.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (nodeTypeHid != null ? nodeTypeHid.hashCode() : 0);
        result = 31 * result + (parentNodeHid != null ? parentNodeHid.hashCode() : 0);
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
     * @return The createdString
     */
    public String getCreatedString() {
        return createdString;
    }

    /**
     * @param String The String
     */
    public void setCreatedString(String String) {
        this.createdString = String;
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
     * @return The lastModifiedString
     */
    public String getLastModifiedString() {
        return lastModifiedString;
    }

    /**
     * @param lastModifiedString The lastModifiedString
     */
    public void setLastModifiedString(String lastModifiedString) {
        this.lastModifiedString = lastModifiedString;
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
     * @return The nodeTypeHid
     */
    public String getNodeTypeHid() {
        return nodeTypeHid;
    }

    /**
     * @param nodeTypeHid The nodeTypeHid
     */
    public void setNodeTypeHid(String nodeTypeHid) {
        this.nodeTypeHid = nodeTypeHid;
    }

    /**
     * @return The parentNodeHid
     */
    public String getParentNodeHid() {
        return parentNodeHid;
    }

    /**
     * @param parentNodeHid The parentNodeHid
     */
    public void setParentNodeHid(String parentNodeHid) {
        this.parentNodeHid = parentNodeHid;
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
        dest.writeString(createdString);
        dest.writeString(description);
        dest.writeByte((byte) (enabled ? 0x01 : 0x00));
        dest.writeString(hid);
        dest.writeString(lastModifiedBy);
        dest.writeString(lastModifiedString);
        Gson gson = new Gson();
        String str = gson.toJson(getLinks());
        dest.writeString(str);
        dest.writeString(name);
        dest.writeString(nodeTypeHid);
        dest.writeString(parentNodeHid);
        dest.writeString(pri);
    }
}