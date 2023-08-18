package com.spindia.rechargeapp.activities_upi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.spindia.rechargeapp.R
import com.spindia.rechargeapp.NewMainActivity

import com.spindia.rechargeapp.model.UserModel
import com.spindia.rechargeapp.utils.AppPrefs

class PaymentSuccess : AppCompatActivity() {

    lateinit var transactionId: String
    lateinit var responseCode: String
    lateinit var approvalRefNo: String
    lateinit var txnRef: String
    lateinit var amount: String
    val UPDATE_BALANCE = "UPDATE_BALANCE"
    lateinit var userModel: UserModel
    lateinit var ivBackBtn:ImageView
    lateinit var tvRepeatBtn:TextView
    lateinit var tvAddedAmount:TextView
    lateinit var tvTransactionId:TextView
    lateinit var tvTransactionRefId:TextView
    lateinit var rl_money:RelativeLayout
    lateinit var ivSucces:ImageView
    lateinit var tvMessage:TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_success)

        val gson = Gson()
        val json = AppPrefs.getStringPref("userModel", this)
        userModel = gson.fromJson(json, UserModel::class.java)

        ivBackBtn=findViewById(R.id.ivBackBtn)
        tvRepeatBtn=findViewById(R.id.tvRepeatBtn)
        tvAddedAmount=findViewById(R.id.tvAddedAmount)
        tvMessage=findViewById(R.id.tvMessage)
        tvTransactionId=findViewById(R.id.tvTransactionId)
        tvTransactionRefId=findViewById(R.id.tvTransactionRefId)
        rl_money=findViewById(R.id.rl_money)
        ivSucces=findViewById(R.id.ivSucces)

        ivBackBtn.setOnClickListener {
            val intent = Intent(this, NewMainActivity::class.java)
            startActivity(intent)
            finish()
        }

        tvRepeatBtn.setOnClickListener {
            onBackPressed()
        }
        transactionId = intent.extras!!.getString("transactionId").toString()
        responseCode = intent.extras!!.getString("responseCode").toString()
        approvalRefNo = intent.extras!!.getString("approvalRefNo").toString()
        txnRef = intent.extras!!.getString("txnRef").toString()
        amount = intent.extras!!.getString("amount").toString()

        Log.e("transactionId", transactionId)
        Log.e("responseCode ", responseCode)
        Log.e("approvalRefNo", approvalRefNo)
        Log.e("txnRef       ", txnRef)



        if (intent.extras!!.getInt("status") == 1) {
            tvMessage.text = "Payment Successful"
            tvAddedAmount.text = "₹" + amount
            tvTransactionId.text = transactionId
            tvTransactionRefId.text = txnRef
            rl_money.setBackgroundColor(resources.getColor(R.color.green))

            ivSucces.setImageResource(R.drawable.ic_success)

        } else if (intent.extras!!.getInt("status") == 2) {
            tvMessage.text = "Payment Pending"
            rl_money.setBackgroundColor(resources.getColor(R.color.amber))
            tvAddedAmount.text = "₹" + amount
            tvTransactionId.text = transactionId
            tvTransactionRefId.text = txnRef
            ivSucces.setImageResource(R.drawable.ic_pending)

        } else {
            tvMessage.text = "Payment Failed"
            tvAddedAmount.text = "₹" + amount
            rl_money.setBackgroundColor(resources.getColor(R.color.material_red_700))
            tvTransactionId.text = transactionId
            tvTransactionRefId.text = txnRef
            ivSucces.setImageResource(R.drawable.ic_failed)

        }

    }


}