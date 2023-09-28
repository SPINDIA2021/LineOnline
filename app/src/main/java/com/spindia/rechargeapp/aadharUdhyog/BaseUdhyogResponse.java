package com.spindia.rechargeapp.aadharUdhyog;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.spindia.rechargeapp.gstRegstration.GSTResponse;

import java.util.ArrayList;

public class BaseUdhyogResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<UdhyogResponse> udhyogResponses;

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

    public ArrayList<UdhyogResponse> getUdhyogResponse() {
        return udhyogResponses;
    }

    public void setUdhyogResponse(ArrayList<UdhyogResponse> udhyogResponses) {
        this.udhyogResponses = udhyogResponses;
    }
}
