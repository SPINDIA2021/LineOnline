package com.spindia.rechargeapp.admin.user

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.webkit.ValueCallback
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.GsonBuilder
import com.paytm.pgsdk.PaytmOrder
import com.paytm.pgsdk.PaytmPGService
import com.paytm.pgsdk.PaytmPaymentTransactionCallback
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails
import com.spindia.rechargeapp.AdminMainActivity
import com.spindia.rechargeapp.NewMainActivity
import com.spindia.rechargeapp.R
import com.spindia.rechargeapp.authentication.response.WalletResponse
import com.spindia.rechargeapp.network.Preferences
import com.spindia.rechargeapp.paytmIntegration.Api
import com.spindia.rechargeapp.paytmIntegration.Checksum
import com.spindia.rechargeapp.paytmIntegration.Constants
import com.spindia.rechargeapp.paytmIntegration.Paytm
import com.spindia.rechargeapp.utils.*
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class AdminUserListActivity : AppCompatActivity(),
    SwipeRefreshLayout.OnRefreshListener {

    lateinit var pvcCardlistResponse: UserListResponse
    lateinit var pvccardAdapter: AdminUserAdapter
    var pvccardListModalArrayList = ArrayList<UserListResponse>()
    lateinit var ivBackBtn:ImageView

    lateinit var mSwipeRefresh:SwipeRefreshLayout
    lateinit var progress_bar:RelativeLayout
    lateinit var rvPancardHistory:RecyclerView
    var walletBalance="0"

    lateinit var tvTitle:TextView

    var mInterstitialAd: InterstitialAd? = null
    var pd: ProgressDialog? = null

    override fun onBackPressed() {
        super.onBackPressed()
        if (Preferences.getString(AppConstants.MOBILE).equals("9799754037")) {
            val intent = Intent(this, AdminMainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            val intent = Intent(this, NewMainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.status_bar, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
        setContentView(R.layout.activity_pancardlist)
        ivBackBtn=findViewById(R.id.ivBackBtn)


        mSwipeRefresh=findViewById(R.id.mSwipeRefresh)
        progress_bar=findViewById(R.id.progress_bar)
        rvPancardHistory=findViewById(R.id.rvPancardHistory)
        tvTitle=findViewById(R.id.tvTitle)

        tvTitle.setText("User List")


        pd = ProgressDialog(this@AdminUserListActivity)
        pd!!.setMessage("Please wait ad is loading..")
        pd!!.show()
        MobileAds.initialize(
            this
        ) { }

        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    pd!!.dismiss()
                    mInterstitialAd = interstitialAd
                    Log.i("TAG", "onAdLoaded")
                    if (mInterstitialAd != null) {
                        mInterstitialAd!!.show(this@AdminUserListActivity)
                    } else {
                        Log.d("TAG", "The interstitial ad wasn't ready yet.")
                    }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle the error
                    Log.d("TAG", loadAdError.toString())
                    mInterstitialAd = null
                }
            })



        ivBackBtn.setOnClickListener { onBackPressed() }

        callServiceGetPVCcard()



        mSwipeRefresh.setOnRefreshListener(this);

        mSwipeRefresh.post(Runnable {
            if (mSwipeRefresh != null) {
                mSwipeRefresh.setRefreshing(true)
            }
            pvccardListModalArrayList.clear()
            callServiceGetPVCcard()

            mSwipeRefresh.setRefreshing(false)

        })


    }




    override fun onRefresh() {

        callServiceGetPVCcard()
        mSwipeRefresh.setRefreshing(false)

    }


    private fun callServiceGetPVCcard() {
        progress_bar.visibility = View.VISIBLE
        System.setProperty("http.keepAlive", "false")
        val httpClient = OkHttpClient.Builder()
        httpClient.readTimeout(5, TimeUnit.MINUTES).connectTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES).retryOnConnectionFailure(true)
            .addInterceptor { chain ->
                val original = chain.request()
                val builder = original.newBuilder()

                //Request request = chain.request().newBuilder().addHeader("parameter", "value").build();
                builder.header("Content-Type", "application/x-www-form-urlencoded")
                val request = builder.method(original.method(), original.body())
                    .build()
                chain.proceed(request)
            }
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val client = httpClient.build()
        val retrofit = Retrofit.Builder()
            .baseUrl(MainIAPI.BASE_URL1)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()


        //creating the retrofit api service
        val apiService = retrofit.create(MainIAPI::class.java)


        val call = apiService.callGetAdminUserListService(
            "userlist"
        )


        //making the call to generate checksum
        call.enqueue(object : Callback<BaseUserListResponse> {
            override fun onResponse(
                call: Call<BaseUserListResponse>,
                response: Response<BaseUserListResponse>
            ) {
                progress_bar.visibility = View.GONE
                if (response.body()!!.status == true) {
                    pvccardListModalArrayList= response.body()!!.data
                    rvPancardHistory.apply {

                        layoutManager = LinearLayoutManager(this@AdminUserListActivity)
                        pvccardAdapter = AdminUserAdapter(
                            context, pvccardListModalArrayList)
                        rvPancardHistory.adapter = pvccardAdapter
                    }
                } else {
                    Toast.makeText(
                        this@AdminUserListActivity,
                        response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()

                }


                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter
            }

            override fun onFailure(call: Call<BaseUserListResponse>, t: Throwable) {
                progress_bar.visibility = View.GONE
                // callServiceFalse(mobileNo);
                Toast.makeText(this@AdminUserListActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }





    private fun createPartFromString(descriptionString: String): RequestBody {
        return RequestBody.create(
            MultipartBody.FORM, descriptionString
        )
    }




}