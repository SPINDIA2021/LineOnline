package com.spindia.rechargeapp.electricity

import android.app.Dialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.spindia.rechargeapp.R
import com.spindia.rechargeapp.model.CircleListModel
import com.spindia.rechargeapp.model.OperatorsModel
import com.spindia.rechargeapp.model.UserModel
import com.spindia.rechargeapp.network.Preferences
import com.spindia.rechargeapp.network_calls.AppApiCalls
import com.spindia.rechargeapp.recharge_services.AllRechargeReportsActivity
import com.spindia.rechargeapp.recharge_services.MainMobilePlans
import com.spindia.rechargeapp.recharge_services.MobilePlansList
import com.spindia.rechargeapp.recharge_services.OfferInnerModelClss
import com.spindia.rechargeapp.utils.*
import okhttp3.OkHttpClient

import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class ElectricityRechargeActivity : AppCompatActivity(), AppApiCalls.OnAPICallCompleteListener,
    OperatorListAdapter.ListAdapterListener, CircleListAdapter.ListAdapterListener  , TextToSpeech.OnInitListener{

    lateinit var operatorAdapter: OperatorListAdapter
    var mobileRechargeModalArrayList = ArrayList<OperatorsModel>()

    private val ELECTRICITY_OPERATOR_API: String = "ELECTRICITY_OPERATOR_API"
    private val CHECKSAMERECHARGE_API: String = "CHECKSAMERECHARGE_API"
    private val RECHARGE_API: String = "RECHARGE_API"
    private val GET_BILL_DETAILS: String = "GET_BILL_DETAILS"
    private val CIRCLE: String = "CIRCLE"

    private lateinit var circleId: String
    private  var circle_code: String=""

    lateinit var circleListAdapter: CircleListAdapter
    var circleListModelArrayList = ArrayList<CircleListModel>()

    private lateinit var operator_code: String
    private lateinit var opName: String
    private lateinit var qr_opcode: String

    var deviceId: String = ""
    var deviceNameDet: String? = ""

    var bottomSheetDialog: BottomSheetDialog? = null
    lateinit var dialog: Dialog
    private val CONFIRMPIN_API: String = "CONFIRMPIN_API"



    private var tts: TextToSpeech? = null

    lateinit var ivBackBtn:ImageView
    lateinit var tvSelectState:TextView
    lateinit var tvOperatorNameRec:TextView
    lateinit var btnRechargeElect:CardView
    lateinit var etElectricityNumber:EditText
    lateinit var btnViewDetails:TextView
    lateinit var progress_bar:ProgressBar
    lateinit var etAmountElect:EditText
    lateinit var etElectricityBillingUnit:EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.black, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
        setContentView(R.layout.activity_electricity_recharge)
        ivBackBtn=findViewById(R.id.ivBackBtn)
        tvSelectState=findViewById(R.id.tvSelectState)
        tvOperatorNameRec=findViewById(R.id.tvOperatorNameRec)
        btnRechargeElect=findViewById(R.id.btnRechargeElect)
        etElectricityNumber=findViewById(R.id.etElectricityNumber)
        btnViewDetails=findViewById(R.id.btnViewDetails)
        progress_bar=findViewById(R.id.progress_bar)
        etAmountElect=findViewById(R.id.etAmountElect)
        etElectricityBillingUnit=findViewById(R.id.etElectricityBillingUnit)

       ivBackBtn.setOnClickListener { onBackPressed() }
      /*  val gson = Gson()
        val json = AppPrefs.getStringPref("userModel", this)
        userModel = gson.fromJson(json, UserModel::class.java)
*/
        tts = TextToSpeech(this, this)

        getOperatorApi("electricity")

        circle()

        tvSelectState.setOnClickListener {
            ShowBottomSheetState()
        }

        tvOperatorNameRec.setOnClickListener {
            ShowBottomSheet()
        }

        deviceId =
            Settings.System.getString(this.contentResolver, Settings.Secure.ANDROID_ID)

        Log.e("DEVICE ID", "" + deviceId)

        deviceNameDet = getDeviceName()
        Log.e("DEVICE Name", "" + deviceNameDet)


        btnRechargeElect.setOnClickListener {
            if (etElectricityNumber.text.toString().isEmpty()) {
                etElectricityNumber.requestFocus()
                etElectricityNumber.setError("Invalid Electricity")
            } /*else if (etElectricityBillingUnit.text.toString().isEmpty()) {
                etElectricityBillingUnit.requestFocus()
                etElectricityBillingUnit.setError("Enter Billing Unit")
            }*/ else if (tvOperatorNameRec.text.toString().isEmpty()) {
                tvOperatorNameRec.requestFocus()
                tvOperatorNameRec.setError("Invalid Operator")
                toast("Invalid Operator")
            } else {
                verifyPin(
                    Preferences.getString(AppConstants.MOBILE),
                    AppPrefs.getStringPref("AppPassword",this).toString()
                )
                //confirmPin()
            }
        }

        btnViewDetails.setOnClickListener {
            if (etElectricityNumber.text.toString().isEmpty()) {
                etElectricityNumber.requestFocus()
                etElectricityNumber.setError("Invalid Electricity")
            } else if (tvOperatorNameRec.text.toString().isEmpty()) {
                tvOperatorNameRec.requestFocus()
                tvOperatorNameRec.setError("Please Select Operator")
            } else {
                /*getBillDetails(
                    etElectricityNumber.text.toString(),
                    qr_opcode
                )*/

                callServiceFetchBill(etElectricityNumber.text.toString(),
                    "62")
            }
        }
    }

    //API CALL FUNCTION DEFINITION

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

    private fun getOperatorApi(
        operator_type: String
    ) {
        progress_bar.visibility = View.VISIBLE
        if (AppCommonMethods(this).isNetworkAvailable) {
            val mAPIcall = AppApiCalls(
                this,
                AppConstants.ELECTRICITY_OPERATOR_API,
                this
            )
            mAPIcall.getElectricityOperators(operator_type)

        } else {
            toast("Internet Error")
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
                CONFIRMPIN_API,
                this
            )
            mAPIcall.verifyPin(cus_mobile, pin)

        } else {
            toast(getString(R.string.error_internet))
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
                CHECKSAMERECHARGE_API,
                this
            )
            mAPIcall.checkIfSameRecharge(cus_id, rec_mobile, amount, operator)

        } else {
            toast(getString(R.string.error_internet))
        }
    }


    private fun rechargeApi(
        cus_id: String,
        rec_mobile: String,
        amount: String,
        operator: String,
        cus_type: String,
        billingUnit: String,
        circle_code: String
    ) {
        progress_bar.visibility = View.VISIBLE

        if (AppCommonMethods(this).isNetworkAvailable) {
            val mAPIcall = AppApiCalls(
                this,
                RECHARGE_API,
                this
            )
            mAPIcall.rechargeApiElectricity(cus_id, rec_mobile, amount, operator, cus_type, billingUnit, circle_code)

        } else {
            toast(getString(R.string.error_internet))
        }
    }


    private fun getBillDetails(
        customerId: String,
        operator: String
    ) {
        progress_bar.visibility = View.VISIBLE

        if (AppCommonMethods(this).isNetworkAvailable) {
            val mAPIcall =
                AppApiCalls(this, GET_BILL_DETAILS, this)
            mAPIcall.getBillDetails(
                customerId, operator
            )
        } else {
            Toast.makeText(this, "Internet Error", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAPICallCompleteListner(item: Any?, flag: String?, result: String) {
        if (flag.equals(ELECTRICITY_OPERATOR_API)) {
            Log.e("ELECTRICITY_OPERATOR", result)
            val jsonObject = JSONObject(result)
            val status = jsonObject.getString(AppConstants.STATUS)
            Log.e(AppConstants.STATUS, status)
            if (status.contains("true")) {
                progress_bar.visibility = View.INVISIBLE

                val cast = jsonObject.getJSONArray("result")

                for (i in 0 until cast.length()) {
                    val notifyObjJson = cast.getJSONObject(i)
                    val operatorname = notifyObjJson.getString("operatorname")
                    Log.e("operator_name ", operatorname)
                    val mobilerechModal = Gson()
                        .fromJson(
                            notifyObjJson.toString(),
                            OperatorsModel::class.java
                        )


                    mobileRechargeModalArrayList.add(mobilerechModal)
                }


            } else {
                progress_bar.visibility = View.INVISIBLE


            }
        }
        if (flag.equals(CHECKSAMERECHARGE_API)) {
            Log.e("CHECKSAMERECHARGE_API", result)
            val jsonObject = JSONObject(result)
            val status = jsonObject.getString(AppConstants.STATUS)
            Log.e(AppConstants.STATUS, status)
            //Log.e(AppConstants.MESSAGE_CODE, messageCode);
            if (status.contains("true")) {
                progress_bar.visibility = View.INVISIBLE

                rechargeApi(
                    Preferences.getString(AppConstants.MOBILE), etElectricityNumber.text.toString(), etAmountElect.text.toString(),
                    operator_code, "RETAILER", etElectricityBillingUnit.text.toString(), circle_code
                )
                //toast("Same Recharge")
            } else {
                progress_bar.visibility = View.INVISIBLE
            }
        }
        if (flag.equals(RECHARGE_API)) {
            Log.e("RECHARGE_API", result)
            val jsonObject = JSONObject(result)
            val status = jsonObject.getString(AppConstants.STATUS)
            Log.e(AppConstants.STATUS, status)
            //Log.e(AppConstants.MESSAGE_CODE, messageCode);
            if (status.contains("true")) {
                progress_bar.visibility = View.INVISIBLE

                val response = jsonObject.getJSONObject("result")
                tts!!.speak(jsonObject.getString("message"), TextToSpeech.QUEUE_FLUSH, null,"")

                showSuccessRechargeDialog()


            } else {
                progress_bar.visibility = View.INVISIBLE
                tts!!.speak(jsonObject.getString("message"), TextToSpeech.QUEUE_FLUSH, null,"")

                toast(jsonObject.getString("message"))
                showFailedRechargeDialog()
            }
        }
        if (flag.equals(GET_BILL_DETAILS)) {
            Log.e("GET_BILL_DETAILS", result)
            val jsonObject = JSONObject(result)
            val status = jsonObject.getString(AppConstants.STATUS)
            Log.e(AppConstants.STATUS, status)
            if (status.contains("true")) {
                progress_bar.visibility = View.INVISIBLE
                val resultObject = jsonObject.getJSONObject("result")
                val status = resultObject.getJSONArray("records")

                for(i in 0 until status.length()) {
                    val notifyObjJson = status.getJSONObject(i)
                    showElectirictyDialog(
                        resultObject.getString("tel"),
                        notifyObjJson.getString("CustomerName"),
                        notifyObjJson.getString("Billamount"),
                        notifyObjJson.getString("Billdate"),
                        notifyObjJson.getString("Duedate")
                    )
                }

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
                ShowBottomSheet()
            } else {
                //progress_bar_state.visibility = View.INVISIBLE
            }
        }


        if (flag.equals(CONFIRMPIN_API)) {
            Log.e("CONFIRMPIN_API", result)
            val jsonObject = JSONObject(result)
            val status = jsonObject.getString(AppConstants.STATUS)
            val messageCode = jsonObject.getString(AppConstants.MESSAGE)
            Log.e(AppConstants.STATUS, status)
            Log.e(AppConstants.MESSAGE, messageCode);
            if (status.contains("true")) {
                progress_bar.visibility = View.INVISIBLE

                checkIfSameRecharge(
                    Preferences.getString(AppConstants.MOBILE), etElectricityNumber.text.toString(), etAmountElect.text.toString(),
                    operator_code
                )
                //dialog.dismiss()
            } else {
                progress_bar.visibility = View.INVISIBLE
                toast(messageCode)
                //z dialog.dismiss()

            }
        }
    }

    //////////////////////////////////////////////


    /*private fun showFailedRechargeDialog() {
        val builder1 =
            AlertDialog.Builder(this)
        builder1.setTitle("Attention!")
        builder1.setMessage("Recharge Failed")
        builder1.setCancelable(true)
        builder1.setPositiveButton(
            "OK"
        ) { dialog, id ->
            etElectricityNumber.setText("")
            etAmountElect.setText("")
            //ivMobileOperatorImg.setImageResource(R.drawable.ic_network_check_black_24dp)
            tvOperatorNameRec.setText("")

            dialog.cancel()
            val intent = Intent(this, AllRechargeReportsActivity::class.java)
            startActivity(intent)
            finish()
        }
        *//*
        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });*//*
        val alert11 = builder1.create()
        alert11.show()
    }*/

    private fun showFailedRechargeDialog() {
        val builder1 =
            AlertDialog.Builder(this)
        builder1.setTitle("Attention!")
        builder1.setMessage("Recharge Failed")
        builder1.setCancelable(true)
        builder1.setPositiveButton(
            "OK"
        ) { dialog, id ->
            etElectricityNumber.setText("")
            etAmountElect.setText("")
            //ivMobileOperatorImg.setImageResource(R.drawable.ic_network_check_black_24dp)
            tvOperatorNameRec.setText("")

            dialog.cancel()
            val intent = Intent(this, AllRechargeReportsActivity::class.java)
            startActivity(intent)
            finish()
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

    lateinit var tvConsumerNumber:TextView
    lateinit var tvConsumerName:TextView
    lateinit var tvConsumerBillAmnt:TextView
    lateinit var tvConsumerBillDate:TextView
    lateinit var tvConsumerDueDate:TextView
    lateinit var btnPay:TextView
    lateinit var ivCloseTab:ImageView
    private fun showElectirictyDialog(
        tel: String?,
        customerName: String?,
        billAmount: String?,
        billDate: String?,
        dueDate: String?
    ) {
        val dialog = Dialog(this, R.style.SheetDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_dialog_electricitybill)
        tvConsumerNumber=dialog.findViewById(R.id.tvConsumerNumber)
        tvConsumerName=dialog.findViewById(R.id.tvConsumerName)
        tvConsumerBillAmnt=dialog.findViewById(R.id.tvConsumerBillAmnt)
        tvConsumerBillDate=dialog.findViewById(R.id.tvConsumerBillDate)
        tvConsumerDueDate=dialog.findViewById(R.id.tvConsumerDueDate)
        btnPay=dialog.findViewById(R.id.btnPay)
        ivCloseTab=dialog.findViewById(R.id.ivCloseTab)

        tvConsumerNumber.setText("Consumer Number : " + tel)
        tvConsumerName.setText("Customer Name : " + customerName)
        tvConsumerBillAmnt.setText("Bill Amount : " + billAmount)
        tvConsumerBillDate.setText("Bill Date: "+billDate)
        tvConsumerDueDate.setText("Due Date: "+dueDate)

        btnPay.setOnClickListener {
            etAmountElect.setText(billAmount)
            dialog.dismiss()
        }

        ivCloseTab.setOnClickListener {
            dialog.dismiss()
        }


        dialog.show()

    }

    fun showFailedBillFetch() {
        val builder1 =
            AlertDialog.Builder(this)
        builder1.setTitle("Bill Fetch Failed!")
        builder1.setMessage("Enter Valid Consumer Number and Billing Unit")
        builder1.setCancelable(true)
        builder1.setPositiveButton(
            "OK"
        ) { dialog, id ->
            dialog.cancel()
        }
        val alert11 = builder1.create()
        alert11.show()
    }

    private fun showConfirmPaymentDialog() {
        val builder1 =
            AlertDialog.Builder(this)
        builder1.setTitle("Attention!")
        builder1.setMessage("Do you want to Proceed Recharge for ${etElectricityNumber.text.toString()}?")
        builder1.setCancelable(true)
        builder1.setPositiveButton(
            "OK"
        ) { dialog, id ->
            //confirmPin()
            verifyPin(
                Preferences.getString(AppConstants.MOBILE),
                AppPrefs.getStringPref("AppPassword",this).toString()
            )
            dialog.cancel()
        }
        builder1.setNegativeButton(
            "CANCEL"
        ) { dialog, id ->
            dialog.cancel()
        }
        val alert11 = builder1.create()
        alert11.show()
    }

    private fun showSuccessRechargeDialog() {
        val builder1 =
            AlertDialog.Builder(this)
        builder1.setTitle("Recharge Submitted!")
        builder1.setMessage("Recharge Request Submitted")
        builder1.setCancelable(false)
        builder1.setPositiveButton(
            "OK"
        ) { dialog, id ->
            etElectricityNumber.setText("")
            etAmountElect.setText("")
            //ivMobileOperatorImg.setImageResource(R.drawable.ic_network_check_black_24dp)
            tvOperatorNameRec.setText("")
            val intent = Intent(this, AllRechargeReportsActivity::class.java)
            startActivity(intent)
            finish()

            /*

               showScratchCard()*/
            dialog.cancel()
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


    lateinit var rvspinner:RecyclerView
    private fun ShowBottomSheet() {
        val view: View = layoutInflater.inflate(R.layout.layout_list_bottomsheet, null)
        rvspinner=view.findViewById(R.id.rvspinner)
        rvspinner.apply {

            layoutManager = LinearLayoutManager(this@ElectricityRechargeActivity)
            operatorAdapter = OperatorListAdapter(
                context, mobileRechargeModalArrayList, this@ElectricityRechargeActivity
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


    lateinit var rvspinnerState:RecyclerView
    private fun ShowBottomSheetState() {
        val view: View = layoutInflater.inflate(R.layout.layout_list_bottomsheet, null)
        rvspinnerState=view.findViewById(R.id.rvspinner)
        rvspinnerState.apply {

            layoutManager = LinearLayoutManager(this@ElectricityRechargeActivity)
            circleListAdapter = CircleListAdapter(
                context, circleListModelArrayList, this@ElectricityRechargeActivity
            )
            rvspinnerState.adapter = circleListAdapter
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
            tvOperatorNameRec.setText(operatorsModel.operatorname)
            operator_code = operatorsModel.opcodenew
            opName = operatorsModel.operatorname
            qr_opcode = operatorsModel.qr_opcode
            bottomSheetDialog!!.dismiss()
        }
    }

  /*  private fun showElectirictyDialog(
        tel: String?,
        operator: String?,
        customerName: String?,
        billAmount: String?,
        billDate: String?,
        dueDate: String?
    ) {
        val dialog = Dialog(this, R.style.SheetDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_dialog_electricitybill)


        dialog.tvConsumerNumber.setText("Consumer Number : " + tel)
        dialog.tvConsumerOperator.setText("Electricity Board : " + operator)
        dialog.tvConsumerName.setText("Customer Name : " + customerName)
        dialog.tvConsumerBillAmnt.setText("Bill Amount : " + billAmount)
        dialog.tvConsumerBillDate.setText("Bill Date : " + billDate)
        dialog.tvConsumerDueDate.setText("Due Date : " + dueDate)

        dialog.btnPay.setOnClickListener {
            etAmountElect.setText(billAmount)
            dialog.dismiss()
        }

        dialog.ivCloseTab.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }*/

    fun getDeviceName(): String? {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer)) {
            model
        } else {
            manufacturer.toString() + " " + model
        }
    }

 /*   fun confirmPin() {
        dialog = Dialog(this, R.style.ThemeOverlay_MaterialComponents_Dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_dialog_confirmpin)

        dialog.etPin.requestFocus()
        dialog.tvDialogCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.getWindow()!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.etPin.setText(AppPrefs.getStringPref("AppPassword",this).toString())

        dialog.tvConfirmPin.setOnClickListener {
            if (dialog.etPin.text.toString().isEmpty()) {
                dialog.etPin.requestFocus()
                dialog.etPin.setError("Please Enter Pin")
            } else {
                verifyPin(
                    userModel.cus_mobile,
                    AppPrefs.getStringPref("AppPassword",this).toString()
                )
                dialog.dismiss()

            }
        }
        dialog.show()
    }*/

    override fun onClickAtOKButton(circleListModel: CircleListModel?) {
        if (circleListModel != null) {
            tvSelectState.setText(circleListModel.state)
            circleId = circleListModel.id
            circle_code = circleListModel.code
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


    private fun callServiceFetchBill(cn: String,op:String) {
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
        val call = apiService.callGetBillService( cn,op)


        //making the call to generate checksum
        call.enqueue(object : Callback<MainFetchBillResponse> {
            override fun onResponse(
                call: Call<MainFetchBillResponse>,
                response: Response<MainFetchBillResponse>
            ) {
                progress_bar.visibility = View.GONE
                if (response.body()!!.getSuccess() == true) {

                    var connectionProfile: ConnectionProfile= response.body()!!.getFetchBillResponse()!!.getConnectionProfile()!!
                    var paymentDetails: ArrayList<PaymentItemDetail> = (response.body()!!.getFetchBillResponse()!!.getPaymentItemDetails() as ArrayList<PaymentItemDetail>?)!!
                        showElectirictyDialog(
                            connectionProfile.getLineItemList()!!.get(0)!!.getValue(),
                            connectionProfile.getLineItemList()!!.get(1)!!.getValue(),
                            paymentDetails.get(0).getLineItemList()!!.get(2)!!.getValue(),
                            paymentDetails.get(0).getLineItemList()!!.get(0)!!.getValue(),
                            paymentDetails.get(0).getLineItemList()!!.get(0)!!.getValue(),
                        )


                } else {
                    Toast.makeText(
                        this@ElectricityRechargeActivity,
                        "Something Went Wrong",
                        Toast.LENGTH_SHORT
                    ).show()

                }


                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter
            }

            override fun onFailure(call: Call<MainFetchBillResponse>, t: Throwable) {
                progress_bar.visibility = View.GONE
                // callServiceFalse(mobileNo);
                Toast.makeText(this@ElectricityRechargeActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
    }