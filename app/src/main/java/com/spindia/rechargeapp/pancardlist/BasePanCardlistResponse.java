package com.spindia.rechargeapp.pancardlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BasePanCardlistResponse {


    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<PanCardlistResponse> panCardlistResponses;

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

    public ArrayList<PanCardlistResponse> getPanCardlistResponses() {
        return panCardlistResponses;
    }

    public void setData(ArrayList<PanCardlistResponse> panCardlistResponses) {
        this.panCardlistResponses = panCardlistResponses;
    }

}
