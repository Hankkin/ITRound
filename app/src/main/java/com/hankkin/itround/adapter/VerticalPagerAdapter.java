package com.hankkin.itround.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hankkin.itround.R;
import com.hankkin.itround.utils.Utils;

import static com.hankkin.itround.utils.Utils.setupItem;


/**
 * Created by GIGAMOLE on 7/27/16.
 */
public class VerticalPagerAdapter extends PagerAdapter {

    private final Utils.LibraryObject[] TWO_WAY_LIBRARIES = new Utils.LibraryObject[]{
            new Utils.LibraryObject(
                    R.mipmap.ic_launcher,
                    "Fintech"
            ),
            new Utils.LibraryObject(
                    R.mipmap.ic_launcher,
                    "Delivery"
            ),
            new Utils.LibraryObject(
                    R.mipmap.ic_launcher,
                    "Social network"
            ),
            new Utils.LibraryObject(
                    R.mipmap.ic_launcher,
                    "E-commerce"
            ),
            new Utils.LibraryObject(
                    R.mipmap.ic_launcher,
                    "Wearable"
            ),
            new Utils.LibraryObject(
                    R.mipmap.ic_launcher,
                    "Internet of things"
            )
    };

    private LayoutInflater mLayoutInflater;

    public VerticalPagerAdapter(final Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return TWO_WAY_LIBRARIES.length;
    }

    @Override
    public int getItemPosition(final Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final View view = mLayoutInflater.inflate(R.layout.item, container, false);

        setupItem(view, TWO_WAY_LIBRARIES[position]);

        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(final View view, final Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        container.removeView((View) object);
    }
}
