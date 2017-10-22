package com.hankkin.itround.bean;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by hankkin on 2017/10/18.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

public class BaseBean implements Serializable{
    public String returnImg(Map<String,Object> map){
        if (map != null){
            return String.valueOf(map.get("url"));
        }
        else {
            return "";
        }
    }
}
