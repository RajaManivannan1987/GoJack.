package com.example.gojack.gojack.AdapterClasses;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.gojack.gojack.R;
import java.util.ArrayList;

/**
 * Created by Im033 on 3/24/2017.
 */

public class HelperViewPagerAdapter extends PagerAdapter {
    private ArrayList<Integer> arrayList;
    private LayoutInflater inflater;
    private Context context;

    public HelperViewPagerAdapter(Context context, ArrayList<Integer> list) {
        this.arrayList = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView( (View) object);
    }

    public Object instantiateItem(ViewGroup viewGroup, int position) {
        View imagiLayout = inflater.inflate(R.layout.pager_item, viewGroup, false);
        assert imagiLayout != null;
        final ImageView imageView = (ImageView) imagiLayout.findViewById(R.id.img_pager_item);

        imageView.setImageResource(arrayList.get(position));
        viewGroup.addView(imagiLayout, 0);
        return imagiLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        super.restoreState(state, loader);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
