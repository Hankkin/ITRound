package com.hankkin.itround.core.login;

import com.hankkin.library.base.BaseView;

/**
 * Created by hankkin on 2017/10/16.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

public interface RegisterView extends BaseView{
    void registerUser();
    void queryUser(boolean isReg);
}
