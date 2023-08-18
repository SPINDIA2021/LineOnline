package com.spindia.rechargeapp.admin.recharge

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.myapp.onlysratchapp.category.CategoryResponse
import com.spindia.rechargeapp.R
import com.spindia.rechargeapp.admin.BaseAdminRechargeResponse
import com.spindia.rechargeapp.category.CategoryContract
import com.spindia.rechargeapp.category.CategoryContract.Presenter
import com.spindia.rechargeapp.category.CategoryPresenter
import com.spindia.rechargeapp.model.RecentRechargeHistoryModal
import com.spindia.rechargeapp.network.Injection
import com.spindia.rechargeapp.network.Preferences
import com.spindia.rechargeapp.network_calls.AppApiCalls
import com.spindia.rechargeapp.pancardlist.BasePanCardlistResponse
import com.spindia.rechargeapp.pancardlist.PancardListAdapter
import com.spindia.rechargeapp.recharge_services.RecentRechargesAdapter
import com.spindia.rechargeapp.utils.*
import com.spindia.rechargeapp.utils.AppCommonMethods.Companion.getCurrentDateTime
import okhttp3.OkHttpClient

import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class AdminRechargeReportsActivity : AppCompatActivity(),
    SwipeRefreshLayout.OnRefreshListener, CategoryContract.View {
    private var presenter: Presenter? = null
   // lateinit var userModel: UserModel
    lateinit var rechargeHistoryModal: AdminRechargeResponse
    lateinit var recentRechargesAdapter: AdminRechargesAdapter
    var recentRechargeHistoryModalArrayList = ArrayList<AdminRechargeResponse>()
    lateinit var ivBackBtn:ImageView

    lateinit var tvFromDateRecHistory:TextView
    lateinit var tvToDateRecHistory:TextView
    lateinit var ll_from_date:LinearLayout
    lateinit var ll_to_date:LinearLayout
    lateinit var mSwipeRefresh:SwipeRefreshLayout
    lateinit var progress_bar:RelativeLayout
    lateinit var rvRechargeHistory:RecyclerView
    var mInterstitialAd: InterstitialAd? = null
    var pd: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.status_bar, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
        setContentView(R.layout.activity_all_recharge_reports)
        ivBackBtn=findViewById(R.id.ivBackBtn)

        tvFromDateRecHistory=findViewById(R.id.tvFromDateRecHistory)
        tvToDateRecHistory=findViewById(R.id.tvToDateRecHistory)
        ll_from_date=findViewById(R.id.ll_from_date)
        ll_to_date=findViewById(R.id.ll_to_date)
        mSwipeRefresh=findViewById(R.id.mSwipeRefresh)
        progress_bar=findViewById(R.id.progress_bar)
        rvRechargeHistory=findViewById(R.id.rvRechargeHistory)


        pd = ProgressDialog(this@AdminRechargeReportsActivity)
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
                        mInterstitialAd!!.show(this@AdminRechargeReportsActivity)
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
        val gson = Gson()
        val json = AppPrefs.getStringPref("userModel", this)
       // userModel = gson.fromJson(json, UserModel::class.java)
        val date = getCurrentDateTime()
        val dateInString = date.toString("dd MMM yyyy")
        tvFromDateRecHistory.setText(dateInString)
        tvToDateRecHistory.setText(dateInString)

        ll_from_date.setOnClickListener { FromdatePicker() }
        ll_to_date.setOnClickListener { TodatePicker() }


        CategoryPresenter(Injection.provideLoginRepository(this), this)

        callServiceGetRechargeHistory()
        mSwipeRefresh.setOnRefreshListener(this);

        mSwipeRefresh.post(Runnable {
            if (mSwipeRefresh != null) {
                mSwipeRefresh.setRefreshing(true)
            }
            recentRechargeHistoryModalArrayList.clear()
           callServiceGetRechargeHistory()
            mSwipeRefresh.setRefreshing(false)

        })


    }


    fun FromdatePicker() {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        var dpd =
            DatePickerDialog(this, { view, mYear, mMonth, mDay ->
                val mmMonth = mMonth + 1
                val date = "$mDay/$mmMonth/$mYear"

                tvFromDateRecHistory.setText(
                    AppCommonMethods.convertDateFormat(
                        "dd/MM/yyyy",
                        "dd MMM yyyy", date
                    ).toString()
                )
                compareTwoDates(
                    tvFromDateRecHistory.text.toString(),
                    tvToDateRecHistory.text.toString()
                )

            }, year, month, day)
        dpd.show()
    }

    fun TodatePicker() {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        var dpd =
            DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                val mmMonth = mMonth + 1
                val date = "$mDay/$mmMonth/$mYear"

                tvToDateRecHistory.setText(
                    AppCommonMethods.convertDateFormat(
                        "dd/MM/yyyy",
                        "dd MMM yyyy", date
                    ).toString()
                )


                compareTwoDates(
                    tvFromDateRecHistory.text.toString(),
                    tvToDateRecHistory.text.toString()
                )


            }, year, month, day)
        dpd.show()
    }

    private fun compareTwoDates(date: String, dateafter: String) {


        val dateFormat = SimpleDateFormat(
            "dd MMM yyyy"
        )
        var convertedDate: Date? = Date()
        var convertedDate2 = Date()
        try {
            convertedDate = dateFormat.parse(date)
            convertedDate2 = dateFormat.parse(dateafter)
            if (convertedDate2.after(convertedDate) || convertedDate2.equals(convertedDate)) {
                recentRechargeHistoryModalArrayList.clear()
                callServiceGetRechargeHistory()


            } else {
                toast("Invalid Date")
            }
        } catch (e: ParseException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }



    override fun onRefresh() {
        recentRechargeHistoryModalArrayList.clear()
       callServiceGetRechargeHistory()


        mSwipeRefresh.setRefreshing(false)

    }

    var retryClick = View.OnClickListener { v ->
        val i = v.tag as Int

        var txnId=recentRechargeHistoryModalArrayList.get(i).requestertxnid
        presenter!!.saveRetry(txnId, this)
    }

    override fun setPresenter(presenter: CategoryContract.Presenter?) {
        this.presenter = presenter
    }

    override fun categoryResponse(categoryResponse: ArrayList<CategoryResponse>?) {
        TODO("Not yet implemented")
    }

    override fun retryResponse(retryResponse: String?) {
        showSuccessOrFailedDialog("Recharge Status",retryResponse!!)
        recentRechargeHistoryModalArrayList.clear()


    }


    private fun showSuccessOrFailedDialog(title: String, message: String) {
        val builder1 =
            AlertDialog.Builder(this)
        builder1.setTitle("" + title)
        builder1.setMessage("" + message)
        builder1.setCancelable(false)
        builder1.setPositiveButton(
            "OK"
        ) { dialog, id ->
            dialog.dismiss()
        }
        /*
        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });*/
        val alert11 = builder1.create()
        alert11.show()
    }



    private fun callServiceGetRechargeHistory() {
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


        val call = apiService.callGetAdminRechargeListService("recharge")


        //making the call to generate checksum
        call.enqueue(object : Callback<BaseAdminRechargeResponse> {
            override fun onResponse(
                call: Call<BaseAdminRechargeResponse>,
                response: Response<BaseAdminRechargeResponse>
            ) {
                progress_bar.visibility = View.GONE
                if (response.body()!!.status == true) {
                    recentRechargeHistoryModalArrayList= response.body()!!.data
                    rvRechargeHistory.apply {

                        layoutManager = LinearLayoutManager(this@AdminRechargeReportsActivity)
                        recentRechargesAdapter = AdminRechargesAdapter(
                            context, recentRechargeHistoryModalArrayList, retryClick
                        )
                        rvRechargeHistory.adapter = recentRechargesAdapter
                    }

                } else {
                    Toast.makeText(
                        this@AdminRechargeReportsActivity,
                        response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()

                }


                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter
            }

            override fun onFailure(call: Call<BaseAdminRechargeResponse>, t: Throwable) {
                progress_bar.visibility = View.GONE
                // callServiceFalse(mobileNo);
                Toast.makeText(this@AdminRechargeReportsActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }


}