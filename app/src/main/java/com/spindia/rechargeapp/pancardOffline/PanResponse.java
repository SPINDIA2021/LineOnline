package com.spindia.rechargeapp.pancardOffline;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PanResponse {


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("memberid")
    @Expose
    private String memberid;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("fathername")
    @Expose
    private String fathername;
    @SerializedName("aadhar")
    @Expose
    private String aadhar;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("sign")
    @Expose
    private String sign;
    @SerializedName("ratype")
    @Expose
    private String ratype;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("guardians_aadhar")
    @Expose
    private String guardiansAadhar;
    @SerializedName("createat")
    @Expose
    private String createat;
    @SerializedName("paymentstatus")
    @Expose
    private String paymentstatus;
    @SerializedName("receiptno")
    @Expose
    private Object receiptno;
    @SerializedName("receiptfile")
    @Expose
    private Object receiptfile;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("adminmsg")
    @Expose
    private Object adminmsg;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFathername() {
        return fathername;
    }

    public void setFathername(String fathername) {
        this.fathername = fathername;
    }

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getRatype() {
        return ratype;
    }

    public void setRatype(String ratype) {
        this.ratype = ratype;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGuardiansAadhar() {
        return guardiansAadhar;
    }

    public void setGuardiansAadhar(String guardiansAadhar) {
        this.guardiansAadhar = guardiansAadhar;
    }

    public String getCreateat() {
        return createat;
    }

    public void setCreateat(String createat) {
        this.createat = createat;
    }

    public String getPaymentstatus() {
        return paymentstatus;
    }

    public void setPaymentstatus(String paymentstatus) {
        this.paymentstatus = paymentstatus;
    }

    public Object getReceiptno() {
        return receiptno;
    }

    public void setReceiptno(Object receiptno) {
        this.receiptno = receiptno;
    }

    public Object getReceiptfile() {
        return receiptfile;
    }

    public void setReceiptfile(Object receiptfile) {
        this.receiptfile = receiptfile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getAdminmsg() {
        return adminmsg;
    }

    public void setAdminmsg(Object adminmsg) {
        this.adminmsg = adminmsg;
    }
}
