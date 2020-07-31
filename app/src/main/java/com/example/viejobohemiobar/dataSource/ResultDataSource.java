package com.example.viejobohemiobar.dataSource;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.viejobohemiobar.model.pojo.Order;
import com.example.viejobohemiobar.model.pojo.OrderLog;
import com.example.viejobohemiobar.service.RetrofitInstance;
import com.example.viejobohemiobar.model.pojo.Result;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private String order = "actual orders";


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


    public LiveData<OrderLog> getOrderLog() {
        MutableLiveData<OrderLog> liveOrderLog = new MutableLiveData<>();
        DocumentReference docRef = getDocumentReference(pending);
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


    private DocumentReference getDocumentReference(String path) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String id;
        if(path.equals(pending)){
            id=pending;
        }else{ id = currentUser.getEmail();}
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection(path).document(id);
    }

    public LiveData<Boolean> updateOrderLog(OrderLog orderLog) {
        MutableLiveData<Boolean> liveBool = new MutableLiveData<>();
        getDocumentReference(pending).set(orderLog).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public LiveData<Result> getActualOrder() {
        MutableLiveData<Result> liveResult = new MutableLiveData<>();
        DocumentReference docRef = getDocumentReference(order);
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

    public LiveData<Boolean> setActualOrder(Result result) {
        MutableLiveData<Boolean> liveBool = new MutableLiveData<>();
        getDocumentReference(order).set(result).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public LiveData<Boolean> deleteActualOrder() {
        MutableLiveData<Boolean> liveBool = new MutableLiveData<>();
        getDocumentReference(order).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
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



