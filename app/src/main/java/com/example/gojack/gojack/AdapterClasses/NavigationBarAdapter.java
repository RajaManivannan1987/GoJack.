package com.example.gojack.gojack.AdapterClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gojack.gojack.R;

/**
 * Created by IM0033 on 8/1/2016.
 */
public class NavigationBarAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private String[] listItemNames = {"GO OFFLINE", "DASHBOARD", "HISTORY", "ACCOUNTS", "PAYTM", "SETTINGS"};
    private int[] listItemImages = {
            R.drawable.offline_menu_icon,
            R.drawable.dashboard_menu_icon,
            R.drawable.history_menu_icon,
            R.drawable.accounts_menu_icon,
            R.drawable.wallet,
            R.drawable.setttings_menu_icon};

    public NavigationBarAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listItemNames.length;
    }

    @Override
    public Object getItem(int i) {
        return listItemImages[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.navigationbar_list_item, viewGroup, false);
        }
        TextView txtView = (TextView) view.findViewById(R.id.navigationBarListTextView);
        ImageView imageView = (ImageView) view.findViewById(R.id.navigationBarListImageView);
        txtView.setText(listItemNames[position]);
        imageView.setImageResource(listItemImages[position]);
        return view;
    }
}
