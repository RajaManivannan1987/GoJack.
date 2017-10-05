package com.example.gojack.gojack.AdapterClasses;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
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
    Activity context;
    private int lastPosition = -1;

    public HistoryAdapter(Activity context, ArrayList<HistoryModel> arrayList) {
        this.context = context;
        this.list = arrayList;
    }

    @Override
    public CustomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomHolder(LayoutInflater.from(context).inflate(R.layout.custom_history_listitem, parent, false));
    }

    @Override
    public void onBindViewHolder(final CustomHolder holder, final int position) {
//        final HistoryModel getListValue = list.get(position);
        holder.rideDateTextView.setText(list.get(position).getDate_time());
        holder.rideFromLocationTextView.setText(list.get(position).getDriver_s_address());
        holder.rideToLocationTextView.setText(list.get(position).getDriver_e_address());
       /* Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/rupee_foradian.ttf");
        holder.rideRateTextView.setTypeface(face);*/
        if (list.get(position).getRide_type().equalsIgnoreCase("courier")) {
            holder.rideTypeImageView.setImageResource(R.drawable.motorcycle1);
            holder.rideDateTextView.setTextColor(Color.parseColor("#000080"));
        } else {
            holder.rideDateTextView.setTextColor(Color.parseColor("#D35400"));
            holder.rideTypeImageView.setImageResource(R.drawable.motorcycle);
        }
        holder.rideRateTextView.setText("Rs " + list.get(position).getFinal_amount());
        holder.historyMainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, HistoryDetails.class);
                i.putExtra("rideId", list.get(position).getRide_id());
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(context, holder.history_cardview, "profile");
                context.startActivity(i, options.toBundle());
            }
        });

        setfadeAnimation(holder.itemView, position);
    }

    @Override
    public void onViewDetachedFromWindow(CustomHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    private void setfadeAnimation(View itemView, int position) {
        if (position > lastPosition) {
            AlphaAnimation animation = new AlphaAnimation(0.0f, 0.5f);
            animation.setDuration(1000);
            itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CustomHolder extends RecyclerView.ViewHolder {
        LinearLayout historyMainLayout;
        TextView rideDateTextView, rideFromLocationTextView, rideToLocationTextView, rideRateTextView;
        ImageView rideTypeImageView;
        CardView history_cardview;

        public CustomHolder(View itemView) {
            super(itemView);
            historyMainLayout = (LinearLayout) itemView.findViewById(R.id.historyMainLayout);
            history_cardview = (CardView) itemView.findViewById(R.id.history_cardview);
            rideDateTextView = (TextView) itemView.findViewById(R.id.rideDateTextView);
            rideFromLocationTextView = (TextView) itemView.findViewById(R.id.rideFromLocationTextView);
            rideToLocationTextView = (TextView) itemView.findViewById(R.id.rideToLocationTextView);
            rideRateTextView = (TextView) itemView.findViewById(R.id.rideRateTextView);
            rideTypeImageView = (ImageView) itemView.findViewById(R.id.rideTypeImageView);
        }
    }
}