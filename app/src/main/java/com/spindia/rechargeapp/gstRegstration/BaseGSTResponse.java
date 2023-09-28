package com.spindia.rechargeapp.gstRegstration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BaseGSTResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<GSTResponse> gstResponses;

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

    public ArrayList<GSTResponse> getGSTResponse() {
        return gstResponses;
    }

    public void setGSTResponse(ArrayList<GSTResponse> gstResponses) {
        this.gstResponses = gstResponses;
    }
}
