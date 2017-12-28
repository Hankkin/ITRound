package com.hankkin.itround.ui.fg;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hankkin.itround.Constant;
import com.hankkin.itround.R;
import com.hankkin.itround.bean.GankBean;
import com.hankkin.itround.bean.UserBean;
import com.hankkin.itround.core.HomeContentPresenter;
import com.hankkin.itround.core.HomeContentView;
import com.hankkin.itround.event.EventMap;
import com.hankkin.itround.manage.UserManager;
import com.hankkin.itround.ui.other.WebActivity;
import com.hankkin.library.base.MvpFragment;
import com.hankkin.library.rx.RxBus;
import com.hankkin.library.utils.GlideUtils;
import com.hankkin.library.utils.ToastUtils;
import com.hankkin.library.view.CustomLoadMoreView;
import com.hankkin.library.view.EmptyLayout;
import com.hankkin.library.view.RecycleViewDivider;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.List;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

/**
 * Created by hankkin on 2017/10/11.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

public class HomeContentFragment extends MvpFragment<HomeContentView,HomeContentPresenter> implements
        SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener, HomeContentView{



    @BindView(R.id.refresh) SwipeRefreshLayout refreshLayout;
    @BindView(R.id.empty) EmptyLayout emptyLayout;
    @BindView(R.id.rv_home) RecyclerView rv;

    private int index;
    private HomeContentAdapter adapter;
    private int page = 1;
    private int dataCount = 0;

    public static HomeContentFragment newInstance(int index) {
        HomeContentFragment fragment = new HomeContentFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initViewsAndEvents(View view) {
        index = getArguments().getInt("index");
        operateBus();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_content;
    }

    @Override
    protected void initData() {
        initSwipe(refreshLayout,this);
        adapter = new HomeContentAdapter();
        rv.setLayoutManager(new LinearLayoutManager(activity));
        rv.addItemDecoration(new RecycleViewDivider(activity,LinearLayoutManager.HORIZONTAL));
        adapter.setLoadMoreView(new CustomLoadMoreView());
        adapter.setOnLoadMoreListener(this,rv);
        rv.setAdapter(adapter);
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GankBean gankBean = (GankBean)adapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putString("url",gankBean.getUrl());
                bundle.putString("title",gankBean.getDesc());
                gotoActivity(WebActivity.class,false,bundle);
            }
        });

        getPresenter().getGankHttp(Constant.REFRESH,HomeFragment.HOME_TAG[index],page);

    }

    public void operateBus(){
        RxBus.getDefault().toObservable(EventMap.BaseEvent.class)
                .subscribe(new Consumer<EventMap.BaseEvent>() {
                    @Override
                    public void accept(EventMap.BaseEvent event) throws Exception {
                        if (event instanceof EventMap.RefreshHomeEvent){
                            refreshLayout.setRefreshing(true);
                            onRefresh();
                        }

                    }
                });
    }

    @Override
    public HomeContentPresenter initPresenter() {
        return new HomeContentPresenter();
    }


    @Override
    public void onRefresh() {
        page = 1;
        getPresenter().getGankHttp(Constant.REFRESH,HomeFragment.HOME_TAG[index],page);
    }

    @Override
    public void onLoadMoreRequested() {
        refreshLayout.setEnabled(false);
        if (dataCount < Constant.PAGESIZE){
            adapter.loadMoreEnd(false);
        }
        else {
            page ++;
            getPresenter().getGankHttp(Constant.LOADMORE,HomeFragment.HOME_TAG[index],page);
        }
        refreshLayout.setEnabled(true);
    }

    @Override
    public void toast(String msg) {
        refreshLayout.setRefreshing(false);
        ToastUtils.showShortToast(msg);
        emptyLayout.showError();
    }

    @Override
    public void getGankData(List<GankBean> data,int flag) {
        dataCount = data.size();
        if (flag == Constant.REFRESH){
            if (data.size()>0){
                adapter.setNewData(data);
                adapter.notifyDataSetChanged();
                emptyLayout.hide();
            }
            else {
                emptyLayout.showEmpty();
            }
            refreshLayout.setRefreshing(false);
        }
        else {
            adapter.addData(data);
            adapter.notifyDataSetChanged();
            adapter.loadMoreComplete();
        }

    }

    @Override
    public void thumbGankSuc(boolean isThumb) {
        if (isThumb){
            ToastUtils.showShortToast("已点赞");
        }else {
            ToastUtils.showShortToast("取消点赞");
        }
    }

    @Override
    public void likeGankSuc(boolean isLike) {
        if (isLike){
            ToastUtils.showShortToast("已喜欢");
        }
        else {
            ToastUtils.showShortToast("取消喜欢");
        }
    }

    @Override
    public void collectGankSuc(boolean isCollect) {
        if (isCollect){
            ToastUtils.showShortToast("已收藏");
        }
        else
        {
            ToastUtils.showShortToast("取消收藏");
        }
    }

    private class HomeContentAdapter extends BaseQuickAdapter<GankBean,BaseViewHolder>{

        UserBean userBean = UserBean.getCurrentUser();

        public HomeContentAdapter() {
            super(R.layout.adapter_home_gank);
        }

        @Override
        protected void convert(BaseViewHolder helper, final GankBean item) {
            helper.setText(R.id.tv_gank_name,item.getWho());
            helper.setText(R.id.tv_gank_tag,item.getType()+"/"+item.getSource());
            helper.setText(R.id.tv_gank_desc,item.getDesc());
            LikeButton btnLike = helper.getView(R.id.like_button);
            LikeButton btnThumb = helper.getView(R.id.thumb_button);
            LikeButton btnCollect = helper.getView(R.id.collect_button);

            if (userBean != null){
                if (userBean.getCollectGankIds() != null){
                    if (userBean.getCollectGankIds().contains(item.get_id())){
                        btnCollect.setLiked(true);
                    }
                    else {
                        btnCollect.setLiked(false);
                    }
                }
                if (userBean.getLikeGankIds() != null){
                    if (userBean.getLikeGankIds().contains(item.get_id())){
                        btnLike.setLiked(true);
                    }
                    else {
                        btnLike.setLiked(false);
                    }
                }
                if (userBean.getThumbGankIds() != null){
                    if (userBean.getThumbGankIds().contains(item.get_id())){
                        btnThumb.setLiked(true);
                    }
                    else {
                        btnThumb.setLiked(false);
                    }
                }
            }

            btnLike.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                        getPresenter().likeGank(item.get_id(),true);
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    getPresenter().likeGank(item.get_id(),false);
                }
            });


            btnThumb.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    getPresenter().thumbGank(item.get_id(),true);
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    getPresenter().thumbGank(item.get_id(),false);
                }
            });

            btnCollect.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    getPresenter().collectGank(item.get_id(),true);
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    getPresenter().collectGank(item.get_id(),false);
                }
            });


            ImageView iv = helper.getView(R.id.iv_img);
            if (item.getImages() != null){
                if (item.getImages().size()>0){
                    iv.setVisibility(View.VISIBLE);
                    GlideUtils.loadImageView(activity,item.getImages().get(0),iv);
                }
            }
            else {
                iv.setVisibility(View.GONE);
            }
        }

    }
}
