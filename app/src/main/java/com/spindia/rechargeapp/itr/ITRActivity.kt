package com.spindia.rechargeapp.itr

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.spindia.rechargeapp.NewMainActivity
import com.spindia.rechargeapp.R
import com.spindia.rechargeapp.authentication.response.WalletResponse
import com.spindia.rechargeapp.network.Preferences
import com.spindia.rechargeapp.pancardOffline.BasePanResponse
import com.spindia.rechargeapp.utils.AppConstants
import com.spindia.rechargeapp.utils.MainIAPI
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ITRActivity : AppCompatActivity() {

    lateinit var edPANNo:EditText
    lateinit var spinner_itryear:Spinner
    lateinit var edFirstName:EditText
    lateinit var edMiddleName:EditText
    lateinit var edLastName:EditText
    lateinit var edFatherName:EditText
    lateinit var edMobNo:EditText
    lateinit var edEmail:EditText
    lateinit var edAdharNo:EditText
    lateinit var tvDOB:TextView
    lateinit var spinner_gender:Spinner
    lateinit var edFullAddress:EditText
    lateinit var edDistrict:EditText
    lateinit var edState:EditText
    lateinit var edCity:EditText
    lateinit var edPincode:EditText
    lateinit var spinner_itrCategory:Spinner
    lateinit var spinner_itrBusiCategory:Spinner
    lateinit var edBankName:EditText
    lateinit var edCustomerName:EditText
    lateinit var edAccountNo:EditText
    lateinit var edConfirmAccountNo:EditText
    lateinit var edIFSCCode:EditText
    lateinit var edBranch:EditText
    lateinit var edAccounType:EditText

    lateinit var btnSubmit:Button
    lateinit var progress_bar:RelativeLayout

    var mCurrentDate: Calendar? = null
    var day = 0
    var month = 0
    var year= 0
    var dayToday = 0
    var monthToday = 0
    var yearToday = 0
    var selecteddate = 0
    var selectedmonth = 0
    var selectedyear = 0
    var date: Date? = null
    var sendDate = ""
    
    var selectedYear="Select Year"
    var gender="Male"
    var selectedItrCategory="Select category"
    var selecteditrBusiCategory="Select Business Category /(व्यापार वर्ग)"
    var walletBalance="0"
    var amount="10"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_itr)

        edPANNo=findViewById(R.id.edPANNo)
        spinner_itryear=findViewById(R.id.spinner_itryear)
        edFirstName=findViewById(R.id.edFirstName)
        edMiddleName=findViewById(R.id.edMiddleName)
        edLastName=findViewById(R.id.edLastName)
        edFatherName=findViewById(R.id.edFatherName)
        edMobNo=findViewById(R.id.edMobNo)
        edEmail=findViewById(R.id.edEmail)
        edAdharNo=findViewById(R.id.edAdharNo)
        tvDOB=findViewById(R.id.tvDOB)
        spinner_gender=findViewById(R.id.spinner_gender)
        edFullAddress=findViewById(R.id.edFullAddress)
        edDistrict=findViewById(R.id.edDistrict)
        edState=findViewById(R.id.edState)
        edCity=findViewById(R.id.edCity)
        edPincode=findViewById(R.id.edPincode)
        spinner_itrCategory=findViewById(R.id.spinner_itrCategory)
        spinner_itrBusiCategory=findViewById(R.id.spinner_itrBusiCategory)
        edBankName=findViewById(R.id.edBankName)
        edCustomerName=findViewById(R.id.edCustomerName)
        edAccountNo=findViewById(R.id.edAccountNo)
        edConfirmAccountNo=findViewById(R.id.edConfirmAccountNo)
        edIFSCCode=findViewById(R.id.edIFSCCode)
        edBranch=findViewById(R.id.edBranch)
        edAccounType=findViewById(R.id.edAccounType)
        btnSubmit=findViewById(R.id.btnSubmit)
        progress_bar = findViewById<RelativeLayout>(R.id.progress_bar)

        initViews()
    }

    fun initViews() {
        callServiceGetWalletBalance()

        val itrYearArray = resources.getStringArray(R.array.itrYear)
        try {
            val adapter: ArrayAdapter<String>
            adapter = ArrayAdapter(
                this,
                R.layout.spinneritem,
                resources.getStringArray(R.array.itrYear)
            )
            adapter.setDropDownViewResource(R.layout.row_simple_spinner_dropdown_item)
            //setting adapter to spinner
            spinner_itryear.setAdapter(adapter)
            spinner_itryear.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View,
                    i: Int,
                    l: Long
                ) {
                    selectedYear=itrYearArray.get(i)
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            })
        } catch (e: Exception) {
        }



        val genderArray = resources.getStringArray(R.array.gender)
        try {
            val adapter: ArrayAdapter<String>
            adapter = ArrayAdapter(
                this,
                R.layout.spinneritem,
                resources.getStringArray(R.array.gender)
            )
            adapter.setDropDownViewResource(R.layout.row_simple_spinner_dropdown_item)
            //setting adapter to spinner
            spinner_gender.setAdapter(adapter)
            spinner_gender.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View,
                    i: Int,
                    l: Long
                ) {
                    gender=genderArray.get(i)
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            })
        } catch (e: java.lang.Exception) {
        }


        val itrCategoryArray = resources.getStringArray(R.array.itrCategory)
        try {
            val adapter: ArrayAdapter<String>
            adapter = ArrayAdapter(
                this,
                R.layout.spinneritem,
                resources.getStringArray(R.array.itrCategory)
            )
            adapter.setDropDownViewResource(R.layout.row_simple_spinner_dropdown_item)
            //setting adapter to spinner
            spinner_itrCategory.setAdapter(adapter)
            spinner_itrCategory.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View,
                    i: Int,
                    l: Long
                ) {
                    selectedItrCategory=itrCategoryArray.get(i)
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            })
        } catch (e: java.lang.Exception) {
        }

        val itrBusiCategoryArray = resources.getStringArray(R.array.itrBusiCategory)
        try {
            val adapter: ArrayAdapter<String>
            adapter = ArrayAdapter(
                this,
                R.layout.spinneritem,
                resources.getStringArray(R.array.itrBusiCategory)
            )
            adapter.setDropDownViewResource(R.layout.row_simple_spinner_dropdown_item)
            //setting adapter to spinner
            spinner_itrBusiCategory.setAdapter(adapter)
            spinner_itrBusiCategory.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View,
                    i: Int,
                    l: Long
                ) {
                    selecteditrBusiCategory=itrBusiCategoryArray.get(i)
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            })
        } catch (e: java.lang.Exception) {
        }




        tvDOB.setOnClickListener(View.OnClickListener {
            try {
                val min = Calendar.getInstance()
                min.add(Calendar.MONTH, -5)
                mCurrentDate = Calendar.getInstance()
                day = mCurrentDate!!.get(Calendar.DAY_OF_MONTH)
                month = mCurrentDate!!.get(Calendar.MONTH)
                year = mCurrentDate!!.get(Calendar.YEAR)
                val datePickerDialog1 = DatePickerDialog(this@ITRActivity,
                    { datePicker, i, i1, i2 ->
                        selecteddate = i
                        selectedmonth = i1 + 1
                        selectedyear = i2
                        sendDate =
                            selectedyear.toString() + "/" + selectedmonth + "/" + selecteddate

                        @SuppressLint("SimpleDateFormat") val srcDf: DateFormat =
                            SimpleDateFormat("dd/MM/yyyy")
                        try {
                            date = srcDf.parse(sendDate)
                            val dateSet = srcDf.format(date)
                            tvDOB.setText(dateSet)
                        } catch (e: ParseException) {
                            e.printStackTrace()
                        }
                    }, year, month, day
                )
                datePickerDialog1.datePicker.maxDate = System.currentTimeMillis() + 1
                datePickerDialog1.show()
            } catch (e: java.lang.Exception) {
            }
        })





        btnSubmit.setOnClickListener {


            if (edPANNo.getText().toString().isEmpty()) {
                Toast.makeText(this@ITRActivity, "Please enter PAN no", Toast.LENGTH_SHORT).show()
            } else if (selectedYear.equals("Select Year")) {
                Toast.makeText(this@ITRActivity, "Please select ITR year", Toast.LENGTH_SHORT)
                    .show()
            } else if (edFirstName.getText().toString().isEmpty()) {
                Toast.makeText(this@ITRActivity,"Please enter first name", Toast.LENGTH_SHORT).show()

            } else if (edLastName.getText().toString().isEmpty()) {
                Toast.makeText(this@ITRActivity, "Please enter Last name", Toast.LENGTH_SHORT).show()

            }else if (edFatherName.getText().toString().isEmpty()) {
                Toast.makeText(this@ITRActivity, "Please enter fathe name", Toast.LENGTH_SHORT).show()

            } else if (edMobNo.getText().toString().isEmpty()) {
                Toast.makeText(this@ITRActivity, "Please enter mobile no", Toast.LENGTH_SHORT).show()

            }else if (edEmail.getText().toString().isEmpty()) {
                Toast.makeText(this@ITRActivity, "Please enter email id", Toast.LENGTH_SHORT).show()

            }else if (edAdharNo.getText().toString().isEmpty()) {
                Toast.makeText(this@ITRActivity, "Please enter aadhar no", Toast.LENGTH_SHORT).show()

            } else if (tvDOB.text.toString().isEmpty()) {
                Toast.makeText(this@ITRActivity, "Please select DOB", Toast.LENGTH_SHORT).show()
            }else if (edFullAddress.text.toString().isEmpty()) {
                Toast.makeText(this@ITRActivity, "Please enter address", Toast.LENGTH_SHORT).show()

            }else if (edDistrict.text.toString().isEmpty()) {
                Toast.makeText(this@ITRActivity, "Please enter district", Toast.LENGTH_SHORT).show()

            }else if (edState.text.toString().isEmpty()) {
                Toast.makeText(this@ITRActivity, "Please enter state", Toast.LENGTH_SHORT).show()

            }else if (edCity.text.toString().isEmpty()) {
                Toast.makeText(this@ITRActivity, "Please enter city", Toast.LENGTH_SHORT).show()

            }else if (edPincode.text.toString().isEmpty()) {
                Toast.makeText(this@ITRActivity, "Please enter pincode", Toast.LENGTH_SHORT).show()

            }else if (selectedItrCategory.equals("Select category")) {
                Toast.makeText(this@ITRActivity, "Please select ITR category", Toast.LENGTH_SHORT)
                    .show()
            }else if (selecteditrBusiCategory.equals("Select Business Category /(व्यापार वर्ग)")) {
                Toast.makeText(this@ITRActivity, "Please select ITR business category", Toast.LENGTH_SHORT)
                    .show()
            }else if (edBankName.text.toString().isEmpty()) {
                Toast.makeText(this@ITRActivity, "Please enter bank name", Toast.LENGTH_SHORT).show()

            }else if (edCustomerName.text.toString().isEmpty()) {
                Toast.makeText(this@ITRActivity, "Please enter customer name", Toast.LENGTH_SHORT).show()

            }else if (edAccountNo.text.toString().isEmpty()) {
                Toast.makeText(this@ITRActivity, "Please enter account number", Toast.LENGTH_SHORT).show()

            }else if (edConfirmAccountNo.text.toString().isEmpty()) {
                Toast.makeText(this@ITRActivity, "Please enter confirm account number", Toast.LENGTH_SHORT).show()

            }else if (!edConfirmAccountNo.text.toString().equals(edAccountNo.text.toString())) {
                Toast.makeText(this@ITRActivity, "Account number and confirm account number should be same", Toast.LENGTH_SHORT).show()

            }else if (edIFSCCode.text.toString().isEmpty()) {
                Toast.makeText(this@ITRActivity, "Please enter IFSC code", Toast.LENGTH_SHORT).show()

            }else if (edBranch.text.toString().isEmpty()) {
                Toast.makeText(this@ITRActivity, "Please enter branch name", Toast.LENGTH_SHORT).show()

            }else if (edAccounType.text.toString().isEmpty()) {
                Toast.makeText(this@ITRActivity, "Please enter account type", Toast.LENGTH_SHORT).show()

            }else{

                if (walletBalance.toDouble() >= amount.toDouble())
                {
                    callServiceSave()

                }else{
                    Toast.makeText(this@ITRActivity,"Insufficient Balance",Toast.LENGTH_LONG).show()
                }


            }
        }

    }


    private fun callServiceSave() {
        progress_bar.setVisibility(View.VISIBLE)
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


        val obj = JSONObject()

        try {
            obj.put("panNo", edPANNo.text.toString())
            obj.put("ITRYear", selectedYear)
            obj.put("firstName", edFirstName.text.toString())
            obj.put("middleName", edMiddleName.text.toString())
            obj.put("lastName",edLastName.text.toString())
            obj.put("fatherName", edFatherName.text.toString())
            obj.put("mobileNo", edMobNo.text.toString())
            obj.put("emailId", edEmail.text.toString())
            obj.put("aadharNo", edAdharNo.text.toString())
            obj.put("dob", tvDOB.text.toString())
            obj.put("gender", gender)
            obj.put("fullAddress", edFullAddress.text.toString())
            obj.put("district", edDistrict.text.toString())
            obj.put("state", edState.text.toString())
            obj.put("city", edCity.text.toString())
            obj.put("pincode", edPincode.text.toString())
            obj.put("itrCategory", selectedItrCategory)
            obj.put("itrBusinessCategory", selecteditrBusiCategory)
            obj.put("bankName", edBankName.text.toString())
            obj.put("customerName", edCustomerName.text.toString())
            obj.put("accountNumber", edAccountNo.text.toString())
            obj.put("ifscCode", edIFSCCode.text.toString())
            obj.put("branchName", edBranch.text.toString())
            obj.put("accountType", edAccounType.text.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }




        //creating the retrofit api service
        val apiService = retrofit.create(MainIAPI::class.java)


        val rtid: RequestBody = createPartFromString(Preferences.getString(AppConstants.MOBILE))

        val formtype: RequestBody = createPartFromString("itr")
        val data: RequestBody = createPartFromString(obj.toString())


        //Call<ScannerResponse> call = apiService.saveScan(orderId1,vpa1,name1,amount1,mon_no1,member_id1,password1);
        val call = apiService.gstItrSave(
            rtid,
            formtype,
            data
        )


        //making the call to generate checksum


        //making the call to generate checksum
        call.enqueue(object : Callback<BasePanResponse> {
            override fun onResponse(
                call: Call<BasePanResponse>,
                response: Response<BasePanResponse>
            ) {
                progress_bar.setVisibility(View.GONE)
                if (response.body()!!.status == true) {

                    Toast.makeText(
                        this@ITRActivity,
                        response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@ITRActivity, NewMainActivity::class.java)
                    //   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@ITRActivity,
                        response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@ITRActivity, NewMainActivity::class.java)
                    //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent)
                }


                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter
            }

            override fun onFailure(call: Call<BasePanResponse>, t: Throwable) {
                progress_bar.setVisibility(View.GONE)
                // callServiceFalse(mobileNo);
                Toast.makeText(this@ITRActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }



    private fun createPartFromString(descriptionString: String): RequestBody {
        return RequestBody.create(
            MultipartBody.FORM, descriptionString
        )
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
                        this@ITRActivity,
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
                Toast.makeText(this@ITRActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }


}