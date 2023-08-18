package com.spindia.rechargeapp.walletHistory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.spindia.rechargeapp.R;

import java.util.ArrayList;

public class WalletHistoryAdapter extends RecyclerView.Adapter<WalletHistoryAdapter.MyHolder> {
    public Context context;
    private ArrayList<WalletHistoryResponse> walletHistoryResponses;



    public WalletHistoryAdapter(Context context, ArrayList<WalletHistoryResponse> walletHistoryResponses) {
        this.context = context;
        this.walletHistoryResponses = walletHistoryResponses;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_wallet, viewGroup, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {

        WalletHistoryResponse model = walletHistoryResponses.get(i);

        if (model.getCredit().equals("0"))
        {
            myHolder.crdt_textView.setText("Debit:  Rs. "+model.getDebit());
        }else if (model.getDebit().equals("0"))
        {
            myHolder.crdt_textView.setText("Credit:  Rs. "+model.getCredit());
        }

        myHolder.currbal_textView.setText("Current Balance:  Rs. "+model.getCurBalance());
        myHolder.datetime_textView.setText("Date: "+model.getCreateat());
        myHolder.remark_textView.setText("Remark: "+model.getRemark());


    }

    @Override
    public int getItemCount() {
        return walletHistoryResponses.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public TextView crdt_textView,currbal_textView;
        public TextView datetime_textView,remark_textView;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            crdt_textView = itemView.findViewById(R.id.crdt_textView);
            currbal_textView = itemView.findViewById(R.id.currbal_textView);
            datetime_textView = itemView.findViewById(R.id.datetime_textView);
            remark_textView = itemView.findViewById(R.id.remark_textView);

        }
    }
}
