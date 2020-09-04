package com.example.viejobohemiobar.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.viejobohemiobar.R;
import com.example.viejobohemiobar.model.pojo.OrderLog;
import com.example.viejobohemiobar.utils.MenuUtils;
import com.example.viejobohemiobar.view.fragment.HomeFragment;
import com.example.viejobohemiobar.view.fragment.LoginFragment;
import com.example.viejobohemiobar.view.fragment.PasswordFragment;
import com.example.viejobohemiobar.view.fragment.ScannerFragment;
import com.example.viejobohemiobar.viewModel.ResultViewModel;
import com.facebook.stetho.Stetho;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements HomeFragment.listener, LoginFragment.loginListener {

    public static final String TAG = "Main Activity: ";

    private long backPressedTime;
    private Toast backToast;
    private ResultViewModel resultViewModel;
    private String table;


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
        Stetho.initializeWithDefaults(this);
        ButterKnife.bind(this);

        resultViewModel = new ViewModelProvider(this).get(ResultViewModel.class);
        MenuUtils.createNotificationChannel(getSystemService(NotificationManager.class));

        setFragment(new HomeFragment());
        setToolBar();
        setNavigationView();
        setDBListener();
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
        return (currentUser == null);
    }


    private void setNavigationView() {
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.home_menu);
        MenuItem menuLogin = navigationView.getMenu().findItem(R.id.navigationViewLoginItem);
        MenuItem buttonLogin = navigationView.getMenu().findItem(R.id.login);
        if (checkUSer()) {
            menuLogin.setVisible(false);
            buttonLogin.setVisible(true);
        } else {
            menuLogin.setVisible(true);
            buttonLogin.setVisible(false);
        }
        navigationView.setNavigationItemSelectedListener(item -> {
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
                    setNavigationView();
                    Toast.makeText(MainActivity.this, "Desconexion exitosa", Toast.LENGTH_SHORT).show();
                    break;
            }
            drawerLayout.closeDrawers();

            return true;
        });


    }

    private void setDBListener() {
        resultViewModel.listenPending();
        dBListenerObserver();

    }

    private void dBListenerObserver() {
        resultViewModel.dbListener.observe(this, aBoolean -> {
            if (aBoolean != null && !checkUSer()) {
                if (aBoolean) {
                    Toast.makeText(MainActivity.this, "NUEVO PEDIDO", Toast.LENGTH_SHORT).show();
                    sendNotification();
                }
            }
        });
    }

    private void sendNotification() {
        double idDouble = Math.random();
        int id = (int) idDouble;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID")
                .setSmallIcon(R.drawable.ic_hamburguer)
                .setContentTitle("PEDIDO NUEVO!")
                .setContentText("Han confirmado un nuevo pedido")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, StaffActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(id, builder.build());
    }

    private void goStaffActivity() {
        Intent intent = new Intent(this, StaffActivity.class);
        startActivity(intent);
    }

    private void goMenuActivity(String table) {
        ResultViewModel resultViewModel = new ViewModelProvider(this).get(ResultViewModel.class);
        resultViewModel.deleteActualOrder(table);
        Intent intent = new Intent(this, MenuActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(MenuActivity.ARG_TABLE, table);
        intent.putExtras(bundle);
        startActivity(intent);
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
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        setFragment(new HomeFragment());
        if ((result != null) && (result.getContents() != null)) {
            validateQRCode(result.getContents());
        } else Toast.makeText(this, "Ocurrio un error", Toast.LENGTH_SHORT).show();


    }

    private void validateQRCode(String qrcode) {
        //BohemioScanner@#1 - BohemioScanner@#1#001
        if (qrcode.startsWith("BohemioScanner@")) {
            qrcode = qrcode.substring(14);
            String[] ids = qrcode.split("#");
            table = ids[1];
            goMenuActivity(table);
        } else Toast.makeText(this, "Ocurrio un error", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void homeListener(Boolean boo) {
        if (boo) {
            setFragment(new ScannerFragment());
        } else {
            goMenuActivity("1");
        }

    }


    @Override
    public void loginFragmentListener() {
        setFragment(new HomeFragment());
        setNavigationView();
    }


}