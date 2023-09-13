package com.spindia.rechargeapp.electricity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FetchBillResponse {

    @SerializedName("connectionProfile")
    @Expose
    private var connectionProfile: ConnectionProfile? = null

    @SerializedName("paymentItemDetails")
    @Expose
    private var paymentItemDetails: List<PaymentItemDetail?>? = null

    @SerializedName("linkPayAdditionalInfo")
    @Expose
    private var linkPayAdditionalInfo: Any? = null

    fun getConnectionProfile(): ConnectionProfile? {
        return connectionProfile
    }

    fun setConnectionProfile(connectionProfile: ConnectionProfile?) {
        this.connectionProfile = connectionProfile
    }

    fun getPaymentItemDetails(): List<PaymentItemDetail?>? {
        return paymentItemDetails
    }

    fun setPaymentItemDetails(paymentItemDetails: List<PaymentItemDetail?>?) {
        this.paymentItemDetails = paymentItemDetails
    }

    fun getLinkPayAdditionalInfo(): Any? {
        return linkPayAdditionalInfo
    }

    fun setLinkPayAdditionalInfo(linkPayAdditionalInfo: Any?) {
        this.linkPayAdditionalInfo = linkPayAdditionalInfo
    }

}