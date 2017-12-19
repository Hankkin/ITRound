package com.hankkin.itround.ui.fg;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.hankkin.itround.Constant;
import com.hankkin.itround.R;
import com.hankkin.itround.adapter.HorizontalPagerAdapter;
import com.hankkin.itround.bean.UserBean;
import com.hankkin.itround.chat.FriendsManager;
import com.hankkin.itround.chat.UserCacheUtils;
import com.hankkin.itround.ui.PersonActivity;
import com.hankkin.itround.widget.GradationScrollView;
import com.hankkin.library.base.BaseFragment;
import com.hankkin.library.utils.ToastUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hankkin on 2017/10/12.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

public class FindFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.scrollView)
    GradationScrollView scrollView;
    @BindView(R.id.iv_banner) ImageView ivBanner;
    @BindView(R.id.tv_find_title) TextView tvTitle;
    @BindView(R.id.ll_find_header) LinearLayout llHeader;
    @BindView(R.id.refresh) SwipeRefreshLayout refreshLayout;
    @BindView(R.id.hicvp) HorizontalInfiniteCycleViewPager hicVp;

    private int height;
    private HorizontalPagerAdapter adapter;

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
        initSwipe(refreshLayout,this);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_find;
    }

    @Override
    protected void initData() {
        getFriends();
    }

    private void getFriends(){

        AVQuery<UserBean> q = UserBean.getQuery(UserBean.class);
        q.limit(100);
        UserBean user = UserBean.getCurrentUser();
        List<String> friendIds = new ArrayList<String>(FriendsManager.getFriendIds());
        friendIds.add(user.getObjectId());
        q.whereNotContainedIn(Constant.OBJECT_ID, friendIds);
        q.orderByDescending(Constant.UPDATED_AT);
        q.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
        q.findInBackground(new FindCallback<UserBean>() {
            @Override
            public void done(List<UserBean> list, AVException e) {
                if (e == null){
                    UserCacheUtils.cacheUsers(list);
                    List<UserBean> userBeanList = new ArrayList<>();
                    for (UserBean user : list){
                        userBeanList.add(user);
                    }
                    adapter = new HorizontalPagerAdapter(getContext(), false,userBeanList);
                    hicVp.setAdapter(adapter);
                    refreshLayout.setRefreshing(false);
                    adapter.setListener(new HorizontalPagerAdapter.PageOnClickListener() {
                        @Override
                        public void onClick(int position) {
                            UserBean userBean = adapter.getItem(position);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("user", (Serializable) userBean);
                            bundle.putBoolean(PersonActivity.CURRENT,false);
                            gotoActivity(PersonActivity.class,false,bundle);
                        }
                    });
                }
                else {
                    ToastUtils.showShortToast(e.getMessage());
                    refreshLayout.setRefreshing(false);
                }

            }
        });
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

    @Override
    public void onRefresh() {
        getFriends();
    }
}
