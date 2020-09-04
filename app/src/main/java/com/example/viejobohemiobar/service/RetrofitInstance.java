package com.example.viejobohemiobar.service;

import com.example.viejobohemiobar.model.pojo.Result;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.Single;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static final String BASE_URL = "https://pastebin.com/";
    private static RetrofitInstance retrofitInstance;
    private static Retrofit retrofit ;

    private RetrofitInstance() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }


    public static synchronized RetrofitInstance getInstance(){
        if(retrofitInstance == null){
            retrofitInstance = new RetrofitInstance();
        }
        return retrofitInstance;
    }

    public Single<Result> getApiService(String path, Integer offset){
        return retrofit.create(ApiService.class).getResult(path, offset);
    }

}
