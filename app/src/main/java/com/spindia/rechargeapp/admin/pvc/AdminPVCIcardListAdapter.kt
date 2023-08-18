package com.spindia.rechargeapp.admin.pvc

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.spindia.rechargeapp.R
import com.spindia.rechargeapp.model.RecentRechargeHistoryModal
import com.spindia.rechargeapp.model.UserModel
import com.spindia.rechargeapp.network.Preferences
import com.spindia.rechargeapp.network_calls.AppApiUrl.IMAGE_URL
import com.spindia.rechargeapp.utils.AppConstants
import com.spindia.rechargeapp.utils.AppPrefs
import java.util.*

class AdminPVCIcardListAdapter(
    context: Context?,
    pancardModalList: ArrayList<AdminPVCListResponse>,
    approveClick:View.OnClickListener,
    rejectClick:View.OnClickListener,
    mapClick:View.OnClickListener
) :
    RecyclerView.Adapter<AdminPVCIcardListAdapter.ViewHolder>() {
    private var pancardModalList: List<AdminPVCListResponse>
    private val mInflater: LayoutInflater
    private var mContext: Context? = null
    private var approveClick: View.OnClickListener
    private var rejectClick: View.OnClickListener
    private var mapClick: View.OnClickListener

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        mContext = parent.context

        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem: View =
            layoutInflater.inflate(R.layout.layout_admin_pvc_i_card_history, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val pancardModal: AdminPVCListResponse =
            pancardModalList[position]

        holder.text_name.setText(pancardModal.name)
       // holder.tv_paystatus.setText(pancardModal.paymentstatus)
       // holder.text_fathername.setText("Father's name: "+pancardModal.fathername)
        holder.text_ApproveStatus.setText(pancardModal.status)
        holder.text_retailerid.setText("Retailer: "+pancardModal.retailerid)
        holder.text_mobileno.setText("Mobile No: "+pancardModal.mobile)

        holder.tvToken.setText(""+pancardModal.id)
        holder.tvTxnDate.setText(pancardModal.createAt)
        holder.text_paystatus.setText("Payment Status: "+pancardModal.paymentstatus)
        val amount=40*pancardModal.totalpdfno
     //   holder.tvPayBtn.setText("PAY("+amount+")")
        holder.text_pdfcount.setText("Total PDF: "+pancardModal.totalpdfno)


        if (pancardModal.paymentstatus.equals("success"))
        {
            holder.tvApproveBtn.visibility=View.VISIBLE
            holder.tvRejectBtn.visibility=View.VISIBLE
        }else{
            holder.tvApproveBtn.visibility=View.GONE
            holder.tvRejectBtn.visibility=View.GONE
        }

        if (pancardModal.status.toString().equals("reject")) {
            holder.tvApproveBtn.visibility=View.GONE
            holder.tvRejectBtn.visibility=View.GONE
            holder.rl_statusDetails.setBackgroundResource(R.drawable.bg_status_failed)
            holder.text_ApproveStatus.text = "REJECTED"
           // holder.text_remark.setText("Remark: "+pancardModal.adminmsg)
         //   holder.text_remark.visibility=View.VISIBLE
       //     holder.tvEditBtn.visibility=View.GONE
        //    holder.tvStatusBtn.visibility=View.GONE

        } else if (pancardModal.status.toString().equals("success")) {
            holder.tvApproveBtn.visibility=View.GONE
            holder.tvRejectBtn.visibility=View.GONE
            holder.text_ApproveStatus.text = "SUCCESS"
            holder.rl_statusDetails.setBackgroundResource(R.drawable.bg_status_success)
         //   holder.text_remark.setText("Ackno No.: "+pancardModal.receiptno)
          //  holder.text_remark.visibility=View.VISIBLE
          //  holder.tvEditBtn.visibility=View.GONE
          //  holder.tvStatusBtn.visibility=View.VISIBLE


        } else if (pancardModal.status.toString().equals("pending")) {
            holder.text_ApproveStatus.text = "PENDING"
            holder.tvApproveBtn.visibility=View.VISIBLE
            holder.tvRejectBtn.visibility=View.VISIBLE
            holder.rl_statusDetails.setBackgroundResource(R.drawable.bg_status_pending)
           // holder.text_remark.visibility=View.GONE
           // holder.tvEditBtn.visibility=View.GONE
           // holder.tvStatusBtn.visibility=View.GONE

        }else if (pancardModal.status.toString().equals("Hold")) {
            holder.text_ApproveStatus.text = "HOLD"
            holder.tvApproveBtn.visibility=View.VISIBLE
            holder.tvRejectBtn.visibility=View.VISIBLE
            holder.rl_statusDetails.setBackgroundResource(R.drawable.bg_status_hold)
       //     holder.text_remark.visibility=View.GONE
           // holder.tvEditBtn.visibility=View.VISIBLE
          //  holder.tvStatusBtn.visibility=View.GONE

        }

        if (Preferences.getString(AppConstants.MOBILE).equals("9799754037"))
        {
            holder.ivMap.visibility=View.VISIBLE
        }else{
            holder.ivMap.visibility=View.GONE
        }


        holder.tvApproveBtn.setTag(position)
        holder.tvApproveBtn.setOnClickListener(approveClick)

        holder.tvRejectBtn.setTag(position)
        holder.tvRejectBtn.setOnClickListener(rejectClick)

        holder.ivMap.setTag(position)
        holder.ivMap.setOnClickListener(mapClick)


        /* if (rechargeHistoryModal.){

            holder.ivOperator.setImageResource(R.drawable.jio);

        }*/
    }

    override fun getItemCount(): Int {
        return pancardModalList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // var ivStatus: ImageView
        var tvToken:TextView
        var tvTxnDate:TextView
        var text_status: TextView
        var text_remark: TextView
        var text_paystatus: TextView
        var text_fathername: TextView
        var text_name: TextView
        var text_ApproveStatus: TextView
        var tvApproveBtn: TextView
        var rl_statusDetails: RelativeLayout
        var tvRejectBtn:TextView
        var tvStatusBtn:TextView
        var ivMap:ImageView
        var text_pdfcount:TextView
        var text_retailerid:TextView
        var text_mobileno:TextView

        init {
            tvToken=itemView.findViewById(R.id.tvToken)
            text_status = itemView.findViewById(R.id.text_status)
            text_remark = itemView.findViewById(R.id.text_remark)
            text_paystatus = itemView.findViewById(R.id.text_paystatus)
            text_fathername = itemView.findViewById(R.id.text_fathername)
            text_name = itemView.findViewById(R.id.text_name)
            tvApproveBtn = itemView.findViewById(R.id.tvApproveBtn)
            text_ApproveStatus=itemView.findViewById(R.id.text_status)
            tvTxnDate=itemView.findViewById(R.id.tvTxnDate)
            rl_statusDetails=itemView.findViewById(R.id.rl_statusDetails)
            tvRejectBtn=itemView.findViewById(R.id.tvRejectBtn)
            tvStatusBtn=itemView.findViewById(R.id.tvStatusBtn)
            ivMap=itemView.findViewById(R.id.ivMap)
            text_pdfcount=itemView.findViewById(R.id.text_pdfcount)
            text_retailerid=itemView.findViewById(R.id.text_retailerid)
            text_mobileno=itemView.findViewById(R.id.text_mobileno)

        }
    }



    // RecyclerView recyclerView;
    init {
        mInflater = LayoutInflater.from(context)
        this.pancardModalList = pancardModalList
        this.approveClick = approveClick
        this.mapClick=mapClick
        this.rejectClick=rejectClick
    }
}