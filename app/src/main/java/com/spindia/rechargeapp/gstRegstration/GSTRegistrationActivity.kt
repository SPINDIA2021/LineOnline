package com.spindia.rechargeapp.gstRegstration

import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import com.spindia.rechargeapp.R

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

        initViews()
    }


    fun initViews() {


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

    }



}