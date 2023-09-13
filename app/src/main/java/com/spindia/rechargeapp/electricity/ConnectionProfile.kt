package com.spindia.rechargeapp.electricity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ConnectionProfile {

    @SerializedName("lineItemList")
    @Expose
    private var lineItemList: List<LineItem?>? = null

    fun getLineItemList(): List<LineItem?>? {
        return lineItemList
    }

    fun setLineItemList(lineItemList: List<LineItem?>?) {
        this.lineItemList = lineItemList
    }
}