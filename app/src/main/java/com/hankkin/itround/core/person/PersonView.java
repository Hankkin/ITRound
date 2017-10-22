package com.hankkin.itround.core.person;

import com.hankkin.itround.bean.UserBean;
import com.hankkin.library.base.BaseView;

/**
 * Created by hankkin on 2017/10/18.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

public interface PersonView  extends BaseView{
    void updateInfo(UserBean userBean);
}
