package com.spindia.rechargeapp.electricity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PaymentItemDetail {

    @SerializedName("disablePayment")
    @Expose
    private var disablePayment: Boolean? = null

    @SerializedName("partPayAllowed")
    @Expose
    private var partPayAllowed: Boolean? = null

    @SerializedName("lineItemList")
    @Expose
    private var lineItemList: List<LineItem__1?>? = null

    fun getDisablePayment(): Boolean? {
        return disablePayment
    }

    fun setDisablePayment(disablePayment: Boolean?) {
        this.disablePayment = disablePayment
    }

    fun getPartPayAllowed(): Boolean? {
        return partPayAllowed
    }

    fun setPartPayAllowed(partPayAllowed: Boolean?) {
        this.partPayAllowed = partPayAllowed
    }

    fun getLineItemList(): List<LineItem__1?>? {
        return lineItemList
    }

    fun setLineItemList(lineItemList: List<LineItem__1?>?) {
        this.lineItemList = lineItemList
    }
}