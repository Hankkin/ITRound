package com.hankkin.itround.bean;

import android.text.TextUtils;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.FollowCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by hankkin on 2017/10/18.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */
@AVClassName("UserBean")
public class UserBean extends AVUser {

    public static final int BOY = 0;
    public static final int GIRL = 1;

    public static final String AVATAR = "avatar";
    public static final String LOCATION = "location";
    public static final String INSTALLATION = "installation";

    public UserBean(){super();}
    private String name;
    private int sex;
    private String occupation;
    private String password;
    private String blog;
    private int stars;
    private int followers;
    private String desc;
    private List<String> thumbGankIds = new ArrayList<>();
    private List<String> likeGankIds = new ArrayList<>();
    private List<String> collectGankIds = new ArrayList<>();


    public static String getCurrentUserId () {
        UserBean currentUser = getCurrentUser(UserBean.class);
        return (null != currentUser ? currentUser.getObjectId() : null);
    }


    public String getAvatarUrl() {
        AVFile avatar = getAVFile(AVATAR);
        if (avatar != null) {
            return avatar.getUrl();
        } else {
            return null;
        }
    }


    public void saveAvatar(String path, final SaveCallback saveCallback) {
        final AVFile file;
        try {
            file = AVFile.withAbsoluteLocalPath(getUsername(), path);
            put(AVATAR, file);
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (null == e) {
                        saveInBackground(saveCallback);
                    } else {
                        if (null != saveCallback) {
                            saveCallback.done(e);
                        }
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static UserBean getCurrentUser() {
        return getCurrentUser(UserBean.class);
    }

    public void updateUserInfo() {
        AVInstallation installation = AVInstallation.getCurrentInstallation();
        if (installation != null) {
            put(INSTALLATION, installation);
            saveInBackground();
        }
    }

    public AVGeoPoint getGeoPoint() {
        return getAVGeoPoint(LOCATION);
    }

    public void setGeoPoint(AVGeoPoint point) {
        put(LOCATION, point);
    }

    public static void signUpByNameAndPwd(String name, String password, SignUpCallback callback) {
        AVUser user = new AVUser();
        user.setUsername(name);
        user.setPassword(password);
        user.signUpInBackground(callback);
    }

    public void removeFriend(String friendId, final SaveCallback saveCallback) {
        unfollowInBackground(friendId, new FollowCallback() {
            @Override
            public void done(AVObject object, AVException e) {
                if (saveCallback != null) {
                    saveCallback.done(e);
                }
            }
        });
    }

    public void findFriendsWithCachePolicy(AVQuery.CachePolicy cachePolicy, FindCallback<UserBean>
            findCallback) {
        AVQuery<UserBean> q = null;
        try {
            q = followeeQuery(UserBean.class);
        } catch (Exception e) {
        }
        q.setCachePolicy(cachePolicy);
        q.setMaxCacheAge(TimeUnit.MINUTES.toMillis(1));
        q.findInBackground(findCallback);
    }



    public List<String> getThumbGankIds() {
        return thumbGankIds;
    }

    public void setThumbGankIds(List<String> thumbGankIds) {
        this.thumbGankIds = thumbGankIds;
    }

    public List<String> getLikeGankIds() {
        return likeGankIds;
    }

    public void setLikeGankIds(List<String> likeGankIds) {
        this.likeGankIds = likeGankIds;
    }

    public List<String> getCollectGankIds() {
        return collectGankIds;
    }

    public void setCollectGankIds(List<String> collectGankIds) {
        this.collectGankIds = collectGankIds;
    }

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }



    public String getName() {
        if (TextUtils.isEmpty(name)){
            return getUsername().substring(0,3)+"***"+getUsername().substring(getUsername().length()-4,getUsername().length());
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return (int) get("sex");
    }

    public void setSex(int sex) {
        this.sex = sex;
    }





    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
