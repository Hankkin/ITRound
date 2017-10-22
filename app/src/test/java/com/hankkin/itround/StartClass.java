package com.hankkin.itround;

/**
 * Created by hankkin on 2017/10/11.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

public class StartClass {
    public static void main(String[] args){
        Observable teacher = new Teacher();
        Observer stu1 = new Student();
        stu1.setName("露露");
        Observer stu2 = new Student();
        stu2.setName("海杰");

        teacher.subcribe(stu1);
        teacher.subcribe(stu2);

        teacher.notifyObservers("点名啦");
    }
}
