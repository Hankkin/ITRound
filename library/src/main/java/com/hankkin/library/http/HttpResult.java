package com.hankkin.library.http;


import com.hankkin.library.bean.BaseBean;

/**
 * Created by hankkin on 2017/10/10.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

public class HttpResult<T> extends BaseBean {



    private boolean error;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    private T results;




    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }
}
