package com.hankkin.itround.ui.fg;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.clans.fab.FloatingActionButton;
import com.hankkin.itround.Constant;
import com.hankkin.itround.R;
import com.hankkin.itround.bean.FinsNewsBean;
import com.hankkin.itround.bean.UserBean;
import com.hankkin.itround.chat.UserCacheUtils;
import com.hankkin.itround.ui.PersonActivity;
import com.hankkin.itround.ui.circle.AddNewsActivity;
import com.hankkin.itround.widget.GradationScrollView;
import com.hankkin.library.base.BaseFragment;
import com.hankkin.library.utils.GlideUtils;
import com.hankkin.library.utils.ScreenUtils;
import com.hankkin.library.utils.TimeUtils;
import com.hankkin.library.utils.ToastUtils;
import com.hankkin.library.view.CustomLoadMoreView;
import com.hankkin.library.view.FullyLinearLayoutManager;
import com.hankkin.library.view.RecycleViewDivider;
import com.hankkin.library.view.imgwatch.ImageWatcher;
import com.hankkin.library.view.imgwatch.MessagePicturesLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hankkin on 2017/10/12.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

public class FindFragment extends BaseFragment implements
        SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener,
        MessagePicturesLayout.Callback, ImageWatcher.OnPictureLongPressListener {

    @BindView(R.id.scrollView)
    GradationScrollView scrollView;
    @BindView(R.id.iv_banner)
    ImageView ivBanner;
    @BindView(R.id.tv_find_title)
    TextView tvTitle;
    @BindView(R.id.ll_find_header)
    LinearLayout llHeader;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.rv_users)
    RecyclerView rvUsers;
    //    @BindView(R.id.empty_find) EmptyLayout emptyLayout;
    @BindView(R.id.rv_find)
    RecyclerView rv;
    @BindView(R.id.fab_find_add)
    FloatingActionButton fbAdd;

    private int height;
    private UsersAdapter adapter;
    private NewsAdapter newsAdapter;
    private int dataCount = 0;
    private ImageWatcher imageWatcher;

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
        initSwipe(refreshLayout, this);

        newsAdapter = new NewsAdapter();

        rv.setLayoutManager(new FullyLinearLayoutManager(activity) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rv.addItemDecoration(new RecycleViewDivider(activity, LinearLayoutManager.HORIZONTAL));
        newsAdapter.setLoadMoreView(new CustomLoadMoreView());
        newsAdapter.setOnLoadMoreListener(this, rv);
        newsAdapter.disableLoadMoreIfNotFullPage(rv);
        rv.setAdapter(newsAdapter);
        fbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoActivity(AddNewsActivity.class);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_find;
    }

    @Override
    protected void initData() {
        //设置可获得焦点
        tvTitle.setFocusable(true);
        tvTitle.setFocusableInTouchMode(true);
        //请求获得焦点
        tvTitle.requestFocus();
        imageWatcher = ImageWatcher.Helper.with(activity) // 一般来讲， ImageWatcher 需要占据全屏的位置
                .setTranslucentStatus(ScreenUtils.getStatusBarHeight(activity)) // 如果是透明状态栏，你需要给ImageWatcher标记 一个偏移值，以修正点击ImageView查看的启动动画的Y轴起点的不正确
                .setErrorImageRes(R.mipmap.pic_error) // 配置error图标 如果不介意使用lib自带的图标，并不一定要调用这个API
                .setOnPictureLongPressListener(this) // 长按图片的回调，你可以显示一个框继续提供一些复制，发送等功能
                .setLoader(new ImageWatcher.Loader() {
                    @Override
                    public void load(Context context, String url, final ImageWatcher.LoadCallback lc) {


                        Glide.with(activity).load(url).into(new SimpleTarget<GlideDrawable>() {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                lc.onResourceReady(resource);
                            }

                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                lc.onLoadFailed(errorDrawable);
                            }

                            @Override
                            public void onLoadStarted(Drawable placeholder) {
                                lc.onLoadStarted(placeholder);
                            }
                        });

                    }
                })
                .create();
        getFriends();
    }

    private void getFriends() {

        AVQuery<UserBean> q = UserBean.getQuery(UserBean.class);
        q.limit(10);
        final UserBean user = UserBean.getCurrentUser();
        List<String> friendIds = new ArrayList<String>();
        friendIds.add(user.getObjectId());
        q.whereNotContainedIn(Constant.OBJECT_ID, friendIds);
        q.orderByDescending(Constant.UPDATED_AT);
        q.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
        q.findInBackground(new FindCallback<UserBean>() {
            @Override
            public void done(List<UserBean> list, AVException e) {
                if (e == null) {
                    UserCacheUtils.cacheUsers(list);
                    List<UserBean> userBeanList = new ArrayList<>();
                    for (UserBean user : list) {
                        userBeanList.add(user);
                    }
                    rvUsers.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                    adapter = new UsersAdapter();
                    adapter.setNewData(userBeanList);
                    rvUsers.setAdapter(adapter);
                    adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            UserBean userBean = (UserBean) adapter.getItem(position);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("id", userBean.getObjectId());
                            bundle.putBoolean(PersonActivity.CURRENT, false);
                            gotoActivity(PersonActivity.class, false, bundle);
                        }
                    });
                    getNews(false);
                } else {
                    ToastUtils.showShortToast(e.getMessage());
                    refreshLayout.setRefreshing(false);
                }

            }
        });
    }

    private void getNews(final boolean isLoad) {
        AVQuery<FinsNewsBean> query = new AVQuery<>(Constant.FINDNEWSBEAN);
        query.limit(Constant.PAGELIMIT);
        query.include("owner");
        query.include("urlsList");
        query.orderByDescending("createdAt");
        if (isLoad) {
            dataCount = newsAdapter.getItemCount();
        } else {
            dataCount = 0;
        }
        query.skip(dataCount);
        query.findInBackground(new FindCallback<FinsNewsBean>() {
            @Override
            public void done(List<FinsNewsBean> list, AVException e) {
                if (list != null) {
                    if (!isLoad) {
                        newsAdapter.setNewData(list);
                        newsAdapter.notifyDataSetChanged();
                        refreshLayout.setRefreshing(false);
                    } else {
                        newsAdapter.addData(list);
                        newsAdapter.notifyDataSetChanged();
                    }
                    dataCount = newsAdapter.getItemCount();
                }
                newsAdapter.loadMoreComplete();
                filterException(e);
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
                            llHeader.setBackgroundColor(Color.argb((int) alpha, 24, 166, 94));
                            if (y >= height / 2) {
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

    @Override
    public void onLoadMoreRequested() {
        refreshLayout.setEnabled(false);
        if (dataCount < Constant.PAGESIZE) {
            newsAdapter.loadMoreEnd(false);
        } else {
            getNews(true);
        }
        refreshLayout.setEnabled(true);
    }

    @Override
    public void onThumbPictureClick(ImageView i, List<ImageView> imageGroupList, List<String> urlList) {
        imageWatcher.show(i, imageGroupList, urlList);
    }

    @Override
    public void onPictureLongPress(ImageView v, String url, int pos) {
        Toast.makeText(v.getContext().getApplicationContext(), "长按了第" + (pos + 1) + "张图片", Toast.LENGTH_SHORT).show();
    }

    private class NewsAdapter extends BaseQuickAdapter<FinsNewsBean, BaseViewHolder> {

        public NewsAdapter() {
            super(R.layout.adapter_news);
        }

        @Override
        protected void convert(BaseViewHolder helper, FinsNewsBean item) {
            helper.setText(R.id.tv_adapter_news_user_name, item.getOwner().getName());
            helper.setText(R.id.tv_adapter_news_time, TimeUtils.date2String(item.getCreatedAt()));
            helper.setText(R.id.tv_adapter_news_content, item.getContent());
            helper.setText(R.id.tv_adapter_news_tag, "#" + item.getTag() + "#");
            if (item.getUrlsList() != null) {
                MessagePicturesLayout messagePicturesLayout = helper.getView(R.id.l_pictures);
                List<String> thumbs = new ArrayList<>();
                List<String> urls = new ArrayList<>();
                for (AVFile i : item.getUrlsList()) {
                    if (!TextUtils.isEmpty(i.getUrl())){
                        thumbs.add(i.getThumbnailUrl(true,200,200));
                        urls.add(i.getUrl());
                    }
                }
                messagePicturesLayout.set(thumbs, urls);
                messagePicturesLayout.setCallback(FindFragment.this);
            }

        }
    }

    private class UsersAdapter extends BaseQuickAdapter<UserBean, BaseViewHolder> {
        public UsersAdapter() {
            super(R.layout.adapter_circle_users);
        }

        @Override
        protected void convert(BaseViewHolder helper, UserBean item) {
            helper.setText(R.id.tv_adapter_cir_user_name, item.getName());
            CircleImageView iv = helper.getView(R.id.img_item);
            if (!TextUtils.isEmpty(item.getAvatarUrl())) {
                GlideUtils.loadImageView(activity, item.getAvatarUrl(), iv);
            }
        }
    }

}
