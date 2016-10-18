package com.example.gojack.gojack.AdapterClasses;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gojack.gojack.Activities.HistoryDetails;
import com.example.gojack.gojack.ModelClasses.HistoryModel;
import com.example.gojack.gojack.R;

import java.util.ArrayList;

/**
 * Created by IM0033 on 9/2/2016.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.CustomHolder> {
    ArrayList<HistoryModel> list;
    Context context;

    public HistoryAdapter(Context context, ArrayList<HistoryModel> arrayList) {
        this.context = context;
        this.list = arrayList;
    }

    @Override
    public CustomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomHolder(LayoutInflater.from(context).inflate(R.layout.custom_history_listitem, parent, false));
    }

    @Override
    public void onBindViewHolder(CustomHolder holder, int position) {
        final HistoryModel getListValue = list.get(position);
        holder.rideDateTextView.setText(getListValue.getDate_time());
        holder.rideFromLocationTextView.setText(getListValue.getStarting_address());
        holder.rideToLocationTextView.setText(getListValue.getEnding_address());
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/rupee_foradian.ttf");
        holder.rideRateTextView.setTypeface(face);
        if (getListValue.getRide_type().equalsIgnoreCase("courier")) {
            holder.rideTypeImageView.setImageResource(R.drawable.courier_icon);
        } else {
            holder.rideTypeImageView.setImageResource(R.drawable.bike_icon);
        }
        holder.rideRateTextView.setText(context.getResources().getString(R.string.rs) + " " + getListValue.getFinal_amount());
        holder.historyMainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, HistoryDetails.class);
                i.putExtra("rideId", getListValue.getRide_id());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CustomHolder extends RecyclerView.ViewHolder {
        LinearLayout historyMainLayout;
        TextView rideDateTextView, rideFromLocationTextView, rideToLocationTextView, rideRateTextView;
        ImageView rideTypeImageView;

        public CustomHolder(View itemView) {
            super(itemView);
            historyMainLayout = (LinearLayout) itemView.findViewById(R.id.historyMainLayout);
            rideDateTextView = (TextView) itemView.findViewById(R.id.rideDateTextView);
            rideFromLocationTextView = (TextView) itemView.findViewById(R.id.rideFromLocationTextView);
            rideToLocationTextView = (TextView) itemView.findViewById(R.id.rideToLocationTextView);
            rideRateTextView = (TextView) itemView.findViewById(R.id.rideRateTextView);
            rideTypeImageView = (ImageView) itemView.findViewById(R.id.rideTypeImageView);
        }
    }
}
