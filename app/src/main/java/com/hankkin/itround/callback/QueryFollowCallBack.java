package com.hankkin.itround.callback;

import com.hankkin.itround.bean.UserBean;

/**
 * Created by huanghaijie on 2017/12/22.
 */

public interface QueryFollowCallBack {
    void querySuc(UserBean userBean);
    void quetFail(String msg);
}
