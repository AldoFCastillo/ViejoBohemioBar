package com.example.viejobohemiobar.dataSource;

import androidx.lifecycle.MutableLiveData;

import com.example.viejobohemiobar.model.pojo.Password;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class UserDataSource {

    private MutableLiveData<String> liveString;
    private MutableLiveData<Boolean> liveChangePass;
    private MutableLiveData<Boolean> liveLoginBool;
    private MutableLiveData<FirebaseUser> firebaseUserMutable;

    private void registerUser(String email, String password) {
        firebaseUserMutable = new MutableLiveData<>();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        firebaseUserMutable.setValue(firebaseUser);
                    }
                }).addOnFailureListener(e -> {
                    FirebaseUser firebaseUser = null;
                    firebaseUserMutable.setValue(firebaseUser);
                });

    }

    public MutableLiveData<FirebaseUser> refreshRegister(String email, String password){
        registerUser(email, password);
        return firebaseUserMutable;
    }

    private void loginUser(String mail, String pass) {
        liveLoginBool = new MutableLiveData<>();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(mail, pass)
                .addOnSuccessListener(authResult -> liveLoginBool.setValue(true))
                .addOnFailureListener(e -> liveLoginBool.setValue(false));

    }

    public MutableLiveData<Boolean> refreshloginUser(String mail, String pass){
        loginUser(mail, pass);
        return liveLoginBool;
    }


    private void changePassword(String pass) {
        liveChangePass = new MutableLiveData<>();
        Password password = new Password();
        password.setPassword(pass);
        getDocumentReference("password").set(password)
                .addOnSuccessListener(aVoid -> liveChangePass.setValue(true))
                .addOnFailureListener(e -> liveChangePass.setValue(false));
    }

    public MutableLiveData<Boolean> refreshChangePassword(String pass){
        changePassword(pass);
        return liveChangePass;
    }


    private void getPassword() {
        liveString= new MutableLiveData<>();
        DocumentReference docRef = getDocumentReference("password");
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            Password password = documentSnapshot.toObject(Password.class);
            String pass = password.getPassword();
            liveString.setValue(pass);
        });
    }

    public MutableLiveData<String> refreshGetPassword(){
        getPassword();
        return liveString;
    }


    private DocumentReference getDocumentReference(String path) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection(path).document(path);
    }


}
