package com.hankkin.itround.api;

import com.hankkin.itround.bean.GankBean;
import com.hankkin.library.http.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by hankkin on 2017/10/12.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

public interface GankApiService {
    @GET("data/{type}/{num}/{page}")
    Observable<HttpResult<List<GankBean>>> getGankData(@Path("type") String type, @Path("num") int num, @Path("page") int pa);
}
