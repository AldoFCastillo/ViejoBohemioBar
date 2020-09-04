package com.example.viejobohemiobar.dataSource;

import android.util.Log;


import androidx.lifecycle.MutableLiveData;

import com.example.viejobohemiobar.model.pojo.Order;
import com.example.viejobohemiobar.model.pojo.OrderLog;
import com.example.viejobohemiobar.service.RetrofitInstance;
import com.example.viejobohemiobar.model.pojo.Result;
import com.example.viejobohemiobar.utils.MenuUtils;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultDataSource {
    public static final String TAG = "Result Data Source: ";
    private static final int FIRST_PAGE = 0;
    public static final String PATH = "CsRYuHA5";

    private String actual = "actual orders";
    public MutableLiveData<Result> liveResult;
    public MutableLiveData<Boolean> loading;
    public MutableLiveData<Boolean> error;
    private MutableLiveData<Boolean> boolListenPending;
    private MutableLiveData<OrderLog> liveOrderLog;
    private MutableLiveData<Boolean> liveUpOrderLogBool;
    private MutableLiveData<Result> liveActualResult;
    private MutableLiveData<Boolean> liveActualOrderBool;
    private MutableLiveData<Boolean> liveDeleteActualBool;
    private MutableLiveData<Boolean> liveDeleteBool;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    private void getProducts() {

        liveResult = new MutableLiveData<>();
        loading = new MutableLiveData<>();
        error = new MutableLiveData<>();
        loading.setValue(true);

        compositeDisposable.add(
                RetrofitInstance.getInstance()
                        .getApiService(PATH, FIRST_PAGE)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Result>() {
                            @Override
                            public void onSuccess(Result value) {
                                liveResult.setValue(value);
                                loading.setValue(false);
                                error.setValue(false);
                            }

                            @Override
                            public void onError(Throwable e) {
                                loading.setValue(false);
                                error.setValue(true);
                                String message = e.getMessage();
                                assert message != null;
                                Log.e(TAG, message);
                            }
                        }));
    }

    public MutableLiveData<Result> refreshGetProducts() {
        getProducts();
        return liveResult;
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    public MutableLiveData<Boolean> getError() {
        return error;
    }


    private void getOrderLog(String path) {
        path = MenuUtils.stringToPath(path);
        liveOrderLog = new MutableLiveData<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        OrderLog orderLog = new OrderLog();
        List<Order> orderList = new ArrayList<>();
        db.collection(path).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot dc : queryDocumentSnapshots.getDocuments()) {
                orderList.add(dc.toObject(Order.class));
            }
            orderLog.setOrderList(orderList);
            liveOrderLog.setValue(orderLog);
        }).addOnFailureListener(e -> {
            OrderLog orderLog1 = null;
            liveOrderLog.setValue(orderLog1);
        });
    }

    public MutableLiveData<OrderLog> refreshGetOrderLog(String path) {
        getOrderLog(path);
        return liveOrderLog;
    }


    private void updateOrderLog(Order order, String path, String id) {
        path = MenuUtils.stringToPath(path);
        liveUpOrderLogBool = new MutableLiveData<>();
        getDocumentReference(path, id).set(order)
                .addOnSuccessListener(aVoid -> liveUpOrderLogBool.setValue(true))
                .addOnFailureListener(e -> liveUpOrderLogBool.setValue(false));
    }


    public MutableLiveData<Boolean> refreshUpdateOrderLog(Order order, String path, String id) {
        updateOrderLog(order, path, id);
        return liveUpOrderLogBool;
    }

    private void deleteOrder(String path, String id) {
        liveDeleteBool = new MutableLiveData<>();
        path = MenuUtils.stringToPath(path);
        getDocumentReference(path, id).delete()
                .addOnSuccessListener(aVoid -> liveDeleteBool.setValue(true))
                .addOnFailureListener(e -> liveDeleteBool.setValue(false));
    }


    public MutableLiveData<Boolean> refreshDeleteOrder(String path, String id) {
        deleteOrder(path, id);
        return liveDeleteBool;
    }


    private DocumentReference getDocumentReference(String path, String table) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection(path).document(table);
    }


    public void getActualOrder(String table) {
        liveActualResult = new MutableLiveData<>();
        DocumentReference docRef = getDocumentReference(actual, table);
        docRef.get().addOnSuccessListener(documentSnapshot -> liveActualResult.setValue(documentSnapshot.toObject(Result.class)))
                .addOnFailureListener(e -> {
                    Result result = null;
                    liveActualResult.setValue(result);
                });
    }

    public MutableLiveData<Result> refreshGetActualOrder(String table) {
        getActualOrder(table);
        return liveActualResult;
    }

    private void setActualOrder(Result result, String table) {
        liveActualOrderBool = new MutableLiveData<>();
        getDocumentReference(actual, table).set(result)
                .addOnSuccessListener(aVoid -> liveActualOrderBool.setValue(true))
                .addOnFailureListener(e -> liveActualOrderBool.setValue(false));
    }

    public MutableLiveData<Boolean> refreshsetActualOrder(Result result, String table) {
        setActualOrder(result, table);
        return liveActualOrderBool;
    }

    private void deleteActualOrder(String table) {
        liveDeleteActualBool = new MutableLiveData<>();
        getDocumentReference(actual, table).delete()
                .addOnSuccessListener(aVoid -> liveDeleteActualBool.setValue(true))
                .addOnFailureListener(e -> liveDeleteActualBool.setValue(false));
    }

    public MutableLiveData<Boolean> refresehDeleteActualOrder(String table) {
        deleteActualOrder(table);
        return liveDeleteActualBool;
    }

    private void listenPending() {
        AtomicBoolean isFirstListener = new AtomicBoolean(true);
        boolListenPending = new MutableLiveData<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String pending = "pending orders";
        db.collection(pending).addSnapshotListener((value, e) -> {
            if (isFirstListener.get()) {
                isFirstListener.set(false);
                return;
            }
            if (e != null) {
                Log.w(TAG, "Listen failed.", e);
                boolListenPending.setValue(null);
            }
            if (value != null) {
                for (DocumentChange dc : value.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            boolListenPending.setValue(true);
                            Log.d(TAG, "Order Added");
                            break;
                        case REMOVED:
                            boolListenPending.setValue(false);
                            break;
                    }
                }
            }

        });

    }

    public MutableLiveData<Boolean> refreshListenPending() {
        listenPending();
        return boolListenPending;
    }
}



