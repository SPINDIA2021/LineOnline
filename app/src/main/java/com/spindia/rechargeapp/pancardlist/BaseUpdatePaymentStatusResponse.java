package com.spindia.rechargeapp.pancardlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseUpdatePaymentStatusResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private UpdatePaymentStatusResponse updatePaymentStatusResponse;

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

    public UpdatePaymentStatusResponse getUpdatePaymentStatusResponse() {
        return updatePaymentStatusResponse;
    }

    public void setUpdatePaymentStatusResponse(UpdatePaymentStatusResponse updatePaymentStatusResponse) {
        this.updatePaymentStatusResponse = updatePaymentStatusResponse;
    }
}
