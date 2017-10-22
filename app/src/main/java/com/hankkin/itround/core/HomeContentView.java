package com.hankkin.itround.core;

import com.hankkin.itround.bean.GankBean;
import com.hankkin.library.base.BaseView;

import java.util.List;

/**
 * Created by hankkin on 2017/10/12.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

public interface HomeContentView extends BaseView {

    void getGankData(List<GankBean> data,int flag);
    void thumbGankSuc(boolean isThumb);
    void likeGankSuc(boolean isLike);
    void collectGankSuc(boolean isCollect);

}
