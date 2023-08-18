package com.spindia.rechargeapp.recharge_services

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.spindia.rechargeapp.R
import java.util.*


class MainOfferDetailsAdapter(
    context: Context?,
    OfferInnerModelClssArrayList: ArrayList<OfferInnerModelClss>,
    mobilePrepaidActivity: MobilePrepaidActivity,


    ) :
    RecyclerView.Adapter<MainOfferDetailsAdapter.ViewHolder>() {
    private val OfferInnerModelClssArrayList: List<OfferInnerModelClss>
    private val mInflater: LayoutInflater
    private val mobilePrepaidActivity: MobilePrepaidActivity

    var mContext: Context? = null
    lateinit var offerDetailsAdapter:OfferDetailsAdapter

    interface ListAdapterListener {
        // create an interface
        fun onClickAtOKButton(OfferInnerModelClss: OfferInnerModelClss?) // create callback function
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        mContext = parent.context
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem: View =
            layoutInflater.inflate(R.layout.row_offermain, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val OfferInnerModelClss: OfferInnerModelClss = OfferInnerModelClssArrayList[position]
        holder.tv_category.setText(OfferInnerModelClss.data!!.trim())

        /*holder.tv_category.setOnClickListener {
            mListener.onClickAtOKButton(OfferInnerModelClss)

        }*/

        holder.rvOffers.apply {

            layoutManager = LinearLayoutManager(mContext)

            offerDetailsAdapter = OfferDetailsAdapter(
                mContext, OfferInnerModelClss.nestedDetailResponse, mobilePrepaidActivity
            )
            holder.rvOffers.adapter = offerDetailsAdapter
        }

        holder.tv_category.setTag(position)
       // holder.tv_category.setOnClickListener(itemClick)
        // holder.ivStatus.setText(rechargeHistoryModal.getAmount());
    }

    override fun getItemCount(): Int {
        return OfferInnerModelClssArrayList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_category: TextView
        var rvOffers: RecyclerView

        init {
            tv_category = itemView.findViewById(R.id.tv_category)
           rvOffers = itemView.findViewById(R.id.rvOffer)
        }
    }

    // RecyclerView recyclerView;
    init {
        mInflater = LayoutInflater.from(context)
        this.OfferInnerModelClssArrayList = OfferInnerModelClssArrayList
         this.mobilePrepaidActivity=mobilePrepaidActivity

    }
}