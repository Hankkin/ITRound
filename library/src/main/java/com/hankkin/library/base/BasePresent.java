package com.hankkin.library.base;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hankkin on 2017/3/29.
 */

public abstract class BasePresent<T>{


    public <T> ObservableTransformer<T,T> setThread(){
        return new ObservableTransformer<T,T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    private T view;

    public T getView() {
        return view;
    }

    public void attach(T view){
        this.view = view;
    }

    public void detach(){
        this.view = null;
    }
}
