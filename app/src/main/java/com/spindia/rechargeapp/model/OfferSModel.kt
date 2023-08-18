package com.spindia.rechargeapp.model

class OfferSModel {
    var recharge_amount: String? = null
    var recharge_long_desc: String? = null
    var recharge_type: String?=null

    override fun toString(): String {
        return "ClassPojo [recharge_amount = $recharge_amount, recharge_long_desc = $recharge_long_desc, recharge_type = $recharge_type]"
    }
}

