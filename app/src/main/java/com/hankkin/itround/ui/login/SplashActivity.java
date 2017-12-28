package com.hankkin.itround.ui.login;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.hankkin.itround.MainActivity;
import com.hankkin.itround.R;
import com.hankkin.itround.bean.UserBean;
import com.hankkin.itround.callback.QueryFollowCallBack;
import com.hankkin.itround.chat.UserCacheUtils;
import com.hankkin.itround.manage.UserManager;
import com.hankkin.library.base.BaseAcitvity;
import com.hankkin.library.utils.LogUtils;

import cn.leancloud.chatkit.LCChatKit;

public class SplashActivity extends BaseAcitvity {
    private static final String TAG = "SplashActivity";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        if (UserManager.isLogin()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    imLogin();
                }
            },2000);

        }
        else {
            gotoActivity(MainActivity.class,true);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void toast(String msg) {

    }

    private void imLogin() {
        UserBean.getCurrentUser().updateUserInfo();
        UserManager.getUserFollow(UserBean.getCurrentUser(), new QueryFollowCallBack() {
            @Override
            public void querySuc(UserBean userBean) {
                UserBean.changeCurrentUser(userBean,true);
                UserCacheUtils.cacheUser(userBean);
                UserBean.changeCurrentUser(userBean,true);
                LCChatKit.getInstance().open(UserBean.getCurrentUserId(), new AVIMClientCallback() {
                    @Override
                    public void done(AVIMClient avimClient, AVIMException e) {
                        if (filterException(e)) {
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }

            @Override
            public void quetFail(String msg) {

            }
        });

    }
}
