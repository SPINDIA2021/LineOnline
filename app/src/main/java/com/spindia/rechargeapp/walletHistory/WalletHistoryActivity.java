package com.spindia.rechargeapp.walletHistory;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spindia.rechargeapp.R;
import com.spindia.rechargeapp.network.Preferences;
import com.spindia.rechargeapp.pvc.PVCCardActivity;
import com.spindia.rechargeapp.utils.AppConstants;
import com.spindia.rechargeapp.utils.MainIAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WalletHistoryActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView rvWallet;
    ImageView ivBackBtn;
    SwipeRefreshLayout mSwipeRefresh;
    RelativeLayout progress_bar;

    WalletHistoryAdapter adapter;

    ArrayList<WalletHistoryResponse> walletHistoryResponses=new ArrayList<>();

    private InterstitialAd mInterstitialAd;
    ProgressDialog pd ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallethistory);
        rvWallet=findViewById(R.id.transaction_recyclerview);
        ivBackBtn=findViewById(R.id.ivBackBtn);
        mSwipeRefresh=findViewById(R.id.mSwipeRefresh);
        progress_bar=findViewById(R.id.progress_bar);
        initView();
    }

    private void initView() {
        callServiceGetWalletData();


        pd= new ProgressDialog(WalletHistoryActivity.this);
        pd.setMessage("Please wait ad is loading..");
        pd.show();
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-7161060381095883/7982531878", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        pd.dismiss();
                        mInterstitialAd = interstitialAd;
                        Log.i("TAG", "onAdLoaded");
                        if (mInterstitialAd != null) {
                            mInterstitialAd.show(WalletHistoryActivity.this);
                        } else {
                            Log.d("TAG", "The interstitial ad wasn't ready yet.");
                        }
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d("TAG", loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });





        mSwipeRefresh.setOnRefreshListener(this);

        mSwipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                if (mSwipeRefresh != null) {
                    mSwipeRefresh.setRefreshing(true);
                }
                walletHistoryResponses.clear();
                callServiceGetWalletData();

                mSwipeRefresh.setRefreshing(false);
            }
        });

    }

    @Override
    public void onRefresh() {
        walletHistoryResponses=new ArrayList<>();
        callServiceGetWalletData();
        mSwipeRefresh.setRefreshing(false);
    }


    private void callServiceGetWalletData() {
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
        RequestBody entry = createPartFromString("all");



        //Call<ScannerResponse> call = apiService.saveScan(orderId1,vpa1,name1,amount1,mon_no1,member_id1,password1);
        Call<BaseWalletHistoryResponse> call = apiService.getAllWalletHistory(retailerId,entry);


        //making the call to generate checksum
        call.enqueue(new Callback<BaseWalletHistoryResponse>() {
            @Override
            public void onResponse(Call<BaseWalletHistoryResponse> call, Response<BaseWalletHistoryResponse> response) {
                progress_bar.setVisibility(View.GONE);

                if (response.body().getStatus()==true)
                {
                    if (response.body().getWalletHistoryResponse().size()!=0)
                    {
                        walletHistoryResponses=response.body().getWalletHistoryResponse();

                        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(WalletHistoryActivity.this);
                        rvWallet.setLayoutManager(linearLayoutManager);
                        adapter=new WalletHistoryAdapter(WalletHistoryActivity.this,walletHistoryResponses);
                        rvWallet.setAdapter(adapter);
                    }else {
                        Toast.makeText(WalletHistoryActivity.this,"No Data Found",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(WalletHistoryActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                }


                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter

            }

            @Override
            public void onFailure(Call<BaseWalletHistoryResponse> call, Throwable t) {
                progress_bar.setVisibility(View.GONE);
                // callServiceFalse(mobileNo);
                Toast.makeText(WalletHistoryActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }
}
