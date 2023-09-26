package com.spindia.rechargeapp.itr

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.spindia.rechargeapp.R

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
    lateinit var edDOB:EditText
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
        edDOB=findViewById(R.id.edDOB)
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

        initViews()
    }

    fun initViews() {


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

                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            })
        } catch (e: Exception) {
        }




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

                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            })
        } catch (e: java.lang.Exception) {
        }


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

                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            })
        } catch (e: java.lang.Exception) {
        }


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

                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            })
        } catch (e: java.lang.Exception) {
        }



    }
    }