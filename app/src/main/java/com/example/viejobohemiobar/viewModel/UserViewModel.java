package com.example.viejobohemiobar.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.viejobohemiobar.dataSource.UserDataSource;
import com.google.firebase.auth.FirebaseUser;

public class UserViewModel extends ViewModel {

    public MutableLiveData<String> liveStringPass;
    public MutableLiveData<Boolean> liveChangePass;
    public MutableLiveData<Boolean> liveLoginBool;
    public MutableLiveData<FirebaseUser> firebaseUserMutable;
    private UserDataSource userDataSource = new UserDataSource();

    public void registerUser(String mail, String pass) {
        firebaseUserMutable = userDataSource.refreshRegister(mail, pass);
    }

    public void loginUser(String mail, String pass) {
        liveLoginBool = userDataSource.refreshloginUser(mail, pass);
    }

    public void changePassword(String pass) {
        liveChangePass = userDataSource.refreshChangePassword(pass);
    }

    public void getPassword() {
        liveStringPass = userDataSource.refreshGetPassword();
    }
}
