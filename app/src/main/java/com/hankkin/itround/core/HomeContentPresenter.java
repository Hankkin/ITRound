package com.hankkin.itround.core;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.hankkin.itround.Constant;
import com.hankkin.itround.api.GankApiService;
import com.hankkin.itround.bean.GankBean;
import com.hankkin.itround.bean.UserBean;
import com.hankkin.itround.http.HttpUtils;
import com.hankkin.itround.manage.UserManager;
import com.hankkin.library.base.BasePresent;
import com.hankkin.library.http.BaseObserver;
import com.hankkin.library.http.HttpResult;
import com.hankkin.library.utils.ToastUtils;

import java.util.List;

/**
 * Created by hankkin on 2017/10/12.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

public class HomeContentPresenter extends BasePresent<HomeContentView> {



    public void getGankHttp(final int flag, String type,  int page) {
        HttpUtils.getInstance().createService(GankApiService.class)
                .getGankData(type, Constant.PAGESIZE,page)
                .compose(this.<HttpResult<List<GankBean>>>setThread())
                .subscribe(new BaseObserver<List<GankBean>>() {
                    @Override
                    protected void onSuccees(HttpResult<List<GankBean>> t) throws Exception {
                        getView().getGankData(t.getResults(),flag);
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        getView().toast(e.getMessage());
                    }
                });
    }



    /**
     * 点赞干货
     */
    public void thumbGank(String gankId,boolean isThumb){
        UserBean userBean = UserBean.getCurrentUser();
        if (userBean != null){
            if (userBean.getThumbGankIds() != null){
                if (!isThumb){
                    userBean.getThumbGankIds().remove(gankId);
                }
                else {
                    userBean.getThumbGankIds().add(gankId);
                }
            }
            else {
                isThumb = true;
                userBean.getThumbGankIds().add(gankId);
            }

        }
        else {
            ToastUtils.showShortToast("请登录");
            return;
        }
        AVObject object = AVObject.createWithoutData("_User", UserManager.getUid());
        object.put("thumbGankIds",userBean.getThumbGankIds());
        final boolean finalIsThumb = isThumb;
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null){
                    getView().thumbGankSuc(finalIsThumb);
                }
                else {
                    getView().toast(e.getMessage());
                }
            }
        });
    }

    /**
     * 喜欢干货
     */
    public void likeGank(String gankId, final boolean isLike){
        UserBean userBean = UserBean.getCurrentUser();
        if (userBean != null){
            if (isLike){
                userBean.getLikeGankIds().add(gankId);
            }
            else {
                userBean.getLikeGankIds().remove(gankId);
            }
        }
        else {
            ToastUtils.showShortToast("请登录");
            return;
        }
        AVObject object = AVObject.createWithoutData("_User", UserManager.getUid());
        object.put("likeGankIds",userBean.getLikeGankIds());
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null){
                    getView().likeGankSuc(isLike);
                }
                else {
                    getView().toast(e.getMessage());
                }
            }
        });
    }

    public void collectGank(String gankId, final boolean isCollect){
        UserBean userBean = UserBean.getCurrentUser();
        if (userBean != null){
            if (!isCollect){
                userBean.getCollectGankIds().remove(gankId);
            }
            else {
                userBean.getCollectGankIds().add(gankId);
            }
        }
        else {
            ToastUtils.showShortToast("请登录");
            return;
        }
        AVObject object = AVObject.createWithoutData("_User", UserManager.getUid());
        object.put("collectGankIds",userBean.getCollectGankIds());
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null){
                    getView().collectGankSuc(isCollect);
                }
                else {
                    getView().toast(e.getMessage());
                }
            }
        });
    }

}
