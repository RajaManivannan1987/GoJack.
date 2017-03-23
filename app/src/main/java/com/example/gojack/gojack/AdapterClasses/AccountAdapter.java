package com.example.gojack.gojack.AdapterClasses;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gojack.gojack.ModelClasses.AccountsModel;
import com.example.gojack.gojack.R;

import java.util.ArrayList;

/**
 * Created by Im033 on 3/21/2017.
 */

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.CustomHolder> {
    private Context context;
    private ArrayList<AccountsModel> list;

    public AccountAdapter(Context context, ArrayList<AccountsModel> accountList) {
        this.context = context;
        this.list = accountList;
    }

    @Override
    public CustomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomHolder(LayoutInflater.from(context).inflate(R.layout.custom_accounts_listitem, parent, false));
    }

    @Override
    public void onBindViewHolder(CustomHolder holder, int position) {
        holder.accountDateTextView.setText(list.get(position).getDate());
        holder.accountAmtTextView.setText(list.get(position).getAmt());
        holder.accountCommisionTextView.setText(list.get(position).getCommission());
        holder.accountTripsTextView.setText(list.get(position).getTotalrides());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CustomHolder extends RecyclerView.ViewHolder {
        TextView accountDateTextView, accountTripsTextView, accountCommisionTextView, accountAmtTextView;

        public CustomHolder(View itemView) {
            super(itemView);
            accountDateTextView = (TextView) itemView.findViewById(R.id.accountDateTextView);
            accountAmtTextView = (TextView) itemView.findViewById(R.id.accountAmtTextView);
            accountCommisionTextView = (TextView) itemView.findViewById(R.id.accountCommisionTextView);
            accountTripsTextView = (TextView) itemView.findViewById(R.id.accountTripsTextView);
        }
    }
}
