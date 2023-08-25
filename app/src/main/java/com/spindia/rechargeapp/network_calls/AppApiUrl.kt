package com.spindia.rechargeapp.network_calls

import com.spindia.rechargeapp.utils.AppConstants


object AppApiUrl : AppConstants {
    //Production Environment
    var BASE_URL = "http://spindiabazaar.com"
    var BASE_URL1 = "http://spindiapan.com/"
   // var BASE_URL = "http://aeps.shreeramenterprises.org.in/"
    var OFFERS_URL = BASE_URL

    var IMAGE_URL = "http://spindiabazaar.com"

    //API URLS


    //GENERAL APP USER URLS

    //url =/applogin/
    val LOGIN_BY_PASSWORD: String = "$BASE_URL/applogin/userLogin"
    val REGISTER_USER: String = "$BASE_URL/applogin/registerenquiry"
    val REGISTER: String = "$BASE_URL/applogin/register_user"
    val GETFORGOTOTP: String = BASE_URL + "/applogin/getpassotp"
    val FORGOT_PASS: String = BASE_URL + "/applogin/forgetPass"
    val NOTE: String = BASE_URL+"/applogin/note"

    // url =/appapi/
    val GET_AEPS_BALANCE: String = "$BASE_URL/MobileAppapi/aepsBalance"

    val GET_DASHBOARD: String = "$BASE_URL/MobileAppapi/dashboard"
    val GET_BALANCE: String = "$BASE_URL/MobileAppapi/walletBalance"
    val GET_PROFILE: String = "$BASE_URL/MobileAppapi/profile"
    val GET_OPERATORS: String = "$BASE_URL/MobileAppapi/getOperators"
    val GET_ELECTIRCITY_OPERATORS: String = "$BASE_URL/MobileAppapi/getOperators"
    val CHECK_IF_SAME_RECHARGE: String = "$BASE_URL/MobileAppapi/checkIfSameRecharge"
    val VERIFY_PIN: String = "$BASE_URL/MobileAppapi/verifyPin"
    val RECHARGE_HISTORY: String = "$BASE_URL/MobileAppapi/shareapi_rechargelist"

    val CIRCLE: String = "$BASE_URL/MobileAppapi/circles"


    //*********************Do Not Copy*********************************//
    val CHANGE_PIN: String = BASE_URL + "/MobileAppapi/changepin"
    val FUND_CREDIT: String = BASE_URL + "/MobileAppapi/viewcreditwallet"
    val FUND_DEBIT: String = BASE_URL + "/MobileAppapi/viewdebitwallet"
    val CHECKSAME_FUNDTRANSFER: String = BASE_URL + "/MobileAppapi/checkifsamefundtransfer"
    val LEDGER_REPORT: String = BASE_URL + "/MobileAppapi/ledgerfromto"
    val COMMISION_REPORT_URL: String = BASE_URL + "/MobileAppapi/getcommslab"
    val BROWSE_PLANS = OFFERS_URL + "/MobileAppapi/roffer_shareapi"
    val BROWSE_PLANS_DTH = OFFERS_URL + "/MobileAppapi/Dthinfo"
    val GETBILLDETAILS = OFFERS_URL + "/MobileAppapi/ElectricityInfo"
    val DISSPUTE_HISTORY: String = BASE_URL + "/MobileAppapi/disputehistory"
    val NEWUSER_URL: String = BASE_URL + "/MobileAppapi/create_retailer_api"
    val NEW_DISTRIBUTOR_URL: String = BASE_URL + "/MobileAppapi/create_distributor_api"
    val USER_LIST: String = BASE_URL + "/MobileAppapi/user_list"
    val GET_USER_ID: String = BASE_URL + "/MobileAppapi/getcusid"
    val FUND_TRANSFER: String = BASE_URL + "/MobileAppapi/direct_credit"
    val RAISE_DISPUTE: String = BASE_URL + "/MobileAppapi/submitdispute"
    val FUND_REQUEST_URL: String = BASE_URL + "/MobileAppapi/fundreq"
    val GET_SUPPORT: String = BASE_URL + "/MobileAppapi/support"
    val USER_DAYBOOK: String = BASE_URL + "/MobileAppapi/userdaybook"
    val UPDATE_WALLET: String = BASE_URL + "/MobileAppapi/add_wallet_balance"
    val GET_UPIDETAILS: String = BASE_URL + "/MobileAppapi/getupidetails"
    val LOGOUT_USER: String = BASE_URL + "/MobileAppapi/userlogout"
    val GETPINOTP: String = BASE_URL + "/MobileAppapi/getpinotp"
    val FORGETPIN: String = BASE_URL + "/MobileAppapi/forgetpin"

    val FUND_MYREQUEST: String = BASE_URL + "/MobileAppapi/viewmyfundreq"

    val USER_SEARCH: String = BASE_URL + "/MobileAppapi/user_list_byname_or_mobile"

    val CHANGE_PASWORD: String = BASE_URL + "/MobileAppapi/changepassword"

    val RECHARGE_HISTORY_BY_MOBILE: String = BASE_URL + "MobileAppapi/rechargehistorybymobile"

    val RECHARGE_HISTORY_BY_DATE: String = BASE_URL + "MobileAppapi/rechargehistorybydate"

    //DMT APIS
    val DMT_LOGIN: String =
        BASE_URL + "/dmt/verifySender"

    val DMT_SENDOTP: String =
        BASE_URL + "/dmt/verifySenderOtp"

    val DMT_REGISTER: String =
        BASE_URL + "/dmt/registerSender"

    val DMT_ADD_BENFICIARY: String =
        BASE_URL + "/dmt/addBeneficiary"

    val DMT_VIEW_BENIFICIARY: String =
        BASE_URL + "/dmt/getBeneficiary"

    val DELETE_RECIPIENT: String =
        BASE_URL + "/dmt/deleteBeneficiary"

    val GET_CHARGE: String =
        BASE_URL + "/dmt/getCharge"

    val DMT_TRANSACTION: String =
        BASE_URL + "/dmt/moneyTransfer"

    val DMT_HISTORY: String =
        BASE_URL + "/dmt/getDmtHistory"

    val CHECK_STATUS: String =
        BASE_URL + "/dmt/checkStatus"

    val DMT_BANK_LIST: String =
        BASE_URL + "/dmt/getBankNames"

    val VERIFY_BANK: String =
        BASE_URL + "/dmt/accountValidation"

    val DMT_COMMISIONSLAB_URL: String =
        BASE_URL + "/dmt/dmt_commission_slab"

    val GET_DMT_SERVICE: String =
        BASE_URL + "/dmt/getDmtApiList"

    //AEPS
    val GET_AEPS_CHARGE: String =
        BASE_URL + "/aeps/getCharge"

    val AEPS_HISTORY: String =
        BASE_URL + "/aeps/aeps_history"

    val AEPS_COMMISIONSLAB_URL: String =
        BASE_URL + "/aeps/aeps_commission_slab"

    val AEPS_BANK_LIST: String =
        BASE_URL + "/aeps/getbank"

    val DUMMY_PID: String =
        BASE_URL + "/aeps/getPidData"

    val AEPS_TRANSACTION: String =
        BASE_URL + "/aeps/aepsapi"

    val AEPS_PAYOUT: String =
        BASE_URL + "/aeps/submitPayout"

    val GET_PAYOUT_DETAILS: String =
        BASE_URL + "/aeps/getPayoutDetails"

    val AEPSPAYOUT_HISTORY: String =
        BASE_URL + "/aeps/payoutHistory"

    val AEPSCOMMISSION_HISTORY: String =
        BASE_URL + "/aeps/aeps_history"

    val USER_PAYOUT_BANK: String =
        BASE_URL + "/aeps/userpayoutbank"

    //MICRO ATM
    val MICRO_ATM_TRANSACTION: String =
        BASE_URL + "/aeps/submitMicroAtmResponse"

    val MICRO_ATM_LOGIN: String =
        BASE_URL + "/aeps/microAtmDetails"
    //******************************************************************//

    // url =rechargeapi/recharge
    val RECHARGE: String = "$BASE_URL/MobileAppapi/shareapi_recharge"

    val GET_PRODUCTS: String =
        BASE_URL + "/MobileAppapi/getproduct"

    val GET_PRODUCT_AND_SERVICE: String =
        BASE_URL + "/MobileAppapi/getproductandservice"

    val GET_SINGLE_PRODUCT_AND_SERVICE: String =
            BASE_URL + "/MobileAppapi/getsingleproductandservice"

    val GET_SERVICE_STATUS: String =
        BASE_URL + "/MobileAppapi/getservicestatus"

    val GET_SERVICE_AMOUNT: String =
        BASE_URL + "/MobileAppapi/getserviceamount"

    val BUY_SERVICE: String =
        BASE_URL + "/MobileAppapi/buyservice"

    val BUY_PRODUCT: String =
        BASE_URL + "/MobileAppapi/buyproduct"

    val PRODUCT_SERVICES_HISTORY: String =
        BASE_URL + "/MobileAppapi/productserviceshistory"

    val OFFER_POPUP: String =
        BASE_URL + "/MobileAppapi/offerpopup"

    val CASH_DEPOSIT_WITH_OTP: String =
        BASE_URL + "/aeps/cashdepositwithotp"

    val VALIDATE_CASH_DEPOSIT_OTP: String =
        BASE_URL + "/aeps/validateCdOtp"

    val BUY_PRODUCT_AND_SERVICE: String =
            BASE_URL + "/MobileAppapi/buyproductandservice"

    val KYC_ONBOARDING: String =
        BASE_URL + "aeps/onboarding"

    val EKYC: String =
        BASE_URL + "/aeps/ekycsubmit"

    val VALIDATE_EKYC_OTP: String =
        BASE_URL + "/aeps/validateekycotp"

    val RESEND_EKYC_OTP: String =
        BASE_URL + "/aeps/resendekycotp"

    val STATE_LIST: String =
        BASE_URL + "/aeps/getaepsstate"

    val CHECK_KYC_STATUS: String =
        BASE_URL + "/aeps/checkkycstatus"

    // UPI Collection

    val DYNAMIC_QR_CODE: String =
        BASE_URL + "/Upi/upicollection_dynamicqr"

    val UPI_SEND_REQUEST: String =
        BASE_URL + "/Upi/upicollection_sendrequest"
}