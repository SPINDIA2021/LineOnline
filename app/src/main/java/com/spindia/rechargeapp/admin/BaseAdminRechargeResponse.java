package com.spindia.rechargeapp.admin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.spindia.rechargeapp.admin.recharge.AdminRechargeResponse;

import java.util.ArrayList;

public class BaseAdminRechargeResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<AdminRechargeResponse> data;

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

    public ArrayList<AdminRechargeResponse> getData() {
        return data;
    }

    public void setData(ArrayList<AdminRechargeResponse> data) {
        this.data = data;
    }

}
