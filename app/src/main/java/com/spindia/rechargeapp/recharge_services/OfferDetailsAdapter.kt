package com.spindia.rechargeapp.recharge_services

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.spindia.rechargeapp.R
import com.spindia.rechargeapp.model.OfferSModel
import java.util.*


class OfferDetailsAdapter(
    context: Context?,
    offerSModelArrayList: ArrayList<MobilePlansList>,
     mobilePrepaidActivity: MobilePrepaidActivity


) :
    RecyclerView.Adapter<OfferDetailsAdapter.ViewHolder>() {
    private val offerSModelArrayList: List<MobilePlansList>
    private val mInflater: LayoutInflater
    private val mobilePrepaidActivity: MobilePrepaidActivity
    var mContext: Context? = null

    interface ListAdapterListener {
        // create an interface
        fun onClickAtOKButton(offerSModel: MobilePlansList?) // create callback function
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        mContext = parent.context
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem: View =
            layoutInflater.inflate(R.layout.data_list_layout, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val offerSModel: MobilePlansList = offerSModelArrayList[position]
        holder.tvOfferDescription.setText(offerSModel.getPlanDescription()!!.trim())
        holder.btnAmount.text =
            mContext!!.resources.getString(R.string.Rupee) + offerSModel.getAmount()!!
        holder.btnAmount.setOnClickListener {
            mobilePrepaidActivity.onClickAtOKButton(offerSModel)

        }
        // holder.ivStatus.setText(rechargeHistoryModal.getAmount());
    }

    override fun getItemCount(): Int {
        return offerSModelArrayList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvOfferDescription: TextView
        var btnAmount: Button

        init {
            tvOfferDescription = itemView.findViewById(R.id.tvOfferDesc)
            btnAmount = itemView.findViewById(R.id.btnAmount)
        }
    }

    // RecyclerView recyclerView;
    init {
        mInflater = LayoutInflater.from(context)
        this.offerSModelArrayList = offerSModelArrayList
        this.mobilePrepaidActivity=mobilePrepaidActivity

    }
}