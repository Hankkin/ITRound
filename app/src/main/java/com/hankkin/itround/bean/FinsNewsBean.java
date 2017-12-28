package com.hankkin.itround.bean;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huanghaijie on 2017/12/25.
 */
@AVClassName("FindNewsBean")
public class FinsNewsBean extends AVObject{
    private String content;
    public List<AVFile> urlsList;
    public UserBean owner;
    private List<NewsCommentBean> comments;
    private String tag;



    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<AVFile> getUrlsList() {
        return JSONObject.parseArray(urlsList.toString(),AVFile.class);
    }

    public void setUrlsList(List<AVFile> urlsList) {
        this.urlsList = urlsList;
    }


    public UserBean getOwner() {
        return AVUser.cast(getAVUser("owner"),UserBean.class);
    }

    public void setOwner(UserBean owner) {
        this.owner = owner;
    }

    public List<NewsCommentBean> getComments() {
        return comments;
    }

    public void setComments(List<NewsCommentBean> comments) {
        this.comments = comments;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


}
