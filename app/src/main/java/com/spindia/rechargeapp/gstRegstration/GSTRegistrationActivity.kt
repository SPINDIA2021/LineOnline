package com.spindia.rechargeapp.gstRegstration

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import com.spindia.rechargeapp.R
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.IOException

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


    var chequeArrayList = java.util.ArrayList<Uri>()
    var adharFrontArrayList = java.util.ArrayList<Uri>()
    var adharBackArrayList = java.util.ArrayList<Uri>()
    var billArrayList = java.util.ArrayList<Uri>()
    var panArrayList = java.util.ArrayList<Uri>()
    var gstArrayList = java.util.ArrayList<Uri>()

    var adharType = "S"
    lateinit var selectType: String

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

        initViews()
    }


    fun initViews() {


        rbSingle!!.setOnClickListener {
            adharType = "S"
            imgAdharBack.visibility = View.GONE
        }

        /*onClicklistener for familyrb radio button click..*/

        /*onClicklistener for familyrb radio button click..*/rbDouble!!.setOnClickListener {
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



}