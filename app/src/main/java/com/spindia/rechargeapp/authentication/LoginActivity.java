package com.spindia.rechargeapp.authentication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spindia.rechargeapp.NewMainActivity;
import com.spindia.rechargeapp.R;
import com.spindia.rechargeapp.authentication.response.BaseCheckMobileResponse;
import com.spindia.rechargeapp.authentication.response.FalseCheckMobileResponse;
import com.spindia.rechargeapp.network.Preferences;
import com.spindia.rechargeapp.recharge_services.MobilePrepaidActivity;
import com.spindia.rechargeapp.utils.AppConstants;
import com.spindia.rechargeapp.utils.AppPrefs;
import com.spindia.rechargeapp.utils.MainIAPI;

import java.io.IOException;
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

public class LoginActivity extends AppCompatActivity {

    RelativeLayout lay_mobile,lay_code,progress_bar;
    EditText etLoginMobile,etLoginEmail,etLoginPassword;
    PinView et_code;
    TextView btnLogin;

    FirebaseAuth auth;
    TextView tvSignupHere;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
           // getWindow().getStatusBarColor() = getResources().getColor(R.color.status_bar, this.getTheme());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        FirebaseApp.initializeApp( this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance());


        lay_mobile=findViewById(R.id.lay_mobile);
        lay_code=findViewById(R.id.lay_code);
        etLoginMobile=findViewById(R.id.etLoginMobile);
        etLoginEmail=findViewById(R.id.etLoginEmail);
        et_code=findViewById(R.id.et_code);
        btnLogin=findViewById(R.id.btnLogin);
        etLoginPassword=findViewById(R.id.etLoginPassword);
        progress_bar=findViewById(R.id.progress_bar);
        tvSignupHere=findViewById(R.id.tvRegisterHere);


        auth =  FirebaseAuth.getInstance();

        tvSignupHere.setVisibility(View.GONE);

        tvSignupHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });


        et_code.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                // this will check th opt code validation
                String txtName = et_code.getText().toString();
                if (txtName.length() == 6) {
                    verifyVerificationCode(et_code.getText().toString().trim());
                } else {}
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etLoginMobile.getText().toString().isEmpty()) {
                    etLoginMobile.requestFocus();
                    etLoginMobile.setError(getString(R.string.error_mobile_number)) ;
                }else {
                    Preferences.saveValue(AppConstants.MOBILE,etLoginMobile.getText().toString());
                    callService(etLoginMobile.getText().toString());

                }
            }
        });
    }


    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);
    }

    private String mVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            if (code != null) {

                // Functions.showToast(getActivity(), jsonObject.optString("msg"));


                // editTextCode.setText(code);
                //verifying the code

            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }


        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;

            lay_code.setVisibility(View.VISIBLE);
            lay_mobile.setVisibility(View.GONE);
            btnLogin.setVisibility(View.GONE);
           // Preferences.saveValue(Preferences.VerificationID,mVerificationId);
            //   mResendToken = forceResendingToken;
        }
    };


    private void verifyVerificationCode(String otp) {
        //creating the credential

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }




    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                           // callApiPhoneRegisteration();
                            Preferences.saveValue(AppConstants.IS_LOGIN,"true");

                            Intent intent=new Intent(LoginActivity.this, NewMainActivity.class);
                            startActivity(intent);

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


    private void loginUserAccount() {
        progress_bar.setVisibility(View.VISIBLE);

        String email, password, mobile;
        email = etLoginEmail.getText().toString();
        password = etLoginPassword.getText().toString();
        mobile = etLoginMobile.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();
                            progress_bar.setVisibility(View.GONE);

                            Preferences.saveValue(AppConstants.IS_LOGIN,"true");

                            Intent intent=new Intent(LoginActivity.this, NewMainActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Login failed! Please try again later", Toast.LENGTH_LONG).show();
                            progress_bar.setVisibility(View.GONE);
                        }
                    }
                });
    }


    private void callService(String mobileNo) {
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



        RequestBody mobile1 = createPartFromString(mobileNo);




        //Call<ScannerResponse> call = apiService.saveScan(orderId1,vpa1,name1,amount1,mon_no1,member_id1,password1);
        Call<BaseCheckMobileResponse> call = apiService.checkMobile(mobile1);


        //making the call to generate checksum
        call.enqueue(new Callback<BaseCheckMobileResponse>() {
            @Override
            public void onResponse(Call<BaseCheckMobileResponse> call, Response<BaseCheckMobileResponse> response) {
                progress_bar.setVisibility(View.GONE);

                if (response.body().getStatus()==true)
                {
                    Intent intent=new Intent(LoginActivity.this, SignUpActivity.class);
                    intent.putExtra("mobileNo",mobileNo);
                    intent.putExtra("exist","Y");
                    startActivity(intent);
                }else {
                    Intent intent=new Intent(LoginActivity.this, SignUpActivity.class);
                    intent.putExtra("mobileNo",mobileNo);
                    intent.putExtra("exist","N");
                    startActivity(intent);
                }


                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter

            }

            @Override
            public void onFailure(Call<BaseCheckMobileResponse> call, Throwable t) {
                progress_bar.setVisibility(View.GONE);
                callServiceFalse(mobileNo);
                //Toast.makeText(ScannerActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }




    private void callServiceFalse(String mobileNo) {
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
                    //    builder.addHeader("Accept", "application/json");

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

       /* Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();*/

        //creating the retrofit api service
        MainIAPI apiService = retrofit.create(MainIAPI.class);


        RequestBody mobileNo1 = createPartFromString(mobileNo);

        Call<FalseCheckMobileResponse> call = apiService.falseCheckMobile(mobileNo1);

        //making the call to generate checksum
        call.enqueue(new Callback<FalseCheckMobileResponse>() {
            @Override
            public void onResponse(Call<FalseCheckMobileResponse> call, Response<FalseCheckMobileResponse> response) {
                progress_bar.setVisibility(View.GONE);

                if (response.body().getStatus()==true)
                {
                    Intent intent=new Intent(LoginActivity.this, SignUpActivity.class);
                    intent.putExtra("mobileNo",mobileNo);
                    intent.putExtra("exist","Y");
                    startActivity(intent);
                }else {
                    Intent intent=new Intent(LoginActivity.this, SignUpActivity.class);
                    intent.putExtra("mobileNo",mobileNo);
                    intent.putExtra("exist","N");
                    startActivity(intent);
                }


                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter

            }

            @Override
            public void onFailure(Call<FalseCheckMobileResponse> call, Throwable t) {
                progress_bar.setVisibility(View.GONE);



                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });



    }


    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

}
