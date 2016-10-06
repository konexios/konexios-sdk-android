
package com.arrow.kronos.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreatedDate {

    @SerializedName("epochSecond")
    @Expose
    private Integer epochSecond;
    @SerializedName("nano")
    @Expose
    private Integer nano;

    /**
     * 
     * @return
     *     The epochSecond
     */
    public Integer getEpochSecond() {
        return epochSecond;
    }

    /**
     * 
     * @param epochSecond
     *     The epochSecond
     */
    public void setEpochSecond(Integer epochSecond) {
        this.epochSecond = epochSecond;
    }

    /**
     * 
     * @return
     *     The nano
     */
    public Integer getNano() {
        return nano;
    }

    /**
     * 
     * @param nano
     *     The nano
     */
    public void setNano(Integer nano) {
        this.nano = nano;
    }

}
