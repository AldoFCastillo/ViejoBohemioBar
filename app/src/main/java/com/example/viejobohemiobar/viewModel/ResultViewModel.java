package com.example.viejobohemiobar.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.viejobohemiobar.dataSource.ResultDataSource;
import com.example.viejobohemiobar.model.pojo.Result;

public class ResultViewModel extends ViewModel {

   private ResultDataSource resultDataSource = new ResultDataSource();

    public ResultViewModel() {

    }

    public LiveData<Result> getResults() {
        return resultDataSource.getProducts();
    }



}
