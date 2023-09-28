package com.spindia.rechargeapp.itr;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ITRResponse {
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
    @SerializedName("panNo")
    @Expose
    private String panNo;
    @SerializedName("ITRYear")
    @Expose
    private String iTRYear;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("middleName")
    @Expose
    private String middleName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("fatherName")
    @Expose
    private String fatherName;
    @SerializedName("mobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("emailId")
    @Expose
    private String emailId;
    @SerializedName("aadharNo")
    @Expose
    private String aadharNo;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("fullAddress")
    @Expose
    private String fullAddress;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("itrCategory")
    @Expose
    private String itrCategory;
    @SerializedName("itrBusinessCategory")
    @Expose
    private String itrBusinessCategory;
    @SerializedName("bankName")
    @Expose
    private String bankName;
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("accountNumber")
    @Expose
    private String accountNumber;
    @SerializedName("ifscCode")
    @Expose
    private String ifscCode;
    @SerializedName("branchName")
    @Expose
    private String branchName;
    @SerializedName("accountType")
    @Expose
    private String accountType;

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

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getITRYear() {
        return iTRYear;
    }

    public void setITRYear(String iTRYear) {
        this.iTRYear = iTRYear;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(String aadharNo) {
        this.aadharNo = aadharNo;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getItrCategory() {
        return itrCategory;
    }

    public void setItrCategory(String itrCategory) {
        this.itrCategory = itrCategory;
    }

    public String getItrBusinessCategory() {
        return itrBusinessCategory;
    }

    public void setItrBusinessCategory(String itrBusinessCategory) {
        this.itrBusinessCategory = itrBusinessCategory;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

}
