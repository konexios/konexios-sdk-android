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

/**
 *  Request for registering new node
 */

public final class NodeRegistrationModel implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<NodeRegistrationModel> CREATOR = new Parcelable.Creator<NodeRegistrationModel>() {
        @Override
        public NodeRegistrationModel createFromParcel(Parcel in) {
            return new NodeRegistrationModel(in);
        }

        @Override
        public NodeRegistrationModel[] newArray(int size) {
            return new NodeRegistrationModel[size];
        }
    };
    /**
     *  node description
     */
    @SerializedName("description")
    @Expose
    private String description;
    /**
     *  flag is node enabled
     */
    @SerializedName("enabled")
    @Expose
    private boolean enabled;
    /**
     *  name of the node
     */
    @SerializedName("name")
    @Expose
    private String name;
    /**
     *  hid of node type
     */
    @SerializedName("nodeTypeHid")
    @Expose
    private String nodeTypeHid;
    /**
     *  parent node hid
     */
    @SerializedName("parentNodeHid")
    @Expose
    private String parentNodeHid;

    public NodeRegistrationModel() {

    }

    protected NodeRegistrationModel(Parcel in) {
        description = in.readString();
        enabled = in.readByte() != 0x00;
        name = in.readString();
        nodeTypeHid = in.readString();
        parentNodeHid = in.readString();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodeRegistrationModel that = (NodeRegistrationModel) o;

        if (enabled != that.enabled) return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (nodeTypeHid != null ? !nodeTypeHid.equals(that.nodeTypeHid) : that.nodeTypeHid != null)
            return false;
        return parentNodeHid != null ? parentNodeHid.equals(that.parentNodeHid) : that.parentNodeHid == null;

    }

    @Override
    public int hashCode() {
        int result = description != null ? description.hashCode() : 0;
        result = 31 * result + (enabled ? 1 : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (nodeTypeHid != null ? nodeTypeHid.hashCode() : 0);
        result = 31 * result + (parentNodeHid != null ? parentNodeHid.hashCode() : 0);
        return result;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeByte((byte) (enabled ? 0x01 : 0x00));
        dest.writeString(name);
        dest.writeString(nodeTypeHid);
        dest.writeString(parentNodeHid);
    }
}
