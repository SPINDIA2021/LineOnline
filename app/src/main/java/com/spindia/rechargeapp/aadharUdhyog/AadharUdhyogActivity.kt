package com.spindia.rechargeapp.aadharUdhyog

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.spindia.rechargeapp.NewMainActivity
import com.spindia.rechargeapp.R
import com.spindia.rechargeapp.network.Preferences
import com.spindia.rechargeapp.pancardOffline.BasePanResponse
import com.spindia.rechargeapp.utils.AppConstants
import com.spindia.rechargeapp.utils.MainIAPI
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import okhttp3.MediaType
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
import java.io.IOException
import java.util.concurrent.TimeUnit

class AadharUdhyogActivity: AppCompatActivity() {

    lateinit var edName:EditText
    lateinit var edEmail:EditText
    lateinit var edMobNo:EditText
    lateinit var edOfficeName:EditText
    lateinit var edPANNo:EditText
    lateinit var edAddress:EditText

    var rgAadhar: RadioGroup? = null
    var rbSingle: RadioButton? = null
    var rbDouble: RadioButton? = null

    lateinit var imgAdharFront: ImageView
    lateinit var imgAdharBack: ImageView

    var adharFrontArrayList = java.util.ArrayList<Uri>()
    var adharBackArrayList = java.util.ArrayList<Uri>()

    var adharFilesFront = java.util.ArrayList<File>()
    var adharFilesBack = java.util.ArrayList<File>()

    var adharType = "S"
    lateinit var selectType: String

    lateinit var btnSubmit:Button
    lateinit var progress_bar:RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aadhar_udhyog)

        edName=findViewById(R.id.edName)
        edEmail=findViewById(R.id.edEmail)
        edMobNo=findViewById(R.id.edMobNo)
        edOfficeName=findViewById(R.id.edOfficeName)
        edPANNo=findViewById(R.id.edPANNo)
        edAddress=findViewById(R.id.edAddress)

        rgAadhar = findViewById(R.id.rg_adhar)
        rbSingle = findViewById(R.id.rb_single)
        rbDouble = findViewById(R.id.rb_double)

        imgAdharFront = findViewById(R.id.imgAdharFront)
        imgAdharBack = findViewById(R.id.imgAdharBack)

        btnSubmit=findViewById(R.id.btnSubmit)
        progress_bar = findViewById<RelativeLayout>(R.id.progress_bar)

        initViews()
    }

    fun initViews() {


        rbSingle!!.setOnClickListener {
            adharType = "S"
            imgAdharBack.visibility = View.GONE
        }

        rbDouble!!.setOnClickListener {
            adharType = "D"
            imgAdharBack.visibility = View.VISIBLE
        }


        imgAdharFront.setOnClickListener {
            selectType = "adharFront"
            adharFrontArrayList = java.util.ArrayList()
            CropImage.activity(null)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this@AadharUdhyogActivity)
        }

        imgAdharBack.setOnClickListener {
            selectType = "adharBack"
            adharBackArrayList = java.util.ArrayList()
            CropImage.activity(null)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this@AadharUdhyogActivity)
        }


        btnSubmit.setOnClickListener {

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


            if (edName.getText().toString().isEmpty()) {
                Toast.makeText(this@AadharUdhyogActivity, "Please enter name", Toast.LENGTH_SHORT).show()
            } else if (edEmail.getText().toString().isEmpty()) {
                Toast.makeText(this@AadharUdhyogActivity, "Please enter email id", Toast.LENGTH_SHORT)
                    .show()
            } else if (edMobNo.getText().toString().isEmpty()) {
                Toast.makeText(this@AadharUdhyogActivity, "Please enter mobile no", Toast.LENGTH_SHORT).show()

            }else if (edOfficeName.getText().toString().isEmpty()) {
                Toast.makeText(this@AadharUdhyogActivity,"Please enter shop/office name", Toast.LENGTH_SHORT).show()

            } else if (edPANNo.getText().toString().isEmpty()) {
                Toast.makeText(this@AadharUdhyogActivity, "Please enter PAN no", Toast.LENGTH_SHORT).show()

            } else if (edAddress.getText().toString().isEmpty()) {
                Toast.makeText(this@AadharUdhyogActivity, "Please enter address", Toast.LENGTH_SHORT).show()

            }else {


                if (adharFilesFront.isEmpty()) {
                    Toast.makeText(
                        this@AadharUdhyogActivity,
                        "Please add aadhar image",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (adharType === "D" && adharFilesBack.isEmpty()) {
                    Toast.makeText(
                        this@AadharUdhyogActivity,
                        "Please add back aadhar image",
                        Toast.LENGTH_LONG
                    ).show()
                }else {
                    callServiceSave()
                }
            }



            }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                var bitmap: Bitmap? = null
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, result.uri)
                    if (selectType == "adharFront") {
                        adharFrontArrayList.add(result.uri)
                        imgAdharFront.setImageBitmap(bitmap)
                    } else if (selectType == "adharBack") {
                        adharBackArrayList.add(result.uri)
                        imgAdharBack.setImageBitmap(bitmap)
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
            obj.put("name", edName.text.toString())
            obj.put("emailId", edEmail.text.toString())
            obj.put("mobileNo", edMobNo.text.toString())
            obj.put("shopName", edOfficeName.text.toString())
            obj.put("panNo",edPANNo.text.toString())
            obj.put("address", edAddress.text.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }


        //creating the retrofit api service
        val apiService = retrofit.create(MainIAPI::class.java)


        val rtid: RequestBody = createPartFromString(Preferences.getString(AppConstants.MOBILE))

        val formtype: RequestBody = createPartFromString("udhog")
        val data: RequestBody = createPartFromString(obj.toString())


        val filesParts = arrayOfNulls<MultipartBody.Part>(2)


        filesParts[0] = MultipartBody.Part.createFormData("images",adharFilesFront.get(0).getName(), RequestBody.create(
            MediaType.parse(""), adharFilesFront.get(0)))
        if (selectType.equals("D"))
        {
            filesParts[1] = MultipartBody.Part.createFormData("images",adharFilesBack.get(0).getName(), RequestBody.create(
                MediaType.parse(""), adharFilesBack.get(0)))

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
                        this@AadharUdhyogActivity,
                        response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@AadharUdhyogActivity, NewMainActivity::class.java)
                    //   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@AadharUdhyogActivity,
                        response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@AadharUdhyogActivity, NewMainActivity::class.java)
                    //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent)
                }


                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter
            }

            override fun onFailure(call: Call<BasePanResponse>, t: Throwable) {
                progress_bar.setVisibility(View.GONE)
                // callServiceFalse(mobileNo);
                Toast.makeText(this@AadharUdhyogActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun createPartFromString(descriptionString: String): RequestBody {
        return RequestBody.create(
            MultipartBody.FORM, descriptionString
        )
    }

}