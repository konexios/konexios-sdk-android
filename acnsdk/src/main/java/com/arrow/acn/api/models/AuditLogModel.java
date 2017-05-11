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

public class AuditLogModel implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AuditLogModel> CREATOR = new Parcelable.Creator<AuditLogModel>() {
        @NonNull
        @Override
        public AuditLogModel createFromParcel(@NonNull Parcel in) {
            return new AuditLogModel(in);
        }

        @NonNull
        @Override
        public AuditLogModel[] newArray(int size) {
            return new AuditLogModel[size];
        }
    };
    @SerializedName("createdBy")
    @Expose
    private String createdBy;
    @SerializedName("createdString")
    @Expose
    private String createdString;
    @SerializedName("objectHid")
    @Expose
    private String objectHid;
    @SerializedName("parameters")
    @Expose
    private JsonElement parameters;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("type")
    @Expose
    private String type;

    public AuditLogModel() {
    }

    protected AuditLogModel(@NonNull Parcel in) {
        createdBy = in.readString();
        createdString = (String) in.readValue(String.class.getClassLoader());
        objectHid = in.readString();
        JsonParser parser = new JsonParser();
        parameters = parser.parse(in.readString()).getAsJsonObject();
        productName = in.readString();
        type = in.readString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuditLogModel that = (AuditLogModel) o;

        if (createdBy != null ? !createdBy.equals(that.createdBy) : that.createdBy != null)
            return false;
        if (createdString != null ? !createdString.equals(that.createdString) : that.createdString != null)
            return false;
        if (objectHid != null ? !objectHid.equals(that.objectHid) : that.objectHid != null)
            return false;
        if (!getParameters().equals(that.getParameters()))
            return false;
        if (productName != null ? !productName.equals(that.productName) : that.productName != null)
            return false;
        return type != null ? type.equals(that.type) : that.type == null;

    }

    @Override
    public int hashCode() {
        int result = createdBy != null ? createdBy.hashCode() : 0;
        result = 31 * result + (createdString != null ? createdString.hashCode() : 0);
        result = 31 * result + (objectHid != null ? objectHid.hashCode() : 0);
        result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
        result = 31 * result + (productName != null ? productName.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
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
     * @return The objectHid
     */
    public String getObjectHid() {
        return objectHid;
    }

    /**
     * @param objectHid The objectHid
     */
    public void setObjectHid(String objectHid) {
        this.objectHid = objectHid;
    }

    /**
     * @return The parameters
     */
    public JsonElement getParameters() {
        if (parameters == null) {
            parameters = new JsonObject();
        }
        return parameters;
    }

    /**
     * @param parameters The parameters
     */
    public void setParameters(JsonElement parameters) {
        this.parameters = parameters;
    }

    /**
     * @return The productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName The productName
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return The type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type
     */
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(createdBy);
        dest.writeValue(createdString);
        dest.writeString(objectHid);
        String str = new Gson().toJson(getParameters());
        dest.writeString(str);
        dest.writeString(productName);
        dest.writeString(type);
    }
}