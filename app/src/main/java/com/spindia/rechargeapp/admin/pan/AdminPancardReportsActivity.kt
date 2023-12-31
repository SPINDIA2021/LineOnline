package com.spindia.rechargeapp.admin.pan

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.webkit.ValueCallback
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.RelativeLayout
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
import com.spindia.rechargeapp.pancardOffline.EditPanCardActivity
import com.spindia.rechargeapp.pancardlist.BaseUpdatePaymentStatusResponse
import com.spindia.rechargeapp.pancardlist.PancardStatusActivity
import com.spindia.rechargeapp.paytmIntegration.Api
import com.spindia.rechargeapp.paytmIntegration.Checksum
import com.spindia.rechargeapp.paytmIntegration.Constants
import com.spindia.rechargeapp.paytmIntegration.Paytm
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

class AdminPancardReportsActivity : AppCompatActivity(),
    SwipeRefreshLayout.OnRefreshListener, PaymentStatusListener {

    lateinit var panCardlistResponse: AdminPancardListResponse
    lateinit var pancardAdapter: AdminPancardListAdapter
    var pancardListModalArrayList = ArrayList<AdminPancardListResponse>()
    lateinit var ivBackBtn:ImageView

    lateinit var mSwipeRefresh:SwipeRefreshLayout
    lateinit var progress_bar:RelativeLayout
    lateinit var rvPancardHistory:RecyclerView
    var walletBalance="0"

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



        pd = ProgressDialog(this@AdminPancardReportsActivity)
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
                        mInterstitialAd!!.show(this@AdminPancardReportsActivity)
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

        callServiceGetPancard()

        callServiceGetWalletBalance()



        mSwipeRefresh.setOnRefreshListener(this);

        mSwipeRefresh.post(Runnable {
            if (mSwipeRefresh != null) {
                mSwipeRefresh.setRefreshing(true)
            }
            pancardListModalArrayList.clear()
            callServiceGetPancard()

            mSwipeRefresh.setRefreshing(false)

        })


    }




    override fun onRefresh() {

        callServiceGetPancard()
        mSwipeRefresh.setRefreshing(false)

    }


    private fun callServiceGetPancard() {
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


        val call = apiService.callGetAdminPanListService(
            "pancard"
        )


        //making the call to generate checksum
        call.enqueue(object : Callback<BaseAdminPancardListResponse> {
            override fun onResponse(
                call: Call<BaseAdminPancardListResponse>,
                response: Response<BaseAdminPancardListResponse>
            ) {
                progress_bar.visibility = View.GONE
                if (response.body()!!.status == true) {
                    pancardListModalArrayList= response.body()!!.data
                    rvPancardHistory.apply {

                        layoutManager = LinearLayoutManager(this@AdminPancardReportsActivity)
                        pancardAdapter = AdminPancardListAdapter(
                            context, pancardListModalArrayList, retryClick,editClick, statusClick,pdfClick
                        )
                        rvPancardHistory.adapter = pancardAdapter
                    }
                } else {
                    Toast.makeText(
                        this@AdminPancardReportsActivity,
                        response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()

                }


                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter
            }

            override fun onFailure(call: Call<BaseAdminPancardListResponse>, t: Throwable) {
                progress_bar.visibility = View.GONE
                // callServiceFalse(mobileNo);
                Toast.makeText(this@AdminPancardReportsActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }


    var orderId=" "
    var token:String=""
    var retryClick = View.OnClickListener { v ->
        val i = v.tag as Int
        token=pancardListModalArrayList.get(i).token
        Preferences.saveValue("TOKEN",token)

        val amount: String = "100.00"
        val upi: String = "anilmitawa64@axl"
        val name: String = "Pancard"
        val desc: String = "Payment"
        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault())
        orderId = df.format(c)

       // generateCheckSumPay()

      // makePayment(amount, upi, name, desc, orderId)


        if (walletBalance.toDouble() >= amount.toDouble())
        {
            val mp: MediaPlayer
            mp = MediaPlayer.create(this, R.raw.pan_payment_success)
            mp.setOnCompletionListener { mp -> // TODO Auto-generated method stub
                var mp = mp
                mp!!.reset()
                mp!!.release()
                mp = null
            }
            mp.start()
            callServiceUpdateStatus(token,"success")

        }else{
            Toast.makeText(this@AdminPancardReportsActivity,"Insufficient Balance",Toast.LENGTH_LONG).show()
        }
    }


    var editClick = View.OnClickListener { v ->
        val i = v.tag as Int

        val name=pancardListModalArrayList.get(i).name
        val mobileno=pancardListModalArrayList.get(i).mobile
        val dob=pancardListModalArrayList.get(i).dob
        val fatherName=pancardListModalArrayList.get(i).fathername
        val emailId=pancardListModalArrayList.get(i).email
        val adharImage=pancardListModalArrayList.get(i).aadhar
        val signImage=pancardListModalArrayList.get(i).sign
        val photo=pancardListModalArrayList.get(i).photo
        val guardianAadhar=pancardListModalArrayList.get(i).guardiansAadhar
        val adharBack=pancardListModalArrayList.get(i).aadharback
        val raType=pancardListModalArrayList.get(i).ratype

        val intent = Intent(this, EditPanCardActivity::class.java)
        intent.putExtra("name",name)
        intent.putExtra("mobileno",mobileno)
        intent.putExtra("dob",dob)
        intent.putExtra("fatherName",fatherName)
        intent.putExtra("emailId",emailId)
        intent.putExtra("adharImage",adharImage)
        intent.putExtra("signImage",signImage)
        intent.putExtra("photo",photo)
        intent.putExtra("guardianAadhar",guardianAadhar)
        intent.putExtra("adharBack",adharBack)
        intent.putExtra("raType",raType)
        intent.putExtra("tokenid",pancardListModalArrayList.get(i).token)
        startActivity(intent)


    }


    var statusClick = View.OnClickListener { v ->
        val i = v.tag as Int

        val receiptno=pancardListModalArrayList.get(i).receiptno


        val intent = Intent(this@AdminPancardReportsActivity, PancardStatusActivity::class.java)
        intent.putExtra("receiptno",receiptno)
        startActivity(intent)
      //  showStatusDailog(receiptno)

    }



    var pdfClick = View.OnClickListener { v ->
        val i = v.tag as Int

        val document=pancardListModalArrayList.get(i).document

        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(document.toString()))
        startActivity(browserIntent)

    }


    lateinit var dialogStatus: Dialog
    lateinit var webViewStatus: WebView
    lateinit var imgClose: ImageView
    private lateinit var mUploadMessage: ValueCallback<Uri>
    lateinit var uploadMessage: ValueCallback<Array<Uri>>
    val REQUEST_SELECT_FILE = 100
    private val FILECHOOSER_RESULTCODE = 1

    private fun showStatusDailog(receiptno: String?) {
        dialogStatus= Dialog(this@AdminPancardReportsActivity)
        dialogStatus.setContentView(R.layout.dailog_webview)

        imgClose=dialogStatus.findViewById(R.id.img_close)
        webViewStatus=dialogStatus.findViewById(R.id.webView)

        webViewStatus.getSettings().setJavaScriptEnabled(true)
        webViewStatus.loadUrl("https://tin.tin.nsdl.com/tan2/servlet/PanStatusTrack?ST_SEARCH_TYPE=P&ST_SEARCH_OPTION=A&captchFlag=true&ST_ACK_NUM="+receiptno)

        webViewStatus.setInitialScale(1)
        webViewStatus.getSettings().setLoadWithOverviewMode(true)
        webViewStatus.getSettings().setUseWideViewPort(true)

        webViewStatus.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        })

        imgClose.setOnClickListener {
            dialogStatus.dismiss()
        }

        dialogStatus.show()
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

    override fun onTransactionCompleted(transactionDetails: TransactionDetails?) {
        try {
            // Get transactions details from response.

            if (transactionDetails!!.status!!.toLowerCase() == "success") {
                Toast.makeText(this, "Transaction success..", Toast.LENGTH_SHORT).show()

                /*rechargeApi(
                    cus_upi ,"", mobileNo,
                    amount.toString(), operator_code, "RETAILER",
                    Preferences.getString(
                        AppConstants.MOBILE)
                )*/


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

    }

    override fun onTransactionSubmitted() {
        Log.e("TAG", "TRANSACTION SUBMIT")
       // callServiceUpdateStatus(token,"submitted")
    }

    override fun onTransactionFailed() {
        // this method is called when transaction is failure.
        Toast.makeText(this, "Failed to complete transaction", Toast.LENGTH_SHORT).show()
        callServiceUpdateStatus(Preferences.getString("TOKEN"),"failed")
    }

    override fun onTransactionCancelled() {
        // this method is called when transaction is cancelled.
        Toast.makeText(this, "Transaction cancelled..", Toast.LENGTH_SHORT).show()

    }

    override fun onAppNotFound() {
        // this method is called when the users device is not having any app installed for making payment.
        Toast.makeText(this, "No app found for making transaction..", Toast.LENGTH_SHORT).show()
    }


    private fun callServiceUpdateStatus( token: String,  status: String) {
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

        val token1: RequestBody = createPartFromString(token)
        val status1: RequestBody = createPartFromString(status)

        //Call<ScannerResponse> call = apiService.saveScan(orderId1,vpa1,name1,amount1,mon_no1,member_id1,password1);
        val call = apiService.updateStatus(token1,status1 )


        //making the call to generate checksum
        call.enqueue(object : Callback<BaseUpdatePaymentStatusResponse> {
            override fun onResponse(
                call: Call<BaseUpdatePaymentStatusResponse>,
                response: Response<BaseUpdatePaymentStatusResponse>
            ) {
                progress_bar.visibility = View.GONE
                if (response.body()!!.status == true) {
                    Toast.makeText(
                        this@AdminPancardReportsActivity,
                        response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                   callServiceGetPancard()
                } else {
                    Toast.makeText(
                        this@AdminPancardReportsActivity,
                        response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    callServiceGetPancard()
                }


                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter
            }

            override fun onFailure(call: Call<BaseUpdatePaymentStatusResponse>, t: Throwable) {
                progress_bar.visibility = View.GONE
                // callServiceFalse(mobileNo);
                Toast.makeText(this@AdminPancardReportsActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun createPartFromString(descriptionString: String): RequestBody {
        return RequestBody.create(
            MultipartBody.FORM, descriptionString
        )
    }



    var merchantId="mkqrkg66337193948105"
    var merchantKey="Tp7FdW#GgHWn#l17"
    /*generating checksum for paytm...*/
    private fun generateCheckSumPay() {
        val paytm = Paytm(
            merchantId,
            merchantKey,
            Constants.CHANNEL_ID,
            "100",
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
            this@AdminPancardReportsActivity,
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
                        this@AdminPancardReportsActivity,
                        "Network connection error: Check your internet connectivity",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun clientAuthenticationFailed(inErrorMessage: String) {
                    Toast.makeText(
                        this@AdminPancardReportsActivity,
                        "Authentication failed: Server error$inErrorMessage", Toast.LENGTH_LONG
                    ).show()
                }

                override fun onErrorLoadingWebPage(
                    iniErrorCode: Int,
                    inErrorMessage: String,
                    inFailingUrl: String
                ) {
                    Toast.makeText(
                        this@AdminPancardReportsActivity,
                        "Unable to load webpage $inErrorMessage",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onBackPressedCancelTransaction() {
                    Toast.makeText(this@AdminPancardReportsActivity, "Transaction cancelled", Toast.LENGTH_LONG).show()
                }

                override fun onTransactionCancel(inErrorMessage: String, inResponse: Bundle) {
                    Toast.makeText(
                        this@AdminPancardReportsActivity,
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
            val mp: MediaPlayer
            mp = MediaPlayer.create(this, R.raw.pan_payment_success)
            mp.setOnCompletionListener { mp -> // TODO Auto-generated method stub
                var mp = mp
                mp!!.reset()
                mp!!.release()
                mp = null
            }
            mp.start()
            callServiceUpdateStatus(token,"success")
        } else {
            callServiceUpdateStatus(token,"failure")
            Toast.makeText(this@AdminPancardReportsActivity, respMsgPay, Toast.LENGTH_SHORT).show()
        }
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
                        this@AdminPancardReportsActivity,
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
                Toast.makeText(this@AdminPancardReportsActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }



}