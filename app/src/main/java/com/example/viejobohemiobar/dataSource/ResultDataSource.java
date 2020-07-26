package com.example.viejobohemiobar.dataSource;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.viejobohemiobar.service.RetrofitInstance;
import com.example.viejobohemiobar.model.pojo.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultDataSource {

    public static final int LIMIT = 7;
    private static final int FIRST_PAGE = 0;
    public static final String PATH = "hw1sVJ3j";
    private MutableLiveData<Result> liveResult;


    public LiveData<Result> getProducts() {

        liveResult = new MutableLiveData<>();
        RetrofitInstance.getInstance()
                .getMercadoApiService()
                .getResult(PATH, FIRST_PAGE)
                .enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {

                        if (response.body() != null) {
                            liveResult.setValue(response.body());
                        }

                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        String message = t.getMessage();
                        System.out.println("ERROR" + message);
                        t.printStackTrace();
                    }
                });

        return liveResult;

    }
}



