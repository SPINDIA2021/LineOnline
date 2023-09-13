package com.spindia.rechargeapp.electricity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LineItem {

    @SerializedName("key")
    @Expose
    private var key: String? = null

    @SerializedName("keyDisplayName")
    @Expose
    private var keyDisplayName: String? = null

    @SerializedName("value")
    @Expose
    private var value: String? = null

    @SerializedName("screen")
    @Expose
    private var screen: List<Int?>? = null

    @SerializedName("requiredForPayment")
    @Expose
    private var requiredForPayment: Boolean? = null

    fun getKey(): String? {
        return key
    }

    fun setKey(key: String?) {
        this.key = key
    }

    fun getKeyDisplayName(): String? {
        return keyDisplayName
    }

    fun setKeyDisplayName(keyDisplayName: String?) {
        this.keyDisplayName = keyDisplayName
    }

    fun getValue(): String? {
        return value
    }

    fun setValue(value: String?) {
        this.value = value
    }

    fun getScreen(): List<Int?>? {
        return screen
    }

    fun setScreen(screen: List<Int?>?) {
        this.screen = screen
    }

    fun getRequiredForPayment(): Boolean? {
        return requiredForPayment
    }

    fun setRequiredForPayment(requiredForPayment: Boolean?) {
        this.requiredForPayment = requiredForPayment
    }
}