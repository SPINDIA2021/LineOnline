package com.spindia.rechargeapp.recharge_services

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MobilePlansList {

    @SerializedName("id")
    @Expose
    private var id: String? = null

    @SerializedName("operatorId")
    @Expose
    private var operatorId: Int? = null

    @SerializedName("circleId")
    @Expose
    private var circleId: Int? = null

    @SerializedName("planType")
    @Expose
    private var planType: Int? = null

    @SerializedName("planTab")
    @Expose
    private var planTab: String? = null

    @SerializedName("planCode")
    @Expose
    private var planCode: String? = null

    @SerializedName("amount")
    @Expose
    private var amount: Double? = null

    @SerializedName("talktime")
    @Expose
    private var talktime: Double? = null

    @SerializedName("validity")
    @Expose
    private var validity: String? = null

    @SerializedName("planName")
    @Expose
    private var planName: String? = null

    @SerializedName("planDescription")
    @Expose
    private var planDescription: String? = null

    @SerializedName("dataBenefit")
    @Expose
    private var dataBenefit: Any? = null

    @SerializedName("isPopular")
    @Expose
    private var isPopular: Int? = null

    @SerializedName("isPromoted")
    @Expose
    private var isPromoted: Int? = null

    @SerializedName("isHidden")
    @Expose
    private var isHidden: Int? = null

    @SerializedName("stringTalktime")
    @Expose
    private var stringTalktime: String? = null

    @SerializedName("special")
    @Expose
    private var special: Boolean? = null

    @SerializedName("validityInDays")
    @Expose
    private var validityInDays: Int? = null

    @SerializedName("languageRegion")
    @Expose
    private var languageRegion: List<String?>? = null

    @SerializedName("type")
    @Expose
    private var type: List<String?>? = null

    @SerializedName("planTypeForWeb")
    @Expose
    private var planTypeForWeb: Int? = null

    @SerializedName("planDetailItemList")
    @Expose
    private var planDetailItemList: List<PlanDetailItem?>? = null

    @SerializedName("planTypeList")
    @Expose
    private var planTypeList: List<Int?>? = null

    @SerializedName("updatedOn")
    @Expose
    private var updatedOn: Long? = null

    @SerializedName("specialTag")
    @Expose
    private var specialTag: Any? = null

    @SerializedName("specialTagBgColor")
    @Expose
    private var specialTagBgColor: Any? = null

    @SerializedName("planBenefitItemList")
    @Expose
    private var planBenefitItemList: Any? = null

    fun getId(): String? {
        return id
    }

    fun setId(id: String?) {
        this.id = id
    }

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

    fun getPlanType(): Int? {
        return planType
    }

    fun setPlanType(planType: Int?) {
        this.planType = planType
    }

    fun getPlanTab(): String? {
        return planTab
    }

    fun setPlanTab(planTab: String?) {
        this.planTab = planTab
    }

    fun getPlanCode(): String? {
        return planCode
    }

    fun setPlanCode(planCode: String?) {
        this.planCode = planCode
    }

    fun getAmount(): Double? {
        return amount
    }

    fun setAmount(amount: Double?) {
        this.amount = amount
    }

    fun getTalktime(): Double? {
        return talktime
    }

    fun setTalktime(talktime: Double?) {
        this.talktime = talktime
    }

    fun getValidity(): String? {
        return validity
    }

    fun setValidity(validity: String?) {
        this.validity = validity
    }

    fun getPlanName(): String? {
        return planName
    }

    fun setPlanName(planName: String?) {
        this.planName = planName
    }

    fun getPlanDescription(): String? {
        return planDescription
    }

    fun setPlanDescription(planDescription: String?) {
        this.planDescription = planDescription
    }

    fun getDataBenefit(): Any? {
        return dataBenefit
    }

    fun setDataBenefit(dataBenefit: Any?) {
        this.dataBenefit = dataBenefit
    }

    fun getIsPopular(): Int? {
        return isPopular
    }

    fun setIsPopular(isPopular: Int?) {
        this.isPopular = isPopular
    }

    fun getIsPromoted(): Int? {
        return isPromoted
    }

    fun setIsPromoted(isPromoted: Int?) {
        this.isPromoted = isPromoted
    }

    fun getIsHidden(): Int? {
        return isHidden
    }

    fun setIsHidden(isHidden: Int?) {
        this.isHidden = isHidden
    }

    fun getStringTalktime(): String? {
        return stringTalktime
    }

    fun setStringTalktime(stringTalktime: String?) {
        this.stringTalktime = stringTalktime
    }

    fun getSpecial(): Boolean? {
        return special
    }

    fun setSpecial(special: Boolean?) {
        this.special = special
    }

    fun getValidityInDays(): Int? {
        return validityInDays
    }

    fun setValidityInDays(validityInDays: Int?) {
        this.validityInDays = validityInDays
    }

    fun getLanguageRegion(): List<String?>? {
        return languageRegion
    }

    fun setLanguageRegion(languageRegion: List<String?>?) {
        this.languageRegion = languageRegion
    }

    fun getType(): List<String?>? {
        return type
    }

    fun setType(type: List<String?>?) {
        this.type = type
    }

    fun getPlanTypeForWeb(): Int? {
        return planTypeForWeb
    }

    fun setPlanTypeForWeb(planTypeForWeb: Int?) {
        this.planTypeForWeb = planTypeForWeb
    }

    fun getPlanDetailItemList(): List<PlanDetailItem?>? {
        return planDetailItemList
    }

    fun setPlanDetailItemList(planDetailItemList: List<PlanDetailItem?>?) {
        this.planDetailItemList = planDetailItemList
    }

    fun getPlanTypeList(): List<Int?>? {
        return planTypeList
    }

    fun setPlanTypeList(planTypeList: List<Int?>?) {
        this.planTypeList = planTypeList
    }

    fun getUpdatedOn(): Long? {
        return updatedOn
    }

    fun setUpdatedOn(updatedOn: Long?) {
        this.updatedOn = updatedOn
    }

    fun getSpecialTag(): Any? {
        return specialTag
    }

    fun setSpecialTag(specialTag: Any?) {
        this.specialTag = specialTag
    }

    fun getSpecialTagBgColor(): Any? {
        return specialTagBgColor
    }

    fun setSpecialTagBgColor(specialTagBgColor: Any?) {
        this.specialTagBgColor = specialTagBgColor
    }

    fun getPlanBenefitItemList(): Any? {
        return planBenefitItemList
    }

    fun setPlanBenefitItemList(planBenefitItemList: Any?) {
        this.planBenefitItemList = planBenefitItemList
    }

}