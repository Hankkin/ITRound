package com.hankkin.itround.ui.login;

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

import com.hankkin.itround.R;
import com.hankkin.itround.core.login.RegisterPresenter;
import com.hankkin.itround.core.login.RegisterView;
import com.hankkin.itround.widget.CodeView;
import com.hankkin.library.base.MvpActivity;
import com.hankkin.library.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author hankkin
 */
public class RegisterActivity extends MvpActivity<RegisterView,RegisterPresenter> implements RegisterView {


    @BindView(R.id.root) RelativeLayout root;
    @BindView(R.id.scrollView) ScrollView mScrollView;
    @BindView(R.id.iv_reg_logo) ImageView ivLogo;
    @BindView(R.id.tv_reg_welcome) TextView tvWelcome;
    @BindView(R.id.iv_reg_clean_pwd) ImageView ivClearPwd;
    @BindView(R.id.et_reg_pwd) EditText etPwd;
    @BindView(R.id.et_reg_code) EditText etCode;
    @BindView(R.id.codeView) CodeView tvGetCode;
    @BindView(R.id.et_reg_name) EditText etTel;
    private int screenHeight = 0;
    private int keyHeight = 0;
    private float scale = 0.6f;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        init();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void toast(String msg) {
        ToastUtils.showLongToast(msg);
        hideProgress();
    }



    private void init() {
        //获取屏幕高度
        screenHeight = this.getResources().getDisplayMetrics().heightPixels;
        //弹起高度为屏幕高度的1/3
        keyHeight = screenHeight / 3;
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
                    Log.e("wenzhihao", "up------>" + (oldBottom - bottom));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mScrollView.smoothScrollTo(0, mScrollView.getHeight());
                        }
                    }, 0);
                    ivLogo.setVisibility(View.GONE);
                    tvWelcome.setVisibility(View.GONE);
                    zoomIn(ivLogo, (oldBottom - bottom) - keyHeight);
                    zoomIn(tvWelcome, (oldBottom - bottom) - keyHeight + 60);
                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
                    Log.e("wenzhihao", "down------>" + (bottom - oldBottom));
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
     *
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
     *
     * @param view
     */
    public void zoomOut(final View view, float dist) {
        ivLogo.setVisibility(View.VISIBLE);
        tvWelcome.setVisibility(View.VISIBLE);
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


    @OnClick(R.id.iv_reg_clean_pwd)
    void clearPwd() {
        etPwd.setText("");
    }

    @OnClick(R.id.codeView)
    void getCode(){
        tvGetCode.refresh();
    }

    @OnClick(R.id.tv_reg_reg)
    void regHttp(){
        if (TextUtils.isEmpty(etTel.getText().toString())){
            ToastUtils.showShortToast("请输入手机号");
            return;
        }
        if (TextUtils.isEmpty(etPwd.getText().toString())){
            ToastUtils.showShortToast("请输入密码");
            return;
        }
        if (TextUtils.isEmpty(etCode.getText().toString())){
            ToastUtils.showShortToast("请输入验证码");
            return;
        }
        if (!etCode.getText().toString().equals(tvGetCode.getTheCode())){
            ToastUtils.showShortToast("验证码不正确");
            return;
        }
        showProgress();
        getPresenter().queryUsers(etTel.getText().toString());
    }


    @OnClick(R.id.tv_reg_login)
    void goLogin(){
        finish();
    }

    /**
     * 输入框监听
     */
    private void watchET() {
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
    public void registerUser() {
        hideProgress();
        toast("注册成功...");
        finish();
    }

    @Override
    public void queryUser(boolean isReg) {
        if (isReg){
            ToastUtils.showShortToast("手机号已被注册");
            return;
        }
        else {
            getPresenter().register(etTel.getText().toString(),
                    etPwd.getText().toString(),
                    etCode.getText().toString());
        }
    }

    @Override
    public RegisterPresenter initPresenter() {
        return new RegisterPresenter();
    }
}
