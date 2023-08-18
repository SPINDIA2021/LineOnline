package com.spindia.rechargeapp.pvc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseUpdatePVCResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private UpdatePVCResponse updatePVCResponse;

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

    public UpdatePVCResponse getUpdatePVCResponse() {
        return updatePVCResponse;
    }

    public void setUpdatePVCResponse(UpdatePVCResponse updatePVCResponse) {
        this.updatePVCResponse = updatePVCResponse;
    }
}
