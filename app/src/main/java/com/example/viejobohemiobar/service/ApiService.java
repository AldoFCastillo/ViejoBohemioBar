package com.example.viejobohemiobar.service;

import com.example.viejobohemiobar.model.pojo.Result;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    //TODO revisar

    @GET("raw/{path}")
    Call<Result> getResult(@Path("path") String path, @Query("offset") Integer offset);
}
