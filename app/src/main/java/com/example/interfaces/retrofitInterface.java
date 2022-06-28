package com.example.mytvdemo.interfaces;

import com.example.mytvdemo.ResDao;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface retrofitInterface {

    @GET("/")
    Call<ResDao> getSearchResultToCall(@Query("MapStr")String mapStr);

    @GET("/")
    Observable<ResDao> getSearchResult(@Query("MapStr")String mapStr);

    @GET("/")
    Observable<ResponseBody> getBaiduCallByRx();



}
