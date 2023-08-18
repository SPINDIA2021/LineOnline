package com.spindia.rechargeapp.recharge_services

import com.spindia.rechargeapp.model.OfferSModel
import java.io.Serializable

class OfferInnerModelClss : Serializable{

        var data: String? = null

        var isOpen = false
        var nestedDetailResponse: ArrayList<OfferSModel> =
            ArrayList<OfferSModel>()


}