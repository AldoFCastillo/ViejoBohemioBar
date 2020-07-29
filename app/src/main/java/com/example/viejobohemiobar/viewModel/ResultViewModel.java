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


    public LiveData<OrderLog> getOrderLog() {

        return resultDataSource.getOrderLog();
    }


    public LiveData<Boolean> updateOrderLog(OrderLog orderLog) {

        return resultDataSource.updateOrderLog(orderLog);
    }

    public LiveData<Result> getActualOrder() {

        return resultDataSource.getActualOrder();
    }

    public LiveData<Boolean> updateActualOrder(Result result) {

        return resultDataSource.setActualOrder(result);
    }

    public LiveData<Boolean> deleteActualOrder (){

        return resultDataSource.deleteActualOrder();
    }


}
