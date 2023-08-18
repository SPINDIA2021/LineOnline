package com.spindia.rechargeapp.walletHistory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletHistoryResponse {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("retailerid")
    @Expose
    private String retailerid;
    @SerializedName("credit")
    @Expose
    private String credit;
    @SerializedName("debit")
    @Expose
    private String debit;
    @SerializedName("cur_balance")
    @Expose
    private String curBalance;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("createat")
    @Expose
    private String createat;
    @SerializedName("updateat")
    @Expose
    private String updateat;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRetailerid() {
        return retailerid;
    }

    public void setRetailerid(String retailerid) {
        this.retailerid = retailerid;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getDebit() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getCurBalance() {
        return curBalance;
    }

    public void setCurBalance(String curBalance) {
        this.curBalance = curBalance;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateat() {
        return createat;
    }

    public void setCreateat(String createat) {
        this.createat = createat;
    }

    public String getUpdateat() {
        return updateat;
    }

    public void setUpdateat(String updateat) {
        this.updateat = updateat;
    }

}
