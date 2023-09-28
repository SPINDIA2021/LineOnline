package com.spindia.rechargeapp.gstRegstration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GSTResponse {

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
    @SerializedName("companyName")
    @Expose
    private String companyName;
    @SerializedName("businessOwner")
    @Expose
    private String businessOwner;
    @SerializedName("fatherName")
    @Expose
    private String fatherName;
    @SerializedName("panNo")
    @Expose
    private String panNo;
    @SerializedName("mobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("addressLine1")
    @Expose
    private String addressLine1;
    @SerializedName("addressLine2")
    @Expose
    private String addressLine2;
    @SerializedName("addressCity")
    @Expose
    private String addressCity;
    @SerializedName("addressState")
    @Expose
    private String addressState;
    @SerializedName("addressPin")
    @Expose
    private String addressPin;
    @SerializedName("businessType")
    @Expose
    private String businessType;
    @SerializedName("businessDesc")
    @Expose
    private String businessDesc;
    @SerializedName("sameAsResidenceAddress")
    @Expose
    private String sameAsResidenceAddress;
    @SerializedName("addressBusiLine1")
    @Expose
    private String addressBusiLine1;
    @SerializedName("addressBusiLine2")
    @Expose
    private String addressBusiLine2;
    @SerializedName("addressBusiCity")
    @Expose
    private String addressBusiCity;
    @SerializedName("addressBusiState")
    @Expose
    private String addressBusiState;
    @SerializedName("addressBusiPin")
    @Expose
    private String addressBusiPin;

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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBusinessOwner() {
        return businessOwner;
    }

    public void setBusinessOwner(String businessOwner) {
        this.businessOwner = businessOwner;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getAddressState() {
        return addressState;
    }

    public void setAddressState(String addressState) {
        this.addressState = addressState;
    }

    public String getAddressPin() {
        return addressPin;
    }

    public void setAddressPin(String addressPin) {
        this.addressPin = addressPin;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessDesc() {
        return businessDesc;
    }

    public void setBusinessDesc(String businessDesc) {
        this.businessDesc = businessDesc;
    }

    public String getSameAsResidenceAddress() {
        return sameAsResidenceAddress;
    }

    public void setSameAsResidenceAddress(String sameAsResidenceAddress) {
        this.sameAsResidenceAddress = sameAsResidenceAddress;
    }

    public String getAddressBusiLine1() {
        return addressBusiLine1;
    }

    public void setAddressBusiLine1(String addressBusiLine1) {
        this.addressBusiLine1 = addressBusiLine1;
    }

    public String getAddressBusiLine2() {
        return addressBusiLine2;
    }

    public void setAddressBusiLine2(String addressBusiLine2) {
        this.addressBusiLine2 = addressBusiLine2;
    }

    public String getAddressBusiCity() {
        return addressBusiCity;
    }

    public void setAddressBusiCity(String addressBusiCity) {
        this.addressBusiCity = addressBusiCity;
    }

    public String getAddressBusiState() {
        return addressBusiState;
    }

    public void setAddressBusiState(String addressBusiState) {
        this.addressBusiState = addressBusiState;
    }

    public String getAddressBusiPin() {
        return addressBusiPin;
    }

    public void setAddressBusiPin(String addressBusiPin) {
        this.addressBusiPin = addressBusiPin;
    }

}
