package com.spindia.rechargeapp.itr;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.spindia.rechargeapp.aadharUdhyog.UdhyogResponse;

import java.util.ArrayList;

public class BaseITRResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<ITRResponse> itrResponses;

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

    public ArrayList<ITRResponse> getITRResponse() {
        return itrResponses;
    }

    public void setITRResponse(ArrayList<ITRResponse> itrResponses) {
        this.itrResponses = itrResponses;
    }
}
