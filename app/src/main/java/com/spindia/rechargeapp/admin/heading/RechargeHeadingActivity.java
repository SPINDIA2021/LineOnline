package com.spindia.rechargeapp.admin.heading;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spindia.rechargeapp.AdminMainActivity;
import com.spindia.rechargeapp.R;
import com.spindia.rechargeapp.pancardOffline.PanCardActivity;
import com.spindia.rechargeapp.pvc.BasePVCResponse;
import com.spindia.rechargeapp.utils.MainIAPI;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RechargeHeadingActivity extends AppCompatActivity {

    EditText edHeading;
    TextView tvTitle;
    ImageView imgBack;
    CardView btnSubmit;
    String from;

    ProgressBar progress_bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heading);

        edHeading=findViewById(R.id.etHeading);
        tvTitle=findViewById(R.id.tv_title);
        imgBack=findViewById(R.id.ivBackBtn);
        btnSubmit=findViewById(R.id.btnSubmit);
        progress_bar=findViewById(R.id.progress_bar);

        initView();
    }

    private void initView() {

        from=getIntent().getStringExtra("from");

        if (from.equals("recharge"))
        {
            tvTitle.setText("Recharge Heading");
        }else if (from.equals("pvc"))
        {
            tvTitle.setText("PVC Heading");
        }else if (from.equals("pan"))
        {
            tvTitle.setText("PAN Heading");
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edHeading.getText().toString().isEmpty())
                {
                    edHeading.setError("Invalid Heading");
                }else {
                    if (from.equals("recharge"))
                    {
                       callServiceSaveHeading("recharge");
                    }else if (from.equals("pvc"))
                    {
                        callServiceSaveHeading("pvcprint");
                    }else if (from.equals("pan"))
                    {
                        callServiceSaveHeading("pancard");
                    }
                }
            }
        });
    }


    private void callServiceSaveHeading(String forr) {

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
                .baseUrl(MainIAPI.BASE_URL1)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        //creating the retrofit api service
        MainIAPI apiService = retrofit.create(MainIAPI.class);


        //Call<ScannerResponse> call = apiService.saveScan(orderId1,vpa1,name1,amount1,mon_no1,member_id1,password1);
        Call<BasePVCResponse> call = apiService.callSaveHeadingService(forr,"save",edHeading.getText().toString());


        //making the call to generate checksum
        call.enqueue(new Callback<BasePVCResponse>() {
            @Override
            public void onResponse(Call<BasePVCResponse> call, Response<BasePVCResponse> response) {
                progress_bar.setVisibility(View.GONE);

                if (response.body().getStatus()==true)
                {

                      Toast.makeText(RechargeHeadingActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(RechargeHeadingActivity.this, AdminMainActivity.class);
                    startActivity(intent);

                }else {
                    Toast.makeText(RechargeHeadingActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                }


                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter

            }

            @Override
            public void onFailure(Call<BasePVCResponse> call, Throwable t) {
                progress_bar.setVisibility(View.GONE);
                // callServiceFalse(mobileNo);
                Toast.makeText(RechargeHeadingActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


}
