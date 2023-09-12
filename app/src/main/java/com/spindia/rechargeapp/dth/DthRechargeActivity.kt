package com.spindia.rechargeapp.dth

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.spindia.rechargeapp.R
import com.spindia.rechargeapp.authentication.response.WalletResponse
import com.spindia.rechargeapp.electricity.OperatorListAdapter

import com.spindia.rechargeapp.model.OperatorsModel
import com.spindia.rechargeapp.model.UserModel
import com.spindia.rechargeapp.network.Preferences
import com.spindia.rechargeapp.network_calls.AppApiCalls
import com.spindia.rechargeapp.network_calls.AppApiUrl
import com.spindia.rechargeapp.recharge_services.*
import com.spindia.rechargeapp.utils.*

import com.spindia.rechargeapp.utils.AppConstants.Companion.OPERATOR_DTH
import com.spindia.rechargeapp.utils.AppConstants.Companion.TRUE
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody

import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

class DthRechargeActivity : AppCompatActivity(), AppApiCalls.OnAPICallCompleteListener,
    OperatorListAdapter.ListAdapterListener {

    lateinit var operatorAdapter: OperatorListAdapter
    var operatorsModelArrayList = ArrayList<OperatorsModel>()
    var bottomSheetDialog: BottomSheetDialog? = null

    lateinit var custToolbar: androidx.appcompat.widget.Toolbar

    var operator_code: String = ""
    lateinit var dialog: Dialog
    private val DTH_INFO: String = "DTH_INFO"


    lateinit var ivBackBtn:ImageView
    lateinit var tvChooseOperator:TextView
    lateinit var cvRechargeDthBtn:CardView
    lateinit var etDthNumber:EditText
    lateinit var etAmountDth:EditText
    lateinit var cvBrowsePlans:CardView
    lateinit var cvWalletBalanceDth:RelativeLayout
    lateinit var progress_bar:ProgressBar
    lateinit var tvWalletBalance:TextView
    lateinit var rvspinner: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.status_bar, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
        setContentView(R.layout.activity_dth_recharge)

        ivBackBtn=findViewById(R.id.ivBackBtn)
        custToolbar=findViewById(R.id.custToolbar)
        tvChooseOperator=findViewById(R.id.tvChooseOperator)
        cvRechargeDthBtn=findViewById(R.id.cvRechargeDthBtn)
        etDthNumber=findViewById(R.id.etDthNumber)
        etAmountDth=findViewById(R.id.etAmountDth)
        cvBrowsePlans=findViewById(R.id.cvBrowsePlans)
        cvWalletBalanceDth=findViewById(R.id.cvWalletBalanceDth)
        progress_bar=findViewById(R.id.progress_bar)
        tvWalletBalance=findViewById(R.id.tvWalletBalance)
       // rvspinner=findViewById(R.id.rvspinner)




        getOperatorApi(OPERATOR_DTH)
        ivBackBtn.setOnClickListener {
            onBackPressed()
        }
        initViews()


        tvChooseOperator.setOnClickListener {
            showOperatorsBottomSheet()

        }

        callServiceGetWalletBalance()

        cvRechargeDthBtn.setOnClickListener {

            if (etDthNumber.text.isNullOrEmpty()) {

                etDthNumber.requestFocus()
                etDthNumber.error =
                    getString(R.string.error_invalid_dth)
                return@setOnClickListener

            } else if (etAmountDth.text.isNullOrEmpty()) {

                etAmountDth.requestFocus()
                etAmountDth.error =
                    getString(R.string.error_invalid_amount)
                return@setOnClickListener
            } else if (tvChooseOperator.text.isNullOrEmpty()) {

                tvChooseOperator.requestFocus()
                tvChooseOperator.error =
                    getString(R.string.error_select_operator)
                return@setOnClickListener
            } else {

                checkIfSameRecharge(
                    Preferences.getString(AppConstants.MOBILE), etDthNumber.text.toString(),
                    etAmountDth.text.toString(), operator_code
                )


            }


        }

        cvBrowsePlans.setOnClickListener {
            if (etDthNumber.text.toString().isEmpty()) {

                etDthNumber.requestFocus()
                etDthNumber.setError("Invalid Dth")
            }  else {
                callServiceBrowsePlans(etDthNumber.text.toString())
              /*  DthCusInfo(
                    etDthNumber.text.toString(),
                    tvChooseOperator.text.toString(),
                    AppPrefs.getStringPref("deviceId", this).toString(),
                    AppPrefs.getStringPref("deviceName", this).toString(),
                    userModel.cus_pin,
                    userModel.cus_pass,
                    userModel.cus_mobile,
                    userModel.cus_type
                )*/
            }
        }

    }

    fun initViews() {

        cvWalletBalanceDth.setBackgroundResource(R.drawable.bg_leftcurved)
        cvBrowsePlans.setBackgroundResource(R.drawable.bg_rightcurved)



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

    fun rechargeApi(
        cus_upi: String,
        cus_id: String,
        rec_mobile: String,
        amount: String,
        operator: String,
        cus_type: String,
        user_mobile: String,
        type:String
    ) {
        progress_bar.visibility = View.VISIBLE

        if (AppCommonMethods(this).isNetworkAvailable) {
            val mAPIcall = AppApiCalls(
                this,
                AppConstants.RECHARGE_API,
                this
            )

            mAPIcall.rechargeApi("",cus_id, rec_mobile, amount, operator, cus_type,Preferences.getString(
                AppConstants.MOBILE),"wallet")

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


                tvWalletBalance.text =
                    "${getString(R.string.Rupee)} ${jsonObject.getString(AppConstants.WALLETBALANCE)}"
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
                verifyPin(Preferences.getString(AppConstants.MOBILE), AppPrefs.getStringPref("AppPassword",this).toString())
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
            if (status.contains(TRUE)) {
                progress_bar.visibility = View.GONE

                val resultObject = jsonObject.getJSONObject("result")
                val message = resultObject.getString(AppConstants.MESS)

                showSuccessOrFailedDialog(getString(R.string.status), message)


            } else {
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
            if (status.contains(TRUE)) {
                progress_bar.visibility = View.GONE
              /*  rechargeApi(
                    Preferences.getString(AppConstants.MOBILE), etDthNumber.text.toString(),
                    etAmountDth.text.toString(), operator_code, "RETAILER"
                )*/

                rechargeApi(
                    "" ,Preferences.getString(AppConstants.MOBILE), etDthNumber.text.toString(),
                    etAmountDth.text.toString(), operator_code, "RETAILER",
                    Preferences.getString(
                        AppConstants.MOBILE),"wallet"
                )

            } else {

                progress_bar.visibility = View.GONE
                showMessageDialog(getString(R.string.error_attention), message)

            }
        }
        if (flag.equals(DTH_INFO)) {
            Log.e("OFFERS", result)
            val jsonObject = JSONObject(result)
            val status = jsonObject.getString(AppConstants.STATUS)
            Log.e(AppConstants.STATUS, status)
            //Log.e(AppConstants.MESSAGE_CODE, messageCode);
            if (status.contains("true")) {
                progress_bar.visibility = View.INVISIBLE

                val resultObject = jsonObject.getJSONObject("result")
                val cast = resultObject.getJSONArray("records")
                for (i in 0 until cast.length()) {
                    val notifyObjJson = cast.getJSONObject(i)


                    showDialogDth(
                        notifyObjJson.getString("customerName"),
                        notifyObjJson.getString("status"),
                        notifyObjJson.getString("planname"),
                        notifyObjJson.getString("Balance"),
                        notifyObjJson.getString("NextRechargeDate"),
                        notifyObjJson.getString("MonthlyRecharge")
                    )

                }
            } else {
                progress_bar.visibility = View.INVISIBLE

            }
        }



    }


    private fun showOperatorsBottomSheet() {
        val view: View = layoutInflater.inflate(R.layout.layout_list_bottomsheet, null)
        rvspinner=view.findViewById(R.id.rvspinner)

        rvspinner.apply {

            layoutManager = LinearLayoutManager(context)
            rvspinner.addItemDecoration(DividerItemDecoration(this@DthRechargeActivity, LinearLayoutManager.VERTICAL))

            operatorAdapter = OperatorListAdapter(
                context, operatorsModelArrayList, this@DthRechargeActivity
            )
            rvspinner.adapter = operatorAdapter
        }

        bottomSheetDialog = BottomSheetDialog(this, R.style.SheetDialog)
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
           // operatorName = operatorsModel.operatorname!!.trim()
           // opshortcode=operatorsModel.qr_opcode
//            opName = mobileRechargeModal.operatorname!!.trim()

            bottomSheetDialog!!.dismiss()
        }
    }

    fun onClickAtOKButton(offerSModel: MobilePlansList?) {
        if (offerSModel != null) {
            etAmountDth.setText(""+offerSModel.getAmount())
            bottomSheetDialogOffers!!.dismiss()
        }
    }


    private fun clearData() {

        etDthNumber.setText("")
        tvChooseOperator.setText("")

        etAmountDth.setText("")


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
            startActivity(intent)
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


    lateinit var tvCusName:TextView
    lateinit var tvCusStatus:TextView
    lateinit var tvCustBalance:TextView
    lateinit var tvCustMonthlyRecharge:TextView
    lateinit var tvCustPlan:TextView
    lateinit var tvCustNxtRec:TextView
    lateinit var ivCloseTab:ImageView

    private fun showDialogDth(
        custName: String?,
        custStatus: String?,
        custPlan: String?,
        custBalance: String?,
        custNxtRec: String?,
        custMonthly: String?
    ) {
        val dialog = Dialog(this, R.style.SheetDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_dialog_dthinfo)
        tvCusStatus=dialog.findViewById(R.id.tvCusStatus)
        tvCusName=dialog.findViewById(R.id.tvCusName)
        tvCustBalance=dialog.findViewById(R.id.tvCustBalance)
        tvCustMonthlyRecharge=findViewById(R.id.tvCustMonthlyRecharge)
        tvCustPlan=findViewById(R.id.tvCustPlan)
        tvCustNxtRec=findViewById(R.id.tvCustNxtRec)
        ivCloseTab=findViewById(R.id.ivCloseTab)

        tvCusName.setText("Name : " + custName)
        tvCusStatus.setText("Status : " + custStatus)
        tvCustBalance.setText("Balance : " + resources.getString(R.string.Rupee) + custBalance)
        tvCustMonthlyRecharge.setText("Monthly Rec : " + custMonthly)
        tvCustPlan.setText("Plan : " + custPlan)
        tvCustNxtRec.setText("Next Recharge : " + custNxtRec)



        ivCloseTab.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun DthCusInfo(
        mobile: String,
        operator: String,
        deviceId: String,
        deviceName: String,
        pin: String,
        pass: String,
        cus_mobile: String,
        cus_type: String
    ) {
        progress_bar.visibility = View.VISIBLE

        if (AppCommonMethods(this).isNetworkAvailable) {
            val mAPIcall =
                AppApiCalls(this, DTH_INFO, this)
            mAPIcall.dthInfo(
                mobile, operator, deviceId, deviceName, pin,
                pass, cus_mobile, cus_type
            )
        } else {

            Toast.makeText(this, "Internet Error", Toast.LENGTH_SHORT).show()
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

                    tvWalletBalance.setText(""+ response.body()!!.data.toString()+".00")
                    /*  Toast.makeText(
                          this@NewMainActivity,
                          response.body()!!.message,
                          Toast.LENGTH_SHORT
                      ).show()*/

                } else {
                    Toast.makeText(
                        this@DthRechargeActivity,
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
                Toast.makeText(this@DthRechargeActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun createPartFromString(descriptionString: String): RequestBody {
        return RequestBody.create(
            MultipartBody.FORM, descriptionString
        )
    }

    var offersModalArrayList = ArrayList<MobilePlansList>()
    var offerMap: HashMap<String, ArrayList<MobilePlansList>> =
        HashMap<String, ArrayList<MobilePlansList>>()
    var offerArrayList: ArrayList<OfferInnerModelClss> = ArrayList<OfferInnerModelClss>()
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
        val call = apiService.callBrowseDTHPlanService( mobileNo)


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
                        this@DthRechargeActivity,
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
                Toast.makeText(this@DthRechargeActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getKey1(i: Int): String? {
        val mkey: ArrayList<String> = ArrayList<String>(offerMap.keys)
        return mkey[i]
    }

 
    lateinit var rvOffersTitle:RecyclerView
    lateinit var offerDetailsAdapter: DTHOfferDetailsAdapter
    lateinit var offerTitleAdapter: MainDTHOfferDetailsAdapter
    var bottomSheetDialogOffers: BottomSheetDialog? = null
    private fun ShowBottomSheetOffers() {
        val view: View = layoutInflater.inflate(R.layout.layout_dialog_offers, null)
        //   rvOffers=view.findViewById(R.id.rvOffers)
        ivCloseTab=view.findViewById(R.id.ivCloseTab)
        rvOffersTitle=view.findViewById(R.id.rvOffers_title)
        /* rvOffers.apply {
 
             layoutManager = LinearLayoutManager(this@DthRechargeActivity)
 
             offerDetailsAdapter = OfferDetailsAdapter(
                 this@DthRechargeActivity, offerArrayList[0].nestedDetailResponse, this@DthRechargeActivity
             )
             rvOffers.adapter = offerDetailsAdapter
         }*/

        rvOffersTitle.apply {

            layoutManager = LinearLayoutManager(this@DthRechargeActivity,  LinearLayoutManager.VERTICAL,
                false)

            offerTitleAdapter = MainDTHOfferDetailsAdapter(
                this@DthRechargeActivity, offerArrayList, this@DthRechargeActivity
            )
            rvOffersTitle.adapter = offerTitleAdapter
        }

        ivCloseTab.setOnClickListener {
            bottomSheetDialogOffers!!.dismiss()
        }


        bottomSheetDialogOffers = BottomSheetDialog(this@DthRechargeActivity, R.style.SheetDialog)
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

}