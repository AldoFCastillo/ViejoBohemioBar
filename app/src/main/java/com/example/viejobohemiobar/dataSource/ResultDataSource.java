package com.example.viejobohemiobar.dataSource;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.viejobohemiobar.model.pojo.OrderLog;
import com.example.viejobohemiobar.service.RetrofitInstance;
import com.example.viejobohemiobar.model.pojo.Result;
import com.example.viejobohemiobar.utils.MenuUtils;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultDataSource {
    public static final String TAG = "Result Data Source: ";
    private static final int FIRST_PAGE = 0;
    public static final String PATH = "34BGgFCk";

    private String actual = "actual orders";
    private String pending = "pending orders";
    private String process = "in process orders";
    private String closed = "closed orders";
    private MutableLiveData<Result> liveResult;
    private MutableLiveData<Boolean> boolListenPending;
    private MutableLiveData<OrderLog> liveOrderLog;
    private MutableLiveData<Boolean> liveupOrderLogBool;
    private MutableLiveData<Result> liveActualResult;
    private MutableLiveData<Boolean> liveActualOrderBool;
    private MutableLiveData<Boolean> liveDeleteBool;


    private void getProducts() {

        liveResult = new MutableLiveData<>();
        RetrofitInstance.getInstance()
                .getApiService()
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


    }

    public MutableLiveData<Result> refreshGetProducts() {
        getProducts();
        return liveResult;
    }


    private void getOrderLog(String path) {

        path = MenuUtils.stringToPath(path);
        liveOrderLog = new MutableLiveData<>();
        DocumentReference docRef = getDocumentReference(path, path);
        docRef.get().addOnSuccessListener(documentSnapshot -> liveOrderLog.setValue(documentSnapshot.toObject(OrderLog.class)))
                .addOnFailureListener(e -> {
                    OrderLog orderLog = null;
                    liveOrderLog.setValue(orderLog);
                });
    }

    public MutableLiveData<OrderLog> refreshGetOrderLog(String path) {
        getOrderLog(path);
        return liveOrderLog;
    }


    private void updateOrderLog(OrderLog orderLog, String path) {
        path = MenuUtils.stringToPath(path);
        liveupOrderLogBool = new MutableLiveData<>();
        getDocumentReference(path, path).set(orderLog)
                .addOnSuccessListener(aVoid -> liveupOrderLogBool.setValue(true))
                .addOnFailureListener(e -> liveupOrderLogBool.setValue(false));
    }

    public MutableLiveData<Boolean> refreshUpdateOrderLog(OrderLog orderLog, String path) {
        updateOrderLog(orderLog, path);
        return liveupOrderLogBool;
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
        liveDeleteBool = new MutableLiveData<>();
        getDocumentReference(actual, table).delete()
                .addOnSuccessListener(aVoid -> liveDeleteBool.setValue(true))
                .addOnFailureListener(e -> liveDeleteBool.setValue(false));
    }

    public MutableLiveData<Boolean> refresehDeleteActualOrder(String table) {
        deleteActualOrder(table);
        return liveDeleteBool;
    }

    private void listenPending() {

        boolListenPending = new MutableLiveData<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(pending).addSnapshotListener((value, e) -> {

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



