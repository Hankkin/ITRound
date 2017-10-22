package com.hankkin.itround.core.login;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SignUpCallback;
import com.hankkin.library.base.BasePresent;

import java.util.List;

/**
 * Created by hankkin on 2017/10/16.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

public class RegisterPresenter extends BasePresent<RegisterView>{

    public void queryUsers(String name){
        AVQuery<AVUser> query = new AVQuery<>("_User");
        query.whereEqualTo("username",name);
        query.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                if (e == null){
                    if (list != null && list.size()>1){
                        getView().queryUser(true);
                    }
                    else {
                        getView().queryUser(false);
                    }
                }
                else {
                    getView().toast(e.getMessage());
                }
            }
        });
    }


    public void register(String name,String pwd,String code){
        AVUser user = new AVUser();
        user.setUsername(name);
        user.setPassword(pwd);
        user.put("verCode",code);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (e == null){
                    getView().registerUser();
                }
                else {
                    getView().toast(e.getMessage());
                }

            }
        });
    }

}
