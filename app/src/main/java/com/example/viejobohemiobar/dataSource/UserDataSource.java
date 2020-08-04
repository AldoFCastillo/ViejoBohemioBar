package com.example.viejobohemiobar.dataSource;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.viejobohemiobar.model.pojo.Password;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class UserDataSource {

    public LiveData<FirebaseUser> registerUser(String email, String password) {
        MutableLiveData<FirebaseUser> firebaseUserMutable = new MutableLiveData<>();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        firebaseUserMutable.setValue(firebaseUser);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                FirebaseUser firebaseUser = null;
                firebaseUserMutable.setValue(firebaseUser);
            }
        });

        return firebaseUserMutable;
    }

    public LiveData<Boolean> loginUser(String mail, String pass) {
        MutableLiveData<Boolean> liveBool = new MutableLiveData<>();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(mail, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
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

    public LiveData<Boolean> changePassword(String pass) {
        MutableLiveData<Boolean> liveBool = new MutableLiveData<>();
        Password password = new Password();
        password.setPassword(pass);
        getDocumentReference("password").set(password).addOnSuccessListener(new OnSuccessListener<Void>() {
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

      public LiveData<String> getPassword() {
        MutableLiveData<String> liveString= new MutableLiveData<>();
        DocumentReference docRef = getDocumentReference("password");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Password password = documentSnapshot.toObject(Password.class);
                String pass = password.getPassword();
                liveString.setValue(pass);
            }
        });
        return liveString;
    }

    private DocumentReference getDocumentReference(String path) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection(path).document(path);
    }


}
