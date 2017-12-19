package com.hankkin.itround.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gigamole.infinitecycleviewpager.VerticalInfiniteCycleViewPager;
import com.hankkin.itround.ITApplication;
import com.hankkin.itround.R;
import com.hankkin.itround.bean.UserBean;
import com.hankkin.itround.utils.Utils;
import com.hankkin.library.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by GIGAMOLE on 7/27/16.
 */
public class HorizontalPagerAdapter extends PagerAdapter {



    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private boolean mIsTwoWay;
    private List<UserBean> data = new ArrayList<>();
    private PageOnClickListener listener;

    public HorizontalPagerAdapter(final Context context, final boolean isTwoWay,List<UserBean> data) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mIsTwoWay = isTwoWay;
        this.data = data;
    }

    public interface PageOnClickListener{
        void onClick(int position);
    }


    @Override
    public int getCount() {
        return mIsTwoWay ? 6 : data.size();
    }

    @Override
    public int getItemPosition(final Object object) {
        return POSITION_NONE;
    }

    public UserBean getItem(int postion){
        return data.get(postion);
    }

    public void setListener(PageOnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final View view;
        if (mIsTwoWay) {
            view = mLayoutInflater.inflate(R.layout.two_way_item, container, false);

            final VerticalInfiniteCycleViewPager verticalInfiniteCycleViewPager =
                    (VerticalInfiniteCycleViewPager) view.findViewById(R.id.vicvp);
//            verticalInfiniteCycleViewPager.setAdapter(
//                    new VerticalPagerAdapter(mContext)
//            );
            verticalInfiniteCycleViewPager.setCurrentItem(position);
        } else {
            view = mLayoutInflater.inflate(R.layout.item, container, false);
            final TextView txt = (TextView) view.findViewById(R.id.txt_item);
            txt.setText(data.get(position).getName());

            final ImageView img = (ImageView) view.findViewById(R.id.img_item);
            GlideUtils.loadImageView(ITApplication.getInstance(),data.get(position).getAvatarUrl(),img);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(position);
            }
        });

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
