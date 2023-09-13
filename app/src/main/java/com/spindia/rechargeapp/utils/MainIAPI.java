package com.spindia.rechargeapp.utils;


import com.spindia.rechargeapp.admin.BaseAdminRechargeResponse;
import com.spindia.rechargeapp.admin.pan.BaseAdminPancardListResponse;
import com.spindia.rechargeapp.admin.pvc.AdminPVCUpdateResponse;
import com.spindia.rechargeapp.admin.pvc.BaseAdminPVCResponse;
import com.spindia.rechargeapp.admin.user.BaseUserListResponse;
import com.spindia.rechargeapp.authentication.response.LoginMainResponse;
import com.spindia.rechargeapp.authentication.response.BaseCheckMobileResponse;
import com.spindia.rechargeapp.authentication.response.FalseCheckMobileResponse;
import com.spindia.rechargeapp.authentication.response.WalletResponse;
import com.spindia.rechargeapp.electricity.MainFetchBillResponse;
import com.spindia.rechargeapp.model.OfferSModel;
import com.spindia.rechargeapp.pancardOffline.BaseHeadingResponse;
import com.spindia.rechargeapp.pancardOffline.BasePanResponse;
import com.spindia.rechargeapp.pancardlist.BasePanCardlistResponse;
import com.spindia.rechargeapp.pancardlist.BaseUpdatePaymentStatusResponse;
import com.spindia.rechargeapp.pancardlist.MainCaptchaResponse;
import com.spindia.rechargeapp.pancardlist.SaveCaptchaResponse;
import com.spindia.rechargeapp.pvc.BasePVCListResponse;
import com.spindia.rechargeapp.pvc.BasePVCResponse;
import com.spindia.rechargeapp.pvc.BaseUpdatePVCResponse;
import com.spindia.rechargeapp.recharge_services.MainMobilePlans;
import com.spindia.rechargeapp.recharge_services.MainOperatorResponse;
import com.spindia.rechargeapp.walletHistory.BaseWalletHistoryResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface MainIAPI {

    // String BASE_URL="https://api.mewarpe.com/";
    String BASE_URL = "https://spindiabazaar.com/";
    String BASE_URL1 = "http://spindiapan.com/";
    String BASE_URL_BROWSEPLANS = "http://206.72.202.90:8050/";


    @Multipart
    @POST("MobileAppapi/shareapi_checkmobile")
    Call<BaseCheckMobileResponse> checkMobile(@Part("mobile") RequestBody mobile);

    @Multipart
    @POST("MobileAppapi/shareapi_checkmobile")
    Call<FalseCheckMobileResponse> falseCheckMobile(@Part("mobile") RequestBody mobile);


    @Multipart
    @POST("MobileAppapi/shareapi_login")
    Call<LoginMainResponse> login(@Part("mobile") RequestBody mobile, @Part("password") RequestBody password);

    @Multipart
    @POST("MobileAppapi/shareapi_login")
    Call<FalseCheckMobileResponse> falseLogin(@Part("mobile") RequestBody mobile, @Part("password") RequestBody password);


    @Multipart
    @POST("MobileAppapi/shareapi_signup")
    Call<LoginMainResponse> signUp(@Part("mobile") RequestBody mobile, @Part("email") RequestBody email, @Part("password") RequestBody password);

    @Multipart
    @POST("MobileAppapi/shareapi_signup")
    Call<FalseCheckMobileResponse> falseSignUp(@Part("mobile") RequestBody mobile,@Part("email") RequestBody email, @Part("password") RequestBody password);

    @Multipart
    @POST("api/offlinepancardapi")
    Call<BasePanResponse> pancardSave(@Part("retailerid") RequestBody retailerid, @Part("name") RequestBody name,
                                      @Part("email") RequestBody email, @Part("mobile") RequestBody mobile,
                                      @Part("fathername") RequestBody fathername, @Part("dob") RequestBody dob,
                                      @Part("ratype") RequestBody ratype, @Part MultipartBody.Part[] aadharfront,@Part MultipartBody.Part[] aadharback,
                                      @Part MultipartBody.Part[] photo, @Part MultipartBody.Part[] sign,
                                      @Part MultipartBody.Part[] guardians_aadhar);


    @Multipart
    @POST("api/offlinepancard_updatepancard")
    Call<BasePanResponse> pancardUpdate(@Part("retailerid") RequestBody retailerid, @Part("name") RequestBody name,
                                      @Part("email") RequestBody email, @Part("mobile") RequestBody mobile,
                                      @Part("fathername") RequestBody fathername, @Part("dob") RequestBody dob,
                                      @Part("ratype") RequestBody ratype, @Part MultipartBody.Part[] aadhar0,@Part MultipartBody.Part[] aadhar1,
                                      @Part MultipartBody.Part[] photo, @Part MultipartBody.Part[] sign,
                                      @Part MultipartBody.Part[] guardians_aadhar,@Part("tokenid") RequestBody tokenid);


    @GET("api/offlinepancardapi_list")
    Call<BasePanCardlistResponse> callGetPanListService(@Query("retailerid") String retailerid);


    @Multipart
    @POST("api/offlinepancard_updatepayment")
    Call<BaseUpdatePaymentStatusResponse> updateStatus(@Part("token") RequestBody token, @Part("status") RequestBody status);

    @Multipart
    @POST("MobileAppapi/getbalancesharerechargeapi")
    Call<WalletResponse> getWalletBalance(@Part("retailerid") RequestBody retailerid, @Part("entry") RequestBody entry);

    @Multipart
    @POST("MobileAppapi/getbalancesharerechargeapi")
    Call<BaseWalletHistoryResponse> getAllWalletHistory(@Part("retailerid") RequestBody retailerid, @Part("entry") RequestBody entry);


    @Multipart
    @POST("MobileAppapi/walletsharerechargeapi")
    Call<BaseWalletHistoryResponse> saveFailureRecharge(@Part("retailerid") RequestBody retailerid, @Part("type") RequestBody type,
                                                        @Part("amount") RequestBody amount, @Part("remark") RequestBody remark);


    @Multipart
    @POST("api/pvcprint_forapp")
    Call<BasePVCResponse> pvccardSave(@Part("retailerid") RequestBody retailerid, @Part("paymentstatus") RequestBody paymentstatus,
                                      @Part("name") RequestBody name, @Part("mobile") RequestBody mobile,
                                      @Part("totalpdfno") RequestBody totalpdfno, @Part("lang") RequestBody lang,
                                      @Part("lat") RequestBody lat, @Part("password1") RequestBody password1,
                                      @Part("password2") RequestBody password2, @Part("password3") RequestBody password3,
                                      @Part("password4") RequestBody password4, @Part("password5") RequestBody password5,
                                      @Part("password6") RequestBody password6, @Part("password7") RequestBody password7,
                                      @Part("password8") RequestBody password8, @Part("password9") RequestBody password9,
                                      @Part("password10") RequestBody password10, @Part MultipartBody.Part[] pdf1,
                                      @Part MultipartBody.Part[] pdf2, @Part MultipartBody.Part[] pdf3,
                                      @Part MultipartBody.Part[] pdf4, @Part MultipartBody.Part[] pdf5,
                                      @Part MultipartBody.Part[] pdf6, @Part MultipartBody.Part[] pdf7,
                                      @Part MultipartBody.Part[] pdf8, @Part MultipartBody.Part[] pdf9,
                                      @Part MultipartBody.Part[] pdf10);

    @GET("api/pvcprint_list")
    Call<BasePVCListResponse> callGetPVCListService(@Query("retailerid") String retailerid);


    @Multipart
    @POST("api/pvcapp_updatepayment")
    Call<BaseUpdatePVCResponse> updatePVCPaymentStatus(@Part("id") RequestBody id, @Part("status") RequestBody status, @Part("amount") RequestBody amount);


    @GET("api/listforadmin")
    Call<BaseAdminRechargeResponse> callGetAdminRechargeListService(@Query("for") String for1);

    @GET("api/listforadmin")
    Call<BaseAdminPVCResponse> callGetAdminPVCListService(@Query("for") String for1);

    @GET("api/listforadmin")
    Call<BaseAdminPancardListResponse> callGetAdminPanListService(@Query("for") String for1);

    @GET("api/listforadmin")
    Call<BaseUserListResponse> callGetAdminUserListService(@Query("for") String for1);

    @Multipart
    @POST("api/changepvcstatus_forapp")
    Call<AdminPVCUpdateResponse> callPVCAdminStatusUpdate(@Part("memid") RequestBody memid, @Part("id") RequestBody id,
                                                     @Part("get_status") RequestBody get_status, @Part("status_message") RequestBody status_message,
                                                          @Part("totalpdf") RequestBody totalpdf, @Part("amount") RequestBody amount,
                                                          @Part("trakingid") RequestBody trakingid, @Part("trakingurl") RequestBody trakingurl);


    @GET("api/headlines")
    Call<BaseHeadingResponse> callGetHeadingService(@Query("for") String for1, @Query("method") String method);

    @GET("api/headlines")
    Call<BasePVCResponse> callSaveHeadingService(@Query("for") String for1, @Query("method") String method
                                                      , @Query("content") String content);

    @GET("get_phn_packages")
    Call<MainMobilePlans> callBrowsePlanService(@Query("phn") String phn);

    @GET("get_phn_oparator")
    Call<MainOperatorResponse> callBrowseOperatorService(@Query("phn") String phn);

    @GET("get_dth_plans")
    Call<MainMobilePlans> callBrowseDTHPlanService(@Query("phn") String phn);

    @GET("api/capchaforpanpdfno")
    Call<MainCaptchaResponse> callgetCaptchaService(@Query("rtid") String rtid);


    @Multipart
    @POST("api/panpdfno")
    Call<SaveCaptchaResponse> saveCaptcha(@Part("token") RequestBody token, @Part("month") RequestBody month,
                                          @Part("year") RequestBody year, @Part("capcha") RequestBody capcha,
                                          @Part("ackno") RequestBody ackno, @Part("forpan") RequestBody forpan,
                                          @Part("rtid") RequestBody rtid);

    @GET("get_electricity_bill")
    Call<MainFetchBillResponse> callGetBillService(@Query("cn") String cn, @Query("op") String op);

}
