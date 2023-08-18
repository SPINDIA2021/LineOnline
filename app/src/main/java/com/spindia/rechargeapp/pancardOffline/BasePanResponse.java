package com.spindia.rechargeapp.pancardOffline;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BasePanResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private PanResponse panResponse;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PanResponse getPanResponse() {
        return panResponse;
    }

    public void setData(PanResponse panResponse) {
        this.panResponse = panResponse;
    }

}
