package com.hankkin.itround;

/**
 * Created by hankkin on 2017/10/11.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

public class Student implements Observer {

    private String name;

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void say(String msg) {
        System.out.println(msg+":"+this.name+"到");
    }
}
