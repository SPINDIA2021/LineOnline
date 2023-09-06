package com.spindia.rechargeapp.recharge_services

import android.app.ProgressDialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.onesignal.OneSignal
import com.paytm.pgsdk.PaytmOrder
import com.paytm.pgsdk.PaytmPGService
import com.paytm.pgsdk.PaytmPaymentTransactionCallback
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails
import com.spindia.rechargeapp.R
import com.spindia.rechargeapp.authentication.response.WalletResponse
import com.spindia.rechargeapp.model.OperatorsModel
import com.spindia.rechargeapp.network.Preferences
import com.spindia.rechargeapp.network_calls.AppApiCalls
import com.spindia.rechargeapp.paytmIntegration.Api
import com.spindia.rechargeapp.paytmIntegration.Checksum
import com.spindia.rechargeapp.paytmIntegration.Constants
import com.spindia.rechargeapp.paytmIntegration.Paytm
import com.spindia.rechargeapp.utils.AppCommonMethods
import com.spindia.rechargeapp.utils.AppConstants
import com.spindia.rechargeapp.utils.MainIAPI
import com.spindia.rechargeapp.utils.toast
import com.spindia.rechargeapp.walletHistory.BaseWalletHistoryResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class RechargeSummaryActivity : AppCompatActivity(), AppApiCalls.OnAPICallCompleteListener, PaymentStatusListener {

    var operator_code: String = ""

    lateinit var custToolbar: androidx.appcompat.widget.Toolbar

    lateinit var ivBackBtn: ImageView
    lateinit var text_netpay_main: TextView
    lateinit var text_mobileno: TextView
    lateinit var text_operator:TextView
    lateinit var text_totalamount:TextView
    lateinit var text_netpay:TextView
    lateinit var text_orderid:TextView
    lateinit var btnProceed:Button

    lateinit var progress_bar:RelativeLayout

    var upiid: String = "anilmitawa64@axl"
    var mobileNo=""
    var operatorname=""
    var amount=""
    var netPay=0.0
    var disAmt=0.0
    var orderId=""
    var cus_upi=""

    var walletBalance="0"

    var mInterstitialAd: InterstitialAd? = null
    var pd: ProgressDialog? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recharge_summary)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.status_bar, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }

   //     OneSignal.initWithContext(this,"4543edd8-e654-4c7b-ac9b-de0eba4c1bf6");



        custToolbar=findViewById(R.id.custToolbar)
        text_netpay_main=findViewById(R.id.text_netpay_main)
        text_mobileno=findViewById(R.id.text_mobileno)
        text_operator=findViewById(R.id.text_operator)
        text_totalamount=findViewById(R.id.text_totalamount)
        text_netpay=findViewById(R.id.text_netpay)
        btnProceed=findViewById(R.id.btnProceed)
        progress_bar=findViewById(R.id.progress_bar)
        ivBackBtn=findViewById(R.id.ivBackBtn)
        text_orderid=findViewById(R.id.text_orderid)


        pd = ProgressDialog(this@RechargeSummaryActivity)
        pd!!.setMessage("Please wait ad is loading..")
        pd!!.show()
        MobileAds.initialize(
            this
        ) { }

        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this, "ca-app-pub-7161060381095883/7982531878", adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    pd!!.dismiss()
                    mInterstitialAd = interstitialAd
                    Log.i("TAG", "onAdLoaded")
                    if (mInterstitialAd != null) {
                        mInterstitialAd!!.show(this@RechargeSummaryActivity)
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


        mobileNo= intent.getStringExtra("mobileno").toString()
        operatorname= intent.getStringExtra("operator").toString()
        amount= intent.getStringExtra("amount").toString()
        cus_upi= intent.getStringExtra("upiid").toString()
        operator_code= intent.getStringExtra("operator_code").toString()




        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault())
        orderId = df.format(c)

        callServiceGetWalletBalance()

        disAmt = (amount.toDouble() / 100.0f * 2).toDouble()

        netPay=amount.toDouble()-disAmt

        text_netpay_main.setText("₹ "+netPay.toString())
        text_netpay.setText("₹ "+netPay.toString())
        text_totalamount.setText("₹ "+amount)
        text_mobileno.setText(mobileNo)
        text_operator.setText(operatorname)
        text_orderid.setText("Order no: "+orderId)

        Preferences.saveValue(Preferences.DebitAmount,netPay.toString())


        ivBackBtn.setOnClickListener {
            onBackPressed()
        }


        btnProceed.setOnClickListener {
            /*rechargeApi(
                cus_upi ,"", mobileNo,
                amount.toString(), operator_code, "RETAILER",
                Preferences.getString(
                    AppConstants.MOBILE)
            )*/


            openDailogPayMethod()
        }


    }



    private fun checkIfSameRecharge(
        cus_id: String,
        rec_mobile: String,
        amount: String,
        operator: String
    ) {
        progress_bar.visibility = View.VISIBLE

        if (AppCommonMethods(this).isNetworkAvailable) {
            val mAPIcall = AppApiCalls(
                this,
                AppConstants.CHECK_SAME_RECHARGE_API,
                this
            )
            mAPIcall.checkIfSameRecharge(cus_id, rec_mobile, amount, operator)

        } else {
            this.toast(getString(R.string.error_internet))
        }
    }


    private fun verifyPin(
        cus_mobile: String,
        pin: String
    ) {
        progress_bar.visibility = View.VISIBLE

        if (AppCommonMethods(this).isNetworkAvailable) {
            val mAPIcall = AppApiCalls(
                this,
                AppConstants.VERFY_PIN_API,
                this
            )
            mAPIcall.verifyPin(cus_mobile, pin)

        } else {
            this.toast(getString(R.string.error_internet))
        }
    }


    private fun rechargeApi(
        cus_upi: String,
        cus_id: String,
        rec_mobile: String,
        amount: String,
        operator: String,
        cus_type: String,
        usermobile:String,
        type:String
    ) {
        progress_bar.visibility = View.VISIBLE



        if (AppCommonMethods(this).isNetworkAvailable) {
            val mAPIcall = AppApiCalls(
                this,
                AppConstants.RECHARGE_API,
                this
            )
            mAPIcall.rechargeApi(cus_upi,cus_id, rec_mobile, amount, operator, cus_type,usermobile,type)

        } else {
            this.toast(getString(R.string.error_internet))
        }
    }





    override fun onAPICallCompleteListner(item: Any?, flag: String?, result: String) {
        if (flag.equals(AppConstants.OPERATOR_API)) {
            Log.e(AppConstants.OPERATOR_API, result)
            val jsonObject = JSONObject(result)
            val status = jsonObject.getString(AppConstants.STATUS)
            Log.e(AppConstants.STATUS, status)
            if (status.contains("true")) {
                progress_bar.visibility = View.GONE

                val cast = jsonObject.getJSONArray("result")

                for (i in 0 until cast.length()) {
                    val notifyObjJson = cast.getJSONObject(i)
                    val operatorname = notifyObjJson.getString("operatorname")
                    Log.e("operator_name ", operatorname)
                    val operatorsModel = Gson()
                        .fromJson(
                            notifyObjJson.toString(),
                            OperatorsModel::class.java
                        )


                }


            } else {

                progress_bar.visibility = View.GONE


            }
        }
        if (flag.equals(AppConstants.BALANCE_API)) {
            progress_bar.visibility = View.GONE
            Log.e(AppConstants.BALANCE_API, result)
            val jsonObject = JSONObject(result)
            val status = jsonObject.getString(AppConstants.STATUS)
            val messageCode = jsonObject.getString(AppConstants.MESSAGE)

            //   val token = jsonObject.getString(AppConstants.TOKEN)
            Log.e(AppConstants.STATUS, status)
            Log.e(AppConstants.MESSAGE, messageCode)
            if (status.contains(AppConstants.TRUE)) {


           /*     tvWalletBalance.text =
                    "${getString(R.string.Rupee)} ${jsonObject.getString(AppConstants.WALLETBALANCE)}"*/
                /* tvAepsBalance.text =
                     "${getString(R.string.Rupee)} ${jsonObject.getString(AEPSBALANCE)}"*/


            } else {
                progress_bar.visibility = View.GONE
                if (messageCode.equals(getString(R.string.error_expired_token))) {
                    AppCommonMethods.logoutOnExpiredDialog(this)
                } else {
                    this.toast(messageCode.trim())
                }
            }
        }
        if (flag.equals(AppConstants.CHECK_SAME_RECHARGE_API)) {
            Log.e(AppConstants.CHECK_SAME_RECHARGE_API, result)
            val jsonObject = JSONObject(result)
            val status = jsonObject.getString(AppConstants.STATUS)
            val message = jsonObject.getString(AppConstants.MESSAGE)
            Log.e(AppConstants.STATUS, status)
            if (status.contains("true")) {
                progress_bar.visibility = View.GONE
              //  verifyPin(userModel.cus_mobile, AppPrefs.getStringPref("AppPassword",this).toString())
                //confirmPinDialog()

            } else {

                progress_bar.visibility = View.GONE
                showMessageDialog(getString(R.string.error_attention), message)

            }
        }
        if (flag.equals(AppConstants.RECHARGE_API)) {
            Log.e(AppConstants.RECHARGE_API, result)
            val jsonObject = JSONObject(result)
            val status = jsonObject.getString(AppConstants.STATUS)
            Log.e(AppConstants.STATUS, status)
            if (status.contains(AppConstants.TRUE)) {
                progress_bar.visibility = View.GONE

                val resultObject = jsonObject.getJSONObject("result")
                val message = resultObject.getString(AppConstants.MESS)

                if (resultObject.getString(AppConstants.MESS).equals("Your Recharge is Success"))
                {
                    val mp: MediaPlayer
                    mp = MediaPlayer.create(this, R.raw.success)
                    mp.setOnCompletionListener { mp -> // TODO Auto-generated method stub
                        var mp = mp
                        mp!!.reset()
                        mp!!.release()
                        mp = null
                    }
                    mp.start()
                    showSuccessOrFailedDialog(getString(R.string.status), message)
                }else if (resultObject.getString(AppConstants.MESS).equals("Your Recharge is Pending"))
                {
                    val mp: MediaPlayer
                    mp = MediaPlayer.create(this, R.raw.pending)
                    mp.setOnCompletionListener { mp -> // TODO Auto-generated method stub
                        var mp = mp
                        mp!!.reset()
                        mp!!.release()
                        mp = null
                    }
                    mp.start()
                    showSuccessOrFailedDialog(getString(R.string.status), message)
                }else if (resultObject.getString(AppConstants.MESS).equals("Your Recharge is Failed"))
                {
                    val mp: MediaPlayer
                    mp = MediaPlayer.create(this, R.raw.faild)
                    mp.setOnCompletionListener { mp -> // TODO Auto-generated method stub
                        var mp = mp
                        mp!!.reset()
                        mp!!.release()
                        mp = null
                    }
                    mp.start()

                    callServiceFailureResponse(getString(R.string.status), message)

                }



             //   tts!!.speak(message, TextToSpeech.QUEUE_FLUSH, null,"")





            } else {
                progress_bar.visibility = View.GONE

                val resultObject = jsonObject.getJSONObject("result")
                val message = resultObject.getString(AppConstants.MESS)
                progress_bar.visibility = View.GONE

                showSuccessOrFailedDialog(getString(R.string.status), message)
            }
        }
        if (flag.equals(AppConstants.VERFY_PIN_API)) {
            Log.e(AppConstants.VERFY_PIN_API, result)
            val jsonObject = JSONObject(result)
            val status = jsonObject.getString(AppConstants.STATUS)
            val message = jsonObject.getString(AppConstants.MESSAGE)
            Log.e(AppConstants.STATUS, status)
            if (status.contains(AppConstants.TRUE)) {
                progress_bar.visibility = View.GONE


            } else {

                progress_bar.visibility = View.GONE
                showMessageDialog(getString(R.string.error_attention), message)

            }
        }

    }



    private fun showMessageDialog(title: String, message: String) {
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

    private fun showSuccessOrFailedDialog(title: String, message: String) {
        val builder1 =
            AlertDialog.Builder(this)
        builder1.setTitle("" + title)
        builder1.setMessage("" + message)
        builder1.setCancelable(false)
        builder1.setPositiveButton(
            "OK"
        ) { dialog, id ->


            val intent = Intent(this, AllRechargeReportsActivity::class.java)
            this.startActivity(intent)
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




    override fun onTransactionCompleted(transactionDetails: TransactionDetails?) {
        try {
            // Get transactions details from response.

            if (transactionDetails!!.status!!.toLowerCase() == "success") {
              /*  Toast.makeText(this, "Transaction success..", Toast.LENGTH_SHORT).show()
                rechargeApi(
                    cus_upi ,"", mobileNo,
                    amount.toString(), operator_code, "RETAILER",
                    Preferences.getString(
                        AppConstants.MOBILE),"upi"
                )*/
              /*  addFundsApi(
                    userModel.cus_id, et_ammount.text.toString(), "UPI Payment",
                    transactionDetails.getTransactionId().toString(),
                    transactionDetails.getTransactionRefId().toString(),
                    AppPrefs.getStringPref("deviceId", this).toString(),
                    AppPrefs.getStringPref("deviceName", this).toString(),
                    userModel.cus_pin,
                    userModel.cus_pass,
                    userModel.cus_mobile, userModel.cus_type
                )
                transactionId = transactionDetails.getTransactionId().toString()
                txnRef = transactionDetails.getTransactionRefId().toString()
                responseCode = transactionDetails.getResponseCode().toString()
                approvalRefNo = transactionDetails.getApprovalRefNo().toString()
                Log.e("transactionId", transactionId)
                Log.e("responseCode ", responseCode)
                Log.e("approvalRefNo", approvalRefNo)
                Log.e("txnRef       ", txnRef)*/

            } else if (transactionDetails.status!!.toLowerCase(Locale.getDefault()) == "submitted") {
                Toast.makeText(this, "Transaction submitted..", Toast.LENGTH_SHORT).show()
               /* val intent = Intent(
                    this@AddMoneyActivity,
                    PaymentSuccess::class.java
                )
                intent.putExtra("status", 2)
                intent.putExtra("amount", et_ammount.text.toString())
                intent.putExtra("transactionId", transactionDetails.getTransactionId())
                intent.putExtra("responseCode", transactionDetails.getResponseCode())
                intent.putExtra("approvalRefNo", transactionDetails.getApprovalRefNo())
                intent.putExtra("txnRef", transactionDetails.getTransactionRefId())
                startActivity(intent)*/
            } else {
                Toast.makeText(this, "Transaction other..", Toast.LENGTH_SHORT).show()
               /* val intent = Intent(
                    this@AddMoneyActivity,
                    PaymentSuccess::class.java
                )
                intent.putExtra("status", 0)
                intent.putExtra("amount", et_ammount.text.toString())
                intent.putExtra("transactionId", transactionDetails.getTransactionId())
                intent.putExtra("responseCode", transactionDetails.getResponseCode())
                intent.putExtra("approvalRefNo", transactionDetails.getApprovalRefNo())
                intent.putExtra("txnRef", transactionDetails.getTransactionRefId())
                startActivity(intent)*/
            }
        } catch (e: Exception) {
          /*  callbackTransactionCancelled()
            callbackTransactionFailed()*/
        }
    }

    override fun onTransactionSuccess() {
        Toast.makeText(this, "Transaction successfully completed..", Toast.LENGTH_SHORT).show()
        rechargeApi(
            cus_upi ,"", mobileNo,
            amount.toString(), operator_code, "RETAILER",
            Preferences.getString(
                AppConstants.MOBILE),"upi"
        )
       // Toast.makeText(this, "Transaction success..", Toast.LENGTH_SHORT).show()


    }

    override fun onTransactionSubmitted() {
        Log.e("TAG", "TRANSACTION SUBMIT")
    }

    override fun onTransactionFailed() {
        // this method is called when transaction is failure.
        Toast.makeText(this, "Failed to complete transaction", Toast.LENGTH_SHORT).show()
    }

    override fun onTransactionCancelled() {
        // this method is called when transaction is cancelled.
        Toast.makeText(this, "Transaction cancelled..", Toast.LENGTH_SHORT).show()
    }

    override fun onAppNotFound() {
        // this method is called when the users device is not having any app installed for making payment.
        Toast.makeText(this, "No app found for making transaction..", Toast.LENGTH_SHORT).show()
    }


    private fun makePayment(
        amount: String,
        upi: String,
        name: String,
        desc: String,
        transactionId: String
    ) {
        // on below line we are calling an easy payment method and passing
        // all parameters to it such as upi id,name, description and others.
        val easyUpiPayment = EasyUpiPayment.Builder()
            .with(this) // on below line we are adding upi id.
            .setPayeeVpa(upi) // on below line we are setting name to which we are making oayment.
            .setPayeeName(name) // on below line we are passing transaction id.
            .setTransactionId(transactionId) // on below line we are passing transaction ref id.
            .setTransactionRefId(transactionId) // on below line we are adding description to payment.
            .setDescription(desc) // on below line we are passing amount which is being paid.
            .setAmount(amount) // on below line we are calling a build method to build this ui.
            .build()
        // on below line we are calling a start
        // payment method to start a payment.
        easyUpiPayment.startPayment()
        // on below line we are calling a set payment
        // status listener method to call other payment methods.
        easyUpiPayment.setPaymentStatusListener(this)
    }


    var dialogBox: BottomSheetDialog? = null
    lateinit var layUPI:LinearLayout
    lateinit var layWallet:LinearLayout
    lateinit var text_wallet:TextView

    fun openDailogPayMethod() {
        dialogBox = BottomSheetDialog(this)
        dialogBox!!.setContentView(R.layout.bottomdailog_paymethod)
        layUPI = dialogBox!!.findViewById(R.id.lay_upi)!!
        layWallet = dialogBox!!.findViewById<LinearLayout>(R.id.lay_wallet)!!
        text_wallet= dialogBox!!.findViewById(R.id.text_wallet)!!

        layUPI.visibility=View.GONE


        val amounttoPay: String = netPay.toString()



        text_wallet.setText("Wallet (Rs. "+walletBalance+" )")

        layUPI!!.setOnClickListener {



            val upi: String = upiid!!
            val name: String = "Recharge"
            val desc: String = "Payment"



            // if the edit text is not empty then
            // we are calling method to make payment.
            makePayment(amounttoPay, upi, name, desc, orderId)

          //  generateCheckSumPay()
            dialogBox!!.dismiss()
        }


        layWallet.setOnClickListener(View.OnClickListener {
            if (walletBalance.toDouble() >= amount.toDouble())
            {
                rechargeApi(
                    cus_upi ,"", mobileNo,
                    amount.toString(), operator_code, "RETAILER",
                    Preferences.getString(
                        AppConstants.MOBILE),"wallet"
                )

                dialogBox!!.dismiss()
            }else{
                Toast.makeText(this@RechargeSummaryActivity,"Insufficient Balance",Toast.LENGTH_LONG).show()
            }

        })


        dialogBox!!.show()
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
                        this@RechargeSummaryActivity,
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
                Toast.makeText(this@RechargeSummaryActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun createPartFromString(descriptionString: String): RequestBody {
        return RequestBody.create(
            MultipartBody.FORM, descriptionString
        )
    }


    private fun callServiceFailureResponse(status: String, message: String) {
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
        val retailerId: RequestBody =
            createPartFromString(Preferences.getString(AppConstants.MOBILE))
        val type: RequestBody = createPartFromString("credit")
        val amount: RequestBody = createPartFromString(Preferences.getString(Preferences.DebitAmount))
        val remark: RequestBody = createPartFromString("recharge failure")


        //Call<ScannerResponse> call = apiService.saveScan(orderId1,vpa1,name1,amount1,mon_no1,member_id1,password1);
        val call = apiService.saveFailureRecharge(retailerId, type,amount,remark)


        //making the call to generate checksum
        call.enqueue(object : Callback<BaseWalletHistoryResponse> {
            override fun onResponse(
                call: Call<BaseWalletHistoryResponse>,
                response: Response<BaseWalletHistoryResponse>
            ) {
                progress_bar.visibility = View.GONE
                if (response.body()!!.status == true) {
                    showSuccessOrFailedDialog(status, message)
                } else {
                    Toast.makeText(
                        this@RechargeSummaryActivity,
                        response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()

                    showSuccessOrFailedDialog(status, message)
                }

            }

            override fun onFailure(call: Call<BaseWalletHistoryResponse>, t: Throwable) {
                progress_bar.visibility = View.GONE
                // callServiceFalse(mobileNo);
                Toast.makeText(this@RechargeSummaryActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }


    var merchantId="mkqrkg66337193948105"
    var merchantKey="Tp7FdW#GgHWn#l17"
    /*generating checksum for paytm...*/
    private fun generateCheckSumPay() {
        val paytm = Paytm(
            merchantId,
            merchantKey,
            Constants.CHANNEL_ID,
            netPay.toString(),
            Constants.WEBSITE,
            Constants.INDUSTRY_TYPE_ID
        )

        //getting the tax amount first.
        val txnAmount = "1"

        //creating a retrofit object.
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(Api.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //creating the retrofit api service
        val apiService: Api = retrofit.create(Api::class.java)

        //creating paytm object
        //containing all the values required


        //creating a call object from the apiService
        val call: Call<Checksum> = apiService.getChecksum(
            merchantId,
            merchantKey,
            paytm.orderId,
            paytm.custId,
            paytm.channelId,
            paytm.txnAmount,
            paytm.website,
            "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID="+paytm.orderId,
            paytm.industryTypeId
        )

        //making the call to generate checksum
        call.enqueue(object : Callback<Checksum> {
            override fun onResponse(call: Call<Checksum>, response: Response<Checksum>) {

                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter
                initializePaytmPayment(response.body()!!.checksumHash, paytm)
            }

            override fun onFailure(call: Call<Checksum>, t: Throwable) {}
        })
    }


    /*initializing payment on paytm..*/
    private fun initializePaytmPayment(checksumHash: String, paytm: Paytm) {

        //getting paytm service

        //use this when using for testing
        // PaytmPGService Service = PaytmPGService.getStagingService();
        val service: PaytmPGService
        /*  if (payMID.equals("rxazcv89315285244163"))
        {
            service = PaytmPGService.getStagingService();
        }else {
            service = PaytmPGService.getProductionService();
        }*/service = PaytmPGService.getProductionService()
        //use this when using for production
        // PaytmPGService Service = PaytmPGService.getProductionService();

        //creating a hashmap and adding all the values required
        val paramMap: MutableMap<String, String> = HashMap()
        paramMap["MID"] = merchantId
        paramMap["ORDER_ID"] = paytm.orderId
        paramMap["CUST_ID"] = paytm.custId
        paramMap["CHANNEL_ID"] = paytm.channelId
        paramMap["TXN_AMOUNT"] = paytm.txnAmount
        paramMap["WEBSITE"] = paytm.website
        paramMap["CALLBACK_URL"] = "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID="+paytm.orderId
        paramMap["CHECKSUMHASH"] = checksumHash
        paramMap["INDUSTRY_TYPE_ID"] = paytm.industryTypeId
        //creating a paytm order object using the hashmap
        val order = PaytmOrder(paramMap as HashMap<String, String>)

        //intializing the paytm service
        service.initialize(order, null)
        service.startPaymentTransaction(
            this@RechargeSummaryActivity,
            true,
            true,
            object : PaytmPaymentTransactionCallback {
                /*Call Backs*/
                override fun someUIErrorOccurred(inErrorMessage: String) {}
                override fun onTransactionResponse(inResponse: Bundle) {
                    getDataPayment(inResponse)
                }

                override fun networkNotAvailable() {
                    Toast.makeText(
                        this@RechargeSummaryActivity,
                        "Network connection error: Check your internet connectivity",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun clientAuthenticationFailed(inErrorMessage: String) {
                    Toast.makeText(
                        this@RechargeSummaryActivity,
                        "Authentication failed: Server error$inErrorMessage", Toast.LENGTH_LONG
                    ).show()
                }

                override fun onErrorLoadingWebPage(
                    iniErrorCode: Int,
                    inErrorMessage: String,
                    inFailingUrl: String
                ) {
                    Toast.makeText(
                        this@RechargeSummaryActivity,
                        "Unable to load webpage $inErrorMessage",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onBackPressedCancelTransaction() {
                    Toast.makeText(this@RechargeSummaryActivity, "Transaction cancelled", Toast.LENGTH_LONG).show()
                }

                override fun onTransactionCancel(inErrorMessage: String, inResponse: Bundle) {
                    Toast.makeText(
                        this@RechargeSummaryActivity,
                        "Transaction Cancelled$inResponse",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }


    var statusPay: String = ""
    var checksumPay: String = ""
    var BANKNAMEPay: String = ""
    var TXNIDPay: String = ""
    var orderIdPay:String? = ""
    var txnAmtPay:String? = ""
    var txnDatePay:String? = ""
    var midPay:String? = ""
    var respCodePay:String? = ""
    var paymentModePay:String? = ""
    var bankTxnIdPay:String? = ""
    var currencyPay:String? = ""
    var gatewayNamePay:String? = ""
    var respMsgPay:String? = ""

    /*getting paytm response from bundle */
    private fun getDataPayment(s: Bundle) {
        statusPay = s.getString("STATUS").toString()
        checksumPay = s.getString("CHECKSUMHASH").toString()
        BANKNAMEPay = s.getString("BANKNAME").toString()
        TXNIDPay = s.getString("TXNID").toString()
        orderIdPay = s.getString("ORDERID")
        txnAmtPay = s.getString("TXNAMOUNT")
        txnDatePay = s.getString("TXNDATE")
        midPay = s.getString("MID")
        respCodePay = s.getString("RESPCODE")
        paymentModePay = s.getString("PAYMENTMODE")
        bankTxnIdPay = s.getString("BANKTXNID")
        currencyPay = s.getString("CURRENCY")
        gatewayNamePay = s.getString("GATEWAYNAME")
        respMsgPay = s.getString("RESPMSG")
        if (respCodePay == "01") {
            rechargeApi(
                cus_upi ,"", mobileNo,
                amount.toString(), operator_code, "RETAILER",
                Preferences.getString(
                    AppConstants.MOBILE),"upi"
            )
        } else {
            Toast.makeText(this@RechargeSummaryActivity, respMsgPay, Toast.LENGTH_SHORT).show()
        }
    }




}