package com.spindia.rechargeapp.network;






import com.spindia.rechargeapp.authentication.response.LoginMainResponse;
import com.spindia.rechargeapp.authentication.response.BaseCheckMobileResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface IApi {


   /*String BASE_URL="https://mewar.sahayatamoney.com/Applogin/";*/
   String BASE_URL="https://spindiabazaar.com/";


    int COMMON_TAG = 10031;
    int COMMON_TAG1 = 10032;
    int COMMON_TAG2 = 10034;
    int COMMON_TAG3 = 10035;
    int COMMON_TAG4 = 10036;
    int COMMON_TAG5 = 10037;
    int COMMON_TAG6 = 10038;
    int COMMON_TAG7 = 10039;
    int COMMON_TAG8 = 10040;
    int COMMON_TAG9 = 10041;
    int COMMON_TAG10 = 10042;
    int COMMON_TAG11 = 10043;
    int COMMON_TAG12 = 10044;





    @Multipart
    @POST("Appapi/checkrechargestatus")
    Call<BaseResponse> saveRetry(@Part("txnid") RequestBody txnid);

 @Multipart
 @POST("Appapi/pancardform")
 Call<BaseResponse> getPanForm(@Part("cus_username") RequestBody cus_username,
                               @Part("password") RequestBody password);


}
