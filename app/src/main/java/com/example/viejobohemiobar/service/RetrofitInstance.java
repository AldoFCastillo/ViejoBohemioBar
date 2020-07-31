package com.example.viejobohemiobar.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static final String BASE_URL = "https://pastebin.com/";
    private static RetrofitInstance retrofitInstance;
    private static Retrofit retrofit ;

    private RetrofitInstance() {

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
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
