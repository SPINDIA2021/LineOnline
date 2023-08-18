package com.spindia.rechargeapp.recharge_services

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
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
import com.myapp.onlysratchapp.category.CategoryResponse
import com.spindia.rechargeapp.R
import com.spindia.rechargeapp.category.CategoryContract
import com.spindia.rechargeapp.category.CategoryContract.Presenter
import com.spindia.rechargeapp.category.CategoryPresenter
import com.spindia.rechargeapp.model.RecentRechargeHistoryModal
import com.spindia.rechargeapp.model.UserModel
import com.spindia.rechargeapp.network.Injection
import com.spindia.rechargeapp.network.Preferences
import com.spindia.rechargeapp.network_calls.AppApiCalls
import com.spindia.rechargeapp.utils.AppCommonMethods
import com.spindia.rechargeapp.utils.AppCommonMethods.Companion.getCurrentDateTime
import com.spindia.rechargeapp.utils.AppConstants
import com.spindia.rechargeapp.utils.AppPrefs
import com.spindia.rechargeapp.utils.toast

import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AllRechargeReportsActivity : AppCompatActivity(), AppApiCalls.OnAPICallCompleteListener,
    SwipeRefreshLayout.OnRefreshListener, CategoryContract.View {
    private var presenter: Presenter? = null
   // lateinit var userModel: UserModel
    lateinit var rechargeHistoryModal: RecentRechargeHistoryModal
    lateinit var recentRechargesAdapter: RecentRechargesAdapter
    var recentRechargeHistoryModalArrayList = ArrayList<RecentRechargeHistoryModal>()
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


        pd = ProgressDialog(this@AllRechargeReportsActivity)
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
                        mInterstitialAd!!.show(this@AllRechargeReportsActivity)
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


        mSwipeRefresh.setOnRefreshListener(this);

        mSwipeRefresh.post(Runnable {
            if (mSwipeRefresh != null) {
                mSwipeRefresh.setRefreshing(true)
            }
            recentRechargeHistoryModalArrayList.clear()
            rechargeHistoryFromTo(
                Preferences.getString("user_mobile"),
                AppCommonMethods.convertDateFormat(
                    "dd MMM yyyy",
                    "yyyy-MM-dd", tvFromDateRecHistory.text.toString()
                ).toString(),
                AppCommonMethods.convertDateFormat(
                    "dd MMM yyyy",
                    "yyyy-MM-dd", tvToDateRecHistory.text.toString()
                ).toString()
            )
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
                rechargeHistoryFromTo(
                    Preferences.getString("user_mobile"),
                    AppCommonMethods.convertDateFormat(
                        "dd MMM yyyy",
                        "yyyy-MM-dd", tvFromDateRecHistory.text.toString()
                    ).toString(),
                    AppCommonMethods.convertDateFormat(
                        "dd MMM yyyy",
                        "yyyy-MM-dd", tvToDateRecHistory.text.toString()
                    ).toString()
                )


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

    //API CALL FUNCTION DEFINITION
    private fun rechargeHistoryFromTo(
        cus_mobile: String,
        fromDate: String,
        toDate: String,

        ) {
        progress_bar.visibility = View.VISIBLE
        if (AppCommonMethods(this).isNetworkAvailable) {
            val mAPIcall = AppApiCalls(
                this,
                AppConstants.REC_HIST_API,
                this
            )
            mAPIcall.rechargeHistoryFromTo(
                Preferences.getString("user_mobile"),
                fromDate,
                toDate
            )

        } else {
            toast(getString(R.string.error_internet))
        }
    }

    override fun onAPICallCompleteListner(item: Any?, flag: String?, result: String) {
        if (flag.equals(AppConstants.REC_HIST_API)) {
            progress_bar.visibility = View.GONE
            Log.e(AppConstants.REC_HIST_API, result)
            val jsonObject = JSONObject(result)
            val status = jsonObject.getString(AppConstants.STATUS)
            val messageCode = jsonObject.getString(AppConstants.MESSAGE)
            Log.e(AppConstants.STATUS, status)
            Log.e(AppConstants.MESSAGE, messageCode)
            if (status.contains(AppConstants.TRUE)) {
                val cast = jsonObject.getJSONArray("data")
                for (i in 0 until cast.length()) {
                    val notifyObjJson = cast.getJSONObject(i)
                    rechargeHistoryModal = Gson()
                        .fromJson(notifyObjJson.toString(), RecentRechargeHistoryModal::class.java)
                    recentRechargeHistoryModalArrayList.add(rechargeHistoryModal)

                }



                rvRechargeHistory.apply {

                    layoutManager = LinearLayoutManager(this@AllRechargeReportsActivity)
                    recentRechargesAdapter = RecentRechargesAdapter(
                        context, recentRechargeHistoryModalArrayList, retryClick
                    )
                    rvRechargeHistory.adapter = recentRechargesAdapter
                }

            } else {
                toast(messageCode.trim())
            }
        }


    }

    override fun onRefresh() {
        recentRechargeHistoryModalArrayList.clear()
        rechargeHistoryFromTo(
            Preferences.getString(Preferences.MOBILE),
            AppCommonMethods.convertDateFormat(
                "dd MMM yyyy",
                "yyyy-MM-dd", tvFromDateRecHistory.text.toString()
            ).toString(),
            AppCommonMethods.convertDateFormat(
                "dd MMM yyyy",
                "yyyy-MM-dd", tvToDateRecHistory.text.toString()
            ).toString()
        )

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
        rechargeHistoryFromTo(
            Preferences.getString(Preferences.MOBILE),
            AppCommonMethods.convertDateFormat(
                "dd MMM yyyy",
                "yyyy-MM-dd", tvFromDateRecHistory.text.toString()
            ).toString(),
            AppCommonMethods.convertDateFormat(
                "dd MMM yyyy",
                "yyyy-MM-dd", tvToDateRecHistory.text.toString()
            ).toString()
        )

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

}