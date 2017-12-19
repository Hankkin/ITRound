package com.hankkin.itround;

/**
 * Created by hankkin on 2017/10/12.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

public class Constant {
    public static final String BASE_URL = "http://gank.io/api/";
    public static final int REFRESH = 0;
    public static final int LOADMORE = 1;
    public static final int PAGESIZE = 20;
    public static final int PAGELIMIT = 19;
    private static final String LEANMESSAGE_CONSTANTS_PREFIX = "com.avoscloud.leanchatlib.";
    public static final String UPDATED_AT = "updatedAt";


    public static final String USER = "user";
    public static final String GANK = "gank";
    public static final String USER_ID = "user_id";
    public static final String OBJECT_ID = "objectId";
    public static final String INVITATION_ACTION = "invitation_action";
    public static final String NOTOFICATION_TAG = getPrefixConstant("notification_tag");
    public static final String NOTIFICATION_SYSTEM = Constant.getPrefixConstant("notification_system_chat");
    public static final String NOTIFICATION_SINGLE_CHAT = Constant.getPrefixConstant("notification_single_chat");
    public static final String NOTIFICATION_GROUP_CHAT = Constant.getPrefixConstant("notification_group_chat");

    public static final String CLOUD_APP_ID = "dgHfTu8jp9XPpjmpG02zP57d-gzGzoHsz";
    public static final String CLOUD_APP_Key = "Mw3zBeIEpdwKYdL7GmMjIUpa";
    public static final String CLOUD_WEIBO_URL = "https://leancloud.cn/1.1/sns/callback/30lr6p195des47bi";
    public static final String CLOUD_QQ_URL = "https://leancloud.cn/1.1/sns/callback/z6mx3p4pfxq1zqi0";

    public static final boolean DEBUG = true;

    public static String getPrefixConstant(String str) {
        return LEANMESSAGE_CONSTANTS_PREFIX + str;
    }
}
