package com.spindia.rechargeapp.electricity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MainFetchBillResponse {

    @SerializedName("success")
    @Expose
    private var success: Boolean? = null

    @SerializedName("data")
    @Expose
    private var fetchBillResponse: FetchBillResponse? = null

    fun getSuccess(): Boolean? {
        return success
    }

    fun setSuccess(success: Boolean?) {
        this.success = success
    }

    fun getFetchBillResponse(): FetchBillResponse? {
        return fetchBillResponse
    }

    fun setFetchBillResponse(fetchBillResponse: FetchBillResponse?) {
        this.fetchBillResponse = fetchBillResponse
    }
}