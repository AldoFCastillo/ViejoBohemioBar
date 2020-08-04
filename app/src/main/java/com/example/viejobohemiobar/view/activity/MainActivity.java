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
import androidx.lifecycle.ViewModelProviders;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.viejobohemiobar.R;
import com.example.viejobohemiobar.view.fragment.HomeFragment;
import com.example.viejobohemiobar.view.fragment.LoginFragment;
import com.example.viejobohemiobar.view.fragment.PasswordFragment;
import com.example.viejobohemiobar.view.fragment.ScannerFragment;
import com.example.viejobohemiobar.viewModel.ResultViewModel;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
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

            setFragment(new HomeFragment());
            setToolBar();
            setNavigationView();

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

                    case R.id.navigationViewOrdersBacklog:
                        goStaffActivity();
                        break;

                    case R.id.registerMenuNavigation:
                        setFragment(new LoginFragment());
                        Snackbar.make(mainLayout, "Registro", Snackbar.LENGTH_LONG).show();
                        break;

                    case R.id.changePassMenuNavigation:
                        setFragment(new PasswordFragment());
                        Snackbar.make(mainLayout, "Cambiar clave", Snackbar.LENGTH_LONG).show();
                        break;


                    case R.id.navigationViewCerrarSesionItem:
                        FirebaseAuth.getInstance().signOut();
                        menuLogin.setVisible(false);
                        buttonLogin.setVisible(true);
                        drawerLayout.closeDrawers();
                        Toast.makeText(MainActivity.this, "Desconexion exitosa", Toast.LENGTH_SHORT).show();
                        break;


                }

                drawerLayout.closeDrawers();

                return false;
            }
        });

    }

    private void goStaffActivity() {
        Intent intent = new Intent(this, StaffActivity.class);
        startActivity(intent);
    }

    private void goMenuActivity(String table) {
        ResultViewModel resultViewModel = ViewModelProviders.of(this).get(ResultViewModel.class);
        resultViewModel.deleteActualOrder(table);
        Intent intent = new Intent(this, MenuActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(MenuActivity.ARG_TABLE, table);
        intent.putExtras(bundle);
        startActivity(intent);
    }

  /*  private void registerUser(String mail, String pass) {
        UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.registerUser(mail, pass).observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    goMenuActivity();
                    Toast.makeText(MainActivity.this, "Ya podes armar tu pedido", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }*/


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
        goMenuActivity(table);

    }

    @Override
    public void homeListener(Boolean boo) {
        if (boo) {
            setFragment(new ScannerFragment());
        } else {
            goMenuActivity("1");
        }

    }


    //TODO fragments de confirmacion. SU PEDIDO HA SIDO CONFIRMADO, PRONTO SERA LLEVADO A SU MESA(SI DESEA HACER UN NUEVO PEDIDO VUELVA A CAPTURAR EL CODIGO QR DE SU MESA)
    //TODO lottie de espera
    //TODO CAMBIAR RECYCLER DEL ORDER POR LISTA DE PRODUCTOS EN STAFF
    //TODO NOTIFICACIONES




    @Override
    public void loginFragmentListener() {
        goStaffActivity();
    }
}