package com.example.viejobohemiobar.dataSource;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.viejobohemiobar.model.pojo.OrderLog;
import com.example.viejobohemiobar.service.RetrofitInstance;
import com.example.viejobohemiobar.model.pojo.Result;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultDataSource {

    private static final int FIRST_PAGE = 0;
    public static final String PATH = "34BGgFCk";
    private MutableLiveData<Result> liveResult;
    private String pending = "pending orders";
    private String process = "in process orders";
    private String closed = "closed orders";
    private String actual = "actual orders";


    public LiveData<Result> getProducts() {

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

        return liveResult;

    }


    public LiveData<OrderLog> getOrderLog(String path) {

        path = stringToPath(path);

        MutableLiveData<OrderLog> liveOrderLog = new MutableLiveData<>();
        DocumentReference docRef = getDocumentReference(path, path);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                liveOrderLog.setValue(documentSnapshot.toObject(OrderLog.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                OrderLog orderLog = null;
                liveOrderLog.setValue(orderLog);
            }
        });
        return liveOrderLog;
    }

    public LiveData<Boolean> updateOrderLog(OrderLog orderLog, String path) {
        path = stringToPath(path);
        MutableLiveData<Boolean> liveBool = new MutableLiveData<>();
        getDocumentReference(path, path).set(orderLog).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                liveBool.setValue(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                liveBool.setValue(false);
            }
        });
        return liveBool;
    }

    private String stringToPath(String path){
        switch(path){
            case "p": path = pending;
                break;
            case "i": path = process;
                break;
            case "c": path = closed;
                break;
        }
        return path;
    }




    private DocumentReference getDocumentReference(String path, String table) {
        String id = table;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection(path).document(id);
    }





    public LiveData<Result> getActualOrder(String table) {
        MutableLiveData<Result> liveResult = new MutableLiveData<>();
        DocumentReference docRef = getDocumentReference(actual, table);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                liveResult.setValue(documentSnapshot.toObject(Result.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Result result = null;
                liveResult.setValue(result);
            }
        });
        return liveResult;
    }

    public LiveData<Boolean> setActualOrder(Result result, String table) {
        MutableLiveData<Boolean> liveBool = new MutableLiveData<>();
        getDocumentReference(actual, table).set(result).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                liveBool.setValue(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                liveBool.setValue(false);
            }
        });
        return liveBool;
    }

    public LiveData<Boolean> deleteActualOrder(String table) {
        MutableLiveData<Boolean> liveBool = new MutableLiveData<>();
        getDocumentReference(actual, table).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                liveBool.setValue(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                liveBool.setValue(false);
            }
        });
        return liveBool;
    }
}



