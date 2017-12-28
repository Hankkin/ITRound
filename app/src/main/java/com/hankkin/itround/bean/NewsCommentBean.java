package com.hankkin.itround.bean;

import com.avos.avoscloud.AVObject;

/**
 * Created by huanghaijie on 2017/12/25.
 */

public class NewsCommentBean extends AVObject {
    private String commentContent;
    private UserBean commentOwner;

    public String getContent() {
        return commentContent;
    }

    public void setContent(String content) {
        this.commentContent = content;
    }

    public UserBean getCommentOwner() {
        return commentOwner;
    }

    public void setCommentOwner(UserBean commentOwner) {
        this.commentOwner = commentOwner;
    }
}
