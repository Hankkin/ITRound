package com.hankkin.itround.ui.fg;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.hankkin.itround.R;
import com.hankkin.itround.adapter.HorizontalPagerAdapter;
import com.hankkin.itround.widget.GradationScrollView;
import com.hankkin.library.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by hankkin on 2017/10/12.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

public class FindFragment extends BaseFragment {

    @BindView(R.id.scrollView)
    GradationScrollView scrollView;
    @BindView(R.id.iv_banner) ImageView ivBanner;
    @BindView(R.id.tv_find_title) TextView tvTitle;
    @BindView(R.id.ll_find_header) LinearLayout llHeader;
    @BindView(R.id.refresh) SwipeRefreshLayout refreshLayout;
    @BindView(R.id.hicvp) HorizontalInfiniteCycleViewPager hicVp;

    private int height;

    public static FindFragment newInstance(int index) {
        FindFragment fragment = new FindFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initViewsAndEvents(View view) {
        initListeners();
        hicVp.setAdapter(new HorizontalPagerAdapter(getContext(), false));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_find;
    }

    @Override
    protected void initData() {

    }


    /**
     * 顶部图片渐变滑动监听
     */
    private void initListeners() {

        ViewTreeObserver vto = ivBanner.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tvTitle.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                height = ivBanner.getHeight();

                scrollView.setScrollViewListener(new GradationScrollView.ScrollViewListener() {
                    @Override
                    public void onScrollChanged(GradationScrollView scrollView, int x, int y, int oldx, int oldy) {
                        // TODO Auto-generated method stub
                        if (y <= 0) {   //设置标题的背景颜色
                            tvTitle.setTextColor(getResources().getColor(R.color.white));
                            tvTitle.setBackground(getResources().getDrawable(R.drawable.bg_find_tran));
                            refreshLayout.setEnabled(true);
                        } else if (y > 0 && y <= height) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
                            float scale = (float) y / height;
                            float alpha = (255 * scale);
                            llHeader.setBackgroundColor(Color.argb((int) alpha, 253, 119, 34));
                            if (y>=height/2){
                                tvTitle.setTextColor(getResources().getColor(R.color.home_content));
                                llHeader.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                tvTitle.setBackground(getResources().getDrawable(R.drawable.bg_find_tran_white));
                            }
                            refreshLayout.setEnabled(false);
                        } else {    //滑动到banner下面设置普通颜色
                            tvTitle.setBackground(getResources().getDrawable(R.drawable.bg_find_tran_white));
                            refreshLayout.setEnabled(false);
                        }
                    }
                });
            }
        });
    }
}
