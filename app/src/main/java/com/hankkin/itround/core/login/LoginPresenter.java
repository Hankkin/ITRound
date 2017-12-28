package com.hankkin.itround.core.login;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.LogInCallback;
import com.hankkin.itround.Constant;
import com.hankkin.itround.bean.UserBean;
import com.hankkin.itround.callback.QueryFollowCallBack;
import com.hankkin.itround.manage.UserManager;
import com.hankkin.library.base.BasePresent;

/**
 * Created by hankkin on 2017/10/16.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

public class LoginPresenter extends BasePresent<LoginView>{

    public void loginHttp(String name,String pwd){
        UserBean.logInInBackground(name, pwd, new LogInCallback<UserBean>() {
            @Override
            public void done(final UserBean avUser, AVException e) {
                if (e == null){
                    UserManager.getUserFollow(avUser, new QueryFollowCallBack() {
                        @Override
                        public void querySuc(UserBean userBean) {
                            getView().loginResult(userBean);
                        }

                        @Override
                        public void quetFail(String msg) {
                            getView().toast(msg);
                        }
                    });
                }
                else {
                    getView().toast(e.getMessage());
                }
            }
        },UserBean.class);
    }

}
