package com.hankkin.itround.core.home;

import com.hankkin.itround.Constant;
import com.hankkin.itround.api.GankApiService;
import com.hankkin.itround.bean.PostGankBean;
import com.hankkin.itround.http.HttpUtils;
import com.hankkin.library.base.BasePresent;
import com.hankkin.library.http.BaseObserver;
import com.hankkin.library.http.HttpResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hankkin on 2017/12/5.
 */

public class PostGankPresenter extends BasePresent<PostGankView> {


    public void postGankHttp(PostGankBean bean){
        Map<String,Object> map = new HashMap<>();
        map.put("url",bean.getUrl());
        map.put("desc",bean.getDesc());
        map.put("who",bean.getWho());
        map.put("type",bean.getType());
        map.put("debug", Constant.DEBUG);
        HttpUtils.getInstance().createService(GankApiService.class)
                .postGank(map)
                .compose(this.<HttpResult<String>>setThread())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccees(HttpResult<String> t) throws Exception {
                        getView().postSuc(t.getMsg());
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        getView().toast(e.getMessage());
                    }
                });
    }

}
