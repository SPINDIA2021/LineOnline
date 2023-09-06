package com.spindia.rechargeapp.recharge_services

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PlanDetailItem {


    @SerializedName("key")
    @Expose
    private var key: String? = null

    @SerializedName("displayName")
    @Expose
    private var displayName: String? = null

    @SerializedName("displayValue")
    @Expose
    private var displayValue: String? = null

    fun getKey(): String? {
        return key
    }

    fun setKey(key: String?) {
        this.key = key
    }

    fun getDisplayName(): String? {
        return displayName
    }

    fun setDisplayName(displayName: String?) {
        this.displayName = displayName
    }

    fun getDisplayValue(): String? {
        return displayValue
    }

    fun setDisplayValue(displayValue: String?) {
        this.displayValue = displayValue
    }
}