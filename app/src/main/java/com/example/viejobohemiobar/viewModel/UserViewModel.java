package com.example.viejobohemiobar.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.viejobohemiobar.dataSource.UserDataSource;
import com.google.firebase.auth.FirebaseUser;

public class UserViewModel extends ViewModel {

    private UserDataSource userDataSource = new UserDataSource();

    public LiveData<FirebaseUser> registerUser(String mail, String pass){
        return userDataSource.registerUser(mail, pass);
    }

    public LiveData<Boolean> loginUser(String mail, String pass){
        return userDataSource.loginUser(mail, pass);
    }

    public LiveData<Boolean> changePassword(String pass){
        return userDataSource.changePassword(pass);
    }

    public LiveData<String> getPassword(){
        return userDataSource.getPassword();
    }
}
