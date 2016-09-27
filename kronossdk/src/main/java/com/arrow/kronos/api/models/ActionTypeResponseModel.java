package com.arrow.kronos.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.arrow.kronos.api.models.ActionTypeModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by osminin on 9/23/2016.
 */

public final class ActionTypeResponseModel implements Parcelable {
    @SerializedName("size")
    private int mSize;
    @SerializedName("data")
    private List<ActionTypeModel> mActionTypes;

    public int getSize() {
        return mSize;
    }

    public void setSize(int size) {
        mSize = size;
    }

    public List<ActionTypeModel> getActionTypes() {
        return mActionTypes;
    }

    public void setActionTypes(List<ActionTypeModel> actionTypes) {
        mActionTypes = actionTypes;
    }

    public ActionTypeResponseModel() {
    }

    protected ActionTypeResponseModel(Parcel in) {
        mSize = in.readInt();
        if (in.readByte() == 0x01) {
            mActionTypes = new ArrayList<ActionTypeModel>();
            in.readList(mActionTypes, ActionTypeModel.class.getClassLoader());
        } else {
            mActionTypes = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mSize);
        if (mActionTypes == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mActionTypes);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ActionTypeResponseModel> CREATOR = new Parcelable.Creator<ActionTypeResponseModel>() {
        @Override
        public ActionTypeResponseModel createFromParcel(Parcel in) {
            return new ActionTypeResponseModel(in);
        }

        @Override
        public ActionTypeResponseModel[] newArray(int size) {
            return new ActionTypeResponseModel[size];
        }
    };
}
