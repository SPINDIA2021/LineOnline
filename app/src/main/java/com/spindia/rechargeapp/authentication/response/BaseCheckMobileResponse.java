package com.spindia.rechargeapp.authentication.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseCheckMobileResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private CheckMobileResponse checkMobileResponse;

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

    public CheckMobileResponse getCheckMobileResponse() {
        return checkMobileResponse;
    }

    public void setCheckMobileResponse(CheckMobileResponse checkMobileResponse) {
        this.checkMobileResponse = checkMobileResponse;
    }

}
