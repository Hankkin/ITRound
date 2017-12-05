package com.hankkin.itround.ui.fg;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.hankkin.itround.R;
import com.hankkin.itround.bean.AVObjectToModel;
import com.hankkin.itround.bean.UserBean;
import com.hankkin.itround.event.EventMap;
import com.hankkin.itround.manage.UserManager;
import com.hankkin.itround.ui.LoginActivity;
import com.hankkin.itround.ui.PersonActivity;
import com.hankkin.library.base.BaseFragment;
import com.hankkin.library.rx.RxBus;
import com.hankkin.library.utils.GlideUtils;
import com.hankkin.library.utils.ToastUtils;
import com.hankkin.library.view.RippleView;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * Created by hankkin on 2017/10/12.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

public class MeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,RippleView.OnRippleCompleteListener{

    private float mSelfHeight = 0;//用以判断是否得到正确的宽高值
    private float mTitleScale;
    private float mSubScribeScale;
    private float mSubScribeScaleX;
    private float mHeadImgScale;

    @BindView(R.id.iv_head)
    ImageView mHeadImage;
    @BindView(R.id.tv_me_title)
    TextView tvTitle;
    @BindView(R.id.tv_me_set)
    TextView mSubscribe;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;
    @BindView(R.id.my_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.refresh) SwipeRefreshLayout refreshLayout;
    @BindView(R.id.scrollView) NestedScrollView scrollView;
    @BindView(R.id.rip_like) RippleView rpLike;
    @BindView(R.id.rip_share) RippleView rpShare;


    private UserBean userBean;


    public static MeFragment newInstance(int index) {
        MeFragment fragment = new MeFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initViewsAndEvents(View view) {

        initSwipe(refreshLayout,this);

        initAnim();


        rpLike.setOnRippleCompleteListener(this);
        rpShare.setOnRippleCompleteListener(this);


    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initData() {
        operateBus();
        refreshData();
    }

    /**
     * 设置头部布局
     */
    private void setHeaderViews(UserBean user){

        if (UserManager.isLogin()){
            if (user != null){
                if (!TextUtils.isEmpty(user.rerturnHeaderImageUrl())){
                    GlideUtils.loadImageView(activity,user.rerturnHeaderImageUrl(),mHeadImage);
                }
                if (!TextUtils.isEmpty(user.getName())){
                    tvTitle.setText(user.getName());
                }
                else {
                    tvTitle.setText(UserManager.readUser().getUsername());
                }
            }
        }
    }

    /**
     * 头部动画设置
     */
    private void initAnim(){
        final float screenW = getResources().getDisplayMetrics().widthPixels;
        final float toolbarHeight = getResources().getDimension(R.dimen.toolbar_height);
        final float initHeight = getResources().getDimension(R.dimen.subscription_head);
        mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (mSelfHeight == 0) {
                    mSelfHeight = tvTitle.getHeight();
                    float distanceTitle = tvTitle.getTop() + (mSelfHeight - toolbarHeight) / 2.0f;
                    float distanceSubscribe = mSubscribe.getY() + (mSubscribe.getHeight() - toolbarHeight) / 2.0f;
                    float distanceHeadImg = mHeadImage.getY() + (mHeadImage.getHeight() - toolbarHeight) / 2.0f;
                    float distanceSubscribeX = screenW / 2.0f - (mSubscribe.getWidth() / 2.0f + getResources().getDimension(R.dimen.dp_10));
                    mTitleScale = distanceTitle / (initHeight - toolbarHeight);
                    mSubScribeScale = distanceSubscribe / (initHeight - toolbarHeight);
                    mHeadImgScale = distanceHeadImg / (initHeight - toolbarHeight);
                    mSubScribeScaleX = distanceSubscribeX / (initHeight - toolbarHeight);
                }
                float scale = 1.0f - (-verticalOffset) / (initHeight - toolbarHeight);
                mHeadImage.setScaleX(scale);
                mHeadImage.setScaleY(scale);
                mHeadImage.setTranslationY(mHeadImgScale * verticalOffset);
                tvTitle.setTranslationY(mTitleScale * verticalOffset);
                mSubscribe.setTranslationY(mSubScribeScale * verticalOffset);
                mSubscribe.setTranslationX(-mSubScribeScaleX * verticalOffset);
                if (scale == 1){
                    refreshLayout.setEnabled(true);
                }
                else {
                    refreshLayout.setEnabled(false);
                }
            }
        });
    }


    private void refreshData(){
        UserManager.getUserInfo(new GetCallback<AVUser>() {
            @Override
            public void done(AVUser user, AVException e) {
                if (e == null){
                    UserManager.saveUser(user);
                    userBean = new AVObjectToModel(user).getModel(UserBean.class);
                    setHeaderViews(userBean);
                }
                else {
                    ToastUtils.showShortToast("请登录");
                }
                refreshLayout.setRefreshing(false);
            }
        });
    }


    @OnClick(R.id.iv_head)
    void headClick(){
        if (UserManager.isLogin()){
            gotoActivity(PersonActivity.class);
        }
        else {
            gotoActivity(LoginActivity.class);
        }
    }


    @Override
    public void onRefresh() {
        refreshData();
    }

    @Override
    public void onComplete(RippleView rippleView) {
        switch (rippleView.getId()){
            case R.id.rip_like:
                ToastUtils.showShortToast("我喜欢的");
                break;
            //分享
            case R.id.rip_share:
                share();
                break;
            default:
                break;
        }
    }



    public void operateBus(){
        RxBus.getDefault().toObservable(EventMap.BaseEvent.class)
                .subscribe(new Consumer<EventMap.BaseEvent>() {
                    @Override
                    public void accept(EventMap.BaseEvent event) throws Exception {
                        if (event instanceof EventMap.LoginEvent){
                            refreshData();
                        }
                        else if (event instanceof EventMap.UpdateUserEvent){
                            refreshData();
                        }
                    }
                });
    }


    /**
     * 分享
     */
    private void share(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "IT圈");
        intent.putExtra(Intent.EXTRA_TEXT, "在浏览器打开该链接下载 [IT圈] https://fir.im/rnsl");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, "IT圈"));
    }
}
