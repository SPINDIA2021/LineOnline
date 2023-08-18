package com.spindia.rechargeapp.wallet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;
import com.spindia.rechargeapp.NewMainActivity;
import com.spindia.rechargeapp.R;
import com.spindia.rechargeapp.authentication.response.WalletResponse;
import com.spindia.rechargeapp.network.Preferences;
import com.spindia.rechargeapp.pancardOffline.BasePanResponse;
import com.spindia.rechargeapp.pancardOffline.EditPanCardActivity;
import com.spindia.rechargeapp.pancardOffline.OfflinePancardActivity;
import com.spindia.rechargeapp.pancardlist.PancardReportsActivity;
import com.spindia.rechargeapp.utils.AppConstants;
import com.spindia.rechargeapp.utils.MainIAPI;
import com.spindia.rechargeapp.walletHistory.BaseWalletHistoryResponse;


import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddToWalletActivity extends AppCompatActivity implements PaymentStatusListener {

    EditText et_ammount;
    CardView cvAddMoneyBtn;
    String upiid = "anilmitawa64@axl";
    String orderId;
    RelativeLayout progress_bar;
    TextView tvWalletBalance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);
        et_ammount=findViewById(R.id.et_ammount);
        cvAddMoneyBtn=findViewById(R.id.cvAddMoneyBtn);
        progress_bar=findViewById(R.id.progress_bar);
        tvWalletBalance=findViewById(R.id.tvWalletBalance);

        initView();
    }

    private static final int TEZ_REQUEST_CODE = 123;

    private static final String GOOGLE_TEZ_PACKAGE_NAME =
            "com.google.android.apps.nbu.paisa.user";
    private void initView() {

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault());
        orderId = df.format(c);

        callServiceGetWalletBalance();


        cvAddMoneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String upi  = upiid;
                String name  = "Wallet";
                String desc  = "Add";


                if (et_ammount.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Please enter amount to add!!",Toast.LENGTH_LONG).show();
                    return;
                }else {
                    Preferences.saveValue(Preferences.AddWalletAmount,et_ammount.getText().toString());
                  //  callServiceSaveData();
                    Intent intent=new Intent(AddToWalletActivity.this, OfflinePancardActivity.class);
                    startActivity(intent);


                    //  makePayment(et_ammount.getText().toString(), upi, name, desc, orderId);

                     /*  Uri uri =
                            new Uri.Builder()
                                    .scheme("upi")
                                    .authority("pay")
                                    .appendQueryParameter("pa", upiid)
                                    .appendQueryParameter("pn", "Meghna Medical And Provision Store")
                                    .appendQueryParameter("mc", "1234")
                                    .appendQueryParameter("tr", "1234")
                                    .appendQueryParameter("tn", "test")
                                    .appendQueryParameter("am", "1")
                                    .appendQueryParameter("cu", "INR")
                                    .appendQueryParameter("url", "")
                                    .build();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(uri);
                    intent.setPackage(GOOGLE_TEZ_PACKAGE_NAME);
                    startActivityForResult(intent, TEZ_REQUEST_CODE);*/
                }

            }
        });

    }

    private void makePayment(String amount, String upi, String name, String desc, String orderId) {

        // all parameters to it such as upi id,name, description and others.
        EasyUpiPayment easyUpiPayment = new EasyUpiPayment.Builder()
                .with(this) // on below line we are adding upi id.
                .setPayeeVpa(upi) // on below line we are setting name to which we are making oayment.
                .setPayeeName(name) // on below line we are passing transaction id.
                .setTransactionId(orderId) // on below line we are passing transaction ref id.
                .setTransactionRefId(orderId) // on below line we are adding description to payment.
                .setDescription(desc) // on below line we are passing amount which is being paid.
                .setAmount(amount+".00") // on below line we are calling a build method to build this ui.
                .build();
        // on below line we are calling a start
        // payment method to start a payment.
        easyUpiPayment.startPayment();
        // on below line we are calling a set payment
        // status listener method to call other payment methods.
        easyUpiPayment.setPaymentStatusListener(this);

    }




    @Override
    public void onTransactionCompleted(TransactionDetails transactionDetails) {

    }

    @Override
    public void onTransactionSuccess() {
        Toast.makeText(this, "Transaction successfully completed..", Toast.LENGTH_SHORT).show();
        callServiceSaveData();
    }



    @Override
    public void onTransactionSubmitted() {
        Toast.makeText(this, "Transaction submitted..", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTransactionFailed() {
        Toast.makeText(this, "Transaction Failed..", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTransactionCancelled() {
        Toast.makeText(this, "Transaction cancelled..", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAppNotFound() {

    }

    private void callServiceSaveData() {
        progress_bar.setVisibility( View.VISIBLE);

        System.setProperty("http.keepAlive", "false");
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.readTimeout(5, TimeUnit.MINUTES).
                connectTimeout(5, TimeUnit.MINUTES).
                writeTimeout(5, TimeUnit.MINUTES).
                retryOnConnectionFailure(true).addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request.Builder builder = original.newBuilder();

                        //Request request = chain.request().newBuilder().addHeader("parameter", "value").build();
                        builder.header("Content-Type", "application/x-www-form-urlencoded");

                        Request request = builder.method(original.method(), original.body())
                                .build();

                        return chain.proceed(request);

                    }
                });

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainIAPI.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        //creating the retrofit api service
        MainIAPI apiService = retrofit.create(MainIAPI.class);




        RequestBody retailerId = createPartFromString(Preferences.getString(AppConstants.MOBILE));
        RequestBody type = createPartFromString("credit");
        RequestBody amount = createPartFromString(Preferences.getString(Preferences.AddWalletAmount));
        RequestBody remark = createPartFromString("add to wallet");




        //Call<ScannerResponse> call = apiService.saveScan(orderId1,vpa1,name1,amount1,mon_no1,member_id1,password1);
        Call<BaseWalletHistoryResponse> call = apiService.saveFailureRecharge(retailerId, type,amount,remark);


        //making the call to generate checksum
        call.enqueue(new Callback<BaseWalletHistoryResponse>() {
            @Override
            public void onResponse(Call<BaseWalletHistoryResponse> call, Response<BaseWalletHistoryResponse> response) {
                progress_bar.setVisibility(View.GONE);

                if (response.body().getStatus()==true)
                {
                    Toast.makeText(AddToWalletActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    callServiceGetWalletBalance();
                }else {
                    Toast.makeText(AddToWalletActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    callServiceGetWalletBalance();
                }


                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter

            }

            @Override
            public void onFailure(Call<BaseWalletHistoryResponse> call, Throwable t) {
                progress_bar.setVisibility(View.GONE);
                // callServiceFalse(mobileNo);
                Toast.makeText(AddToWalletActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }



    private void callServiceGetWalletBalance() {
        progress_bar.setVisibility(View.VISIBLE);
        System.setProperty("http.keepAlive", "false");
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.readTimeout(5, TimeUnit.MINUTES).
                connectTimeout(5, TimeUnit.MINUTES).
                writeTimeout(5, TimeUnit.MINUTES).
                retryOnConnectionFailure(true).addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request.Builder builder = original.newBuilder();

                        //Request request = chain.request().newBuilder().addHeader("parameter", "value").build();
                        builder.header("Content-Type", "application/x-www-form-urlencoded");

                        Request request = builder.method(original.method(), original.body())
                                .build();

                        return chain.proceed(request);

                    }
                });

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainIAPI.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //creating the retrofit api service
        MainIAPI apiService = retrofit.create(MainIAPI.class);


        RequestBody retailerId = createPartFromString(Preferences.getString(AppConstants.MOBILE));
        RequestBody entry = createPartFromString("single");

        //Call<ScannerResponse> call = apiService.saveScan(orderId1,vpa1,name1,amount1,mon_no1,member_id1,password1);
        Call<WalletResponse> call = apiService.getWalletBalance(retailerId,entry );


        //making the call to generate checksum
        call.enqueue(new Callback<WalletResponse>() {
            @Override
            public void onResponse(Call<WalletResponse> call, Response<WalletResponse> response) {
                progress_bar.setVisibility(View.GONE);


                if (response.body().getStatus()==true)
                {
                    tvWalletBalance.setText("( Rs. "+ response.body().getData().toString()+" )");
                }else {
                    Toast.makeText(AddToWalletActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                }


                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter

            }

            @Override
            public void onFailure(Call<WalletResponse> call, Throwable t) {
                progress_bar.setVisibility(View.GONE);
                // callServiceFalse(mobileNo);
                Toast.makeText(AddToWalletActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }




    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                MultipartBody.FORM, descriptionString);
    }

}
