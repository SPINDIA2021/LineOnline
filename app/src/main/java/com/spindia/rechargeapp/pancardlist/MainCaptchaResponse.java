package com.spindia.rechargeapp.pancardlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MainCaptchaResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private CaptchaResponse data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public CaptchaResponse getData() {
        return data;
    }

    public void setData(CaptchaResponse data) {
        this.data = data;
    }
}
