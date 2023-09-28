package com.spindia.rechargeapp.gstRegstration

import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.spindia.rechargeapp.NewMainActivity
import com.spindia.rechargeapp.R
import com.spindia.rechargeapp.authentication.response.WalletResponse
import com.spindia.rechargeapp.network.Preferences
import com.spindia.rechargeapp.pancardOffline.BasePanResponse
import com.spindia.rechargeapp.pancardlist.PancardReportsActivity
import com.spindia.rechargeapp.utils.AppConstants
import com.spindia.rechargeapp.utils.MainIAPI
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

class GSTRegistrationActivity: AppCompatActivity() {

    lateinit var spinner_busitype: Spinner

    lateinit var spinner_resdAdd: Spinner

    lateinit var lay_BusiAdd:LinearLayout

    lateinit var edCompName:EditText
    lateinit var edOwnerName:EditText
    lateinit var edFatherName:EditText
    lateinit var edPANNo:EditText
    lateinit var edMobNo:EditText
    lateinit var edEmail:EditText
    lateinit var edAddressLine1:EditText
    lateinit var edAddressLine2:EditText
    lateinit var edAddressCity:EditText
    lateinit var edAddressState:EditText
    lateinit var edAddressPin:EditText
    lateinit var edbusiDesc:EditText
    lateinit var edBusiAddressLine1:EditText
    lateinit var edBusiAddressLine2:EditText
    lateinit var edBusiAddressCity:EditText
    lateinit var edBusiAddressState:EditText
    lateinit var edBusiAddressPin:EditText


    var rgAadhar: RadioGroup? = null
    var rbSingle: RadioButton? = null
    var rbDouble:RadioButton? = null

    lateinit var imgAdharFront: ImageView
    lateinit var imgAdharBack:ImageView
    lateinit var imgPancard:ImageView
    lateinit var imgGSTForm:ImageView
    lateinit var imgBill:ImageView
    lateinit var imgCheque:ImageView

    lateinit var btnSubmit:Button
    lateinit var progress_bar:RelativeLayout


    var chequeArrayList = java.util.ArrayList<Uri>()
    var adharFrontArrayList = java.util.ArrayList<Uri>()
    var adharBackArrayList = java.util.ArrayList<Uri>()
    var billArrayList = java.util.ArrayList<Uri>()
    var panArrayList = java.util.ArrayList<Uri>()
    var gstArrayList = java.util.ArrayList<Uri>()


    var chequeFiles = java.util.ArrayList<File>()
    var adharFilesFront = java.util.ArrayList<File>()
    var adharFilesBack = java.util.ArrayList<File>()
    var billFiles = java.util.ArrayList<File>()
    var panFiles = java.util.ArrayList<File>()
    var gstFiles = java.util.ArrayList<File>()

    var walletBalance="0"
    var amount="10"

    var adharType = "S"
    lateinit var selectType: String

    var busiType = "Select Type"
    var sameAsResdAdd = "Yes"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gstreg)

        edCompName=findViewById(R.id.edCompName)
        edOwnerName=findViewById(R.id.edOwnerName)
        edFatherName=findViewById(R.id.edFatherName)
        edPANNo=findViewById(R.id.edPANNo)
        edMobNo=findViewById(R.id.edMobNo)
        edEmail=findViewById(R.id.edEmail)
        edAddressLine1=findViewById(R.id.edAddressLine1)
        edAddressLine2=findViewById(R.id.edAddressLine2)
        edAddressCity=findViewById(R.id.edAddressCity)
        edAddressState=findViewById(R.id.edAddressState)
        edAddressPin=findViewById(R.id.edAddressPin)
        edbusiDesc=findViewById(R.id.edbusiDesc)
        edBusiAddressLine1=findViewById(R.id.edBusiAddressLine1)
        edBusiAddressLine2=findViewById(R.id.edBusiAddressLine2)
        edBusiAddressCity=findViewById(R.id.edBusiAddressCity)
        edBusiAddressState=findViewById(R.id.edBusiAddressState)
        edBusiAddressPin=findViewById(R.id.edBusiAddressPin)

        spinner_busitype=findViewById(R.id.spinner_busitype)
        spinner_resdAdd=findViewById(R.id.spinner_resdAdd)
        lay_BusiAdd=findViewById(R.id.lay_BusiAdd)

        rgAadhar = findViewById(R.id.rg_adhar)
        rbSingle = findViewById(R.id.rb_single)
        rbDouble = findViewById(R.id.rb_double)

        imgBill = findViewById<ImageView>(R.id.imgBill)
        imgAdharFront = findViewById(R.id.imgAdharFront)
        imgAdharBack = findViewById(R.id.imgAdharBack)
        imgPancard = findViewById<ImageView>(R.id.imgPancard)
        imgCheque = findViewById(R.id.imgCheque)
        imgGSTForm = findViewById<ImageView>(R.id.imgGstForm)
        progress_bar = findViewById<RelativeLayout>(R.id.progress_bar)

        btnSubmit=findViewById(R.id.btnSubmit)

        initViews()
    }


    fun initViews() {

        callServiceGetWalletBalance()

        rbSingle!!.setOnClickListener {
            adharType = "S"
            imgAdharBack.visibility = View.GONE
        }

       rbDouble!!.setOnClickListener {
            adharType = "D"
            imgAdharBack.visibility = View.VISIBLE
        }


        val busiTypeArray = resources.getStringArray(R.array.businessType)
        try {
            val adapter: ArrayAdapter<String>
            adapter = ArrayAdapter(
                this,
                R.layout.spinneritem,
                resources.getStringArray(R.array.businessType)
            )
            adapter.setDropDownViewResource(R.layout.row_simple_spinner_dropdown_item)
            //setting adapter to spinner
            spinner_busitype.setAdapter(adapter)
            spinner_busitype.setOnItemSelectedListener(object : OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View,
                    i: Int,
                    l: Long
                ) {
                    busiType=busiTypeArray.get(i)

                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            })
        } catch (e: Exception) {
        }


        val sameAsResAddArray = resources.getStringArray(R.array.sameAsResAdd)
        try {
            val adapter: ArrayAdapter<String>
            adapter = ArrayAdapter(
                this,
                R.layout.spinneritem,
                resources.getStringArray(R.array.sameAsResAdd)
            )
            adapter.setDropDownViewResource(R.layout.row_simple_spinner_dropdown_item)
            //setting adapter to spinner
            spinner_resdAdd.setAdapter(adapter)
            spinner_resdAdd.setOnItemSelectedListener(object : OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View,
                    i: Int,
                    l: Long
                ) {
                    sameAsResdAdd=sameAsResAddArray.get(i)
                    if (sameAsResAddArray[i] == "No") {
                        lay_BusiAdd.setVisibility(View.VISIBLE)
                    } else {
                        lay_BusiAdd.setVisibility(View.GONE)
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            })
        } catch (e: java.lang.Exception) {
        }



        imgCheque.setOnClickListener(View.OnClickListener {
            selectType = "cheque"
            chequeArrayList = ArrayList<Uri>()
            CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON)
                .start(this@GSTRegistrationActivity)
        })

        imgBill.setOnClickListener(View.OnClickListener {
            selectType = "bill"
            billArrayList = ArrayList<Uri>()
            CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON)
                .start(this@GSTRegistrationActivity)
        })

        imgGSTForm.setOnClickListener(View.OnClickListener {
            selectType = "gst"
            gstArrayList = ArrayList<Uri>()
            CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON)
                .start(this@GSTRegistrationActivity)
        })

        imgPancard.setOnClickListener(View.OnClickListener {
            selectType = "pan"
            panArrayList = ArrayList<Uri>()
            CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON)
                .start(this@GSTRegistrationActivity)
        })


        imgAdharFront.setOnClickListener {
            selectType = "adharFront"
            adharFrontArrayList = java.util.ArrayList()
            CropImage.activity(null)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this@GSTRegistrationActivity)
        }

        imgAdharBack.setOnClickListener {
            selectType = "adharBack"
            adharBackArrayList = java.util.ArrayList()
            CropImage.activity(null)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this@GSTRegistrationActivity)
        }


        btnSubmit.setOnClickListener {


            chequeFiles = java.util.ArrayList<File>()
            for (i in chequeArrayList.indices) {
                if (chequeArrayList.get(i).getPath() != null && chequeArrayList.get(i)
                        .getPath() != ""
                ) {
                    val mCurrentPhotoPath: String = chequeArrayList.get(0).getPath()!!
                    val mImageFile = File(Uri.parse(mCurrentPhotoPath).path)
                    chequeFiles.add(mImageFile)
                }
            }

            adharFilesFront = java.util.ArrayList<File>()
            for (i in adharFrontArrayList.indices) {
                if (adharFrontArrayList[i].path != null && adharFrontArrayList[i].path != "") {
                    val mCurrentPhotoPath = adharFrontArrayList[0].path
                    val mImageFile = File(Uri.parse(mCurrentPhotoPath).path)
                    adharFilesFront.add(mImageFile)
                }
            }

            adharFilesBack = java.util.ArrayList<File>()
            for (i in adharBackArrayList.indices) {
                if (adharBackArrayList[i].path != null && adharBackArrayList[i].path != "") {
                    val mCurrentPhotoPath = adharBackArrayList[0].path
                    val mImageFile = File(Uri.parse(mCurrentPhotoPath).path)
                    adharFilesBack.add(mImageFile)
                }
            }

            billFiles = java.util.ArrayList<File>()
            for (i in billArrayList.indices) {
                if (billArrayList.get(i).getPath() != null && billArrayList.get(i)
                        .getPath() != ""
                ) {
                    val mCurrentPhotoPath: String = billArrayList.get(0).getPath()!!
                    val mImageFile = File(Uri.parse(mCurrentPhotoPath).path)
                    billFiles.add(mImageFile)
                }
            }

            panFiles = java.util.ArrayList<File>()
            for (i in panArrayList.indices) {
                if (panArrayList.get(i).getPath() != null && panArrayList.get(i)
                        .getPath() != ""
                ) {
                    val mCurrentPhotoPath: String = panArrayList.get(0).getPath()!!
                    val mImageFile = File(Uri.parse(mCurrentPhotoPath).path)
                    panFiles.add(mImageFile)
                }
            }

            gstFiles = java.util.ArrayList<File>()
            for (i in gstArrayList.indices) {
                if (gstArrayList.get(i).getPath() != null && gstArrayList.get(i)
                        .getPath() != ""
                ) {
                    val mCurrentPhotoPath: String = gstArrayList.get(0).getPath()!!
                    val mImageFile = File(Uri.parse(mCurrentPhotoPath).path)
                    gstFiles.add(mImageFile)
                }
            }

            if (edCompName.getText().toString().isEmpty()) {
                Toast.makeText(this@GSTRegistrationActivity, "Please enter company name", Toast.LENGTH_SHORT).show()
            } else if (edOwnerName.getText().toString().isEmpty()) {
                Toast.makeText(this@GSTRegistrationActivity, "Please enter owner name", Toast.LENGTH_SHORT)
                    .show()
            } else if (edFatherName.getText().toString().isEmpty()) {
                Toast.makeText(this@GSTRegistrationActivity,"Please enter father name", Toast.LENGTH_SHORT).show()

            } else if (edPANNo.getText().toString().isEmpty()) {
                Toast.makeText(this@GSTRegistrationActivity, "Please enter PAN no", Toast.LENGTH_SHORT).show()

            } else if (edMobNo.getText().toString().isEmpty()) {
                Toast.makeText(this@GSTRegistrationActivity, "Please enter mobile no", Toast.LENGTH_SHORT).show()

            }else if (edEmail.getText().toString().isEmpty()) {
                Toast.makeText(this@GSTRegistrationActivity, "Please enter email id", Toast.LENGTH_SHORT).show()

            }else if (edAddressLine1.getText().toString().isEmpty()) {
                Toast.makeText(this@GSTRegistrationActivity, "Please enter address", Toast.LENGTH_SHORT).show()

            }else if (edAddressCity.getText().toString().isEmpty()) {
                Toast.makeText(this@GSTRegistrationActivity, "Please enter city", Toast.LENGTH_SHORT).show()

            }else if (edAddressState.getText().toString().isEmpty()) {
                Toast.makeText(this@GSTRegistrationActivity, "Please enter state", Toast.LENGTH_SHORT).show()

            }else if (edAddressPin.getText().toString().isEmpty()) {
                Toast.makeText(this@GSTRegistrationActivity, "Please enter pincode", Toast.LENGTH_SHORT).show()

            }else if (busiType.equals("Select Type")) {
                Toast.makeText(this@GSTRegistrationActivity, "Please select business type", Toast.LENGTH_SHORT).show()

            }else if (!sameAsResdAdd.equals("Yes")) {
                if (edBusiAddressLine1.getText().toString().isEmpty()) {
                    Toast.makeText(this@GSTRegistrationActivity, "Please enter business address", Toast.LENGTH_SHORT).show()

                }else if (edBusiAddressCity.getText().toString().isEmpty()) {
                    Toast.makeText(this@GSTRegistrationActivity, "Please enter business city", Toast.LENGTH_SHORT).show()

                }else if (edBusiAddressState.getText().toString().isEmpty()) {
                    Toast.makeText(this@GSTRegistrationActivity, "Please enter business state", Toast.LENGTH_SHORT).show()

                }else if (edBusiAddressPin.getText().toString().isEmpty()) {
                    Toast.makeText(this@GSTRegistrationActivity, "Please enter business pincode", Toast.LENGTH_SHORT).show()

                }else{


                    //Util.hideKeyBorad(PanCardActivity.this, v);
                    if (adharFilesFront.isEmpty()) {
                        Toast.makeText(
                            this@GSTRegistrationActivity,
                            "Please add aadhar image",
                            Toast.LENGTH_LONG
                        ).show()
                    } else if (adharType === "D" && adharFilesBack.isEmpty()) {
                        Toast.makeText(
                            this@GSTRegistrationActivity,
                            "Please add back aadhar image",
                            Toast.LENGTH_LONG
                        ).show()
                    } else if (chequeFiles.isEmpty()) {
                        Toast.makeText(
                            this@GSTRegistrationActivity,
                            "Please add cheque image",
                            Toast.LENGTH_LONG
                        ).show()
                    } else if (gstFiles.isEmpty()) {
                        Toast.makeText(this@GSTRegistrationActivity, "Please add gst image", Toast.LENGTH_LONG)
                            .show()
                    } else if (billFiles.isEmpty()) {
                        Toast.makeText(this@GSTRegistrationActivity, "Please add electricity image", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        if (walletBalance.toDouble() >= amount.toDouble())
                        {
                            callServiceSave()

                        }else{
                            Toast.makeText(this@GSTRegistrationActivity,"Insufficient Balance",Toast.LENGTH_LONG).show()
                        }

                    }
                }
            }else{


                if (adharFilesFront.isEmpty()) {
                    Toast.makeText(
                        this@GSTRegistrationActivity,
                        "Please add aadhar image",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (adharType === "D" && adharFilesBack.isEmpty()) {
                    Toast.makeText(
                        this@GSTRegistrationActivity,
                        "Please add back aadhar image",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (chequeFiles.isEmpty()) {
                    Toast.makeText(
                        this@GSTRegistrationActivity,
                        "Please add cheque image",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (gstFiles.isEmpty()) {
                    Toast.makeText(this@GSTRegistrationActivity, "Please add gst image", Toast.LENGTH_LONG)
                        .show()
                } else if (billFiles.isEmpty()) {
                    Toast.makeText(this@GSTRegistrationActivity, "Please add electricity image", Toast.LENGTH_LONG)
                        .show()
                } else {


                    if (walletBalance.toDouble() >= amount.toDouble())
                    {
                        callServiceSave()

                    }else{
                        Toast.makeText(this@GSTRegistrationActivity,"Insufficient Balance",Toast.LENGTH_LONG).show()
                    }


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
            obj.put("companyName", edCompName.text.toString())
            obj.put("businessOwner", edOwnerName.text.toString())
            obj.put("fatherName", edFatherName.text.toString())
            obj.put("panNo", edPANNo.text.toString())
            obj.put("mobileNo",edMobNo.text.toString())
            obj.put("email", edEmail.text.toString())
            obj.put("addressLine1", edAddressLine1.text.toString())
            obj.put("addressLine2", edAddressLine2.text.toString())
            obj.put("addressCity", edAddressCity.text.toString())
            obj.put("addressState", edAddressState.text.toString())
            obj.put("addressPin", edAddressPin.text.toString())
            obj.put("businessType", busiType)
            obj.put("businessDesc", edbusiDesc.text.toString())
            obj.put("sameAsResidenceAddress", sameAsResdAdd)
            obj.put("addressBusiLine1", edBusiAddressLine1.text.toString())
            obj.put("addressBusiLine2", edBusiAddressLine2.text.toString())
            obj.put("addressBusiCity", edBusiAddressCity.text.toString())
            obj.put("addressBusiState", edBusiAddressState.text.toString())
            obj.put("addressBusiPin", edBusiAddressPin.text.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }




        //creating the retrofit api service
        val apiService = retrofit.create(MainIAPI::class.java)


        val rtid: RequestBody = createPartFromString(Preferences.getString(AppConstants.MOBILE))

        val formtype: RequestBody = createPartFromString("gst")
        val data: RequestBody = createPartFromString(obj.toString())


        val filesParts = arrayOfNulls<MultipartBody.Part>(6)

        filesParts[0] = MultipartBody.Part.createFormData("images",gstFiles.get(0).getName(), RequestBody.create(MediaType.parse(""), gstFiles.get(0)))

        filesParts[1] = MultipartBody.Part.createFormData("images",panFiles.get(0).getName(), RequestBody.create(MediaType.parse(""), panFiles.get(0)))
        filesParts[2] = MultipartBody.Part.createFormData("images",chequeFiles.get(0).getName(), RequestBody.create(MediaType.parse(""), chequeFiles.get(0)))
        filesParts[3] = MultipartBody.Part.createFormData("images",billFiles.get(0).getName(), RequestBody.create(MediaType.parse(""), billFiles.get(0)))
        filesParts[4] = MultipartBody.Part.createFormData("images",adharFilesFront.get(0).getName(), RequestBody.create(MediaType.parse(""), adharFilesFront.get(0)))
      if (selectType.equals("D"))
      {
          filesParts[5] = MultipartBody.Part.createFormData("images",adharFilesBack.get(0).getName(), RequestBody.create(MediaType.parse(""), adharFilesBack.get(0)))

      }



        //Call<ScannerResponse> call = apiService.saveScan(orderId1,vpa1,name1,amount1,mon_no1,member_id1,password1);
        val call = apiService.gstItrUdhyogSave(
            rtid,
            formtype,
            data,
            filesParts
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
                        this@GSTRegistrationActivity,
                        response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@GSTRegistrationActivity, NewMainActivity::class.java)
                    //   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@GSTRegistrationActivity,
                        response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@GSTRegistrationActivity, NewMainActivity::class.java)
                    //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent)
                }


                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter
            }

            override fun onFailure(call: Call<BasePanResponse>, t: Throwable) {
                progress_bar.setVisibility(View.GONE)
                // callServiceFalse(mobileNo);
                Toast.makeText(this@GSTRegistrationActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                var bitmap: Bitmap? = null
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, result.uri)
                    if (selectType == "cheque") {
                        chequeArrayList.add(result.uri)
                        imgCheque.setImageBitmap(bitmap)
                    } else if (selectType == "adharFront") {
                        adharFrontArrayList.add(result.uri)
                        imgAdharFront.setImageBitmap(bitmap)
                    } else if (selectType == "adharBack") {
                        adharBackArrayList.add(result.uri)
                        imgAdharBack.setImageBitmap(bitmap)
                    } else if (selectType == "bill") {
                        billArrayList.add(result.uri)
                        imgBill.setImageBitmap(bitmap)
                    } else if (selectType == "gst") {
                        gstArrayList.add(result.uri)
                        imgGSTForm.setImageBitmap(bitmap)
                    } else if (selectType == "pan") {
                        panArrayList.add(result.uri)
                        imgPancard.setImageBitmap(bitmap)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                //   Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.error, Toast.LENGTH_LONG).show()
            }
        }
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
                        this@GSTRegistrationActivity,
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
                Toast.makeText(this@GSTRegistrationActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }


}