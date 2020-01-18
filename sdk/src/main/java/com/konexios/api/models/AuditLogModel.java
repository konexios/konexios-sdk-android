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

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

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
        return Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdString, that.createdString) &&
                Objects.equals(objectHid, that.objectHid) &&
                Objects.equals(parameters, that.parameters) &&
                Objects.equals(productName, that.productName) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdBy, createdString, objectHid, parameters, productName, type);
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