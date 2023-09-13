package com.spindia.rechargeapp.recharge_services

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MainOperatorResponse {

    @SerializedName("success")
    @Expose
    private var success: Boolean? = null

    @SerializedName("data")
    @Expose
    private var operatorResponse: OperatorResponse? = null

    fun getSuccess(): Boolean? {
        return success
    }

    fun setSuccess(success: Boolean?) {
        this.success = success
    }

    fun getOperatorResponse(): OperatorResponse? {
        return operatorResponse
    }

    fun setOperatorResponse(operatorResponse: OperatorResponse?) {
        this.operatorResponse = operatorResponse
    }

}