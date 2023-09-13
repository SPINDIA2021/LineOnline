package com.spindia.rechargeapp.recharge_services

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OperatorResponse {

    @SerializedName("operatorId")
    @Expose
    private var operatorId: Int? = null

    @SerializedName("circleId")
    @Expose
    private var circleId: Int? = null

    fun getOperatorId(): Int? {
        return operatorId
    }

    fun setOperatorId(operatorId: Int?) {
        this.operatorId = operatorId
    }

    fun getCircleId(): Int? {
        return circleId
    }

    fun setCircleId(circleId: Int?) {
        this.circleId = circleId
    }
}