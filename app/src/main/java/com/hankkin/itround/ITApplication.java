package com.hankkin.itround;

import android.app.Application;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMClientEventHandler;
import com.hankkin.itround.bean.UserBean;
import com.hankkin.itround.chat.AddRequest;
import com.hankkin.itround.chat.CustomUserProvider;
import com.hankkin.itround.chat.PushManager;
import com.hankkin.itround.greendao.dao.DaoMaster;
import com.hankkin.itround.greendao.dao.DaoSession;
import com.hankkin.itround.ui.LoginActivity;
import com.hankkin.library.utils.Utils;

import cn.leancloud.chatkit.LCChatKit;

/**
 * Created by hankkin on 2017/10/12.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

public class ITApplication extends Application {
    private static ITApplication application;
    private static DaoSession daoSession;
    private static DaoMaster.DevOpenHelper sHelper;
    private static DaoMaster sDaoMaster;
    private static SQLiteDatabase db;


    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        UserBean.alwaysUseSubUserClass(UserBean.class);

        AVObject.registerSubclass(AddRequest.class);

        //初始化LeanCloud
        AVOSCloud.initialize(this,Constant.CLOUD_APP_ID,Constant.CLOUD_APP_Key);
        //开启调试日志
        AVOSCloud.setDebugLogEnabled(true);

        LCChatKit.getInstance().setProfileProvider(new CustomUserProvider());

        AVOSCloud.setLastModifyEnabled(true);
        PushManager.getInstance().init(this);
        AVIMClient.setUnreadNotificationEnabled(true);
        AVIMClient.setOfflineMessagePush(true);
        AVIMClient.setAutoOpen(true);
        LCChatKit.getInstance().init(this,Constant.CLOUD_APP_ID,Constant.CLOUD_APP_Key);
        AVIMClient.setClientEventHandler(new AVIMClientEventHandler() {
            @Override
            public void onConnectionPaused(AVIMClient avimClient) {

            }

            @Override
            public void onConnectionResume(AVIMClient avimClient) {

            }

            @Override
            public void onClientOffline(AVIMClient avimClient, int i) {
                if (i == 4111){
                    new MaterialDialog.Builder(application)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Intent intent = new Intent(application, LoginActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .content(getResources().getString(R.string.other_device))
                            .positiveText(R.string.ok)
                            .show();
                }
            }
        });

        Utils.init(this);
        initDB();
    }

    public static ITApplication getInstance() {
        return application;
    }

    private void initDB(){
        sHelper = new DaoMaster.DevOpenHelper(application,"it.db",null);
        db = sHelper.getWritableDatabase();
        sDaoMaster = new DaoMaster(db);
        daoSession = sDaoMaster.newSession();
    }

    public static SQLiteDatabase getDb() {
        return db;
    }

    public static DaoSession getDaoSession(){
        return daoSession;
    }

}
