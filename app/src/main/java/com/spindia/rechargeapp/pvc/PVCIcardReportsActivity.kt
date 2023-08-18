package com.spindia.rechargeapp.pvc

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
import com.google.android.gms.location.FusedLocationProviderClient
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

class PVCIcardReportsActivity : AppCompatActivity(),
    SwipeRefreshLayout.OnRefreshListener, PaymentStatusListener {

    lateinit var pvcCardlistResponse: PVCListResponse
    lateinit var pvccardAdapter: PVCIcardListAdapter
    var pvccardListModalArrayList = ArrayList<PVCListResponse>()
    lateinit var ivBackBtn:ImageView

    lateinit var mSwipeRefresh:SwipeRefreshLayout
    lateinit var progress_bar:RelativeLayout
    lateinit var rvPancardHistory:RecyclerView
    var walletBalance="0"

    lateinit var tvTitle:TextView

    private var mInterstitialAd: InterstitialAd? = null
    lateinit var pd: ProgressDialog

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


        tvTitle.setText("PVC I-Card History")


        pd = ProgressDialog(this@PVCIcardReportsActivity)
        pd.setMessage("Please wait ad is loading..")
        pd.show()
        MobileAds.initialize(
            this
        ) { }

        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    pd.dismiss()
                    mInterstitialAd = interstitialAd
                    Log.i("TAG", "onAdLoaded")
                    if (mInterstitialAd != null) {
                        mInterstitialAd!!.show(this@PVCIcardReportsActivity)
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

        callServiceGetWalletBalance()



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


        val call = apiService.callGetPVCListService(
            Preferences.getString(AppConstants.MOBILE)
        )


        //making the call to generate checksum
        call.enqueue(object : Callback<BasePVCListResponse> {
            override fun onResponse(
                call: Call<BasePVCListResponse>,
                response: Response<BasePVCListResponse>
            ) {
                progress_bar.visibility = View.GONE
                if (response.body()!!.status == true) {
                    pvccardListModalArrayList= response.body()!!.pvcListResponse
                    rvPancardHistory.apply {

                        layoutManager = LinearLayoutManager(this@PVCIcardReportsActivity)
                        pvccardAdapter = PVCIcardListAdapter(
                            context, pvccardListModalArrayList, retryClick,editClick, statusClick
                        )
                        rvPancardHistory.adapter = pvccardAdapter
                    }
                } else {
                    Toast.makeText(
                        this@PVCIcardReportsActivity,
                        response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()

                }


                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter
            }

            override fun onFailure(call: Call<BasePVCListResponse>, t: Throwable) {
                progress_bar.visibility = View.GONE
                // callServiceFalse(mobileNo);
                Toast.makeText(this@PVCIcardReportsActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }


    var orderId=" "
    var token:Int=0
    var retryClick = View.OnClickListener { v ->
        val i = v.tag as Int
        token=pvccardListModalArrayList.get(i).id
        Preferences.saveValue("TOKEN",token)
        val totalAmount=pvccardListModalArrayList.get(i).totalpdfno*40

        val amount: String = totalAmount.toString()
        val upi: String = "anilmitawa64@axl"
        val name: String = "Pancard"
        val desc: String = "Payment"
        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault())
        orderId = df.format(c)

       // generateCheckSumPay()

      // makePayment(amount, upi, name, desc, orderId)


       openDailogPayMethod(amount,token.toString())
    }



    var editClick = View.OnClickListener { v ->
        val i = v.tag as Int

        val strUri =
            "http://maps.google.com/maps?q=loc:" + pvccardListModalArrayList.get(i).lat.toString() + "," + pvccardListModalArrayList.get(i).lang.toString() + " (" + "Label which you want" + ")"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))

        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")

        startActivity(intent)

       /* val name=pvccardListModalArrayList.get(i).name
        val mobileno=pvccardListModalArrayList.get(i).mobile
        val dob=pvccardListModalArrayList.get(i).dob
        val fatherName=pvccardListModalArrayList.get(i).fathername
        val emailId=pvccardListModalArrayList.get(i).email
        val adharImage=pvccardListModalArrayList.get(i).aadhar
        val signImage=pvccardListModalArrayList.get(i).sign
        val photo=pvccardListModalArrayList.get(i).photo
        val guardianAadhar=pvccardListModalArrayList.get(i).guardiansAadhar
        val raType=pvccardListModalArrayList.get(i).ratype

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
        intent.putExtra("raType",raType)
        intent.putExtra("tokenid",pvccardListModalArrayList.get(i).token)
        startActivity(intent)*/


    }


    var statusClick = View.OnClickListener { v ->
        val i = v.tag as Int

      /*  val receiptno=pvccardListModalArrayList.get(i).receiptno

        showStatusDailog(receiptno)*/

    }

    lateinit var dialogStatus: Dialog
    lateinit var webViewStatus: WebView
    lateinit var imgClose: ImageView
    private lateinit var mUploadMessage: ValueCallback<Uri>
    lateinit var uploadMessage: ValueCallback<Array<Uri>>
    val REQUEST_SELECT_FILE = 100
    private val FILECHOOSER_RESULTCODE = 1

    private fun showStatusDailog(receiptno: String?) {
        dialogStatus= Dialog(this@PVCIcardReportsActivity)
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
      //  callServiceUpdateStatus(Preferences.getString("TOKEN"),"failed")
    }

    override fun onTransactionCancelled() {
        // this method is called when transaction is cancelled.
        Toast.makeText(this, "Transaction cancelled..", Toast.LENGTH_SHORT).show()

    }

    override fun onAppNotFound() {
        // this method is called when the users device is not having any app installed for making payment.
        Toast.makeText(this, "No app found for making transaction..", Toast.LENGTH_SHORT).show()
    }


    private fun callServiceUpdateStatus(token: String, status: String, amountToPay: String) {
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
        val amount1: RequestBody = createPartFromString(amountToPay)

        //Call<ScannerResponse> call = apiService.saveScan(orderId1,vpa1,name1,amount1,mon_no1,member_id1,password1);
        val call = apiService.updatePVCPaymentStatus(token1,status1,amount1 )


        //making the call to generate checksum
        call.enqueue(object : Callback<BaseUpdatePVCResponse> {
            override fun onResponse(
                call: Call<BaseUpdatePVCResponse>,
                response: Response<BaseUpdatePVCResponse>
            ) {
                progress_bar.visibility = View.GONE
                if (response.body()!!.status == true) {
                    Toast.makeText(
                        this@PVCIcardReportsActivity,
                        response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                   callServiceGetPVCcard()
                } else {
                    Toast.makeText(
                        this@PVCIcardReportsActivity,
                        response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    callServiceGetPVCcard()
                }


                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter
            }

            override fun onFailure(call: Call<BaseUpdatePVCResponse>, t: Throwable) {
                progress_bar.visibility = View.GONE
                // callServiceFalse(mobileNo);
                Toast.makeText(this@PVCIcardReportsActivity, t.message, Toast.LENGTH_SHORT).show()
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
            this@PVCIcardReportsActivity,
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
                        this@PVCIcardReportsActivity,
                        "Network connection error: Check your internet connectivity",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun clientAuthenticationFailed(inErrorMessage: String) {
                    Toast.makeText(
                        this@PVCIcardReportsActivity,
                        "Authentication failed: Server error$inErrorMessage", Toast.LENGTH_LONG
                    ).show()
                }

                override fun onErrorLoadingWebPage(
                    iniErrorCode: Int,
                    inErrorMessage: String,
                    inFailingUrl: String
                ) {
                    Toast.makeText(
                        this@PVCIcardReportsActivity,
                        "Unable to load webpage $inErrorMessage",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onBackPressedCancelTransaction() {
                    Toast.makeText(this@PVCIcardReportsActivity, "Transaction cancelled", Toast.LENGTH_LONG).show()
                }

                override fun onTransactionCancel(inErrorMessage: String, inResponse: Bundle) {
                    Toast.makeText(
                        this@PVCIcardReportsActivity,
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
           /* val mp: MediaPlayer
            mp = MediaPlayer.create(this, R.raw.pan_payment_success)
            mp.setOnCompletionListener { mp -> // TODO Auto-generated method stub
                var mp = mp
                mp!!.reset()
                mp!!.release()
                mp = null
            }
            mp.start()*/
            //callServiceUpdateStatus(token,"success")
        } else {
            //callServiceUpdateStatus(token,"failure")
            Toast.makeText(this@PVCIcardReportsActivity, respMsgPay, Toast.LENGTH_SHORT).show()
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
                        this@PVCIcardReportsActivity,
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
                Toast.makeText(this@PVCIcardReportsActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }


    var dialogBox: BottomSheetDialog? = null
    lateinit var layUPI:LinearLayout
    lateinit var layWallet:LinearLayout
    lateinit var text_wallet:TextView

    fun openDailogPayMethod(amountToPay: String, tokenUpdate: String) {
        dialogBox = BottomSheetDialog(this)
        dialogBox!!.setContentView(R.layout.bottomdailog_paymethod)
        layUPI = dialogBox!!.findViewById(R.id.lay_upi)!!
        layWallet = dialogBox!!.findViewById<LinearLayout>(R.id.lay_wallet)!!
        text_wallet= dialogBox!!.findViewById(R.id.text_wallet)!!

        layUPI.visibility=View.GONE





        text_wallet.setText("Wallet (Rs. "+walletBalance+" )")

        layUPI!!.setOnClickListener {



            val upi: String = "anilmitawa64@axl"
            val name: String = "Recharge"
            val desc: String = "Payment"



            // if the edit text is not empty then
            // we are calling method to make payment.
            makePayment(amountToPay, upi, name, desc, orderId)

            //  generateCheckSumPay()
            dialogBox!!.dismiss()
        }


        layWallet.setOnClickListener(View.OnClickListener {
            if (walletBalance.toDouble() >= amountToPay.toDouble())
            {
                /*val mp: MediaPlayer
                mp = MediaPlayer.create(this, R.raw.pan_payment_success)
                mp.setOnCompletionListener { mp -> // TODO Auto-generated method stub
                    var mp = mp
                    mp!!.reset()
                    mp!!.release()
                    mp = null
                }
                mp.start()*/
                callServiceUpdateStatus(tokenUpdate,"success",amountToPay)
                dialogBox!!.dismiss()
            }else{
                Toast.makeText(this@PVCIcardReportsActivity,"Insufficient Balance",Toast.LENGTH_LONG).show()
            }
        })


        dialogBox!!.show()
    }


}