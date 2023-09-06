package com.spindia.rechargeapp.recharge_services

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails
import com.spindia.rechargeapp.R
import com.spindia.rechargeapp.authentication.response.WalletResponse
import com.spindia.rechargeapp.model.CircleListModel
import com.spindia.rechargeapp.model.OfferSModel
import com.spindia.rechargeapp.model.OperatorsModel
import com.spindia.rechargeapp.network.Preferences
import com.spindia.rechargeapp.network_calls.AppApiCalls
import com.spindia.rechargeapp.network_calls.AppApiUrl
import com.spindia.rechargeapp.pancardOffline.BaseHeadingResponse
import com.spindia.rechargeapp.utils.AppCommonMethods
import com.spindia.rechargeapp.utils.AppConstants
import com.spindia.rechargeapp.utils.MainIAPI
import com.spindia.rechargeapp.utils.toast
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit


class MobilePrepaidActivity : AppCompatActivity(), AppApiCalls.OnAPICallCompleteListener,
    OperatorListAdapter.ListAdapterListener, OfferDetailsAdapter.ListAdapterListener, MainOfferDetailsAdapter.ListAdapterListener,
    CircleListAdapter.ListAdapterListener ,TextToSpeech.OnInitListener , PaymentStatusListener {

    lateinit var operatorAdapter: OperatorListAdapter
    var operatorsModelArrayList = ArrayList<OperatorsModel>()
    var bottomSheetDialog: BottomSheetDialog? = null

   // lateinit var userModel: UserModel

    var circleId: String = ""

    lateinit var circleListAdapter: CircleListAdapter
    var circleListModelArrayList = ArrayList<CircleListModel>()

    var operator_code: String = ""
    var opshortcode: String = ""
    lateinit var dialog: Dialog
    private val MOBILEOFFERS_API: String = "MOBILEOFFERS_API"
    private val CIRCLE: String = "CIRCLE"
    var bottomSheetDialogOffers: BottomSheetDialog? = null
    var offersModalArrayList = ArrayList<MobilePlansList>()
    lateinit var offerDetailsAdapter: OfferDetailsAdapter
    lateinit var offerTitleAdapter: MainOfferDetailsAdapter
    var operatorCode: Int = 0
    private var tts: TextToSpeech? = null

    lateinit var custToolbar: androidx.appcompat.widget.Toolbar

    lateinit var ivBackBtn: ImageView
    lateinit var tvCircle: TextView
    lateinit var tvChooseOperator: TextView
    lateinit var cvRechargePrepaidBtn:CardView
    lateinit var etMobileNumberPrepaid:EditText
    lateinit var etUPIIDPrepaid:EditText
    lateinit var etAmountPrepaid:EditText
    lateinit var cvBrowsePlans:CardView
    lateinit var progress_bar:RelativeLayout
    lateinit var ivOperatorImagePrepaid:ImageView
    lateinit var ivHistory:ImageView

    lateinit var rvspinner:RecyclerView

    lateinit var tvHeading: TextView


    var offerMap: HashMap<String, ArrayList<MobilePlansList>> =
        HashMap<String, ArrayList<MobilePlansList>>()

    var offerArrayList: ArrayList<OfferInnerModelClss> = ArrayList<OfferInnerModelClss>()

    var operatorName=""
    var upiid: String = "anilmitawa64@axl"
    var upiCustomer=""

    private var mInterstitialAd: InterstitialAd? = null
    var pd: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mobile_prepaid)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.status_bar, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }

        custToolbar=findViewById(R.id.custToolbar)
        tvCircle=findViewById(R.id.tvCircle)
        tvChooseOperator=findViewById(R.id.tvChooseOperator)
        cvRechargePrepaidBtn=findViewById(R.id.cvRechargePrepaidBtn)
        etMobileNumberPrepaid=findViewById(R.id.etMobileNumberPrepaid)
        etAmountPrepaid=findViewById(R.id.etAmountPrepaid)
        cvBrowsePlans=findViewById(R.id.cvBrowsePlans)
        progress_bar=findViewById(R.id.progress_bar)
        ivOperatorImagePrepaid=findViewById(R.id.ivOperatorImagePrepaid)
        ivBackBtn=findViewById(R.id.ivBackBtn)
        etUPIIDPrepaid=findViewById(R.id.etUPIIDPrepaid)
        ivHistory=findViewById(R.id.ivHistory)
        tvHeading = findViewById(R.id.tvmarque)


        pd = ProgressDialog(this@MobilePrepaidActivity)
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
                        mInterstitialAd!!.show(this@MobilePrepaidActivity)
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



        callServiceGetHeading()



        ivHistory.setOnClickListener {
            val intent = Intent(this, AllRechargeReportsActivity::class.java)
            startActivity(intent)
        }



        ivBackBtn.setOnClickListener {
            onBackPressed()
        }
        
    /*    val gson = Gson()
        val json = AppPrefs.getStringPref(AppConstants.USER_MODEL, this)
        userModel = gson.fromJson(json, UserModel::class.java)*/


        tts = TextToSpeech(this, this)

       // getBalanceApi(userModel.cus_mobile)

        circle()

        tvCircle.setOnClickListener {
            ShowBottomSheetState()
        }

        getOperatorApi(AppConstants.OPERATOR_MOBILE)


        tvChooseOperator.setOnClickListener {
            showOperatorsBottomSheet()
        }

        cvRechargePrepaidBtn.setOnClickListener {

            /*if (etUPIIDPrepaid.text.isNullOrEmpty()) {

                etUPIIDPrepaid.requestFocus()
                etUPIIDPrepaid.error =
                    getString(R.string.error_upi_id)
                return@setOnClickListener
            } else*/ if (!AppCommonMethods.checkForMobile(etMobileNumberPrepaid)) {

                etMobileNumberPrepaid.requestFocus()
                etMobileNumberPrepaid.error =
                    getString(R.string.error_mobile_number)
                return@setOnClickListener
            } else

                if (etAmountPrepaid.text.isNullOrEmpty()) {

                    etAmountPrepaid.requestFocus()
                    etAmountPrepaid.error =
                        getString(R.string.error_invalid_amount)
                    return@setOnClickListener
                } else {
                  /*  rechargeApi(
                        etUPIIDPrepaid.text.toString() ,"", etMobileNumberPrepaid.text.toString(),
                        etAmountPrepaid.text.toString(), operator_code, "RETAILER"
                    )*/



                    val intent = Intent(this, RechargeSummaryActivity::class.java)
                    intent.putExtra("mobileno",etMobileNumberPrepaid.text.toString())
                    intent.putExtra("operator",operatorName)
                    intent.putExtra("amount",etAmountPrepaid.text.toString())
                    intent.putExtra("upiid",etUPIIDPrepaid.text.toString() )
                    intent.putExtra("operator_code",operator_code)
                    this.startActivity(intent)
/*

                    val c = Calendar.getInstance().time
                    val df = SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault())
                    val transcId: String = df.format(c)


                    val amount: String = etAmountPrepaid.getText().toString()+".00"
                    val upi: String = upiid!!
                    val name: String = "Recharge"
                    val desc: String = "Payment"

                    if (TextUtils.isEmpty(amount) && TextUtils.isEmpty(upi) && TextUtils.isEmpty(name) && TextUtils.isEmpty(
                            desc
                        )
                    ) {
                        Toast.makeText(
                            this@MobilePrepaidActivity,
                            "Please enter amount..",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // if the edit text is not empty then
                        // we are calling method to make payment.
                        makePayment(amount, upi, name, desc, transcId)
                    }

*/


                }


        }


        cvBrowsePlans.setOnClickListener {

            if (!AppCommonMethods.checkForMobile(etMobileNumberPrepaid)) {

                etMobileNumberPrepaid.requestFocus()
                etMobileNumberPrepaid.setError("Invalid Mobile")
            } else {

                callServiceBrowsePlans(etMobileNumberPrepaid.text.toString())
                /*offersApi(
                    etMobileNumberPrepaid.text.toString(),
                    operator_code,
                    circleId
                )*/

            }

        }

      /*  cvWalletBalancePrepaid.setBackgroundResource(R.drawable.bg_leftcurved)
        cvWalletBalancePrepaid.visibility=View.GONE
       */

        cvBrowsePlans.setBackgroundResource(R.drawable.bg_rightcurved)
    //    cvBrowsePlans.visibility=View.GONE

    }


    //API CALL FUNCTION DEFINITION
    private fun getOperatorApi(
        operator_type: String
    ) {
        progress_bar.visibility = View.VISIBLE
        if (AppCommonMethods(this).isNetworkAvailable) {
            val mAPIcall = AppApiCalls(
                this,
                AppConstants.OPERATOR_API,
                this
            )
            mAPIcall.getOperators(operator_type)

        } else {
            this.toast(getString(R.string.error_internet))
        }
    }


    private fun getBalanceApi(
        cus_id: String
    ) {
        progress_bar.visibility = View.VISIBLE
        if (AppCommonMethods(this).isNetworkAvailable) {
            val mAPIcall = AppApiCalls(
                this,
                AppConstants.BALANCE_API,
                this
            )
            mAPIcall.getBalance(cus_id)

        } else {
            this.toast(getString(R.string.error_internet))
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
        usermobile: String
    ) {
        progress_bar.visibility = View.VISIBLE

        if (AppCommonMethods(this).isNetworkAvailable) {
            val mAPIcall = AppApiCalls(
                this,
                AppConstants.RECHARGE_API,
                this
            )
            mAPIcall.rechargeApi(cus_upi,cus_id, rec_mobile, amount, operator, cus_type,usermobile,"upi" )

        } else {
            this.toast(getString(R.string.error_internet))
        }
    }

    private fun offersApi(
        mobile: String,
        operator: String,
        circle_code: String
    ) {
        progress_bar.visibility = View.VISIBLE

        if (AppCommonMethods(this).isNetworkAvailable) {
            val mAPIcall =
                AppApiCalls(this, MOBILEOFFERS_API, this)
            mAPIcall.mobileOffers(
                mobile, operator, circle_code
            )
        } else {
            Toast.makeText(this, "Internet Error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun circle() {
        //progress_bar.visibility = View.VISIBLE

        if (AppCommonMethods(this).isNetworkAvailable) {
            val mAPIcall =
                AppApiCalls(this, CIRCLE, this)
            mAPIcall.circle()
        } else {

            Toast.makeText(this, "Internet Error", Toast.LENGTH_SHORT).show()
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

                    operatorsModelArrayList.add(operatorsModel)
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

                if (resultObject.getString("chainstatus").equals("success"))
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
                }else if (resultObject.getString("chainstatus").equals("pending"))
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
                }else if (resultObject.getString("chainstatus").equals("failed"))
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

                }



             //   tts!!.speak(message, TextToSpeech.QUEUE_FLUSH, null,"")



                showSuccessOrFailedDialog(getString(R.string.status), message)

            } else {
                progress_bar.visibility = View.GONE

                val resultObject = jsonObject.getJSONObject("result")
                val message = resultObject.getString(AppConstants.MESS)
                progress_bar.visibility = View.GONE
                tts!!.speak(message, TextToSpeech.QUEUE_FLUSH, null,"")

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
        if (flag.equals(MOBILEOFFERS_API)) {
            Log.e("MOBILEOFFERS_API", result)
            val jsonObject = JSONObject(result)
            val status = jsonObject.getString(AppConstants.STATUS)
            Log.e(AppConstants.STATUS, status)
            //Log.e(AppConstants.MESSAGE_CODE, messageCode);
            if (status.contains("true")) {
                progress_bar.visibility = View.INVISIBLE
                offerArrayList.clear()

           /*     val resultObject = jsonObject.getJSONObject("result")
                val cast = resultObject.getJSONArray("PlanDescription")
                for (i in 0 until cast.length()) {
                    val notifyObjJson = cast.getJSONObject(i)
                    val recharge_amount = notifyObjJson.getString("recharge_amount")
                    Log.e("price", recharge_amount)
                    val offerSModel = Gson()
                        .fromJson(notifyObjJson.toString(), OfferSModel::class.java)
                    offersModalArrayList.add(offerSModel)






                }

                // create hashmap for filter data in arraylist basis of category
                try {
                    val map: HashMap<String, ArrayList<OfferSModel>> =
                        HashMap<String, ArrayList<OfferSModel>>()
                    for (i in offersModalArrayList.indices) {

                        if (offersModalArrayList.get(i)
                                .recharge_type != null && !offersModalArrayList.get(i).recharge_type.equals("")

                        ) {
                            if (map.containsKey(
                                    offersModalArrayList.get(i).recharge_type
                                )
                            ) {
                                val l2: ArrayList<OfferSModel> =
                                    map[offersModalArrayList.get(i).recharge_type]!!
                                l2.add(offersModalArrayList.get(i))
                                map[offersModalArrayList.get(i).recharge_type!!] = l2
                                offerMap = map
                            } else {
                                val l2: ArrayList<OfferSModel> =
                                    ArrayList<OfferSModel>()
                                l2.add(offersModalArrayList.get(i))
                                map[offersModalArrayList.get(i).recharge_type!!] = l2
                                offerMap = map
                            }

                        }
                    }
                } catch (e: java.lang.Exception) {
                }


                Log.e("OPERATOR MAP", offerMap.toString())

                for (i in 0 until offerMap.size) {

                    val innerModelClass = OfferInnerModelClss()
                    val key: String = getKey1(i)!!
                    val mainList: ArrayList<OfferSModel> = offerMap.get(key)!!

                    innerModelClass.data=key
                    innerModelClass.isOpen=false
                    innerModelClass.nestedDetailResponse=mainList
                    offerArrayList.add(innerModelClass)

                }

                Log.e("OPERATOR offerArrayList", offerArrayList.toString())
                ShowBottomSheetOffers()*/

            } else {

                progress_bar.visibility = View.INVISIBLE

            }
        }
        if (flag.equals(CIRCLE)) {
            circleListModelArrayList.clear()
            Log.e("CIRCLE", result)
            val jsonObject = JSONObject(result)
            val status = jsonObject.getString(AppConstants.STATUS)
            Log.e(AppConstants.STATUS, status)
            if (status.contains("true")) {
                progress_bar.visibility = View.GONE
                //progress_bar_state.visibility = View.INVISIBLE

                val cast = jsonObject.getJSONArray("result")
                for (i in 0 until cast.length()) {
                    val notifyObjJson = cast.getJSONObject(i)
                    val stateModel = Gson()
                        .fromJson(
                            notifyObjJson.toString(),
                            CircleListModel::class.java
                        )
                    circleListModelArrayList.add(stateModel)
                }
                //ShowBottomSheetState()
            } else {
                progress_bar.visibility = View.GONE
                //progress_bar_state.visibility = View.INVISIBLE
            }
        }

    }


    private fun ShowBottomSheetState() {
        val view: View = layoutInflater.inflate(R.layout.layout_list_bottomsheet, null)
        rvspinner=view.findViewById(R.id.rvspinner)
        rvspinner.apply {

            layoutManager = LinearLayoutManager(this@MobilePrepaidActivity)
            circleListAdapter = CircleListAdapter(
                context, circleListModelArrayList, this@MobilePrepaidActivity
            )
            rvspinner.adapter = circleListAdapter
        }

        bottomSheetDialog = BottomSheetDialog(this@MobilePrepaidActivity, R.style.SheetDialog)
        bottomSheetDialog!!.setContentView(view)
        val bottomSheetBehavior: BottomSheetBehavior<*> =
            BottomSheetBehavior.from(view.parent as View)
        bottomSheetBehavior.peekHeight = 600
        bottomSheetDialog!!.show()
    }


    private fun showOperatorsBottomSheet() {
        val view: View = layoutInflater.inflate(R.layout.layout_list_bottomsheet, null)
        rvspinner=view.findViewById(R.id.rvspinner)
        rvspinner.apply {

            layoutManager = LinearLayoutManager(this@MobilePrepaidActivity)
            rvspinner.addItemDecoration(DividerItemDecoration(this@MobilePrepaidActivity, LinearLayoutManager.VERTICAL))

            operatorAdapter = OperatorListAdapter(
                context, operatorsModelArrayList, this@MobilePrepaidActivity
            )

            rvspinner.adapter = operatorAdapter
        }

        bottomSheetDialog = BottomSheetDialog(this@MobilePrepaidActivity, R.style.SheetDialog)
        bottomSheetDialog!!.setContentView(view)
        val bottomSheetBehavior: BottomSheetBehavior<*> =
            BottomSheetBehavior.from(view.parent as View)
        bottomSheetBehavior.peekHeight = 600
        bottomSheetDialog!!.show()
    }

    override fun onClickAtOKButton(operatorsModel: OperatorsModel?) {
        if (operatorsModel != null) {
            tvChooseOperator.text = operatorsModel.operatorname


            operator_code = operatorsModel.opshortcode!!.trim()
            operatorName = operatorsModel.operatorname!!.trim()
            opshortcode=operatorsModel.qr_opcode
//            opName = mobileRechargeModal.operatorname!!.trim()
            Glide.with(this)
                .load(AppApiUrl.IMAGE_URL + operatorsModel.operator_image)
                .into(ivOperatorImagePrepaid)
            bottomSheetDialog!!.dismiss()
        }
    }

    private fun clearData() {

        etMobileNumberPrepaid.setText("")
        tvChooseOperator.setText("")
        ivOperatorImagePrepaid.setImageDrawable(resources.getDrawable(R.drawable.icons_cellulartower))
        etAmountPrepaid.setText("")

    }

    override fun onResume() {
        super.onResume()
        clearData()
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
            clearData()

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


   // lateinit var rvOffers:RecyclerView
    lateinit var ivCloseTab:ImageView
    lateinit var rvOffersTitle:RecyclerView
    private fun ShowBottomSheetOffers() {
        val view: View = layoutInflater.inflate(R.layout.layout_dialog_offers, null)
     //   rvOffers=view.findViewById(R.id.rvOffers)
        ivCloseTab=view.findViewById(R.id.ivCloseTab)
        rvOffersTitle=view.findViewById(R.id.rvOffers_title)
       /* rvOffers.apply {

            layoutManager = LinearLayoutManager(this@MobilePrepaidActivity)

            offerDetailsAdapter = OfferDetailsAdapter(
                this@MobilePrepaidActivity, offerArrayList[0].nestedDetailResponse, this@MobilePrepaidActivity
            )
            rvOffers.adapter = offerDetailsAdapter
        }*/

        rvOffersTitle.apply {

            layoutManager = LinearLayoutManager(this@MobilePrepaidActivity,  LinearLayoutManager.VERTICAL,
                false)

            offerTitleAdapter = MainOfferDetailsAdapter(
                this@MobilePrepaidActivity, offerArrayList, this@MobilePrepaidActivity
            )
            rvOffersTitle.adapter = offerTitleAdapter
        }

        ivCloseTab.setOnClickListener {
            bottomSheetDialogOffers!!.dismiss()
        }


        bottomSheetDialogOffers = BottomSheetDialog(this@MobilePrepaidActivity, R.style.SheetDialog)
        bottomSheetDialogOffers!!.setContentView(view)
        val bottomSheetBehavior: BottomSheetBehavior<*> =
            BottomSheetBehavior.from(view.parent as View)
        bottomSheetBehavior.peekHeight = 600
        bottomSheetDialogOffers!!.show()

       /* bottomSheetDialogOffers = BottomSheetDialog(this, R.style.SheetDialog)
        bottomSheetDialogOffers!!.setContentView(view)
        val bottomSheetBehavior: BottomSheetBehavior<*> =
            BottomSheetBehavior.from(parent as View)
        bottomSheetBehavior.peekHeight = 1000
        bottomSheetDialogOffers!!.show()*/
    }

    override fun onClickAtOKButton(offerSModel: MobilePlansList?) {
        if (offerSModel != null) {
            etAmountPrepaid.setText(""+offerSModel.getAmount())
            bottomSheetDialogOffers!!.dismiss()
        }
    }

    override fun onClickAtOKButton(circleListModel: CircleListModel?) {
        if (circleListModel != null) {
            tvCircle.setText(circleListModel.state)
            circleId = circleListModel.code
            bottomSheetDialog!!.dismiss()
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","The Language specified is not supported!")
            } else {
             //   tts!!.speak("Text to speech", TextToSpeech.QUEUE_FLUSH, null,"")

                Log.e("TTS","buttonSpeak!!.isEnabled = true")
                // buttonSpeak!!.isEnabled = true
            }

        } else {
            Log.e("TTS", "Initilization Failed!")
        }
    }

    override fun onTransactionCompleted(transactionDetails: TransactionDetails?) {
        try {
            // Get transactions details from response.

            if (transactionDetails!!.status!!.toLowerCase() == "success") {
                Toast.makeText(this, "Transaction success..", Toast.LENGTH_SHORT).show()
                rechargeApi(
                    etUPIIDPrepaid.text.toString() ,"", etMobileNumberPrepaid.text.toString(),
                    etAmountPrepaid.text.toString(), operator_code, "RETAILER", Preferences.getString(
                        AppConstants.MOBILE)
                )
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


    private fun getKey1(i: Int): String? {
        val mkey: ArrayList<String> = ArrayList<String>(offerMap.keys)
        return mkey[i]
    }

    override fun onClickAtOKButton(OfferInnerModelClss: OfferInnerModelClss?) {
        offerDetailsAdapter = OfferDetailsAdapter(
            this@MobilePrepaidActivity, OfferInnerModelClss!!.nestedDetailResponse, this@MobilePrepaidActivity
        )
        offerDetailsAdapter.notifyDataSetChanged()
    }


    private fun callServiceGetHeading() {
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


        //Call<ScannerResponse> call = apiService.saveScan(orderId1,vpa1,name1,amount1,mon_no1,member_id1,password1);
        val call = apiService.callGetHeadingService("recharge", "list")


        //making the call to generate checksum
        call.enqueue(object : Callback<BaseHeadingResponse> {
            override fun onResponse(
                call: Call<BaseHeadingResponse>,
                response: Response<BaseHeadingResponse>
            ) {
                progress_bar.visibility = View.GONE
                if (response.body()!!.status == true) {
                   /* Toast.makeText(
                        this@MobilePrepaidActivity,
                        response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
*/
                    tvHeading.setText(response.body()!!.getData().get(0).getContent());
                    tvHeading.setSelected(true)
                } else {
                    Toast.makeText(
                        this@MobilePrepaidActivity,
                        response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }


                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter
            }

            override fun onFailure(call: Call<BaseHeadingResponse>, t: Throwable) {
                progress_bar.visibility = View.GONE
                // callServiceFalse(mobileNo);
                Toast.makeText(this@MobilePrepaidActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun callServiceBrowsePlans(mobileNo: String) {
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
            .baseUrl(MainIAPI.BASE_URL_BROWSEPLANS)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()


        //creating the retrofit api service
        val apiService = retrofit.create(MainIAPI::class.java)



        //Call<ScannerResponse> call = apiService.saveScan(orderId1,vpa1,name1,amount1,mon_no1,member_id1,password1);
        val call = apiService.callBrowsePlanService( mobileNo)


        //making the call to generate checksum
        call.enqueue(object : Callback<MainMobilePlans> {
            override fun onResponse(
                call: Call<MainMobilePlans>,
                response: Response<MainMobilePlans>
            ) {
                progress_bar.visibility = View.GONE
                if (response.body()!!.getSuccess() == true) {

                    offersModalArrayList= response.body()!!.getSubMobilePlansResponse()!!.getMobilePlansList() as ArrayList<MobilePlansList>

                    try {
                        val map: HashMap<String, ArrayList<MobilePlansList>> =
                            HashMap<String, ArrayList<MobilePlansList>>()
                        for (i in offersModalArrayList.indices) {

                            if (offersModalArrayList.get(i)
                                    .getPlanTab() != null && !offersModalArrayList.get(i).getPlanTab().equals("")

                            ) {
                                if (map.containsKey(
                                        offersModalArrayList.get(i).getPlanTab()
                                    )
                                ) {
                                    val l2: ArrayList<MobilePlansList> =
                                        map[offersModalArrayList.get(i).getPlanTab()]!!
                                    l2.add(offersModalArrayList.get(i))
                                    map[offersModalArrayList.get(i).getPlanTab()!!] = l2
                                    offerMap = map
                                } else {
                                    val l2: ArrayList<MobilePlansList> =
                                        ArrayList<MobilePlansList>()
                                    l2.add(offersModalArrayList.get(i))
                                    map[offersModalArrayList.get(i).getPlanTab()!!] = l2
                                    offerMap = map
                                }

                            }
                        }
                    } catch (e: java.lang.Exception) {
                    }

                    Log.e("OPERATOR MAP", offerMap.toString())

                    for (i in 0 until offerMap.size) {

                        val innerModelClass = OfferInnerModelClss()
                        val key: String = getKey1(i)!!
                        val mainList: ArrayList<MobilePlansList> = offerMap.get(key)!!

                        innerModelClass.data=key
                        innerModelClass.isOpen=false
                        innerModelClass.nestedDetailResponse=mainList
                        offerArrayList.add(innerModelClass)

                    }

                    Log.e("OPERATOR offerArrayList", offerArrayList.toString())
                    ShowBottomSheetOffers()
                } else {
                    Toast.makeText(
                        this@MobilePrepaidActivity,
                        "Something Went Wrong",
                        Toast.LENGTH_SHORT
                    ).show()

                }


                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter
            }

            override fun onFailure(call: Call<MainMobilePlans>, t: Throwable) {
                progress_bar.visibility = View.GONE
                // callServiceFalse(mobileNo);
                Toast.makeText(this@MobilePrepaidActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

}