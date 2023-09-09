package com.spindia.rechargeapp.pvc;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spindia.rechargeapp.NewMainActivity;
import com.spindia.rechargeapp.R;
import com.spindia.rechargeapp.network.Preferences;
import com.spindia.rechargeapp.pancardOffline.BaseHeadingResponse;
import com.spindia.rechargeapp.pancardOffline.BasePanResponse;
import com.spindia.rechargeapp.pancardOffline.PanCardActivity;
import com.spindia.rechargeapp.pancardlist.PancardReportsActivity;
import com.spindia.rechargeapp.utils.AppConstants;
import com.spindia.rechargeapp.utils.MainIAPI;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
import retrofit2.http.Part;

public class PVCCardActivity extends AppCompatActivity {

    EditText edName, edMobile, edPassword1,edPassword2,edPassword3,edPassword4,edPassword5,edPassword6,
             edPassword7,edPassword8,edPassword9,edPassword10;

    LinearLayout  layPassword1,layPassword2,layPassword3,layPassword4,layPassword5,layPassword6,
            layPassword7,layPassword8,layPassword9,layPassword10;

    LinearLayout layout1,layout2,layout3,layout4,layout5,layout6,layout7,
            layout8,layout9,layout10;

    Spinner spinner_count,spinner_pass1,spinner_pass2,spinner_pass3,spinner_pass4,spinner_pass5,
            spinner_pass6,spinner_pass7,spinner_pass8,spinner_pass9,spinner_pass10;

    ImageView imgpdf1,imgpdf2,imgpdf3,imgpdf4,imgpdf5,imgpdf6,imgpdf7,imgpdf8,imgpdf9,imgpdf10;

    TextView text_pdf1,text_pdf2,text_pdf3,text_pdf4,text_pdf5,
            text_pdf6,text_pdf7,text_pdf8,text_pdf9,text_pdf10;

    View view1,view2,view3, view4, view5, view6, view7, view8, view9;

    Button btnSubmit;

    RelativeLayout progress_bar;

    TextView tvHeading;

    String selectType;
    String totalPDF="1";

    private static final int PICK_FILE = 101;

    ArrayList<String> pdf1ArrayList=new ArrayList<>();
    ArrayList<String> pdf2ArrayList=new ArrayList<>();
    ArrayList<String> pdf3ArrayList=new ArrayList<>();
    ArrayList<String> pdf4ArrayList=new ArrayList<>();
    ArrayList<String> pdf5ArrayList=new ArrayList<>();
    ArrayList<String> pdf6ArrayList=new ArrayList<>();
    ArrayList<String> pdf7ArrayList=new ArrayList<>();
    ArrayList<String> pdf8ArrayList=new ArrayList<>();
    ArrayList<String> pdf9ArrayList=new ArrayList<>();
    ArrayList<String> pdf10ArrayList=new ArrayList<>();


    ArrayList<File> pdf1Files=new ArrayList<>();
    ArrayList<File> pdf2Files=new ArrayList<>();
    ArrayList<File> pdf3Files=new ArrayList<>();
    ArrayList<File> pdf4Files=new ArrayList<>();
    ArrayList<File> pdf5Files=new ArrayList<>();
    ArrayList<File> pdf6Files=new ArrayList<>();
    ArrayList<File> pdf7Files=new ArrayList<>();
    ArrayList<File> pdf8Files=new ArrayList<>();
    ArrayList<File> pdf9Files=new ArrayList<>();
    ArrayList<File> pdf10Files=new ArrayList<>();


    double distanceCovered=0.0;



    double latitude, longitude, givenLatitude=26.797779, givenLongitude = 75.854989;

    private GpsTracker gpsTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actiivty_pvc_card);

        edName=findViewById(R.id.edName);
        edMobile=findViewById(R.id.edMobile);
        tvHeading=findViewById(R.id.tvmarque_pvc);

        edPassword1=findViewById(R.id.edPassword1);
        edPassword2=findViewById(R.id.edPassword2);
        edPassword3=findViewById(R.id.edPassword3);
        edPassword4=findViewById(R.id.edPassword4);
        edPassword5=findViewById(R.id.edPassword5);
        edPassword6=findViewById(R.id.edPassword6);
        edPassword7=findViewById(R.id.edPassword7);
        edPassword8=findViewById(R.id.edPassword8);
        edPassword9=findViewById(R.id.edPassword9);
        edPassword10=findViewById(R.id.edPassword10);

        layPassword1=findViewById(R.id.layPassword1);
        layPassword2=findViewById(R.id.layPassword2);
        layPassword3=findViewById(R.id.layPassword3);
        layPassword4=findViewById(R.id.layPassword4);
        layPassword5=findViewById(R.id.layPassword5);
        layPassword6=findViewById(R.id.layPassword6);
        layPassword7=findViewById(R.id.layPassword7);
        layPassword8=findViewById(R.id.layPassword8);
        layPassword9=findViewById(R.id.layPassword9);
        layPassword10=findViewById(R.id.layPassword10);


        layout1=findViewById(R.id.layout1);
        layout2=findViewById(R.id.layout2);
        layout3=findViewById(R.id.layout3);
        layout4=findViewById(R.id.layout4);
        layout5=findViewById(R.id.layout5);
        layout6=findViewById(R.id.layout6);
        layout7=findViewById(R.id.layout7);
        layout8=findViewById(R.id.layout8);
        layout9=findViewById(R.id.layout9);
        layout10=findViewById(R.id.layout10);

        spinner_count=findViewById(R.id.spinner_count);

        spinner_pass1=findViewById(R.id.spinner_pass1);
        spinner_pass2=findViewById(R.id.spinner_pass2);
        spinner_pass3=findViewById(R.id.spinner_pass3);
        spinner_pass4=findViewById(R.id.spinner_pass4);
        spinner_pass5=findViewById(R.id.spinner_pass5);
        spinner_pass6=findViewById(R.id.spinner_pass6);
        spinner_pass7=findViewById(R.id.spinner_pass7);
        spinner_pass8=findViewById(R.id.spinner_pass8);
        spinner_pass9=findViewById(R.id.spinner_pass9);
        spinner_pass10=findViewById(R.id.spinner_pass10);


        imgpdf1=findViewById(R.id.imgpdf1);
        imgpdf2=findViewById(R.id.imgpdf2);
        imgpdf3=findViewById(R.id.imgpdf3);
        imgpdf4=findViewById(R.id.imgpdf4);
        imgpdf5=findViewById(R.id.imgpdf5);
        imgpdf6=findViewById(R.id.imgpdf6);
        imgpdf7=findViewById(R.id.imgpdf7);
        imgpdf8=findViewById(R.id.imgpdf8);
        imgpdf9=findViewById(R.id.imgpdf9);
        imgpdf10=findViewById(R.id.imgpdf10);

        text_pdf1=findViewById(R.id.text_pdf1);
        text_pdf2=findViewById(R.id.text_pdf2);
        text_pdf3=findViewById(R.id.text_pdf3);
        text_pdf4=findViewById(R.id.text_pdf4);
        text_pdf5=findViewById(R.id.text_pdf5);
        text_pdf6=findViewById(R.id.text_pdf6);
        text_pdf7=findViewById(R.id.text_pdf7);
        text_pdf8=findViewById(R.id.text_pdf8);
        text_pdf9=findViewById(R.id.text_pdf9);
        text_pdf10=findViewById(R.id.text_pdf10);


        view1=findViewById(R.id.view1);
        view2=findViewById(R.id.view2);
        view3=findViewById(R.id.view3);
        view4=findViewById(R.id.view4);
        view5=findViewById(R.id.view5);
        view6=findViewById(R.id.view6);
        view7=findViewById(R.id.view7);
        view8=findViewById(R.id.view8);
        view9=findViewById(R.id.view9);


        btnSubmit=findViewById(R.id.btnSubmit);
        progress_bar=findViewById(R.id.progress_bar);

        initView();

    }


    private InterstitialAd mInterstitialAd;
    FusedLocationProviderClient mFusedLocationClient;
    ProgressDialog pd ;
    private void initView() {



        pd= new ProgressDialog(PVCCardActivity.this);
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
                            mInterstitialAd.show(PVCCardActivity.this);
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



        callServiceGetHeading();


        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        getLocation();


        layout1.setVisibility(View.VISIBLE);
        layout2.setVisibility(View.GONE);
        layout3.setVisibility(View.GONE);
        layout4.setVisibility(View.GONE);
        layout5.setVisibility(View.GONE);
        layout6.setVisibility(View.GONE);
        layout7.setVisibility(View.GONE);
        layout8.setVisibility(View.GONE);
        layout9.setVisibility(View.GONE);
        layout10.setVisibility(View.GONE);

        view1.setVisibility(View.VISIBLE);
        view2.setVisibility(View.GONE);
        view3.setVisibility(View.GONE);
        view4.setVisibility(View.GONE);
        view5.setVisibility(View.GONE);
        view6.setVisibility(View.GONE);
        view7.setVisibility(View.GONE);
        view8.setVisibility(View.GONE);
        view9.setVisibility(View.GONE);


        layPassword1.setVisibility(View.GONE);
        layPassword2.setVisibility(View.GONE);
        layPassword3.setVisibility(View.GONE);
        layPassword4.setVisibility(View.GONE);
        layPassword5.setVisibility(View.GONE);
        layPassword6.setVisibility(View.GONE);
        layPassword7.setVisibility(View.GONE);
        layPassword8.setVisibility(View.GONE);
        layPassword9.setVisibility(View.GONE);
        layPassword10.setVisibility(View.GONE);


        String[] counterList= getResources().getStringArray(R.array.counter);
        try {
            ArrayAdapter<String> adapter;
            adapter = new ArrayAdapter<String>(this, R.layout.spinneritem, getResources().getStringArray(R.array.counter));
            adapter.setDropDownViewResource(R.layout.row_simple_spinner_dropdown_item);
            //setting adapter to spinner
            spinner_count.setAdapter(adapter);

            spinner_count.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (counterList[i].equals("1"))
                    {
                        totalPDF="1";
                        layout1.setVisibility(View.VISIBLE);
                        layout2.setVisibility(View.GONE);
                        layout3.setVisibility(View.GONE);
                        layout4.setVisibility(View.GONE);
                        layout5.setVisibility(View.GONE);
                        layout6.setVisibility(View.GONE);
                        layout7.setVisibility(View.GONE);
                        layout8.setVisibility(View.GONE);
                        layout9.setVisibility(View.GONE);
                        layout10.setVisibility(View.GONE);

                        view1.setVisibility(View.VISIBLE);
                        view2.setVisibility(View.GONE);
                        view3.setVisibility(View.GONE);
                        view4.setVisibility(View.GONE);
                        view5.setVisibility(View.GONE);
                        view6.setVisibility(View.GONE);
                        view7.setVisibility(View.GONE);
                        view8.setVisibility(View.GONE);
                        view9.setVisibility(View.GONE);

                    }else if (counterList[i].equals("2"))
                    {
                        totalPDF="2";
                        layout1.setVisibility(View.VISIBLE);
                        layout2.setVisibility(View.VISIBLE);
                        layout3.setVisibility(View.GONE);
                        layout4.setVisibility(View.GONE);
                        layout5.setVisibility(View.GONE);
                        layout6.setVisibility(View.GONE);
                        layout7.setVisibility(View.GONE);
                        layout8.setVisibility(View.GONE);
                        layout9.setVisibility(View.GONE);
                        layout10.setVisibility(View.GONE);

                        view1.setVisibility(View.VISIBLE);
                        view2.setVisibility(View.VISIBLE);
                        view3.setVisibility(View.GONE);
                        view4.setVisibility(View.GONE);
                        view5.setVisibility(View.GONE);
                        view6.setVisibility(View.GONE);
                        view7.setVisibility(View.GONE);
                        view8.setVisibility(View.GONE);
                        view9.setVisibility(View.GONE);
                    }else if (counterList[i].equals("3"))
                    {
                        totalPDF="3";
                        layout1.setVisibility(View.VISIBLE);
                        layout2.setVisibility(View.VISIBLE);
                        layout3.setVisibility(View.VISIBLE);
                        layout4.setVisibility(View.GONE);
                        layout5.setVisibility(View.GONE);
                        layout6.setVisibility(View.GONE);
                        layout7.setVisibility(View.GONE);
                        layout8.setVisibility(View.GONE);
                        layout9.setVisibility(View.GONE);
                        layout10.setVisibility(View.GONE);

                        view1.setVisibility(View.VISIBLE);
                        view2.setVisibility(View.VISIBLE);
                        view3.setVisibility(View.VISIBLE);
                        view4.setVisibility(View.GONE);
                        view5.setVisibility(View.GONE);
                        view6.setVisibility(View.GONE);
                        view7.setVisibility(View.GONE);
                        view8.setVisibility(View.GONE);
                        view9.setVisibility(View.GONE);
                    }else if (counterList[i].equals("4"))
                    {
                        totalPDF="4";
                        layout1.setVisibility(View.VISIBLE);
                        layout2.setVisibility(View.VISIBLE);
                        layout3.setVisibility(View.VISIBLE);
                        layout4.setVisibility(View.VISIBLE);
                        layout5.setVisibility(View.GONE);
                        layout6.setVisibility(View.GONE);
                        layout7.setVisibility(View.GONE);
                        layout8.setVisibility(View.GONE);
                        layout9.setVisibility(View.GONE);
                        layout10.setVisibility(View.GONE);


                        view1.setVisibility(View.VISIBLE);
                        view2.setVisibility(View.VISIBLE);
                        view3.setVisibility(View.VISIBLE);
                        view4.setVisibility(View.VISIBLE);
                        view5.setVisibility(View.GONE);
                        view6.setVisibility(View.GONE);
                        view7.setVisibility(View.GONE);
                        view8.setVisibility(View.GONE);
                        view9.setVisibility(View.GONE);
                    }else if (counterList[i].equals("5"))
                    {
                        totalPDF="5";
                        layout1.setVisibility(View.VISIBLE);
                        layout2.setVisibility(View.VISIBLE);
                        layout3.setVisibility(View.VISIBLE);
                        layout4.setVisibility(View.VISIBLE);
                        layout5.setVisibility(View.VISIBLE);
                        layout6.setVisibility(View.GONE);
                        layout7.setVisibility(View.GONE);
                        layout8.setVisibility(View.GONE);
                        layout9.setVisibility(View.GONE);
                        layout10.setVisibility(View.GONE);


                        view1.setVisibility(View.VISIBLE);
                        view2.setVisibility(View.VISIBLE);
                        view3.setVisibility(View.VISIBLE);
                        view4.setVisibility(View.VISIBLE);
                        view5.setVisibility(View.VISIBLE);
                        view6.setVisibility(View.GONE);
                        view7.setVisibility(View.GONE);
                        view8.setVisibility(View.GONE);
                        view9.setVisibility(View.GONE);
                    }else if (counterList[i].equals("6"))
                    {
                        totalPDF="6";
                        layout1.setVisibility(View.VISIBLE);
                        layout2.setVisibility(View.VISIBLE);
                        layout3.setVisibility(View.VISIBLE);
                        layout4.setVisibility(View.VISIBLE);
                        layout5.setVisibility(View.VISIBLE);
                        layout6.setVisibility(View.VISIBLE);
                        layout7.setVisibility(View.GONE);
                        layout8.setVisibility(View.GONE);
                        layout9.setVisibility(View.GONE);
                        layout10.setVisibility(View.GONE);

                        view1.setVisibility(View.VISIBLE);
                        view2.setVisibility(View.VISIBLE);
                        view3.setVisibility(View.VISIBLE);
                        view4.setVisibility(View.VISIBLE);
                        view5.setVisibility(View.VISIBLE);
                        view6.setVisibility(View.VISIBLE);
                        view7.setVisibility(View.GONE);
                        view8.setVisibility(View.GONE);
                        view9.setVisibility(View.GONE);
                    }else if (counterList[i].equals("7"))
                    {
                        totalPDF="7";
                        layout1.setVisibility(View.VISIBLE);
                        layout2.setVisibility(View.VISIBLE);
                        layout3.setVisibility(View.VISIBLE);
                        layout4.setVisibility(View.VISIBLE);
                        layout5.setVisibility(View.VISIBLE);
                        layout6.setVisibility(View.VISIBLE);
                        layout7.setVisibility(View.VISIBLE);
                        layout8.setVisibility(View.GONE);
                        layout9.setVisibility(View.GONE);
                        layout10.setVisibility(View.GONE);

                        view1.setVisibility(View.VISIBLE);
                        view2.setVisibility(View.VISIBLE);
                        view3.setVisibility(View.VISIBLE);
                        view4.setVisibility(View.VISIBLE);
                        view5.setVisibility(View.VISIBLE);
                        view6.setVisibility(View.VISIBLE);
                        view7.setVisibility(View.VISIBLE);
                        view8.setVisibility(View.GONE);
                        view9.setVisibility(View.GONE);
                    }else if (counterList[i].equals("8"))
                    {
                        totalPDF="8";
                        layout1.setVisibility(View.VISIBLE);
                        layout2.setVisibility(View.VISIBLE);
                        layout3.setVisibility(View.VISIBLE);
                        layout4.setVisibility(View.VISIBLE);
                        layout5.setVisibility(View.VISIBLE);
                        layout6.setVisibility(View.VISIBLE);
                        layout7.setVisibility(View.VISIBLE);
                        layout8.setVisibility(View.VISIBLE);
                        layout9.setVisibility(View.GONE);
                        layout10.setVisibility(View.GONE);

                        view1.setVisibility(View.VISIBLE);
                        view2.setVisibility(View.VISIBLE);
                        view3.setVisibility(View.VISIBLE);
                        view4.setVisibility(View.VISIBLE);
                        view5.setVisibility(View.VISIBLE);
                        view6.setVisibility(View.VISIBLE);
                        view7.setVisibility(View.VISIBLE);
                        view8.setVisibility(View.VISIBLE);
                        view9.setVisibility(View.GONE);
                    }else if (counterList[i].equals("9"))
                    {
                        totalPDF="9";
                        layout1.setVisibility(View.VISIBLE);
                        layout2.setVisibility(View.VISIBLE);
                        layout3.setVisibility(View.VISIBLE);
                        layout4.setVisibility(View.VISIBLE);
                        layout5.setVisibility(View.VISIBLE);
                        layout6.setVisibility(View.VISIBLE);
                        layout7.setVisibility(View.VISIBLE);
                        layout8.setVisibility(View.VISIBLE);
                        layout9.setVisibility(View.VISIBLE);
                        layout10.setVisibility(View.GONE);

                        view1.setVisibility(View.VISIBLE);
                        view2.setVisibility(View.VISIBLE);
                        view3.setVisibility(View.VISIBLE);
                        view4.setVisibility(View.VISIBLE);
                        view5.setVisibility(View.VISIBLE);
                        view6.setVisibility(View.VISIBLE);
                        view7.setVisibility(View.VISIBLE);
                        view8.setVisibility(View.VISIBLE);
                        view9.setVisibility(View.VISIBLE);
                    }else if (counterList[i].equals("10"))
                    {
                        totalPDF="10";
                        layout1.setVisibility(View.VISIBLE);
                        layout2.setVisibility(View.VISIBLE);
                        layout3.setVisibility(View.VISIBLE);
                        layout4.setVisibility(View.VISIBLE);
                        layout5.setVisibility(View.VISIBLE);
                        layout6.setVisibility(View.VISIBLE);
                        layout7.setVisibility(View.VISIBLE);
                        layout8.setVisibility(View.VISIBLE);
                        layout9.setVisibility(View.VISIBLE);
                        layout10.setVisibility(View.VISIBLE);

                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });
        }catch (Exception e){}




        String[] showPass1= getResources().getStringArray(R.array.setPassword);
        try {
            ArrayAdapter<String> adapter;
            adapter = new ArrayAdapter<String>(this, R.layout.spinneritem, getResources().getStringArray(R.array.setPassword));
            adapter.setDropDownViewResource(R.layout.row_simple_spinner_dropdown_item);
            //setting adapter to spinner
            spinner_pass1.setAdapter(adapter);

            spinner_pass1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (showPass1[i].equals("Yes"))
                    {
                        layPassword1.setVisibility(View.VISIBLE);

                    }else
                    {
                        layPassword1.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });
        }catch (Exception e){}



        String[] showPass2= getResources().getStringArray(R.array.setPassword);
        try {
            ArrayAdapter<String> adapter;
            adapter = new ArrayAdapter<String>(this, R.layout.spinneritem, getResources().getStringArray(R.array.setPassword));
            adapter.setDropDownViewResource(R.layout.row_simple_spinner_dropdown_item);
            //setting adapter to spinner
            spinner_pass2.setAdapter(adapter);

            spinner_pass2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (showPass2[i].equals("Yes"))
                    {
                        layPassword2.setVisibility(View.VISIBLE);

                    }else
                    {
                        layPassword2.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });
        }catch (Exception e){}


        String[] showPass3= getResources().getStringArray(R.array.setPassword);
        try {
            ArrayAdapter<String> adapter;
            adapter = new ArrayAdapter<String>(this, R.layout.spinneritem, getResources().getStringArray(R.array.setPassword));
            adapter.setDropDownViewResource(R.layout.row_simple_spinner_dropdown_item);
            //setting adapter to spinner
            spinner_pass3.setAdapter(adapter);

            spinner_pass3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (showPass3[i].equals("Yes"))
                    {
                        layPassword3.setVisibility(View.VISIBLE);

                    }else
                    {
                        layPassword3.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });
        }catch (Exception e){}


        String[] showPass4= getResources().getStringArray(R.array.setPassword);
        try {
            ArrayAdapter<String> adapter;
            adapter = new ArrayAdapter<String>(this, R.layout.spinneritem, getResources().getStringArray(R.array.setPassword));
            adapter.setDropDownViewResource(R.layout.row_simple_spinner_dropdown_item);
            //setting adapter to spinner
            spinner_pass4.setAdapter(adapter);

            spinner_pass4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (showPass4[i].equals("Yes"))
                    {
                        layPassword4.setVisibility(View.VISIBLE);

                    }else
                    {
                        layPassword4.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });
        }catch (Exception e){}


        String[] showPass5= getResources().getStringArray(R.array.setPassword);
        try {
            ArrayAdapter<String> adapter;
            adapter = new ArrayAdapter<String>(this, R.layout.spinneritem, getResources().getStringArray(R.array.setPassword));
            adapter.setDropDownViewResource(R.layout.row_simple_spinner_dropdown_item);
            //setting adapter to spinner
            spinner_pass5.setAdapter(adapter);

            spinner_pass5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (showPass5[i].equals("Yes"))
                    {
                        layPassword5.setVisibility(View.VISIBLE);

                    }else
                    {
                        layPassword5.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });
        }catch (Exception e){}


        String[] showPass6= getResources().getStringArray(R.array.setPassword);
        try {
            ArrayAdapter<String> adapter;
            adapter = new ArrayAdapter<String>(this, R.layout.spinneritem, getResources().getStringArray(R.array.setPassword));
            adapter.setDropDownViewResource(R.layout.row_simple_spinner_dropdown_item);
            //setting adapter to spinner
            spinner_pass6.setAdapter(adapter);

            spinner_pass6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (showPass6[i].equals("Yes"))
                    {
                        layPassword6.setVisibility(View.VISIBLE);

                    }else
                    {
                        layPassword6.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });
        }catch (Exception e){}


        String[] showPass7= getResources().getStringArray(R.array.setPassword);
        try {
            ArrayAdapter<String> adapter;
            adapter = new ArrayAdapter<String>(this, R.layout.spinneritem, getResources().getStringArray(R.array.setPassword));
            adapter.setDropDownViewResource(R.layout.row_simple_spinner_dropdown_item);
            //setting adapter to spinner
            spinner_pass7.setAdapter(adapter);

            spinner_pass7.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (showPass7[i].equals("Yes"))
                    {
                        layPassword7.setVisibility(View.VISIBLE);

                    }else
                    {
                        layPassword7.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });
        }catch (Exception e){}


        String[] showPass8= getResources().getStringArray(R.array.setPassword);
        try {
            ArrayAdapter<String> adapter;
            adapter = new ArrayAdapter<String>(this, R.layout.spinneritem, getResources().getStringArray(R.array.setPassword));
            adapter.setDropDownViewResource(R.layout.row_simple_spinner_dropdown_item);
            //setting adapter to spinner
            spinner_pass8.setAdapter(adapter);

            spinner_pass8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (showPass8[i].equals("Yes"))
                    {
                        layPassword8.setVisibility(View.VISIBLE);

                    }else
                    {
                        layPassword8.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });
        }catch (Exception e){}

        String[] showPass9= getResources().getStringArray(R.array.setPassword);
        try {
            ArrayAdapter<String> adapter;
            adapter = new ArrayAdapter<String>(this, R.layout.spinneritem, getResources().getStringArray(R.array.setPassword));
            adapter.setDropDownViewResource(R.layout.row_simple_spinner_dropdown_item);
            //setting adapter to spinner
            spinner_pass9.setAdapter(adapter);

            spinner_pass9.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (showPass9[i].equals("Yes"))
                    {
                        layPassword9.setVisibility(View.VISIBLE);

                    }else
                    {
                        layPassword9.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });
        }catch (Exception e){}

        String[] showPass10= getResources().getStringArray(R.array.setPassword);
        try {
            ArrayAdapter<String> adapter;
            adapter = new ArrayAdapter<String>(this, R.layout.spinneritem, getResources().getStringArray(R.array.setPassword));
            adapter.setDropDownViewResource(R.layout.row_simple_spinner_dropdown_item);
            //setting adapter to spinner
            spinner_pass10.setAdapter(adapter);

            spinner_pass10.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (showPass10[i].equals("Yes"))
                    {
                        layPassword10.setVisibility(View.VISIBLE);

                    }else
                    {
                        layPassword10.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });
        }catch (Exception e){}



        imgpdf1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType="pdf1";

               /* Intent pdfIntent = new Intent(Intent.ACTION_GET_CONTENT);
                pdfIntent.setType("application/pdf");
                pdfIntent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(pdfIntent, 12);*/

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(Intent.createChooser(intent, "Select a File"), PICK_FILE);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Toast.makeText(PVCCardActivity.this, "Please install a File Manager.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });


        imgpdf2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType="pdf2";


                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(Intent.createChooser(intent, "Select a File"), PICK_FILE);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Toast.makeText(PVCCardActivity.this, "Please install a File Manager.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });


        imgpdf3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType="pdf3";


                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(Intent.createChooser(intent, "Select a File"), PICK_FILE);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Toast.makeText(PVCCardActivity.this, "Please install a File Manager.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        imgpdf4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType="pdf4";


                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(Intent.createChooser(intent, "Select a File"), PICK_FILE);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Toast.makeText(PVCCardActivity.this, "Please install a File Manager.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        imgpdf5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType="pdf5";


                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(Intent.createChooser(intent, "Select a File"), PICK_FILE);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Toast.makeText(PVCCardActivity.this, "Please install a File Manager.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        imgpdf6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType="pdf6";


                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(Intent.createChooser(intent, "Select a File"), PICK_FILE);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Toast.makeText(PVCCardActivity.this, "Please install a File Manager.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });


        imgpdf7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType="pdf7";


                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(Intent.createChooser(intent, "Select a File"), PICK_FILE);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Toast.makeText(PVCCardActivity.this, "Please install a File Manager.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });


        imgpdf8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType="pdf8";


                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(Intent.createChooser(intent, "Select a File"), PICK_FILE);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Toast.makeText(PVCCardActivity.this, "Please install a File Manager.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });


        imgpdf9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType="pdf9";


                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(Intent.createChooser(intent, "Select a File"), PICK_FILE);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Toast.makeText(PVCCardActivity.this, "Please install a File Manager.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        imgpdf10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType="pdf10";


                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(Intent.createChooser(intent, "Select a File"), PICK_FILE);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Toast.makeText(PVCCardActivity.this, "Please install a File Manager.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edName.getText().toString().isEmpty())
                {
                    Toast.makeText(PVCCardActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
                    return;
                }else if (edMobile.getText().toString().isEmpty())
                {
                    Toast.makeText(PVCCardActivity.this, "Please enter mobile no", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    //Util.hideKeyBorad(PanCardActivity.this, v);


                    pdf1Files=new ArrayList<>();
                    for (int i = 0; i < pdf1ArrayList.size(); i++) {

                        if (pdf1ArrayList.get(i) != null && !pdf1ArrayList.get(i).equals("")) {
                            String mCurrentPhotoPath =  pdf1ArrayList.get(0);
                            File mImageFile = new File(mCurrentPhotoPath);
                            pdf1Files.add(mImageFile);
                        }
                    }

                    pdf2Files=new ArrayList<>();
                    for (int i = 0; i < pdf2ArrayList.size(); i++) {

                        if (pdf2ArrayList.get(i) != null && !pdf2ArrayList.get(i).equals("")) {
                            String mCurrentPhotoPath =  pdf2ArrayList.get(0);
                            File mImageFile = new File(mCurrentPhotoPath);
                            pdf2Files.add(mImageFile);
                        }
                    }

                    pdf3Files=new ArrayList<>();
                    for (int i = 0; i < pdf3ArrayList.size(); i++) {

                        if (pdf3ArrayList.get(i) != null && !pdf3ArrayList.get(i).equals("")) {
                            String mCurrentPhotoPath =  pdf3ArrayList.get(0);
                            File mImageFile = new File(mCurrentPhotoPath);
                            pdf3Files.add(mImageFile);
                        }
                    }

                    pdf4Files=new ArrayList<>();
                    for (int i = 0; i < pdf4ArrayList.size(); i++) {

                        if (pdf4ArrayList.get(i)!= null && !pdf4ArrayList.get(i).equals("")) {
                            String mCurrentPhotoPath =  pdf4ArrayList.get(0);
                            File mImageFile = new File(mCurrentPhotoPath);
                            pdf4Files.add(mImageFile);
                        }
                    }

                    pdf5Files=new ArrayList<>();
                    for (int i = 0; i < pdf5ArrayList.size(); i++) {

                        if (pdf5ArrayList.get(i) != null && !pdf5ArrayList.get(i).equals("")) {
                            String mCurrentPhotoPath =  pdf5ArrayList.get(0);
                            File mImageFile = new File(mCurrentPhotoPath);
                            pdf5Files.add(mImageFile);
                        }
                    }


                    pdf6Files=new ArrayList<>();
                    for (int i = 0; i < pdf6ArrayList.size(); i++) {

                        if (pdf6ArrayList.get(i) != null && !pdf6ArrayList.get(i).equals("")) {
                            String mCurrentPhotoPath =  pdf6ArrayList.get(0);
                            File mImageFile = new File(mCurrentPhotoPath);
                            pdf6Files.add(mImageFile);
                        }
                    }

                    pdf7Files=new ArrayList<>();
                    for (int i = 0; i < pdf7ArrayList.size(); i++) {

                        if (pdf7ArrayList.get(i) != null && !pdf7ArrayList.get(i).equals("")) {
                            String mCurrentPhotoPath =  pdf7ArrayList.get(0);
                            File mImageFile = new File(mCurrentPhotoPath);
                            pdf7Files.add(mImageFile);
                        }
                    }


                    pdf8Files=new ArrayList<>();
                    for (int i = 0; i < pdf8ArrayList.size(); i++) {

                        if (pdf8ArrayList.get(i) != null && !pdf8ArrayList.get(i).equals("")) {
                            String mCurrentPhotoPath =  pdf8ArrayList.get(0);
                            File mImageFile = new File(mCurrentPhotoPath);
                            pdf8Files.add(mImageFile);
                        }
                    }

                    pdf9Files=new ArrayList<>();
                    for (int i = 0; i < pdf9ArrayList.size(); i++) {

                        if (pdf9ArrayList.get(i) != null && !pdf9ArrayList.get(i).equals("")) {
                            String mCurrentPhotoPath =  pdf9ArrayList.get(0);
                            File mImageFile = new File(mCurrentPhotoPath);
                            pdf9Files.add(mImageFile);
                        }
                    }

                    pdf10Files=new ArrayList<>();
                    for (int i = 0; i < pdf10ArrayList.size(); i++) {

                        if (pdf10ArrayList.get(i) != null && !pdf10ArrayList.get(i).equals("")) {
                            String mCurrentPhotoPath =  pdf10ArrayList.get(0);
                            File mImageFile = new File(mCurrentPhotoPath);
                            pdf10Files.add(mImageFile);
                        }
                    }

                        callServiceSave();

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


        Location loc1 = new Location("");
        loc1.setLatitude(givenLatitude);
        loc1.setLongitude(givenLongitude);

        Location loc2 = new Location("");
        loc2.setLatitude(latitude);
        loc2.setLongitude(longitude);

        float distanceInMeters = loc1.distanceTo(loc2);
        float distanceCalculated = loc1.distanceTo(loc2)/1000;
        double newDistKM= Double.parseDouble(new DecimalFormat("##.##").format(distanceCalculated));
        distanceCovered= newDistKM;

        RequestBody paymentStatus,retailerId;

        String retailerid=Preferences.getString(AppConstants.MOBILE);

        retailerId=createPartFromString(retailerid);
        paymentStatus=createPartFromString("pending");
        RequestBody name = createPartFromString(edName.getText().toString());
        RequestBody mobileNo = createPartFromString(edMobile.getText().toString());
        RequestBody totalPDF1 = createPartFromString(totalPDF);
        RequestBody lang = createPartFromString(String.valueOf(longitude));
        RequestBody lat = createPartFromString(String.valueOf(latitude));
        RequestBody password1 = createPartFromString(edPassword1.getText().toString());
        RequestBody password2 = createPartFromString(edPassword2.getText().toString());
        RequestBody password3 = createPartFromString(edPassword3.getText().toString());
        RequestBody password4 = createPartFromString(edPassword4.getText().toString());
        RequestBody password5 = createPartFromString(edPassword5.getText().toString());
        RequestBody password6 = createPartFromString(edPassword6.getText().toString());
        RequestBody password7 = createPartFromString(edPassword7.getText().toString());
        RequestBody password8 = createPartFromString(edPassword8.getText().toString());
        RequestBody password9 = createPartFromString(edPassword9.getText().toString());
        RequestBody password10 = createPartFromString(edPassword10.getText().toString());


        MultipartBody.Part[] pdf1Parts = new MultipartBody.Part[pdf1Files.size()];
        for (int index = 0; index < pdf1Files.size(); index++) {
            RequestBody reqFile = RequestBody.create(MediaType.parse("application/pdf"), pdf1Files.get(index));
            pdf1Parts[index] = MultipartBody.Part.createFormData("pdf1", pdf1Files.get(index).getName(), reqFile);
        }

        MultipartBody.Part[] pdf2Parts = new MultipartBody.Part[pdf2Files.size()];
        for (int index = 0; index < pdf2Files.size(); index++) {
            RequestBody reqFile = RequestBody.create(MediaType.parse("application/pdf"), pdf2Files.get(index));
            pdf2Parts[index] = MultipartBody.Part.createFormData("pdf2", pdf2Files.get(index).getName(), reqFile);
        }

        MultipartBody.Part[] pdf3Parts = new MultipartBody.Part[pdf3Files.size()];
        for (int index = 0; index < pdf3Files.size(); index++) {
            RequestBody reqFile = RequestBody.create(MediaType.parse("application/pdf"), pdf3Files.get(index));
            pdf3Parts[index] = MultipartBody.Part.createFormData("pdf3", pdf3Files.get(index).getName(), reqFile);
        }

        MultipartBody.Part[] pdf4Parts = new MultipartBody.Part[pdf4Files.size()];
        for (int index = 0; index < pdf4Files.size(); index++) {
            RequestBody reqFile = RequestBody.create(MediaType.parse("application/pdf"), pdf4Files.get(index));
            pdf4Parts[index] = MultipartBody.Part.createFormData("pdf4", pdf4Files.get(index).getName(), reqFile);
        }

        MultipartBody.Part[] pdf5Parts = new MultipartBody.Part[pdf5Files.size()];
        for (int index = 0; index < pdf5Files.size(); index++) {
            RequestBody reqFile = RequestBody.create(MediaType.parse("application/pdf"), pdf5Files.get(index));
            pdf5Parts[index] = MultipartBody.Part.createFormData("pdf5", pdf5Files.get(index).getName(), reqFile);
        }


        MultipartBody.Part[] pdf6Parts = new MultipartBody.Part[pdf6Files.size()];
        for (int index = 0; index < pdf6Files.size(); index++) {
            RequestBody reqFile = RequestBody.create(MediaType.parse("application/pdf"), pdf6Files.get(index));
            pdf6Parts[index] = MultipartBody.Part.createFormData("pdf6", pdf6Files.get(index).getName(), reqFile);
        }


        MultipartBody.Part[] pdf7Parts = new MultipartBody.Part[pdf7Files.size()];
        for (int index = 0; index < pdf7Files.size(); index++) {
            RequestBody reqFile = RequestBody.create(MediaType.parse("application/pdf"), pdf7Files.get(index));
            pdf7Parts[index] = MultipartBody.Part.createFormData("pdf7", pdf7Files.get(index).getName(), reqFile);
        }

        MultipartBody.Part[] pdf8Parts = new MultipartBody.Part[pdf8Files.size()];
        for (int index = 0; index < pdf8Files.size(); index++) {
            RequestBody reqFile = RequestBody.create(MediaType.parse("application/pdf"), pdf8Files.get(index));
            pdf8Parts[index] = MultipartBody.Part.createFormData("pdf8", pdf8Files.get(index).getName(), reqFile);
        }

        MultipartBody.Part[] pdf9Parts = new MultipartBody.Part[pdf9Files.size()];
        for (int index = 0; index < pdf9Files.size(); index++) {
            RequestBody reqFile = RequestBody.create(MediaType.parse("application/pdf"), pdf9Files.get(index));
            pdf9Parts[index] = MultipartBody.Part.createFormData("pdf9", pdf9Files.get(index).getName(), reqFile);
        }

        MultipartBody.Part[] pdf10Parts = new MultipartBody.Part[pdf10Files.size()];
        for (int index = 0; index < pdf10Files.size(); index++) {
            RequestBody reqFile = RequestBody.create(MediaType.parse("application/pdf"), pdf10Files.get(index));
            pdf10Parts[index] = MultipartBody.Part.createFormData("pdf10", pdf10Files.get(index).getName(), reqFile);
        }


        //Call<ScannerResponse> call = apiService.saveScan(orderId1,vpa1,name1,amount1,mon_no1,member_id1,password1);
        Call<BasePVCResponse> call = apiService.pvccardSave(retailerId,paymentStatus,name,mobileNo,
                totalPDF1,lang,lat,password1,password2,password3,password4,password5,password6,password7,
                password8,password9,password10,pdf1Parts,pdf2Parts,pdf3Parts,pdf4Parts,pdf5Parts,pdf6Parts,
                pdf7Parts,pdf8Parts,pdf9Parts,pdf10Parts);


        //making the call to generate checksum
        call.enqueue(new Callback<BasePVCResponse>() {
            @Override
            public void onResponse(Call<BasePVCResponse> call, Response<BasePVCResponse> response) {
                progress_bar.setVisibility(View.GONE);

                if (response.body().getStatus()==true)
                {



                    Toast.makeText(PVCCardActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(PVCCardActivity.this, NewMainActivity.class);
                    //   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else {
                    Toast.makeText(PVCCardActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(PVCCardActivity.this, NewMainActivity.class);
                    //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }


                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter

            }

            @Override
            public void onFailure(Call<BasePVCResponse> call, Throwable t) {
                progress_bar.setVisibility(View.GONE);
                // callServiceFalse(mobileNo);
                Toast.makeText(PVCCardActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (selectType.equals("pdf1"))
            { // Initialize result data
                // check condition
                if (data != null) {
                    // When data is not equal to empty
                    // Get PDf uri
                    Uri sUri = data.getData();


                    String sPath = sUri.getPath();
                    // set Uri on text view
                    text_pdf1.setText(Html.fromHtml(
                            "<big><b>PDF Uri</b></big><br>"
                                    + sUri)+"    "+Html.fromHtml(
                            "<big><b>PDF Path</b></big><br>"
                                    + sPath));

                    imgpdf1.setImageResource(R.drawable.pdf);

                   /* // Get PDF path

                    // Set path on text view
                    tvPath.setText(Html.fromHtml(
                            "<big><b>PDF Path</b></big><br>"
                                    + sPath));*/

                    Uri uri = data.getData();
                    String uriString = uri.toString();
                    File myFile = new File(uriString);

                    String path = getFilePathFromURI(PVCCardActivity.this,uri);
                    Log.d("ioooo",path);

                    pdf1ArrayList.add(path);
                }
            }else  if (selectType.equals("pdf2"))
            {
                if (data != null) {

                    Uri sUri = data.getData();
                    String sPath = sUri.getPath();
                    text_pdf2.setText(Html.fromHtml(
                            "<big><b>PDF Uri</b></big><br>"
                                    + sUri)+"    "+Html.fromHtml(
                            "<big><b>PDF Path</b></big><br>"
                                    + sPath));
                    imgpdf2.setImageResource(R.drawable.pdf);

                    Uri uri = data.getData();
                    String uriString = uri.toString();
                    File myFile = new File(uriString);

                    String path = getFilePathFromURI(PVCCardActivity.this,uri);
                    Log.d("ioooo",path);

                    pdf2ArrayList.add(path);
                }
            }else  if (selectType.equals("pdf3"))
            {
                if (data != null) {

                    Uri sUri = data.getData();
                    String sPath = sUri.getPath();
                    text_pdf3.setText(Html.fromHtml(
                            "<big><b>PDF Uri</b></big><br>"
                                    + sUri)+"    "+Html.fromHtml(
                            "<big><b>PDF Path</b></big><br>"
                                    + sPath));
                    imgpdf3.setImageResource(R.drawable.pdf);

                    Uri uri = data.getData();
                    String uriString = uri.toString();
                    File myFile = new File(uriString);

                    String path = getFilePathFromURI(PVCCardActivity.this,uri);
                    Log.d("ioooo",path);

                    pdf3ArrayList.add(path);
                }
            }else  if (selectType.equals("pdf4"))
            {
                if (data != null) {

                    Uri sUri = data.getData();
                    String sPath = sUri.getPath();
                    text_pdf4.setText(Html.fromHtml(
                            "<big><b>PDF Uri</b></big><br>"
                                    + sUri)+"    "+Html.fromHtml(
                            "<big><b>PDF Path</b></big><br>"
                                    + sPath));
                    imgpdf4.setImageResource(R.drawable.pdf);

                    Uri uri = data.getData();
                    String uriString = uri.toString();
                    File myFile = new File(uriString);

                    String path = getFilePathFromURI(PVCCardActivity.this,uri);
                    Log.d("ioooo",path);

                    pdf4ArrayList.add(path);
                }
            }else  if (selectType.equals("pdf5"))
            {
                if (data != null) {

                    Uri sUri = data.getData();
                    String sPath = sUri.getPath();
                    text_pdf5.setText(Html.fromHtml(
                            "<big><b>PDF Uri</b></big><br>"
                                    + sUri)+"    "+Html.fromHtml(
                            "<big><b>PDF Path</b></big><br>"
                                    + sPath));
                    imgpdf5.setImageResource(R.drawable.pdf);

                    Uri uri = data.getData();
                    String uriString = uri.toString();
                    File myFile = new File(uriString);

                    String path = getFilePathFromURI(PVCCardActivity.this,uri);
                    Log.d("ioooo",path);

                    pdf5ArrayList.add(path);
                }
            }else  if (selectType.equals("pdf6"))
            {
                if (data != null) {

                    Uri sUri = data.getData();
                    String sPath = sUri.getPath();
                    text_pdf6.setText(Html.fromHtml(
                            "<big><b>PDF Uri</b></big><br>"
                                    + sUri)+"    "+Html.fromHtml(
                            "<big><b>PDF Path</b></big><br>"
                                    + sPath));
                    imgpdf6.setImageResource(R.drawable.pdf);

                    Uri uri = data.getData();
                    String uriString = uri.toString();
                    File myFile = new File(uriString);

                    String path = getFilePathFromURI(PVCCardActivity.this,uri);
                    Log.d("ioooo",path);

                    pdf6ArrayList.add(path);
                }
            }else  if (selectType.equals("pdf7"))
            {
                if (data != null) {

                    Uri sUri = data.getData();
                    String sPath = sUri.getPath();
                    text_pdf7.setText(Html.fromHtml(
                            "<big><b>PDF Uri</b></big><br>"
                                    + sUri)+"    "+Html.fromHtml(
                            "<big><b>PDF Path</b></big><br>"
                                    + sPath));
                    imgpdf7.setImageResource(R.drawable.pdf);

                    Uri uri = data.getData();
                    String uriString = uri.toString();
                    File myFile = new File(uriString);

                    String path = getFilePathFromURI(PVCCardActivity.this,uri);
                    Log.d("ioooo",path);

                    pdf7ArrayList.add(path);
                }
            }else  if (selectType.equals("pdf8"))
            {
                if (data != null) {

                    Uri sUri = data.getData();
                    String sPath = sUri.getPath();
                    text_pdf8.setText(Html.fromHtml(
                            "<big><b>PDF Uri</b></big><br>"
                                    + sUri)+"    "+Html.fromHtml(
                            "<big><b>PDF Path</b></big><br>"
                                    + sPath));
                    imgpdf8.setImageResource(R.drawable.pdf);

                    Uri uri = data.getData();
                    String uriString = uri.toString();
                    File myFile = new File(uriString);

                    String path = getFilePathFromURI(PVCCardActivity.this,uri);
                    Log.d("ioooo",path);

                    pdf8ArrayList.add(path);
                }
            }else  if (selectType.equals("pdf9"))
            {
                if (data != null) {

                    Uri sUri = data.getData();
                    String sPath = sUri.getPath();
                    text_pdf9.setText(Html.fromHtml(
                            "<big><b>PDF Uri</b></big><br>"
                                    + sUri)+"    "+Html.fromHtml(
                            "<big><b>PDF Path</b></big><br>"
                                    + sPath));
                    imgpdf9.setImageResource(R.drawable.pdf);

                    Uri uri = data.getData();
                    String uriString = uri.toString();
                    File myFile = new File(uriString);

                    String path = getFilePathFromURI(PVCCardActivity.this,uri);
                    Log.d("ioooo",path);

                    pdf9ArrayList.add(path);
                }
            }else  if (selectType.equals("pdf10"))
            {
                if (data != null) {

                    Uri sUri = data.getData();
                    String sPath = sUri.getPath();
                    text_pdf10.setText(Html.fromHtml(
                            "<big><b>PDF Uri</b></big><br>"
                                    + sUri)+"    "+Html.fromHtml(
                            "<big><b>PDF Path</b></big><br>"
                                    + sPath));
                    imgpdf10.setImageResource(R.drawable.pdf);

                    Uri uri = data.getData();
                    String uriString = uri.toString();
                    File myFile = new File(uriString);

                    String path = getFilePathFromURI(PVCCardActivity.this,uri);
                    Log.d("ioooo",path);

                    pdf10ArrayList.add(path);
                }
            }

        }
    }



    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }



    public static String getFilePathFromURI(Context context, Uri contentUri) {
        //copy file and send new file path
        String fileName = getFileName(contentUri);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }
        if (!TextUtils.isEmpty(fileName)) {
            File copyFile = new File(wallpaperDirectory + File.separator + fileName);
            // create folder if not exists

            copy(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    public static String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }


    public static void copy(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            copystream(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final int BUFFER_SIZE = 1024 * 2;
    private static final String IMAGE_DIRECTORY = "/demonuts_upload_gallery";


    public static int copystream(InputStream input, OutputStream output) throws Exception, IOException {
        byte[] buffer = new byte[BUFFER_SIZE];

        BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
        BufferedOutputStream out = new BufferedOutputStream(output, BUFFER_SIZE);
        int count = 0, n = 0;
        try {
            while ((n = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                out.write(buffer, 0, n);
                count += n;
            }
            out.flush();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                Log.e(e.getMessage(), String.valueOf(e));
            }
            try {
                in.close();
            } catch (IOException e) {
                Log.e(e.getMessage(), String.valueOf(e));
            }
        }
        return count;
    }


    public void getLocation(){
        gpsTracker = new GpsTracker(PVCCardActivity.this);
        if(gpsTracker.canGetLocation()){
             latitude = gpsTracker.getLatitude();
             longitude = gpsTracker.getLongitude();

            Log.e("LATLANG","Latitude:  " +String.valueOf(latitude));
            Log.e("LATLANG","Longitude:  " +String.valueOf(longitude));


        }else{
            gpsTracker.showSettingsAlert();
        }
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
        Call<BaseHeadingResponse> call = apiService.callGetHeadingService("pvcprint","list");


        //making the call to generate checksum
        call.enqueue(new Callback<BaseHeadingResponse>() {
            @Override
            public void onResponse(Call<BaseHeadingResponse> call, Response<BaseHeadingResponse> response) {
                progress_bar.setVisibility(View.GONE);

                if (response.body().getStatus()==true)
                {
                   // Toast.makeText(PVCCardActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    tvHeading.setText(response.body().getData().get(0).getContent());
                    tvHeading.setSelected(true);
                }else {
                    Toast.makeText(PVCCardActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                }


                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter

            }

            @Override
            public void onFailure(Call<BaseHeadingResponse> call, Throwable t) {
                progress_bar.setVisibility(View.GONE);
                // callServiceFalse(mobileNo);
                Toast.makeText(PVCCardActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }



}
