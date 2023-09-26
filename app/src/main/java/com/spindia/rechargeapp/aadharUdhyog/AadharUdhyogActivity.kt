package com.spindia.rechargeapp.aadharUdhyog

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.spindia.rechargeapp.R
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.IOException

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

    var adharType = "S"
    lateinit var selectType: String

    lateinit var btnSubmit:Button

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


}