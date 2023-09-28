package com.spindia.rechargeapp.itr

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
import java.lang.Exception
import java.util.*

class ITRListAdapter(
    context: Context?,
    pancardModalList: ArrayList<ITRResponse>,
    retryClick:View.OnClickListener,
    editClick:View.OnClickListener,
    statusClick:View.OnClickListener,
    pdfClick:View.OnClickListener,
    panInfoClick: View.OnClickListener
) :
    RecyclerView.Adapter<ITRListAdapter.ViewHolder>() {
    private var pancardModalList: List<ITRResponse>
    private val mInflater: LayoutInflater
    private var mContext: Context? = null
    private var retryClick: View.OnClickListener
    private var editClick: View.OnClickListener
    private var statusClick: View.OnClickListener
    private var pdfClick: View.OnClickListener
    private var panInfoClick: View.OnClickListener

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        mContext = parent.context

        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem: View =
            layoutInflater.inflate(R.layout.layout_itr_history, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val pancardModal: ITRResponse =
            pancardModalList[position]

        try {
            holder.text_name.setText("Name: "+pancardModal.firstName+" "+pancardModal.middleName+" "+pancardModal.lastName)
        }catch (e:Exception)
        {
            holder.text_name.setText("Name: "+pancardModal.firstName+" "+pancardModal.lastName)
        }

        holder.text_mobileno.setText("Mob No: "+pancardModal.mobileNo)
        holder.text_panNo.setText("PAN No: "+pancardModal.panNo)

        holder.tvTxnDate.setText(pancardModal.createat)
        holder.text_itrCategory.setText("ITR Category: "+pancardModal.itrCategory)
        holder.text_itryear.setText(""+pancardModal.itrYear)
        holder.text_aadharno.setText("Aadhar no: "+pancardModal.aadharNo)


       /* if (pancardModal.status.equals("success"))
        {
            holder.tvPayBtn.visibility=View.GONE
        }else{
            holder.tvPayBtn.visibility=View.VISIBLE
        }
*/

        if (pancardModal.status.toString().equals("Reject")) {
         //   holder.tvPayBtn.visibility = View.GONE
            holder.rl_statusDetails.setBackgroundResource(R.drawable.bg_status_failed)

       //     holder.tvEditBtn.visibility=View.GONE
         //   holder.tvStatusBtn.visibility=View.GONE


        } else if (pancardModal.status.toString().equals("Success")) {
            holder.text_status.text = "SUCCESS"
            holder.rl_statusDetails.setBackgroundResource(R.drawable.bg_status_success)

           // holder.tvEditBtn.visibility=View.GONE
        //    holder.tvStatusBtn.visibility=View.VISIBLE



        } else if (pancardModal.status.toString().equals("pending")) {
            holder.text_status.text = "PENDING"
            holder.rl_statusDetails.setBackgroundResource(R.drawable.bg_status_pending)

        //    holder.tvEditBtn.visibility=View.GONE
         //   holder.tvStatusBtn.visibility=View.GONE


        }else if (pancardModal.status.toString().equals("Hold")) {
            holder.text_status.text = "HOLD"

            holder.rl_statusDetails.setBackgroundResource(R.drawable.bg_status_hold)
          //  holder.text_remark.visibility=View.GONE
         //   holder.tvEditBtn.visibility=View.VISIBLE
        //    holder.tvStatusBtn.visibility=View.GONE

        }

     /*   if(pancardModal.document!=null)
        {
            holder.ivPdf.visibility=View.VISIBLE
        }else{
            holder.ivPdf.visibility=View.GONE
        }*/

        holder.tvPayBtn.setTag(position)
        holder.tvPayBtn.setOnClickListener(retryClick)

        holder.tvEditBtn.setTag(position)
        holder.tvEditBtn.setOnClickListener(editClick)

        holder.tvStatusBtn.setTag(position)
        holder.tvStatusBtn.setOnClickListener(statusClick)


       /* holder.ivPdf.setTag(position)
        holder.ivPdf.setOnClickListener(pdfClick)

        holder.btnpanInfo.setTag(position)
        holder.btnpanInfo.setOnClickListener(panInfoClick)*/

    }

    override fun getItemCount(): Int {
        return pancardModalList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // var ivStatus: ImageView
        var text_itryear: TextView
        var text_name: TextView
        var text_panNo: TextView
        var text_aadharno:TextView

        var tvTxnDate:TextView
        var text_status: TextView


        var text_mobileno: TextView
        var text_itrCategory:TextView


        var tvPayBtn: TextView
        var rl_statusDetails: RelativeLayout
        var tvEditBtn:TextView
        var tvStatusBtn:TextView

        //var ivPdf:ImageView
       // var btnpanInfo: Button

        init {
            text_aadharno=itemView.findViewById(R.id.text_aadharno)
            text_status = itemView.findViewById(R.id.text_status)
            text_name = itemView.findViewById(R.id.text_name)
            text_panNo = itemView.findViewById(R.id.text_panNo)
            text_mobileno = itemView.findViewById(R.id.text_mobileno)

            tvPayBtn = itemView.findViewById(R.id.tvPayBtn)
            text_itryear=itemView.findViewById(R.id.text_itryear)
            tvTxnDate=itemView.findViewById(R.id.tvTxnDate)
            rl_statusDetails=itemView.findViewById(R.id.rl_statusDetails)
            tvEditBtn=itemView.findViewById(R.id.tvEditBtn)
            tvStatusBtn=itemView.findViewById(R.id.tvStatusBtn)
            text_itrCategory=itemView.findViewById(R.id.text_itrCategory)
           // ivPdf=itemView.findViewById(R.id.ivPdf)
          //  btnpanInfo=itemView.findViewById(R.id.btnpanInfo);

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
        this.panInfoClick=panInfoClick
    }
}