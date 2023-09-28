package com.spindia.rechargeapp.gstRegstration

import android.app.ProgressDialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.gson.GsonBuilder
import com.spindia.rechargeapp.AdminMainActivity
import com.spindia.rechargeapp.NewMainActivity
import com.spindia.rechargeapp.R
import com.spindia.rechargeapp.authentication.response.WalletResponse
import com.spindia.rechargeapp.network.Preferences
import com.spindia.rechargeapp.pancardOffline.EditPanCardActivity
import com.spindia.rechargeapp.pancardlist.BasePanCardlistResponse
import com.spindia.rechargeapp.pancardlist.PanCardlistResponse
import com.spindia.rechargeapp.pancardlist.PancardListAdapter
import com.spindia.rechargeapp.pancardlist.PancardStatusActivity
import com.spindia.rechargeapp.utils.AppConstants
import com.spindia.rechargeapp.utils.MainIAPI
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class GSTReportActivity: AppCompatActivity(),  SwipeRefreshLayout.OnRefreshListener {
    lateinit var ivBackBtn: ImageView
    lateinit var mSwipeRefresh:SwipeRefreshLayout
    lateinit var progress_bar: RelativeLayout
    lateinit var gstlistResponse: GSTResponse
    lateinit var gstAdapter: GSTListAdapter
    var gstListModalArrayList = ArrayList<GSTResponse>()
    lateinit var rvPancardHistory: RecyclerView
    lateinit var pd: ProgressDialog
    var walletBalance="0"
    private var mInterstitialAd: InterstitialAd? = null
    lateinit var tvTitle:TextView

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
        tvTitle=findViewById(R.id.tvTitle)

        tvTitle.setText("GST List")


        mSwipeRefresh=findViewById(R.id.mSwipeRefresh)
        progress_bar=findViewById(R.id.progress_bar)
        rvPancardHistory=findViewById(R.id.rvPancardHistory)

        pd = ProgressDialog(this@GSTReportActivity)
        pd.setMessage("Please wait ad is loading..")
        pd.show()
        MobileAds.initialize(
            this
        ) { }

        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this, "ca-app-pub-7161060381095883/7982531878", adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    pd.dismiss()
                    mInterstitialAd = interstitialAd
                    Log.i("TAG", "onAdLoaded")
                    if (mInterstitialAd != null) {
                        mInterstitialAd!!.show(this@GSTReportActivity)
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

        callServiceGetGSTList()

        callServiceGetWalletBalance()



        mSwipeRefresh.setOnRefreshListener(this);

        mSwipeRefresh.post(Runnable {
            if (mSwipeRefresh != null) {
                mSwipeRefresh.setRefreshing(true)
            }
            gstListModalArrayList.clear()
            callServiceGetGSTList()

            mSwipeRefresh.setRefreshing(false)

        })


    }

    private fun callServiceGetGSTList() {
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

        val rtid: RequestBody = createPartFromString(Preferences.getString(AppConstants.MOBILE))

        val formtype: RequestBody = createPartFromString("gst")



        val call = apiService.callGetGSTListService(rtid,formtype)


        //making the call to generate checksum
        call.enqueue(object : Callback<BaseGSTResponse> {
            override fun onResponse(
                call: Call<BaseGSTResponse>,
                response: Response<BaseGSTResponse>
            ) {
                progress_bar.visibility = View.GONE
                if (response.body()!!.status == true) {
                    gstListModalArrayList= response.body()!!.gstResponse
                    rvPancardHistory.apply {

                        layoutManager = LinearLayoutManager(this@GSTReportActivity)
                        gstAdapter = GSTListAdapter(
                            context, gstListModalArrayList, retryClick,editClick, statusClick,pdfClick,panInfoClick
                        )
                        rvPancardHistory.adapter = gstAdapter
                    }
                } else {
                    Toast.makeText(
                        this@GSTReportActivity,
                        response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()

                }


                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter
            }

            override fun onFailure(call: Call<BaseGSTResponse>, t: Throwable) {
                progress_bar.visibility = View.GONE
                // callServiceFalse(mobileNo);
                Toast.makeText(this@GSTReportActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun createPartFromString(descriptionString: String): RequestBody {
        return RequestBody.create(
            MultipartBody.FORM, descriptionString
        )
    }



    var orderId=" "
    var token:String=""
    var retryClick = View.OnClickListener { v ->
        val i = v.tag as Int

    }


    var editClick = View.OnClickListener { v ->
        val i = v.tag as Int


    }


    var statusClick = View.OnClickListener { v ->
        val i = v.tag as Int

    }

    var pdfClick = View.OnClickListener { v ->
        val i = v.tag as Int


    }



    var panInfoClick = View.OnClickListener { v ->
        val i = v.tag as Int


    }



    private fun callServiceGetWalletBalance() {
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
            .baseUrl(MainIAPI.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()


        //creating the retrofit api service
        val apiService = retrofit.create(MainIAPI::class.java)

        val retailerId: RequestBody = createPartFromString(Preferences.getString(AppConstants.MOBILE))
        val entry: RequestBody = createPartFromString("single")

        //Call<ScannerResponse> call = apiService.saveScan(orderId1,vpa1,name1,amount1,mon_no1,member_id1,password1);
        val call = apiService.getWalletBalance(retailerId,entry )


        //making the call to generate checksum
        call.enqueue(object : Callback<WalletResponse> {
            override fun onResponse(
                call: Call<WalletResponse>,
                response: Response<WalletResponse>
            ) {
                progress_bar.visibility = View.GONE
                if (response.body()!!.status == true) {

                    walletBalance=response.body()!!.data.toString()

                    /*  Toast.makeText(
                          this@NewMainActivity,
                          response.body()!!.message,
                          Toast.LENGTH_SHORT
                      ).show()*/

                } else {
                    Toast.makeText(
                        this@GSTReportActivity,
                        response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()

                }


                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter
            }

            override fun onFailure(call: Call<WalletResponse>, t: Throwable) {
                progress_bar.visibility = View.GONE
                // callServiceFalse(mobileNo);
                Toast.makeText(this@GSTReportActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onRefresh() {
        callServiceGetGSTList()
        mSwipeRefresh.setRefreshing(false)
    }


}