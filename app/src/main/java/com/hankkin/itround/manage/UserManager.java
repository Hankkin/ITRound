package com.hankkin.itround.manage;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.hankkin.itround.Constant;
import com.hankkin.itround.bean.UserBean;
import com.hankkin.itround.callback.FindUsersCallBack;
import com.hankkin.itround.callback.QueryFollowCallBack;
import com.hankkin.itround.chat.PushManager;
import com.hankkin.library.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.chatkit.LCChatKit;

/**
 * Created by hankkin on 2017/10/17.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

public class UserManager {

    private static UserManager instance = new UserManager();
    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public static boolean isLogin() {
        if (UserBean.getCurrentUser() != null) {
            return true;
        } else {
            return false;
        }
    }

    public static String getUid() {
        return Utils.getSpUtils().getString(Constant.USER_ID);
    }


    public static void logoutUser(){
        LCChatKit.getInstance().close(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                Utils.getSpUtils().clear();
            }
        });
        PushManager.getInstance().unsubscribeCurrentUserChannel();
        UserBean.logOut();
    }


    public static void getUserInfo(final GetCallback<UserBean> callback) {
        AVQuery<UserBean> query = UserBean.getQuery(UserBean.class);
        query.getInBackground(UserBean.getCurrentUserId(), new GetCallback<UserBean>() {
            @Override
            public void done(UserBean user, AVException e) {
                callback.done(user, e);
            }
        });
    }

    public static void getUserFollow(final UserBean avUser, final QueryFollowCallBack callBack){
        final AVQuery<AVObject> query = new AVQuery<>(Constant.FOLLOWEE);
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int i, AVException e) {
                if (e == null)
                {
                    avUser.setStars(i);
                    AVQuery<AVObject> query1 = new AVQuery<>(Constant.FOLLOWER);
                    query1.countInBackground(new CountCallback() {
                        @Override
                        public void done(int i, AVException e) {
                            if (e == null){
                                avUser.setFollowers(i);
                                callBack.querySuc(avUser);
                            }
                            else {
                                callBack.quetFail(e.getMessage());
                            }
                        }
                    });
                }
                else {
                    callBack.quetFail(e.getMessage());
                }
            }
        });
    }

}
