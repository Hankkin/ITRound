package com.hankkin.itround;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.avos.avoscloud.AVOSCloud;
import com.hankkin.itround.chat.CustomUserProvider;
import com.hankkin.itround.greendao.dao.DaoMaster;
import com.hankkin.itround.greendao.dao.DaoSession;
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

        //初始化LeanCloud
        AVOSCloud.initialize(this,Constant.CLOUD_APP_ID,Constant.CLOUD_APP_Key);
        //开启调试日志
        AVOSCloud.setDebugLogEnabled(true);

        LCChatKit.getInstance().setProfileProvider(CustomUserProvider.getInstance());
        LCChatKit.getInstance().init(this,Constant.CLOUD_APP_ID,Constant.CLOUD_APP_Key);

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
