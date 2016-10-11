package com.arrow.kronos.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FindAllDevicesResponse implements Parcelable {

    @SerializedName("data")
    @Expose
    private List<DeviceModel> data = new ArrayList<>();
    @SerializedName("page")
    @Expose
    private int page;
    @SerializedName("size")
    @Expose
    private int size;
    @SerializedName("totalPages")
    @Expose
    private int totalPages;
    @SerializedName("totalSize")
    @Expose
    private int totalSize;

    /**
     *
     * @return
     * The data
     */
    public List<DeviceModel> getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(List<DeviceModel> data) {
        this.data = data;
    }

    /**
     *
     * @return
     * The page
     */
    public int getPage() {
        return page;
    }

    /**
     *
     * @param page
     * The page
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     *
     * @return
     * The size
     */
    public int getSize() {
        return size;
    }

    /**
     *
     * @param size
     * The size
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     *
     * @return
     * The totalPages
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     *
     * @param totalPages
     * The totalPages
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    /**
     *
     * @return
     * The totalSize
     */
    public int getTotalSize() {
        return totalSize;
    }

    /**
     *
     * @param totalSize
     * The totalSize
     */
    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }


    protected FindAllDevicesResponse(Parcel in) {
        if (in.readByte() == 0x01) {
            data = new ArrayList<DeviceModel>();
            in.readList(data, DeviceModel.class.getClassLoader());
        } else {
            data = null;
        }
        page = in.readInt();
        size = in.readInt();
        totalPages = in.readInt();
        totalSize = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (data == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(data);
        }
        dest.writeInt(page);
        dest.writeInt(size);
        dest.writeInt(totalPages);
        dest.writeInt(totalSize);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FindAllDevicesResponse> CREATOR = new Parcelable.Creator<FindAllDevicesResponse>() {
        @Override
        public FindAllDevicesResponse createFromParcel(Parcel in) {
            return new FindAllDevicesResponse(in);
        }

        @Override
        public FindAllDevicesResponse[] newArray(int size) {
            return new FindAllDevicesResponse[size];
        }
    };
}