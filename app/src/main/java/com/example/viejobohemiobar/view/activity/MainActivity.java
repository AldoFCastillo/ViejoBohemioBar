package com.example.viejobohemiobar.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.viejobohemiobar.R;
import com.example.viejobohemiobar.model.pojo.Result;
import com.example.viejobohemiobar.view.fragment.HomeFragment;
import com.example.viejobohemiobar.view.fragment.LoginFragment;
import com.example.viejobohemiobar.view.fragment.MenuFragment;
import com.example.viejobohemiobar.view.fragment.OrderFragment;
import com.example.viejobohemiobar.view.fragment.RecyclerMenuFragment;
import com.example.viejobohemiobar.view.fragment.ScannerFragment;
import com.example.viejobohemiobar.viewModel.ResultViewModel;
import com.example.viejobohemiobar.viewModel.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements HomeFragment.listener, LoginFragment.loginListener {

    private long backPressedTime;
    private Toast backToast;
    private String mail;
    private String pass;
    private FirebaseUser firebaseUser;
    private MenuItem menuLogin;
    private MenuItem buttonLogin;


    @BindView(R.id.toolbarMain)
    Toolbar toolbar;
    @BindView(R.id.drawerHome)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigationViewHome)
    NavigationView navigationView;
    @BindView(R.id.mainLayout)
    ConstraintLayout mainLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (checkUSer()) {
            resetOrder().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    setFragment(new HomeFragment());
                    setToolBar();
                    setNavigationView();
                }
            });
        }else {
            setFragment(new HomeFragment());
            setToolBar();
            setNavigationView();
        }

    }


    public void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerFragmentMain, fragment);
        fragmentTransaction.commit();
    }

    private void setToolBar() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }


    private Boolean checkUSer() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return (currentUser != null);
    }


    private void setNavigationView() {
        menuLogin = navigationView.getMenu().findItem(R.id.navigationViewLoginItem);
        buttonLogin = navigationView.getMenu().findItem(R.id.login);
        if (!checkUSer()) {
            menuLogin.setVisible(false);
            buttonLogin.setVisible(true);
        } else {
            menuLogin.setVisible(true);
            buttonLogin.setVisible(false);
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.login:
                        setFragment(new LoginFragment());
                        Snackbar.make(mainLayout, "Login", Snackbar.LENGTH_LONG).show();
                        break;

                    case R.id.navigationViewPedidosPendientesItem:
                        goStaffActivity("p");
                        break;

                    case R.id.navigationViewPedidosPreparacionItem:
                        goStaffActivity("i");
                        break;

                    case R.id.navigationViewPedidosEntregadosItem:
                        goStaffActivity("c");
                        break;

                    case R.id.navigationViewCerrarSesionItem:
                        FirebaseAuth.getInstance().signOut();
                        menuLogin.setVisible(false);
                        buttonLogin.setVisible(true);
                        drawerLayout.closeDrawers();
                        Toast.makeText(MainActivity.this, "Desconexion exitosa", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.registerMenuNavigation:
                        setFragment(new LoginFragment());
                        Snackbar.make(mainLayout, "Registro", Snackbar.LENGTH_LONG).show();
                        break;
                }

                drawerLayout.closeDrawers();

                return false;
            }
        });

    }

    private void goStaffActivity(String path) {
        Intent intent = new Intent(this, StaffActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(StaffActivity.KEY_PATH, path);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void goMenuActivity() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    private void registerUser(String mail, String pass) {
        UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.registerUser(mail, pass).observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    goMenuActivity();
                    //setFragment(new MenuFragment());
                    Toast.makeText(MainActivity.this, "Hola! Ya podes armar tu pedido", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            //Fragment fragment = fragmentManager.findFragmentById(R.id.containerFragmentMain);
            super.onBackPressed();

        } else {
            backToast = Toast.makeText(getBaseContext(), "Presiona atras nuevamente para salir", Toast.LENGTH_SHORT);
            backToast.show();
            setFragment(new HomeFragment());

        }
        backPressedTime = System.currentTimeMillis();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String table = "1";

        /*IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        String text;
        if ((result != null) && (result.getContents() != null)) {
            text = result.getContents();
        } else text = "Error";*/
        mail = "mesa" + table + "@cliente.com";
        pass = "cliente" + table;
        resetOrder();
        registerUser(mail, pass);

    }

    @Override
    public void homeListener(Boolean boo) {
        if (boo) {
            setFragment(new ScannerFragment());
        } else {
            mail = "mesa1@cliente.com";
            pass = "cliente1";
            resetOrder().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    registerUser(mail, pass);
                }
            });

        }

    }


    //TODO fragments de confirmacion. SU PEDIDO HA SIDO CONFIRMADO, PRONTO SERA LLEVADO A SU MESA(SI DESEA HACER UN NUEVO PEDIDO VUELVA A CAPTURAR EL CODIGO QR DE SU MESA)
    //TODO lottie de espera
    //TODO CAMBIAR RECYCLER DEL ORDER POR LISTA DE PRODUCTOS EN STAFF


    public LiveData<Boolean> resetOrder() {
        MutableLiveData<Boolean> liveBool = new MutableLiveData();
        ResultViewModel resultViewModel = ViewModelProviders.of(this).get(ResultViewModel.class);
        UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        resultViewModel.deleteActualOrder();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser == null) {
            userViewModel.loginUser(mail, pass).observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    if (aBoolean) {
                        firebaseUser = mAuth.getCurrentUser();
                        deleteUser().observe(MainActivity.this, new Observer<Boolean>() {
                            @Override
                            public void onChanged(Boolean aBoolean) {
                                liveBool.setValue(true);
                            }
                        });
                    } else liveBool.setValue(true);
                }
            });
        } else {
            deleteUser().observe(MainActivity.this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    liveBool.setValue(true);
                }
            });

        }
        return liveBool;
    }

    private LiveData<Boolean> deleteUser() {
        MutableLiveData<Boolean> liveBool = new MutableLiveData();
        AuthCredential authCredential = EmailAuthProvider.getCredential("user@example.com", "password1234");
        firebaseUser.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                firebaseUser.delete();
                liveBool.setValue(true);
            }
        });
        return liveBool;
    }


    @Override
    public void loginFragmentListener() {
        goStaffActivity("p");
    }
}