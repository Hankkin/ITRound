package com.hankkin.itround.ui.fg;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hankkin.itround.R;
import com.hankkin.itround.bean.GankBean;
import com.hankkin.itround.bean.UserBean;
import com.hankkin.itround.callback.FindUsersCallBack;
import com.hankkin.itround.manage.UserManager;
import com.hankkin.itround.widget.LetterComparator;
import com.hankkin.itround.widget.PinnedHeaderDecoration;
import com.hankkin.library.base.BaseFragment;
import com.hankkin.library.utils.GlideUtils;
import com.hankkin.library.utils.LogUtils;
import com.hankkin.library.utils.ToastUtils;
import com.hankkin.library.view.EmptyLayout;
import com.kekstudio.dachshundtablayout.DachshundTabLayout;
import com.kekstudio.dachshundtablayout.indicators.LineFadeIndicator;
import com.kekstudio.dachshundtablayout.indicators.PointFadeIndicator;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.mobike.library.MobikeView;
import com.yalantis.jellytoolbar.listener.JellyListener;
import com.yalantis.jellytoolbar.widget.JellyToolbar;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import cc.solart.wave.WaveSideBarView;
import cn.leancloud.chatkit.activity.LCIMConversationFragment;
import cn.leancloud.chatkit.activity.LCIMConversationListFragment;

import static android.support.design.widget.TabLayout.MODE_FIXED;
import static android.support.design.widget.TabLayout.MODE_SCROLLABLE;

/**
 * Created by hankkin on 2017/10/12.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

public class CircleFragment extends BaseFragment{
    private final static String TAG = "CircleFragment";

    @BindView(R.id.toolbar)
    JellyToolbar toolbar;
    @BindView(R.id.tv_circle_title)
    TextView tvTitle;

    public static final String HOME_TAG[] = {
            "会话",
            "联系人",
            };

    @BindView(R.id.tab)
    DachshundTabLayout tabLayout;
    @BindView(R.id.vp)
    ViewPager viewPager;


    private AppCompatEditText editText;


    private JellyListener jellyListener = new JellyListener() {
        @Override
        public void onCancelIconClicked() {
            if (TextUtils.isEmpty(editText.getText())) {
                toolbar.collapse();
            } else {
                editText.getText().clear();
            }
        }

        @Override
        public void onToolbarExpandingStarted() {
            tvTitle.setVisibility(View.GONE);
        }

        @Override
        public void onToolbarCollapsed() {
            tvTitle.setVisibility(View.VISIBLE);
        }
    };

    public static CircleFragment newInstance(int index) {
        CircleFragment fragment = new CircleFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initViewsAndEvents(View view) {
        toolbar.setJellyListener(jellyListener);


        editText = (AppCompatEditText) LayoutInflater.from(activity).inflate(R.layout.edit_text, null);
        editText.setBackgroundResource(R.color.transparent);
        toolbar.setContentView(editText);

        viewPager.setAdapter(new CircleMainFragmentAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(MODE_FIXED);
        tabLayout.setAnimatedIndicator(new PointFadeIndicator(tabLayout));
        viewPager.setOffscreenPageLimit(HOME_TAG.length);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_circle;
    }

    @Override
    protected void initData() {

    }



    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * Created by Hankkin on 15/9/20.
     */
    public static class CircleMainFragmentAdapter extends FragmentStatePagerAdapter {
        public CircleMainFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i == 0){
                return new LCIMConversationListFragment();
            }
            else {
                return ContactFragment.newInstance(i);
            }
        }

        @Override
        public int getCount() {
            return HOME_TAG.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return HOME_TAG[position];
        }

    }


}
