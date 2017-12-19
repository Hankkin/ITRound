package com.hankkin.itround.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.hankkin.itround.MainActivity;
import com.hankkin.itround.R;
import com.hankkin.itround.bean.UserBean;
import com.hankkin.itround.chat.UserCacheUtils;
import com.hankkin.itround.core.login.LoginPresenter;
import com.hankkin.itround.core.login.LoginView;
import com.hankkin.itround.event.EventMap;
import com.hankkin.itround.manage.UserManager;
import com.hankkin.itround.ui.login.RegisterActivity;
import com.hankkin.library.base.MvpActivity;
import com.hankkin.library.rx.RxBus;
import com.hankkin.library.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.leancloud.chatkit.LCChatKit;

public class LoginActivity extends MvpActivity<LoginView,LoginPresenter> implements LoginView {


    @BindView(R.id.root) RelativeLayout root;
    @BindView(R.id.scrollView) ScrollView mScrollView;
    @BindView(R.id.iv_login_logo) ImageView ivLogo;
    @BindView(R.id.tv_login_welcome) TextView tvWelcome;
    @BindView(R.id.iv_login_clear_pwd) ImageView ivClearPwd;
    @BindView(R.id.iv_login_clean_phone) ImageView ivClearName;
    @BindView(R.id.et_login_pwd) EditText etPwd;
    @BindView(R.id.et_login_name) EditText etName;

    private int screenHeight = 0;//屏幕高度
    private int keyHeight = 0; //软件盘弹起后所占高度
    private float scale = 0.6f; //logo缩放比例


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
//        StatusBarUtil.setTransparent(this);
    }

    @Override
    protected void initData() {
        init();
    }


    private void init(){
        screenHeight = this.getResources().getDisplayMetrics().heightPixels; //获取屏幕高度
        keyHeight = screenHeight / 3;//弹起高度为屏幕高度的1/3
        mScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        watchET();

        root.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                /* old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值
              现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起*/
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
                    Log.e("wenzhihao", "up------>"+(oldBottom - bottom));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mScrollView.smoothScrollTo(0, mScrollView.getHeight());
                        }
                    }, 0);
                    zoomIn(ivLogo, (oldBottom - bottom) - keyHeight);
                    zoomIn(tvWelcome, (oldBottom - bottom) - keyHeight+70);
                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
                    Log.e("wenzhihao", "down------>"+(bottom - oldBottom));
                    //键盘收回后，logo恢复原来大小，位置同样回到初始位置
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mScrollView.smoothScrollTo(0, mScrollView.getHeight());
                        }
                    }, 0);
                    zoomOut(ivLogo, (bottom - oldBottom) - keyHeight);
                    zoomOut(tvWelcome, (bottom - oldBottom) - keyHeight);
                }
            }
        });
    }

    /**
     * 缩小
     * @param view
     */
    public void zoomIn(final View view, float dist) {
        view.setPivotY(view.getHeight());
        view.setPivotX(view.getWidth() / 2);
        AnimatorSet mAnimatorSet = new AnimatorSet();
        ObjectAnimator mAnimatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, scale);
        ObjectAnimator mAnimatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, scale);
        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(view, "translationY", 0.0f, -dist);

        mAnimatorSet.play(mAnimatorTranslateY).with(mAnimatorScaleX);
        mAnimatorSet.play(mAnimatorScaleX).with(mAnimatorScaleY);
        mAnimatorSet.setDuration(300);
        mAnimatorSet.start();
    }

    /**
     * f放大
     * @param view
     */
    public void zoomOut(final View view, float dist) {
        view.setPivotY(view.getHeight());
        view.setPivotX(view.getWidth() / 2);
        AnimatorSet mAnimatorSet = new AnimatorSet();

        ObjectAnimator mAnimatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", scale, 1.0f);
        ObjectAnimator mAnimatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", scale, 1.0f);
        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(view, "translationY", view.getTranslationY(), 0);

        mAnimatorSet.play(mAnimatorTranslateY).with(mAnimatorScaleX);
        mAnimatorSet.play(mAnimatorScaleX).with(mAnimatorScaleY);
        mAnimatorSet.setDuration(300);
        mAnimatorSet.start();
    }

    @OnClick(R.id.iv_login_clean_phone)
    void clearName(){
        etName.setText("");
    }

    @OnClick(R.id.iv_login_clear_pwd)
    void clearPwd(){
        etPwd.setText("");
    }

    @OnClick(R.id.tv_login_reg)
    void btnRegClick(){
        gotoActivity(RegisterActivity.class);
    }

    @OnClick(R.id.tv_login_forget)
    void btnForgetClick(){
//        Intent intent = new Intent(this,ForgetActivity.class);
//        startActivity(intent);
    }


    @OnClick(R.id.tv_login_login)
    void btnLoginClick(){
        if (TextUtils.isEmpty(etName.getText().toString())){
            ToastUtils.showShortToast("请输入用户名");
            return;
        }
        if (TextUtils.isEmpty(etPwd.getText().toString())){
            ToastUtils.showShortToast("请输入密码");
            return;
        }
        showProgress();
        getPresenter().loginHttp(etName.getText().toString(),etPwd.getText().toString());

    }

    private void watchET(){
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && ivClearName.getVisibility() == View.GONE) {
                    ivClearName.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    ivClearName.setVisibility(View.GONE);
                }
            }
        });

        etPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && ivClearPwd.getVisibility() == View.GONE) {
                    ivClearPwd.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    ivClearPwd.setVisibility(View.GONE);
                }
                if (s.toString().isEmpty()){
                    return;
                }
                if (!s.toString().matches("[A-Za-z0-9]+")) {
                    String temp = s.toString();
                    s.delete(temp.length() - 1, temp.length());
                    etPwd.setSelection(s.length());
                }
            }
        });
    }


    @Override
    public void toast(String msg) {
        ToastUtils.showShortToast(msg);
        hideProgress();
    }

    @Override
    public void loginResult(UserBean user) {
        if (UserBean.getCurrentUser() != null){
            UserBean.getCurrentUser().updateUserInfo();
        }
        UserCacheUtils.cacheUser(UserBean.getCurrentUser());
        LCChatKit.getInstance().open(user.getObjectId(), new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (e == null){
                    ToastUtils.showShortToast("登录成功");
                }
                else {
                    ToastUtils.showShortToast(e.getMessage());
                }
                RxBus.getDefault().post(new EventMap.LoginEvent());
                hideProgress();
                gotoActivity(MainActivity.class);
            }
        });

    }

    @Override
    public LoginPresenter initPresenter() {
        return new LoginPresenter();
    }
}
