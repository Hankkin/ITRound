package com.hankkin.itround.ui.fg;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.hankkin.itround.R;
import com.hankkin.itround.event.EventMap;
import com.hankkin.itround.ui.home.PostGankActivity;
import com.hankkin.library.base.BaseFragment;
import com.hankkin.library.rx.RxBus;
import com.kekstudio.dachshundtablayout.DachshundTabLayout;

import butterknife.BindView;

import static android.support.design.widget.TabLayout.MODE_SCROLLABLE;

/**
 * Created by hankkin on 2017/10/11.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

public class HomeFragment extends BaseFragment{

    @BindView(R.id.fab_menu_add) FloatingActionMenu fb;
    @BindView(R.id.fab_refresh) FloatingActionButton fbRefresh;
    @BindView(R.id.fab_write) FloatingActionButton fbWrite;

    public enum HomeTag{

    }


    public static final String HOME_TAG[] = {
            "all",
            "福利",
            "Android",
            "iOS",
            "休息视频",
            "拓展资源",
            "前端"};

    @BindView(R.id.tab) DachshundTabLayout tabLayout;
    @BindView(R.id.vp) ViewPager viewPager;

    @Override
    protected void initViewsAndEvents(View view) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    public static HomeFragment newInstance(int index) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initData() {

        viewPager.setAdapter(new MainFragmentAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(MODE_SCROLLABLE);
        viewPager.setOffscreenPageLimit(HOME_TAG.length);

        fb.setClosedOnTouchOutside(true);

        fbRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getDefault().post(new EventMap.RefreshHomeEvent());
                fb.close(false);
            }
        });
        fbWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(PostGankActivity.class);
                fb.close(false);
            }
        });

    }




    /**
     * Created by Hankkin on 15/9/20.
     */
    public class MainFragmentAdapter extends FragmentStatePagerAdapter {
        public MainFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return HomeContentFragment.newInstance(i);
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
