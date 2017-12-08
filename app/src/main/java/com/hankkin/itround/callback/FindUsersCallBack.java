package com.hankkin.itround.callback;

import com.hankkin.itround.bean.UserBean;

import java.util.List;

/**
 * Created by huanghaijie on 2017/12/8.
 */

public interface FindUsersCallBack {
    void findAllUser(List<UserBean> data);
    void findFail(String msg);
}
