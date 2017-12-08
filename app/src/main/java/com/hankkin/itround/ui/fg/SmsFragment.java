package com.hankkin.itround.ui.fg;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hankkin.itround.R;
import com.hankkin.itround.bean.UserBean;
import com.hankkin.itround.callback.FindUsersCallBack;
import com.hankkin.itround.manage.UserManager;
import com.hankkin.itround.widget.LetterComparator;
import com.hankkin.itround.widget.PinnedHeaderDecoration;
import com.hankkin.library.base.BaseFragment;
import com.hankkin.library.utils.GlideUtils;
import com.hankkin.library.utils.LogUtils;
import com.hankkin.library.view.EmptyLayout;
import com.yalantis.jellytoolbar.listener.JellyListener;
import com.yalantis.jellytoolbar.widget.JellyToolbar;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import cc.solart.wave.WaveSideBarView;

/**
 * Created by hankkin on 2017/10/12.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

public class SmsFragment extends BaseFragment{


    public static SmsFragment newInstance(int index) {
        SmsFragment fragment = new SmsFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initViewsAndEvents(View view) {


    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sms;
    }

    @Override
    protected void initData() {



    }



}
