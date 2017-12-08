package com.hankkin.itround.bean;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hankkin on 2017/10/18.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

public class UserBean extends BaseBean {

    public static final int BOY = 0;
    public static final int GIRL = 1;

    public UserBean(){super();}
    private String objectId;
    private String username;
    private String name;
    private int sex;
    private Map<String,Object> headerImage;
    private String email;
    private String occupation;
    private String password;
    private String blog;
    private int stars;
    private int followers;
    private String desc;
    private List<String> thumbGankIds = new ArrayList<>();
    private List<String> likeGankIds = new ArrayList<>();
    private List<String> collectGankIds = new ArrayList<>();

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

    public String rerturnHeaderImageUrl(){
        return returnImg(headerImage);
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        if (TextUtils.isEmpty(name)){
            return username;
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public Map<String, Object> getHeaderImage() {
        return headerImage;
    }

    public void setHeaderImage(Map<String, Object> headerImage) {
        this.headerImage = headerImage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
