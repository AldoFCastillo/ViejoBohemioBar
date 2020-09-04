package com.example.viejobohemiobar.service;

import com.example.viejobohemiobar.model.pojo.Result;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {



    @GET("raw/{path}")
    Single<Result> getResult(@Path("path") String path, @Query("offset") Integer offset);
}
