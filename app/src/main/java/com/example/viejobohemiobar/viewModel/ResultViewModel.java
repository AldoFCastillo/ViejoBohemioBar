package com.example.viejobohemiobar.viewModel;

import androidx.lifecycle.LiveData;

import androidx.lifecycle.ViewModel;

import com.example.viejobohemiobar.dataSource.ResultDataSource;
import com.example.viejobohemiobar.model.pojo.OrderLog;
import com.example.viejobohemiobar.model.pojo.Result;


public class ResultViewModel extends ViewModel {

    private ResultDataSource resultDataSource = new ResultDataSource();


    public ResultViewModel() {

    }

    public LiveData<Result> getResults() {
        return resultDataSource.getProducts();
    }


    public LiveData<OrderLog> getOrderLog(String path) {

        return resultDataSource.getOrderLog(path);
    }


    public LiveData<Boolean> updateOrderLog(OrderLog orderLog, String path) {

        return resultDataSource.updateOrderLog(orderLog, path);
    }

    public LiveData<Result> getActualOrder(String table) {

        return resultDataSource.getActualOrder(table);
    }

    public LiveData<Boolean> updateActualOrder(Result result, String table) {

        return resultDataSource.setActualOrder(result, table);
    }

    public LiveData<Boolean> deleteActualOrder (String table){

        return resultDataSource.deleteActualOrder(table);
    }


}
