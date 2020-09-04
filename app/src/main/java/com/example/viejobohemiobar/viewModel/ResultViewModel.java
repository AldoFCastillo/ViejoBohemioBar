package com.example.viejobohemiobar.viewModel;

import androidx.lifecycle.LiveData;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.viejobohemiobar.dataSource.ResultDataSource;
import com.example.viejobohemiobar.model.pojo.Order;
import com.example.viejobohemiobar.model.pojo.OrderLog;
import com.example.viejobohemiobar.model.pojo.Result;


public class ResultViewModel extends ViewModel {

    private ResultDataSource resultDataSource = new ResultDataSource();
    public MutableLiveData<Boolean> dbListener = new MutableLiveData<>();
    public MutableLiveData<OrderLog> orderLogData = new MutableLiveData<>();
    public MutableLiveData<Boolean> orderLogBool = new MutableLiveData<>();
    public MutableLiveData<Result> resultData = new MutableLiveData<>();
    public MutableLiveData<Boolean> loading =new MutableLiveData<>();
    public MutableLiveData<Boolean> error =new MutableLiveData<>();
    public MutableLiveData<Result> resultActualData = new MutableLiveData<>();
    public MutableLiveData<Boolean> updateActualOrderBool = new MutableLiveData<>();
    public MutableLiveData<Boolean> deleteActualOrderBool = new MutableLiveData<>();
    public MutableLiveData<Boolean> deleteOrderBool = new MutableLiveData<>();


    public ResultViewModel() {

    }

    public void getResults() {

        resultData = resultDataSource.refreshGetProducts();
        loading = resultDataSource.getLoading();
        error = resultDataSource.getError();
    }


    public void getOrderLog(String path) {

        orderLogData = resultDataSource.refreshGetOrderLog(path);
    }


    public void updateOrderLog(Order order, String path, String id) {

        orderLogBool = resultDataSource.refreshUpdateOrderLog(order, path, id);
    }

    public void deleteOrder(String path, String id){
        deleteOrderBool = resultDataSource.refreshDeleteOrder(path, id);
    }


    public void getActualOrder(String table) {

        resultActualData = resultDataSource.refreshGetActualOrder(table);
    }

    public void updateActualOrder(Result result, String table) {

        updateActualOrderBool = resultDataSource.refreshsetActualOrder(result, table);
    }

    public void deleteActualOrder(String table) {

        deleteActualOrderBool = resultDataSource.refresehDeleteActualOrder(table);
    }

    public void listenPending() {
        dbListener = resultDataSource.refreshListenPending();

    }


}
