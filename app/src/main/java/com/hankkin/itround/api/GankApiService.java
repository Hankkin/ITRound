package com.hankkin.itround.api;

import com.hankkin.itround.bean.GankBean;
import com.hankkin.library.http.HttpResult;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by hankkin on 2017/10/12.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

public interface GankApiService {
    @GET("data/{type}/{num}/{page}")
    Observable<HttpResult<List<GankBean>>> getGankData(@Path("type") String type, @Path("num") int num, @Path("page") int pa);

    @POST("add2gank")
    @FormUrlEncoded
    Observable<HttpResult<String>> postGank(@FieldMap Map<String,Object> map);
}
