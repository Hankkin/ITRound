package com.hankkin.itround;

/**
 * Created by hankkin on 2017/10/11.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

public interface Observable {
    public void subcribe(Observer observer);
    public void unSubcribe(Observer observer);
    public void notifyObservers(String msg);
}
