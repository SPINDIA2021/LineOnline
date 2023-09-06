package com.spindia.rechargeapp.recharge_services

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MainMobilePlans {
    @SerializedName("success")
    @Expose
    private var success: Boolean? = null

    @SerializedName("data")
    @Expose
    private var subMobilePlansResponse: SubMobilePlansResponse? = null

    fun getSuccess(): Boolean? {
        return success
    }

    fun setSuccess(success: Boolean?) {
        this.success = success
    }

    fun getSubMobilePlansResponse(): SubMobilePlansResponse? {
        return subMobilePlansResponse
    }

    fun setSubMobilePlansResponse(subMobilePlansResponse: SubMobilePlansResponse?) {
        this.subMobilePlansResponse = subMobilePlansResponse
    }

}