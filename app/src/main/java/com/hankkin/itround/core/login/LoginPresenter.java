package com.hankkin.itround.core.login;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.hankkin.library.base.BasePresent;

/**
 * Created by hankkin on 2017/10/16.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

public class LoginPresenter extends BasePresent<LoginView>{

    public void loginHttp(String name,String pwd){
        AVUser.logInInBackground(name, pwd, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e == null){
                    getView().loginResult(avUser);
                }
                else {
                    getView().toast(e.getMessage());
                }
            }
        });
    }

}
