package com.hankkin.itround.bean;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.google.gson.Gson;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by harryfeng on 2016/11/2.
 */

public class AVObjectToModel implements Serializable {

    private HashMap<String, Object> values = new HashMap<String, Object>();

    public HashMap<String, Object> getValues() {
        return values;
    }

    public void setValues(HashMap<String, Object> values) {
        this.values = values;
    }

    public <T> T getModel(Class<T> classOfT) {
        Gson gson = new Gson();
        String AVUserJsonString = gson.toJson(this.getValues());
        return gson.fromJson(AVUserJsonString, classOfT);
    }

    private Map<String,Object> makeMapFromAVFiel(AVFile file){
        Map<String,Object> map = new HashMap<>();
        map.put("__type", "File");
        map.put("name", file.getOriginalName());
        map.put("objectId", file.getObjectId());
        map.put("url", file.getUrl());
        return map;
    }

    public AVObjectToModel(AVObject object) {

        // Loop the keys in the ParseObject
        for(String key : object.keySet()) {
            @SuppressWarnings("rawtypes")
            Class classType = object.get(key).getClass();
            if(classType == byte[].class || classType == String.class ||classType == Float.class||
                    classType == Integer.class || classType == Boolean.class ||classType == BigDecimal.class||
                classType == Date.class
                ||classType == JSONObject.class) {
                values.put(key, object.get(key));
            }else if(classType == AVFile.class){
                values.put(key,makeMapFromAVFiel((AVFile) object.get(key)));
            }
            else if(classType == AVObject.class) {
                AVObjectToModel parseUserObject = new AVObjectToModel((AVObject)object.get(key));
                values.put(key, parseUserObject.getValues());
            }
            else if (classType == AVUser.class){
                AVObjectToModel parseUserObject = new AVObjectToModel((AVUser)object.get(key));
                values.put(key, parseUserObject.getValues());
            }
            else if (classType == ArrayList.class){
                values.put(key,object.get(key));
            }
            else {
                // You might want to add more conditions here, for embedded ParseObject, ParseFile, etc.
            }
        }
        values.put("objectId",object.getObjectId());
        values.put("createdAt",object.getCreatedAt());
        values.put("updatedAt",object.getUpdatedAt());
    }


}
