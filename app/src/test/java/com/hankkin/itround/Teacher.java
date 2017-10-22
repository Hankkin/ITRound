package com.hankkin.itround;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hankkin on 2017/10/11.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

public class Teacher implements Observable {

    private List<Observer> observers = new ArrayList<>();

    @Override
    public void subcribe(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void unSubcribe(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String msg) {
        for (Observer observer:observers){
            observer.say(msg);
        }
    }
}
