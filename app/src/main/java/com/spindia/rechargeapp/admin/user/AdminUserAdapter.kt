package com.spindia.rechargeapp.admin.user

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

class AdminUserAdapter(
    context: Context?,
    rechargeHistoryModalList: ArrayList<UserListResponse>
) :
    RecyclerView.Adapter<AdminUserAdapter.ViewHolder>() {
    private var rechargeHistoryModalList: List<UserListResponse>
    private val mInflater: LayoutInflater
    private var mContext: Context? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        mContext = parent.context

        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem: View =
            layoutInflater.inflate(R.layout.layout_user_history, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val rechargeHistoryModal: UserListResponse =
            rechargeHistoryModalList[position]
        holder.tvId.setText(""+rechargeHistoryModal.id)
        holder.tvDate.setText(rechargeHistoryModal.createat)
        holder.text_name.setText(rechargeHistoryModal.mobile)
        holder.text_email.text = "Email Id: "+rechargeHistoryModal.email

    }

    override fun getItemCount(): Int {
        return rechargeHistoryModalList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvId: TextView
        var tvDate: TextView
        var text_email: TextView
        var text_name: TextView


        init {
            tvId = itemView.findViewById(R.id.tvId)
            tvDate = itemView.findViewById(R.id.tvTxnDate)
            text_email = itemView.findViewById(R.id.text_email)
            text_name = itemView.findViewById(R.id.text_name)

        }
    }

    fun filterList(filterdNames: ArrayList<UserListResponse>) {
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
    }
}