package com.spindia.rechargeapp.admin.recharge

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

class AdminRechargesAdapter(
    context: Context?,
    rechargeHistoryModalList: ArrayList<AdminRechargeResponse>,
    retryClick:View.OnClickListener
) :
    RecyclerView.Adapter<AdminRechargesAdapter.ViewHolder>() {
    private var rechargeHistoryModalList: List<AdminRechargeResponse>
    private val mInflater: LayoutInflater
    private var mContext: Context? = null
    private var retryClick: View.OnClickListener

  //  lateinit var userModel: UserModel
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        mContext = parent.context
       // val gson = Gson()
        //val json = AppPrefs.getStringPref("userModel", mContext)
    //    userModel = gson.fromJson(json, UserModel::class.java)
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem: View =
            layoutInflater.inflate(R.layout.layout_admin_recharge_history, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val rechargeHistoryModal: AdminRechargeResponse =
            rechargeHistoryModalList[position]
        holder.tvTxnId.setText(""+rechargeHistoryModal.recid)
        holder.tvDate.setText(rechargeHistoryModal.reqdate)
        holder.tvOperator.setText(rechargeHistoryModal.operator)
        holder.tvRecAmount.text =
            mContext!!.resources.getString(R.string.Rupee) + " " + rechargeHistoryModal.amount
        holder.tvBalance.visibility =
            View.GONE
        holder.tvOperator.text = rechargeHistoryModal.operator
        holder.tvStatus.setText(rechargeHistoryModal.status)
        holder.tvRecnNumber.text = rechargeHistoryModal.mobileno
        holder.tvOpeningBalance.visibility =
            View.GONE


      /*  Glide.with(mContext!!)
            .load(IMAGE_URL + rechargeHistoryModal.operator_image)
            .into(holder.ivOperator)
        */


        holder.tvRefId.text = rechargeHistoryModal.statusdesc
        holder.tvRetailerId.text = "Retailer Id: "+rechargeHistoryModal.apiclid
        /*if (userModel.cus_type.equals("retailer")) {

            if (rechargeHistoryModal.retailer.equals("")) {
                holder.tvCommisionRecvd.text =
                    mContext!!.resources.getString(R.string.Rupee) + "0"


            } else {
                holder.tvCommisionRecvd.text =
                    mContext!!.resources.getString(R.string.Rupee) + " " + rechargeHistoryModal.retailer


            }
        } else if (userModel.cus_type.equals("distributor")) {
            if (rechargeHistoryModal.distributor.equals("")) {
                holder.tvCommisionRecvd.text =
                    mContext!!.resources.getString(R.string.Rupee) + "0"


            } else {
                holder.tvCommisionRecvd.text =
                    mContext!!.resources.getString(R.string.Rupee) + " " + rechargeHistoryModal.distributor


            }


        } else {*/
            if (rechargeHistoryModal.master.equals("")) {
                holder.tvCommisionRecvd.text =
                    mContext!!.resources.getString(R.string.Rupee) + "0"


            } else {
                holder.tvCommisionRecvd.text =
                    mContext!!.resources.getString(R.string.Rupee) + " " + rechargeHistoryModal.master


            }
       // }


        // holder.ivStatus.setText(rechargeHistoryModal.getAmount());
        if (rechargeHistoryModal.status.toString().equals("FAILED")) {
           // holder.tvDisputedStatus.visibility = View.GONE ///to be changed
          //  holder.tvRetry.visibility=View.GONE ///to be changed
           // holder.tvStatus.text = "FAILED" ///to be changed
            holder.rl_statusDetails.setBackgroundResource(R.drawable.bg_status_failed)
            // holder.ivStatus.setImageResource(R.drawable.ic_failed)
            if (rechargeHistoryModal.disputeStatus.toString().equals("disputed")) {
            ///    holder.tvDisputedStatus.visibility = View.GONE  ///to be changed
              //  holder.tvRetry.visibility=View.GONE ///to be changed
             //   holder.tvDisputedStatus.visibility = View.VISIBLE  ///to be changed
            } else {
              //  holder.tvDisputedStatus.visibility = View.GONE  ///to be changed
             //   holder.tvRetry.visibility=View.GONE  ///to be changed
            }
        } else if (rechargeHistoryModal.status.toString().equals("SUCCESS")) {
            holder.tvStatus.text = "SUCCESS"
            holder.rl_statusDetails.setBackgroundResource(R.drawable.bg_status_success)
            //holder.ivStatus.setImageResource(R.drawable.checked_right)
         //   holder.tvDisputedStatus.visibility = View.VISIBLE  ///to be changed
         //   holder.tvRetry.visibility=View.GONE ///to be changed

            if (rechargeHistoryModal.disputeStatus.equals("disputed")) {
            //    holder.tvDisputedStatus.visibility = View.VISIBLE  ///to be changed
              //  holder.tvRetry.visibility=View.GONE  ///to be changed
            } else {
             //   holder.tvDisputedStatus.visibility = View.GONE  ///to be changed
            //    holder.tvRetry.visibility=View.GONE ///to be changed
            }
        } else if (rechargeHistoryModal.status.toString().equals("PENDING")) {
          //  holder.tvDisputedStatus.visibility = View.GONE  ///to be changed
            //holder.tvRetry.visibility=View.VISIBLE  ///to be changed
            holder.tvStatus.text = "PENDING"
            holder.rl_statusDetails.setBackgroundResource(R.drawable.bg_status_pending)

            // holder.ivStatus.setImageResource(R.drawable.ic_pending)
            if (rechargeHistoryModal.disputeStatus.equals("disputed")) {
             //   holder.tvDisputedStatus.visibility = View.VISIBLE ///to be changed
                holder.tvDisputedStatus.setText("DISPUTED")
            } else {
             //   holder.tvDisputedStatus.visibility = View.GONE  ///to be changed
            }
        } else {
       //     holder.tvDisputedStatus.visibility = View.VISIBLE ///to be changed
         //   holder.tvRetry.visibility=View.GONE  ///to be changed
        }
        holder.tvDisputedStatus.setOnClickListener {
            val bundle = Bundle()
            /*      val disputeIntent = Intent(mContext, RaiseDisputeActivity::class.java)
                  bundle.putSerializable("rechargeDetails", rechargeHistoryModal)
                  disputeIntent.putExtras(bundle)
                  mContext!!.startActivity(disputeIntent)*/
        }

        holder.tvRetry.setTag(position)
        holder.tvRetry.setOnClickListener(retryClick)
        /* if (rechargeHistoryModal.){

            holder.ivOperator.setImageResource(R.drawable.jio);

        }*/
    }

    override fun getItemCount(): Int {
        return rechargeHistoryModalList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivOperator: ImageView

        // var ivStatus: ImageView
        var tvTxnId: TextView
        var tvDate: TextView
        var tvRecAmount: TextView
        var tvBalance: TextView
        var tvStatus: TextView
        var tvRecnNumber: TextView
        var tvDisputedStatus: TextView
        var tvRetry:TextView
        var tvOpeningBalance: TextView
        var tvCommisionRecvd: TextView
        var tvOperator: TextView
        var tvRefId: TextView
        var rl_statusDetails: RelativeLayout
        var tvRetailerId:TextView

        init {
            tvTxnId = itemView.findViewById(R.id.tvTxnId)
            tvDate = itemView.findViewById(R.id.tvTxnDate)
            tvRecAmount = itemView.findViewById(R.id.tvRecAmount)
            tvBalance = itemView.findViewById(R.id.tvClosingBal)
            tvStatus = itemView.findViewById(R.id.tvRecStatus)
            tvRecnNumber = itemView.findViewById(R.id.tvRechargeMobileNumber)
            tvDisputedStatus = itemView.findViewById(R.id.tvDisputeBtn)
            tvRetry=itemView.findViewById(R.id.tvRetryBtn)
            ivOperator = itemView.findViewById(R.id.ivOperator)
            tvOpeningBalance = itemView.findViewById(R.id.tvOpeningBal)
            tvCommisionRecvd = itemView.findViewById(R.id.tvCommissionRcvd)
            tvOperator = itemView.findViewById(R.id.tvOperatorName)
            tvRefId = itemView.findViewById(R.id.tvRecId)
            rl_statusDetails = itemView.findViewById(R.id.rl_statusDetails)
            tvRetailerId=itemView.findViewById(R.id.tvRetailerId)
        }
    }

    fun filterList(filterdNames: ArrayList<AdminRechargeResponse>) {
        rechargeHistoryModalList = filterdNames
        notifyDataSetChanged()
    }

    companion object {
        const val imgUrl = "http://edigitalvillage.in/assets/operator_img/"
    }

    // RecyclerView recyclerView;
    init {
        mInflater = LayoutInflater.from(context)
        this.rechargeHistoryModalList = rechargeHistoryModalList
        this.retryClick = retryClick
    }
}