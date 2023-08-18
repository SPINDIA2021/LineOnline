package com.spindia.rechargeapp.walletHistory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.spindia.rechargeapp.walletHistory.WalletHistoryResponse;

import java.util.ArrayList;

public class BaseWalletHistoryResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<WalletHistoryResponse> walletHistoryResponse;

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

    public ArrayList<WalletHistoryResponse> getWalletHistoryResponse() {
        return walletHistoryResponse;
    }

    public void setWalletHistoryResponse(ArrayList<WalletHistoryResponse> data) {
        this.walletHistoryResponse = data;
    }

}
