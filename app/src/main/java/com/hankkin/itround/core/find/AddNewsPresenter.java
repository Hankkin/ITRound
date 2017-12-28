package com.hankkin.itround.core.find;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.hankkin.itround.bean.FinsNewsBean;
import com.hankkin.library.base.BasePresent;

import java.util.List;

/**
 * Created by huanghaijie on 2017/12/26.
 */

public class AddNewsPresenter extends BasePresent<AddNewsView> {


    public void addNewsHttp(FinsNewsBean bean){
        AVObject finsNewsBean = new AVObject("FindNewsBean");
        finsNewsBean.put("content",bean.getContent());
        finsNewsBean.put("urlsList",bean.urlsList);
        finsNewsBean.put("tag",bean.getTag());
        finsNewsBean.put("owner",bean.owner);
        finsNewsBean.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null){
                    getView().addNewsSuc();
                }
                else {
                    getView().addNewsFail(e.getMessage());
                }
            }
        });
    }


}
