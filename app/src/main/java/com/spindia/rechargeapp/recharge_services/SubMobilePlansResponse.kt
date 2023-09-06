package com.spindia.rechargeapp.recharge_services

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SubMobilePlansResponse {

    @SerializedName("plans")
    @Expose
    private var mobilePlansList: List<MobilePlansList?>? = null

    fun getMobilePlansList(): List<MobilePlansList?>? {
        return mobilePlansList
    }

    fun setMobilePlansList(mobilePlansList: List<MobilePlansList?>?) {
        this.mobilePlansList = mobilePlansList
    }
}