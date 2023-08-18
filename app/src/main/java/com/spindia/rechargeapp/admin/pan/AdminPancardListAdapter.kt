package com.spindia.rechargeapp.admin.pan

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
import com.spindia.rechargeapp.network_calls.AppApiUrl.IMAGE_URL
import com.spindia.rechargeapp.utils.AppPrefs
import java.util.*

class AdminPancardListAdapter(
    context: Context?,
    pancardModalList: ArrayList<AdminPancardListResponse>,
    retryClick:View.OnClickListener,
    editClick:View.OnClickListener,
    statusClick:View.OnClickListener,
    pdfClick:View.OnClickListener
) :
    RecyclerView.Adapter<AdminPancardListAdapter.ViewHolder>() {
    private var pancardModalList: List<AdminPancardListResponse>
    private val mInflater: LayoutInflater
    private var mContext: Context? = null
    private var retryClick: View.OnClickListener
    private var editClick: View.OnClickListener
    private var statusClick: View.OnClickListener
    private var pdfClick: View.OnClickListener

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        mContext = parent.context

        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem: View =
            layoutInflater.inflate(R.layout.layout_admin_pancard_history, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val pancardModal: AdminPancardListResponse =
            pancardModalList[position]

        holder.text_name.setText(pancardModal.name)
       // holder.tv_paystatus.setText(pancardModal.paymentstatus)
        holder.text_fathername.setText("Father's name: "+pancardModal.fathername)
        holder.text_ApproveStatus.setText(pancardModal.status)

        holder.tvToken.setText(pancardModal.token)
        holder.tvTxnDate.setText(pancardModal.createat)
        holder.text_paystatus.setText("Payment Status: "+pancardModal.paymentstatus)
        holder.text_retailerid.setText("Retailer Id: "+pancardModal.memberid)


        if (pancardModal.paymentstatus.equals("success"))
        {
           // holder.tvPayBtn.visibility=View.GONE ///to be changed
        }else{
           // holder.tvPayBtn.visibility=View.VISIBLE ///to be changed
        }


        if (pancardModal.status.toString().equals("Reject")) {
            //holder.tvPayBtn.visibility = View.GONE  ///to be changed
            holder.rl_statusDetails.setBackgroundResource(R.drawable.bg_status_failed)
            holder.text_ApproveStatus.text = "REJECTED"
            holder.text_remark.setText("Remark: "+pancardModal.adminmsg)
            holder.text_remark.visibility=View.VISIBLE ///to be changed
          //  holder.tvEditBtn.visibility=View.GONE ///to be changed
           // holder.tvStatusBtn.visibility=View.GONE ///to be changed

        } else if (pancardModal.status.toString().equals("Success")) {
            holder.text_ApproveStatus.text = "SUCCESS"
            holder.rl_statusDetails.setBackgroundResource(R.drawable.bg_status_success)
            holder.text_remark.setText("Ackno No.: "+pancardModal.receiptno)
            holder.text_remark.visibility=View.VISIBLE ///to be changed
       //     holder.tvEditBtn.visibility=View.GONE  ///to be changed
       //     holder.tvStatusBtn.visibility=View.VISIBLE ///to be changed


        } else if (pancardModal.status.toString().equals("pending")) {
            holder.text_ApproveStatus.text = "PENDING"
            holder.rl_statusDetails.setBackgroundResource(R.drawable.bg_status_pending)
            holder.text_remark.visibility=View.GONE  ///to be changed
     //       holder.tvEditBtn.visibility=View.GONE  ///to be changed
      //      holder.tvStatusBtn.visibility=View.GONE  ///to be changed

        }else if (pancardModal.status.toString().equals("Hold")) {
            holder.text_ApproveStatus.text = "HOLD"
            holder.rl_statusDetails.setBackgroundResource(R.drawable.bg_status_hold)
            holder.text_remark.visibility=View.GONE  ///to be changed
       //     holder.tvEditBtn.visibility=View.VISIBLE  ///to be changed
       //     holder.tvStatusBtn.visibility=View.GONE  ///to be changed

        }

        if(pancardModal.document!=null)
        {
            holder.ivPdf.visibility=View.VISIBLE ///to be changed
        }else{
            holder.ivPdf.visibility=View.GONE  ///to be changed
        }

        holder.tvPayBtn.setTag(position)
        holder.tvPayBtn.setOnClickListener(retryClick)

        holder.tvEditBtn.setTag(position)
        holder.tvEditBtn.setOnClickListener(editClick)

        holder.tvStatusBtn.setTag(position)
        holder.tvStatusBtn.setOnClickListener(statusClick)


        holder.ivPdf.setTag(position)
        holder.ivPdf.setOnClickListener(pdfClick)

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
        var tvPayBtn: TextView
        var rl_statusDetails: RelativeLayout
        var tvEditBtn:TextView
        var tvStatusBtn:TextView
        var ivPdf:ImageView
        var text_retailerid:TextView

        init {
            tvToken=itemView.findViewById(R.id.tvToken)
            text_status = itemView.findViewById(R.id.text_status)
            text_remark = itemView.findViewById(R.id.text_remark)
            text_paystatus = itemView.findViewById(R.id.text_paystatus)
            text_fathername = itemView.findViewById(R.id.text_fathername)
            text_name = itemView.findViewById(R.id.text_name)
            tvPayBtn = itemView.findViewById(R.id.tvPayBtn)
            text_ApproveStatus=itemView.findViewById(R.id.text_status)
            tvTxnDate=itemView.findViewById(R.id.tvTxnDate)
            rl_statusDetails=itemView.findViewById(R.id.rl_statusDetails)
            tvEditBtn=itemView.findViewById(R.id.tvEditBtn)
            tvStatusBtn=itemView.findViewById(R.id.tvStatusBtn)
            ivPdf=itemView.findViewById(R.id.ivPdf)
            text_retailerid=itemView.findViewById(R.id.text_retailerid)

        }
    }



    // RecyclerView recyclerView;
    init {
        mInflater = LayoutInflater.from(context)
        this.pancardModalList = pancardModalList
        this.retryClick = retryClick
        this.editClick=editClick
        this.statusClick=statusClick
        this.pdfClick=pdfClick
    }
}