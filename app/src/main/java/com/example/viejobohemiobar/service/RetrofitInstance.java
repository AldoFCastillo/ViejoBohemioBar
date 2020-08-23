package com.example.viejobohemiobar.service;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

    public ApiService getApiService() {
        return retrofit.create(ApiService.class);

    }

}
