package com.hankkin.itround.manage;

import android.text.TextUtils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.hankkin.itround.Constant;
import com.hankkin.itround.bean.AVObjectToModel;
import com.hankkin.itround.bean.UserBean;
import com.hankkin.itround.callback.FindUsersCallBack;
import com.hankkin.library.utils.Utils;

import java.util.ArrayList;
import java.util.List;

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

    public UserBean userBean;


    public UserBean getUserBean() {
        if (userBean == null){
            return readUser();
        }
        else {
            return userBean;
        }
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public static boolean isLogin() {
        if (!TextUtils.isEmpty(Utils.getSpUtils().getString(Constant.USER_ID))) {
            return true;
        } else {
            return false;
        }
    }

    public static String getUid() {
        return Utils.getSpUtils().getString(Constant.USER_ID);
    }

    public static void saveUser(AVUser user) {
        Utils.getSpUtils().saveObject(Constant.USER, new AVObjectToModel(user).getModel(UserBean.class));
        Utils.getSpUtils().put(Constant.USER_ID, user.getObjectId());
    }

    public static UserBean readUser() {
        UserBean userBean = Utils.getSpUtils().getObject(Constant.USER, UserBean.class);
        getInstance().setUserBean(userBean);
        return userBean;
    }

    public static void getUserInfo(final GetCallback<AVUser> callback) {
        AVQuery<AVUser> query = new AVQuery<>("_User");
        query.getInBackground(UserManager.getUid(), new GetCallback<AVUser>() {
            @Override
            public void done(AVUser user, AVException e) {
                callback.done(user, e);
            }
        });
    }

    /**
     * 查询所有用户
     * @param callBack
     */
    public static void queryAllUser(final FindUsersCallBack callBack){
        AVQuery<AVUser> query = new AVQuery<>("_User");
        query.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                if (e == null){
                    List<UserBean> userBeanList = new ArrayList<>();
                    for (AVUser user : list){
                        UserBean userBean = new AVObjectToModel(user).getModel(UserBean.class);
                        userBeanList.add(userBean);
                    }
                    callBack.findAllUser(userBeanList);
                }
                else {
                    callBack.findFail(e.getMessage());
                }
            }
        });
    }

}
