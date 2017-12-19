package com.hankkin.itround.core.person;

import android.graphics.Bitmap;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SaveCallback;
import com.hankkin.itround.bean.UserBean;
import com.hankkin.itround.chat.UserCacheUtils;
import com.hankkin.itround.manage.UserManager;
import com.hankkin.library.base.BasePresent;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hankkin on 2017/10/18.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

public class PersonPresenter extends BasePresent<PersonView> {

    /**
     * 更新用户信息并且获取最新用户
     */
    public void updateUserHttp(String key,Object value){
        UserBean userBean = UserBean.getCurrentUser();
        userBean.put(key,value);
        userBean.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null){
                    AVQuery<UserBean> query = UserBean.getQuery(UserBean.class);
                    query.getInBackground(UserManager.getUid(), new GetCallback<UserBean>() {
                        @Override
                        public void done(UserBean user, AVException e) {
                            if (e == null){
                                getView().updateInfo(user);
                                UserCacheUtils.cacheUser(user);
                            }
                            else {
                                getView().toast(e.getMessage());
                            }
                        }
                    });
                }
                else {
                    getView().toast(e.getMessage());
                }
            }
        });

    }

    public void updateIcon(final Bitmap bitmap){
        if (bitmap == null){
            return;
        }
         final Map<String, Object> headImage = new HashMap<>();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        final AVFile file = new AVFile("icon.png", baos.toByteArray());
        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    headImage.put("__type", "File");
                    headImage.put("name", file.getOriginalName());
                    headImage.put("objectId", file.getObjectId());
                    headImage.put("url", file.getUrl());
                    updateUserHttp("headerImage",headImage);
                } else {
                    getView().toast(e.getMessage());
                }
            }
        });
    }
}
