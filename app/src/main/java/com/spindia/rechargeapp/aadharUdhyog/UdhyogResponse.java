package com.spindia.rechargeapp.aadharUdhyog;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UdhyogResponse {
    @SerializedName("rtid")
    @Expose
    private String rtid;
    @SerializedName("formtype")
    @Expose
    private String formtype;
    @SerializedName("adminmsg")
    @Expose
    private Object adminmsg;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("createat")
    @Expose
    private String createat;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("emailId")
    @Expose
    private String emailId;
    @SerializedName("mobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("shopName")
    @Expose
    private String shopName;
    @SerializedName("panNo")
    @Expose
    private String panNo;
    @SerializedName("address")
    @Expose
    private String address;

    public String getRtid() {
        return rtid;
    }

    public void setRtid(String rtid) {
        this.rtid = rtid;
    }

    public String getFormtype() {
        return formtype;
    }

    public void setFormtype(String formtype) {
        this.formtype = formtype;
    }

    public Object getAdminmsg() {
        return adminmsg;
    }

    public void setAdminmsg(Object adminmsg) {
        this.adminmsg = adminmsg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateat() {
        return createat;
    }

    public void setCreateat(String createat) {
        this.createat = createat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
