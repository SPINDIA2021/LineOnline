package com.spindia.rechargeapp.paytmIntegration

import com.google.gson.annotations.SerializedName

class Checksum(
    @field:SerializedName("CHECKSUMHASH") val checksumHash: String, @field:SerializedName(
        "ORDER_ID"
    ) val orderId: String, @field:SerializedName("payt_STATUS") val paytStatus: String
)