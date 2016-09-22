package com.kronossdk.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by osminin on 8/30/2016.
 */

public final class HistoricalEventResponse implements Parcelable {
    @SerializedName("totalPages")
    private Integer mTotalPages;
    @SerializedName("totalElements")
    private Integer mTotalElements;
    @SerializedName("page")
    private Integer mPage;
    @SerializedName("size")
    private Integer mSize;
    @SerializedName("data")
    private List<Data> mData;

    public Integer getTotalPages() {
        return mTotalPages;
    }

    public void setTotalPages(Integer totalPages) {
        mTotalPages = totalPages;
    }

    public Integer getTotalElements() {
        return mTotalElements;
    }

    public void setTotalElements(Integer totalElements) {
        mTotalElements = totalElements;
    }

    public Integer getPage() {
        return mPage;
    }

    public void setPage(Integer page) {
        mPage = page;
    }

    public Integer getSize() {
        return mSize;
    }

    public void setSize(Integer size) {
        mSize = size;
    }

    public List<Data> getData() {
        return mData;
    }

    public void setData(List<Data> data) {
        mData = data;
    }

    public static class Data implements Parcelable {
        @SerializedName("deviceActionTypeName")
        private String mDeviceActionTypeName;
        @SerializedName("criteria")
        private String mCriteria;
        @SerializedName("createdDate")
        private Long mCreatedDate;
        @SerializedName("status")
        private String mStatus;

        public String getDeviceActionTypeName() {
            return mDeviceActionTypeName;
        }

        public void setDeviceActionTypeName(String deviceActionTypeName) {
            mDeviceActionTypeName = deviceActionTypeName;
        }

        public String getCriteria() {
            return mCriteria;
        }

        public void setCriteria(String criteria) {
            mCriteria = criteria;
        }

        public Long getCreatedDate() {
            return mCreatedDate;
        }

        public void setCreatedDate(Long createdDate) {
            mCreatedDate = createdDate;
        }

        public String getStatus() {
            return mStatus;
        }

        public void setStatus(String status) {
            mStatus = status;
        }

        protected Data(Parcel in) {
            mDeviceActionTypeName = in.readString();
            mCriteria = in.readString();
            mCreatedDate = in.readByte() == 0x00 ? null : in.readLong();
            mStatus = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mDeviceActionTypeName);
            dest.writeString(mCriteria);
            if (mCreatedDate == null) {
                dest.writeByte((byte) (0x00));
            } else {
                dest.writeByte((byte) (0x01));
                dest.writeLong(mCreatedDate);
            }
            dest.writeString(mStatus);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {
            @Override
            public Data createFromParcel(Parcel in) {
                return new Data(in);
            }

            @Override
            public Data[] newArray(int size) {
                return new Data[size];
            }
        };
    }

    protected HistoricalEventResponse(Parcel in) {
        mTotalPages = in.readByte() == 0x00 ? null : in.readInt();
        mTotalElements = in.readByte() == 0x00 ? null : in.readInt();
        mPage = in.readByte() == 0x00 ? null : in.readInt();
        mSize = in.readByte() == 0x00 ? null : in.readInt();
        if (in.readByte() == 0x01) {
            mData = new ArrayList<>();
            in.readList(mData, Data.class.getClassLoader());
        } else {
            mData = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (mTotalPages == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(mTotalPages);
        }
        if (mTotalElements == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(mTotalElements);
        }
        if (mPage == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(mPage);
        }
        if (mSize == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(mSize);
        }
        if (mData == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mData);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<HistoricalEventResponse> CREATOR = new Parcelable.Creator<HistoricalEventResponse>() {
        @Override
        public HistoricalEventResponse createFromParcel(Parcel in) {
            return new HistoricalEventResponse(in);
        }

        @Override
        public HistoricalEventResponse[] newArray(int size) {
            return new HistoricalEventResponse[size];
        }
    };
}