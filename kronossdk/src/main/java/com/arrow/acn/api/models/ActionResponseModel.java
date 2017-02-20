package com.arrow.acn.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by osminin on 9/15/2016.
 */

public final class ActionResponseModel implements Parcelable {
    @SerializedName("data")
    List<DeviceActionModel> mActions;

    public List<DeviceActionModel> getActions() {
        return mActions;
    }

    public void setActions(List<DeviceActionModel> actions) {
        mActions = actions;
    }

    protected ActionResponseModel(Parcel in) {
        if (in.readByte() == 0x01) {
            mActions = new ArrayList<>();
            in.readList(mActions, DeviceActionModel.class.getClassLoader());
        } else {
            mActions = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (mActions == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mActions);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ActionResponseModel> CREATOR = new Parcelable.Creator<ActionResponseModel>() {
        @Override
        public ActionResponseModel createFromParcel(Parcel in) {
            return new ActionResponseModel(in);
        }

        @Override
        public ActionResponseModel[] newArray(int size) {
            return new ActionResponseModel[size];
        }
    };
}