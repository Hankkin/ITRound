package com.hankkin.itround.ui.fg;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

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
import com.hankkin.library.view.EmptyLayout;
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

/**
 * Created by hankkin on 2017/10/12.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

public class CircleFragment extends BaseFragment{
    private final static String TAG = "CircleFragment";

    @BindView(R.id.toolbar)
    JellyToolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView rv;
    @BindView(R.id.side_view)
    WaveSideBarView sideBarView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.empty)
    EmptyLayout emptyLayout;


    private AppCompatEditText editText;
    private CircleFriendAdapter adapter;


    private JellyListener jellyListener = new JellyListener() {
        @Override
        public void onCancelIconClicked() {
            if (TextUtils.isEmpty(editText.getText())) {
                toolbar.collapse();
            } else {
                editText.getText().clear();
            }
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
        initSwipe(refreshLayout, null);
        rv.setLayoutManager(new LinearLayoutManager(activity));
        final PinnedHeaderDecoration decoration = new PinnedHeaderDecoration();
        decoration.registerTypePinnedHeader(1, new PinnedHeaderDecoration.PinnedHeaderCreator() {
            @Override
            public boolean create(RecyclerView parent, int adapterPosition) {
                return true;
            }
        });
        rv.addItemDecoration(decoration);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_circle;
    }

    @Override
    protected void initData() {


        sideBarView.setOnTouchLetterChangeListener(new WaveSideBarView.OnTouchLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                int pos = adapter.getLetterPosition(letter);
                if (pos != -1) {
                    rv.scrollToPosition(pos);
                    LinearLayoutManager mLayoutManager =
                            (LinearLayoutManager) rv.getLayoutManager();
                    mLayoutManager.scrollToPositionWithOffset(pos, 0);
                }
            }
        });

        queryUser();

    }

    private void queryUser() {
        UserManager.queryAllUser(new FindUsersCallBack() {
            @Override
            public void findAllUser(List<UserBean> data) {
                LogUtils.e(TAG,data.size()+"");
                Collections.sort(data, new LetterComparator());
                adapter = new CircleFriendAdapter();
                adapter.setNewData(data);
                rv.setAdapter(adapter);
                refreshLayout.setRefreshing(false);
                refreshLayout.setEnabled(false);
            }

            @Override
            public void findFail(String msg) {
                refreshLayout.setRefreshing(false);
                refreshLayout.setEnabled(false);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }



    private class CircleFriendAdapter extends BaseQuickAdapter<UserBean, BaseViewHolder> {


        public CircleFriendAdapter() {
            super(R.layout.adapter_circle_friend);
        }

        @Override
        protected void convert(BaseViewHolder helper, final UserBean item) {
            helper.setText(R.id.tv_adapter_cir_name, item.getName());
            helper.setText(R.id.tv_adapter_cir_desc, item.getDesc());

            if (item.getSex() == UserBean.BOY) {
                helper.setImageResource(R.id.iv_adapter_cir_sex, R.mipmap.da_ren_list_boy);
            } else {
                helper.setImageResource(R.id.iv_adapter_cir_sex, R.mipmap.da_ren_list_girl);

            }
            ImageView ivHeader = helper.getView(R.id.iv_cir_icon);
            if (!TextUtils.isEmpty(item.rerturnHeaderImageUrl())) {
                GlideUtils.loadImageView(activity, item.rerturnHeaderImageUrl(), ivHeader);
            }
        }
        public int getLetterPosition(String letter){
            for (int i = 0 ; i < getData().size(); i++){
                if(getData().get(i).getUsername().equals(letter)){
                    return i;
                }
            }
            return -1;
        }

    }


}
