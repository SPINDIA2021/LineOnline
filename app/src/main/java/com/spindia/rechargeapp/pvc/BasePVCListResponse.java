package com.spindia.rechargeapp.pvc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BasePVCListResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<PVCListResponse> pvcListResponses;

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

    public ArrayList<PVCListResponse> getPVCListResponse() {
        return pvcListResponses;
    }

    public void setPVCListResponse(ArrayList<PVCListResponse> pvcListResponses) {
        this.pvcListResponses = pvcListResponses;
    }
}
