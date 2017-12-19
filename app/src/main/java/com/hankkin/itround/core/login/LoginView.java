package com.hankkin.itround.core.login;

import com.avos.avoscloud.AVUser;
import com.hankkin.itround.bean.UserBean;
import com.hankkin.library.base.BaseView;

/**
 * Created by hankkin on 2017/10/16.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

public interface LoginView extends BaseView {

    void loginResult(UserBean user);
}
