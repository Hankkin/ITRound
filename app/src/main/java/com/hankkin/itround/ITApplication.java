package com.hankkin.itround;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.hankkin.library.utils.Utils;

/**
 * Created by hankkin on 2017/10/12.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

public class ITApplication extends Application {
    private static ITApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        //初始化LeanCloud
        AVOSCloud.initialize(this,Constant.CLOUD_APP_ID,Constant.CLOUD_APP_Key);
        //开启调试日志
        AVOSCloud.setDebugLogEnabled(true);

        Utils.init(this);
    }

    public static ITApplication getInstance() {
        return application;
    }

}
