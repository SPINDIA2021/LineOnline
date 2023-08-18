package com.spindia.rechargeapp.admin.recharge;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdminRechargeResponse {

    @SerializedName("recid")
    @Expose
    private Integer recid;
    @SerializedName("apiclid")
    @Expose
    private String apiclid;
    @SerializedName("apisourceid")
    @Expose
    private String apisourceid;
    @SerializedName("requestertxnid")
    @Expose
    private String requestertxnid;
    @SerializedName("mobileno")
    @Expose
    private String mobileno;
    @SerializedName("operator")
    @Expose
    private String operator;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("recmedium")
    @Expose
    private String recmedium;
    @SerializedName("reqtime")
    @Expose
    private String reqtime;
    @SerializedName("restime")
    @Expose
    private String restime;
    @SerializedName("reqdate")
    @Expose
    private String reqdate;
    @SerializedName("retailer")
    @Expose
    private String retailer;
    @SerializedName("distributor")
    @Expose
    private String distributor;
    @SerializedName("master")
    @Expose
    private String master;
    @SerializedName("ret_com_type")
    @Expose
    private String retComType;
    @SerializedName("dis_com_type")
    @Expose
    private String disComType;
    @SerializedName("mas_com_type")
    @Expose
    private String masComType;
    @SerializedName("api_com_type")
    @Expose
    private String apiComType;
    @SerializedName("api")
    @Expose
    private String api;
    @SerializedName("profitloss")
    @Expose
    private String profitloss;
    @SerializedName("trnx_checktime")
    @Expose
    private Integer trnxChecktime;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("statusdesc")
    @Expose
    private String statusdesc;
    @SerializedName("responsecode")
    @Expose
    private String responsecode;
    @SerializedName("serverresponse")
    @Expose
    private String serverresponse;
    @SerializedName("promo_id")
    @Expose
    private Integer promoId;
    @SerializedName("ip")
    @Expose
    private String ip;
    @SerializedName("optional1")
    @Expose
    private String optional1;
    @SerializedName("optional2")
    @Expose
    private String optional2;
    @SerializedName("optional3")
    @Expose
    private String optional3;
    @SerializedName("optional4")
    @Expose
    private String optional4;
    @SerializedName("optional5")
    @Expose
    private String optional5;
    @SerializedName("extra")
    @Expose
    private String extra;
    @SerializedName("ret_commi")
    @Expose
    private String retCommi;
    @SerializedName("api_msg")
    @Expose
    private String apiMsg;
    @SerializedName("wellborn_trans_no")
    @Expose
    private String wellbornTransNo;
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("dispute_status")
    @Expose
    private String disputeStatus;
    @SerializedName("deviceName")
    @Expose
    private Object deviceName;
    @SerializedName("deviceId")
    @Expose
    private Object deviceId;
    @SerializedName("tds")
    @Expose
    private String tds;
    @SerializedName("cus_upi")
    @Expose
    private String cusUpi;
    @SerializedName("type")
    @Expose
    private String type;

    public Integer getRecid() {
        return recid;
    }

    public void setRecid(Integer recid) {
        this.recid = recid;
    }

    public String getApiclid() {
        return apiclid;
    }

    public void setApiclid(String apiclid) {
        this.apiclid = apiclid;
    }

    public String getApisourceid() {
        return apisourceid;
    }

    public void setApisourceid(String apisourceid) {
        this.apisourceid = apisourceid;
    }

    public String getRequestertxnid() {
        return requestertxnid;
    }

    public void setRequestertxnid(String requestertxnid) {
        this.requestertxnid = requestertxnid;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRecmedium() {
        return recmedium;
    }

    public void setRecmedium(String recmedium) {
        this.recmedium = recmedium;
    }

    public String getReqtime() {
        return reqtime;
    }

    public void setReqtime(String reqtime) {
        this.reqtime = reqtime;
    }

    public String getRestime() {
        return restime;
    }

    public void setRestime(String restime) {
        this.restime = restime;
    }

    public String getReqdate() {
        return reqdate;
    }

    public void setReqdate(String reqdate) {
        this.reqdate = reqdate;
    }

    public String getRetailer() {
        return retailer;
    }

    public void setRetailer(String retailer) {
        this.retailer = retailer;
    }

    public String getDistributor() {
        return distributor;
    }

    public void setDistributor(String distributor) {
        this.distributor = distributor;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getRetComType() {
        return retComType;
    }

    public void setRetComType(String retComType) {
        this.retComType = retComType;
    }

    public String getDisComType() {
        return disComType;
    }

    public void setDisComType(String disComType) {
        this.disComType = disComType;
    }

    public String getMasComType() {
        return masComType;
    }

    public void setMasComType(String masComType) {
        this.masComType = masComType;
    }

    public String getApiComType() {
        return apiComType;
    }

    public void setApiComType(String apiComType) {
        this.apiComType = apiComType;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getProfitloss() {
        return profitloss;
    }

    public void setProfitloss(String profitloss) {
        this.profitloss = profitloss;
    }

    public Integer getTrnxChecktime() {
        return trnxChecktime;
    }

    public void setTrnxChecktime(Integer trnxChecktime) {
        this.trnxChecktime = trnxChecktime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusdesc() {
        return statusdesc;
    }

    public void setStatusdesc(String statusdesc) {
        this.statusdesc = statusdesc;
    }

    public String getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }

    public String getServerresponse() {
        return serverresponse;
    }

    public void setServerresponse(String serverresponse) {
        this.serverresponse = serverresponse;
    }

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getOptional1() {
        return optional1;
    }

    public void setOptional1(String optional1) {
        this.optional1 = optional1;
    }

    public String getOptional2() {
        return optional2;
    }

    public void setOptional2(String optional2) {
        this.optional2 = optional2;
    }

    public String getOptional3() {
        return optional3;
    }

    public void setOptional3(String optional3) {
        this.optional3 = optional3;
    }

    public String getOptional4() {
        return optional4;
    }

    public void setOptional4(String optional4) {
        this.optional4 = optional4;
    }

    public String getOptional5() {
        return optional5;
    }

    public void setOptional5(String optional5) {
        this.optional5 = optional5;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getRetCommi() {
        return retCommi;
    }

    public void setRetCommi(String retCommi) {
        this.retCommi = retCommi;
    }

    public String getApiMsg() {
        return apiMsg;
    }

    public void setApiMsg(String apiMsg) {
        this.apiMsg = apiMsg;
    }

    public String getWellbornTransNo() {
        return wellbornTransNo;
    }

    public void setWellbornTransNo(String wellbornTransNo) {
        this.wellbornTransNo = wellbornTransNo;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDisputeStatus() {
        return disputeStatus;
    }

    public void setDisputeStatus(String disputeStatus) {
        this.disputeStatus = disputeStatus;
    }

    public Object getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(Object deviceName) {
        this.deviceName = deviceName;
    }

    public Object getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Object deviceId) {
        this.deviceId = deviceId;
    }

    public String getTds() {
        return tds;
    }

    public void setTds(String tds) {
        this.tds = tds;
    }

    public String getCusUpi() {
        return cusUpi;
    }

    public void setCusUpi(String cusUpi) {
        this.cusUpi = cusUpi;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
