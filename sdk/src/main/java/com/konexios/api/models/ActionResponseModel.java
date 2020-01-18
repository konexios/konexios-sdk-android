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
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by osminin on 9/15/2016.
 */

public final class ActionResponseModel implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ActionResponseModel> CREATOR = new Parcelable.Creator<ActionResponseModel>() {
        @NonNull
        @Override
        public ActionResponseModel createFromParcel(@NonNull Parcel in) {
            return new ActionResponseModel(in);
        }

        @NonNull
        @Override
        public ActionResponseModel[] newArray(int size) {
            return new ActionResponseModel[size];
        }
    };
    @Nullable
    @SerializedName("data")
    List<DeviceActionModel> actionModels;

    public ActionResponseModel() {
    }

    protected ActionResponseModel(@NonNull Parcel in) {
        if (in.readByte() == 0x01) {
            actionModels = new ArrayList<>();
            in.readList(actionModels, DeviceActionModel.class.getClassLoader());
        } else {
            actionModels = null;
        }

    }

    @Nullable
    public List<DeviceActionModel> getActions() {
        return actionModels;
    }

    public void setActions(List<DeviceActionModel> actions) {
        this.actionModels = actions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActionResponseModel that = (ActionResponseModel) o;
        return Objects.equals(actionModels, that.actionModels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actionModels);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        if (actionModels == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(actionModels);
        }
    }
}