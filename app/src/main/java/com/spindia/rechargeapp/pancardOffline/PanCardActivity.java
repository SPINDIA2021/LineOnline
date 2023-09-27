package com.spindia.rechargeapp.pancardOffline;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.gson.JsonObject;
import com.spindia.rechargeapp.NewMainActivity;
import com.spindia.rechargeapp.R;
import com.spindia.rechargeapp.authentication.LoginActivity;
import com.spindia.rechargeapp.authentication.SignUpActivity;
import com.spindia.rechargeapp.authentication.response.BaseCheckMobileResponse;
import com.spindia.rechargeapp.network.Preferences;
import com.spindia.rechargeapp.network.Util;
import com.spindia.rechargeapp.pancardlist.PancardReportsActivity;
import com.spindia.rechargeapp.pvc.PVCCardActivity;
import com.spindia.rechargeapp.utils.AppConstants;
import com.spindia.rechargeapp.utils.MainIAPI;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
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
import retrofit2.http.Part;

public class PanCardActivity extends AppCompatActivity {
    ImageView imgAdharFront,imgAdharBack,imgSignature,imgGardianAadhar;
    TextView tvDOB,textGardianAadhar;
    EditText edFirstName, edFathersName, edMobile, edEmailAddress;
    Button btnSubmit;
    ImageView imgProfile;
    String selectType;

    Calendar mCurrentDate;
    int day,month, year,dayToday,monthToday,yearToday;
    int selecteddate,selectedmonth,selectedyear;
    Date date;
    String sendDate="";
    String adharType="S";
    int age;
    RelativeLayout progress_bar;
    RadioGroup rgAadhar;
    RadioButton rbSingle, rbDouble;
    TextView textSignature;
    TextView tvHeading;
    ArrayList<File> profileFiles=new ArrayList<>();
    ArrayList<File> adharFilesFront=new ArrayList<>();
    ArrayList<File> adharFilesBack=new ArrayList<>();
    ArrayList<File> garAdharFiles=new ArrayList<>();
    ArrayList<File> signFiles=new ArrayList<>();

    private InterstitialAd mInterstitialAd;
    ProgressDialog pd ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actiivty_pancard);

        imgProfile=findViewById(R.id.imgPhoto);
        imgAdharFront=findViewById(R.id.imgAdharFront);
        imgAdharBack=findViewById(R.id.imgAdharBack);
        imgSignature=findViewById(R.id.imgSignature);
        tvDOB=findViewById(R.id.tvDOB);
        edFirstName=findViewById(R.id.edFirstName);
        edFathersName=findViewById(R.id.edFathersName);
        edMobile=findViewById(R.id.edMobile);
        edEmailAddress=findViewById(R.id.edEmailAddress);
        imgGardianAadhar=findViewById(R.id.imgGardianAadhar);
        textGardianAadhar=findViewById(R.id.textGardianAadhar);
        btnSubmit=findViewById(R.id.btnSubmit);
        progress_bar=findViewById(R.id.progress_bar);
        rgAadhar=findViewById(R.id.rg_adhar);
        rbSingle=findViewById(R.id.rb_single);
        rbDouble=findViewById(R.id.rb_double);
        textSignature=findViewById(R.id.textSignature);
        tvHeading=findViewById(R.id.tvmarque_pan);

        initView();

    }

    private void initView() {

        imgGardianAadhar.setVisibility(View.GONE);
        textGardianAadhar.setVisibility(View.GONE);

        callServiceGetHeading();


        pd= new ProgressDialog(PanCardActivity.this);
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
                            mInterstitialAd.show(PanCardActivity.this);
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





        tvDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    final Calendar min = Calendar.getInstance();
                    min.add(Calendar.MONTH, -5);

                    mCurrentDate = Calendar.getInstance();
                    day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
                    month = mCurrentDate.get(Calendar.MONTH);
                    year = mCurrentDate.get(Calendar.YEAR);

                    DatePickerDialog datePickerDialog1 = new DatePickerDialog(PanCardActivity.this, new DatePickerDialog.OnDateSetListener() {

                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                            selecteddate = i;
                            selectedmonth = i1 + 1;
                            selectedyear = i2;



                            sendDate = selectedyear + "/" + selectedmonth + "/" + selecteddate;

                            age=getAge(selecteddate,selectedmonth,selectedyear);
                            Log.e("AGGEEE: ", String.valueOf(age));

                            if (age<18)
                            {
                                imgGardianAadhar.setVisibility(View.VISIBLE);
                                textGardianAadhar.setVisibility(View.VISIBLE);
                                textSignature.setText("Guardian Signature");
                            }else {
                                imgGardianAadhar.setVisibility(View.GONE);
                                textGardianAadhar.setVisibility(View.GONE);
                                textSignature.setText("Signature");
                            }

                            @SuppressLint("SimpleDateFormat") DateFormat srcDf = new SimpleDateFormat("dd/MM/yyyy");
                            try {
                                date = srcDf.parse(sendDate);
                                String dateSet=srcDf.format(date);
                                tvDOB.setText(dateSet);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                    }, year, month, day);

                    datePickerDialog1.getDatePicker().setMaxDate(System.currentTimeMillis() + 1);
                    datePickerDialog1.show();
                } catch(Exception e) {}
            }
        });


        rbSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adharType="S";
                imgAdharBack.setVisibility(View.GONE);

            }
        });

        /*onClicklistener for familyrb radio button click..*/
        rbDouble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                adharType="D";

                imgAdharBack.setVisibility(View.VISIBLE);
            }
        });





        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType="profile";
                profileArrayList=new ArrayList<>();
                CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(PanCardActivity.this);
            }
        });

        imgAdharFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType="adharFront";
                adharFrontArrayList=new ArrayList<>();
                CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(PanCardActivity.this);
            }
        });

        imgAdharBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType="adharBack";
               adharBackArrayList=new ArrayList<>();
                CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(PanCardActivity.this);
            }
        });

        imgGardianAadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType="gaurdian";
                garAdharArrayList=new ArrayList<>();
                CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(PanCardActivity.this);
            }
        });

        imgSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType="signature";
                signatureArrayList=new ArrayList<>();
                CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(PanCardActivity.this);
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edFirstName.getText().toString().isEmpty())
                {
                    Toast.makeText(PanCardActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
                    return;
                }else if (edMobile.getText().toString().isEmpty())
                {
                    Toast.makeText(PanCardActivity.this, "Please enter mobile no", Toast.LENGTH_SHORT).show();
                    return;
                }else if (tvDOB.getText().toString().isEmpty())
                {
                    Toast.makeText(PanCardActivity.this, "Please select DOB", Toast.LENGTH_SHORT).show();
                    return;
                }else if (edFathersName.getText().toString().isEmpty())
                {
                    Toast.makeText(PanCardActivity.this, "Please enter Father's name", Toast.LENGTH_SHORT).show();
                    return;
                }else if (edEmailAddress.getText().toString().isEmpty())
                {
                    Toast.makeText(PanCardActivity.this, "Please enter email address", Toast.LENGTH_SHORT).show();
                    return;
                }/*else if (profileArrayList.isEmpty())
                {
                    Toast.makeText(PanCardActivity.this, "Photo cannot be empty", Toast.LENGTH_SHORT).show();
                }else if (adharArrayList.isEmpty())
                {
                    Toast.makeText(PanCardActivity.this, "Aadhar image cannot be empty", Toast.LENGTH_SHORT).show();
                }else if (signatureArrayList.isEmpty())
                {
                    Toast.makeText(PanCardActivity.this, "Signature image cannot be empty", Toast.LENGTH_SHORT).show();
                }*//*else if (age<18)
                {
                    if (garAdharArrayList.isEmpty())
                    {
                        Toast.makeText(PanCardActivity.this, "Guardian Aadhar image cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                }*/else {
                    //Util.hideKeyBorad(PanCardActivity.this, v);


                    profileFiles=new ArrayList<>();
                    for (int i = 0; i < profileArrayList.size(); i++) {

                        if (profileArrayList.get(i).getPath() != null && !profileArrayList.get(i).getPath().equals("")) {
                            String mCurrentPhotoPath =  profileArrayList.get(0).getPath();
                            File mImageFile = new File(Uri.parse(mCurrentPhotoPath).getPath());
                            profileFiles.add(mImageFile);
                        }
                    }

                    adharFilesFront=new ArrayList<>();
                    for (int i = 0; i < adharFrontArrayList.size(); i++) {

                        if (adharFrontArrayList.get(i).getPath() != null && !adharFrontArrayList.get(i).getPath().equals("")) {
                            String mCurrentPhotoPath =  adharFrontArrayList.get(0).getPath();
                            File mImageFile = new File(Uri.parse(mCurrentPhotoPath).getPath());
                            adharFilesFront.add(mImageFile);
                        }
                    }

                    adharFilesBack=new ArrayList<>();
                    for (int i = 0; i < adharBackArrayList.size(); i++) {

                        if (adharBackArrayList.get(i).getPath() != null && !adharBackArrayList.get(i).getPath().equals("")) {
                            String mCurrentPhotoPath =  adharBackArrayList.get(0).getPath();
                            File mImageFile = new File(Uri.parse(mCurrentPhotoPath).getPath());
                            adharFilesBack.add(mImageFile);
                        }
                    }

                    garAdharFiles=new ArrayList<>();
                    for (int i = 0; i < garAdharArrayList.size(); i++) {

                        if (garAdharArrayList.get(i).getPath() != null && !garAdharArrayList.get(i).getPath().equals("")) {
                            String mCurrentPhotoPath =  garAdharArrayList.get(0).getPath();
                            File mImageFile = new File(Uri.parse(mCurrentPhotoPath).getPath());
                            garAdharFiles.add(mImageFile);
                        }
                    }

                    signFiles=new ArrayList<>();
                    for (int i = 0; i < signatureArrayList.size(); i++) {

                        if (signatureArrayList.get(i).getPath() != null && !signatureArrayList.get(i).getPath().equals("")) {
                            String mCurrentPhotoPath =  signatureArrayList.get(0).getPath();
                            File mImageFile = new File(Uri.parse(mCurrentPhotoPath).getPath());
                            signFiles.add(mImageFile);
                        }
                    }

                    if (adharFilesFront.isEmpty())
                    {
                        Toast.makeText(PanCardActivity.this,"Please select aadhar image",Toast.LENGTH_LONG).show();
                    }else if (adharType=="D"&& adharFilesBack.isEmpty())
                    {
                        Toast.makeText(PanCardActivity.this,"Please select back aadhar image",Toast.LENGTH_LONG).show();
                    }
                    else if (signFiles.isEmpty())
                    {
                        Toast.makeText(PanCardActivity.this,"Please select signature image",Toast.LENGTH_LONG).show();
                    }
                    else if (profileFiles.isEmpty())
                    {
                        Toast.makeText(PanCardActivity.this,"Please select photo",Toast.LENGTH_LONG).show();
                    }else {
                        callServiceSave();

                    }


                }
            }
        });
    }

    private void callServiceSave() {

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


        RequestBody ratype,retailerId;

        RequestBody name = createPartFromString(edFirstName.getText().toString());
        RequestBody mobileNo = createPartFromString(edMobile.getText().toString());
        RequestBody fatherName = createPartFromString(edFathersName.getText().toString());
        RequestBody emailId = createPartFromString(edEmailAddress.getText().toString());
        RequestBody dob = createPartFromString(tvDOB.getText().toString());
        retailerId=createPartFromString(Preferences.getString(AppConstants.MOBILE));

        if (age<18)
        {
            ratype = createPartFromString("Y");
        }else {
            ratype = createPartFromString("N");
        }



        MultipartBody.Part[] profilesParts = new MultipartBody.Part[profileFiles.size()];
        for (int index = 0; index < profileFiles.size(); index++) {
            RequestBody reqFile = RequestBody.create(MediaType.parse(""), profileFiles.get(index));
            profilesParts[index] = MultipartBody.Part.createFormData("photo", profileFiles.get(index).getName(), reqFile);
        }

        MultipartBody.Part[] adharPartsFront = new MultipartBody.Part[adharFilesFront.size()];
        for (int index = 0; index < adharFilesFront.size(); index++) {
            RequestBody reqFile = RequestBody.create(MediaType.parse(""), adharFilesFront.get(index));
            adharPartsFront[index] = MultipartBody.Part.createFormData("aadharfront", adharFilesFront.get(index).getName(), reqFile);
        }

        MultipartBody.Part[] adharPartsBack = new MultipartBody.Part[adharFilesBack.size()];
        for (int index = 0; index < adharFilesBack.size(); index++) {
            RequestBody reqFile = RequestBody.create(MediaType.parse(""), adharFilesBack.get(index));
            adharPartsBack[index] = MultipartBody.Part.createFormData("aadharback", adharFilesBack.get(index).getName(), reqFile);
        }

        MultipartBody.Part[] guarAdharParts = new MultipartBody.Part[garAdharFiles.size()];
        for (int index = 0; index < garAdharFiles.size(); index++) {
            RequestBody reqFile = RequestBody.create(MediaType.parse(""), garAdharFiles.get(index));
            guarAdharParts[index] = MultipartBody.Part.createFormData("guardians_aadhar", garAdharFiles.get(index).getName(), reqFile);
        }

        MultipartBody.Part[] signParts = new MultipartBody.Part[signFiles.size()];
        for (int index = 0; index < signFiles.size(); index++) {
            RequestBody reqFile = RequestBody.create(MediaType.parse(""), signFiles.get(index));
            signParts[index] = MultipartBody.Part.createFormData("sign", signFiles.get(index).getName(), reqFile);
        }


        //Call<ScannerResponse> call = apiService.saveScan(orderId1,vpa1,name1,amount1,mon_no1,member_id1,password1);
        Call<BasePanResponse> call = apiService.pancardSave(retailerId,name,emailId,mobileNo,
                fatherName,dob,ratype,adharPartsFront,adharPartsBack,profilesParts,signParts,guarAdharParts);


        //making the call to generate checksum
        call.enqueue(new Callback<BasePanResponse>() {
            @Override
            public void onResponse(Call<BasePanResponse> call, Response<BasePanResponse> response) {
                progress_bar.setVisibility(View.GONE);

                if (response.body().getStatus()==true)
                {
                    MediaPlayer mp=new MediaPlayer();
                        mp = MediaPlayer.create(PanCardActivity.this, R.raw.pan_card_submit);
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            MediaPlayer mp1 = mp;
                            mp1.reset();
                            mp1.release();
                            mp1 = null;
                        }
                    });
                    mp.start();

                    Toast.makeText(PanCardActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(PanCardActivity.this, PancardReportsActivity.class);
                 //   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else {
                    Toast.makeText(PanCardActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(PanCardActivity.this, PancardReportsActivity.class);
                  //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }


                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter

            }

            @Override
            public void onFailure(Call<BasePanResponse> call, Throwable t) {
                progress_bar.setVisibility(View.GONE);
               // callServiceFalse(mobileNo);
                Toast.makeText(PanCardActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    ArrayList<Uri> profileArrayList=new ArrayList<>();
    ArrayList<Uri> adharFrontArrayList=new ArrayList<>();
    ArrayList<Uri> adharBackArrayList=new ArrayList<>();
    ArrayList<Uri> garAdharArrayList=new ArrayList<>();
    ArrayList<Uri> signatureArrayList=new ArrayList<>();
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == AppCompatActivity.RESULT_OK) {
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), result.getUri());


                    if (selectType.equals("profile"))
                    {
                        profileArrayList.add(result.getUri());
                        imgProfile.setImageBitmap( bitmap);
                    }else if (selectType.equals("adharFront"))
                    {
                        adharFrontArrayList.add(result.getUri());
                        imgAdharFront.setImageBitmap( bitmap);
                    }else if (selectType.equals("adharBack"))
                    {
                        adharBackArrayList.add(result.getUri());
                        imgAdharBack.setImageBitmap( bitmap);
                    }else if (selectType.equals("gaurdian"))
                    {
                        garAdharArrayList.add(result.getUri());
                        imgGardianAadhar.setImageBitmap( bitmap);
                    }else if (selectType.equals("signature"))
                    {
                        signatureArrayList.add(result.getUri());
                        imgSignature.setImageBitmap( bitmap);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
             //   Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int getAge(int year, int month, int dayOfMonth) {
        return Period.between(
                LocalDate.of(year, month, dayOfMonth),
                LocalDate.now()
        ).getYears();
    }


    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }


    private void callServiceGetHeading() {

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
        Call<BaseHeadingResponse> call = apiService.callGetHeadingService("pancard","list");


        //making the call to generate checksum
        call.enqueue(new Callback<BaseHeadingResponse>() {
            @Override
            public void onResponse(Call<BaseHeadingResponse> call, Response<BaseHeadingResponse> response) {
                progress_bar.setVisibility(View.GONE);

                if (response.body().getStatus()==true)
                {
                  //  Toast.makeText(PanCardActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    tvHeading.setText(response.body().getData().get(0).getContent());
                    tvHeading.setSelected(true);
                }else {
                    Toast.makeText(PanCardActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                }


                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter

            }

            @Override
            public void onFailure(Call<BaseHeadingResponse> call, Throwable t) {
                progress_bar.setVisibility(View.GONE);
                // callServiceFalse(mobileNo);
                Toast.makeText(PanCardActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }




}
