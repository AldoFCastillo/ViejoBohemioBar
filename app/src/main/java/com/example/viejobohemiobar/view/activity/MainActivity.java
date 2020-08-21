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
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.viejobohemiobar.R;
import com.example.viejobohemiobar.model.pojo.OrderLog;
import com.example.viejobohemiobar.view.fragment.HomeFragment;
import com.example.viejobohemiobar.view.fragment.LoginFragment;
import com.example.viejobohemiobar.view.fragment.PasswordFragment;
import com.example.viejobohemiobar.view.fragment.ScannerFragment;
import com.example.viejobohemiobar.viewModel.ResultViewModel;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements HomeFragment.listener, LoginFragment.loginListener {

    public static final String TAG = "Main Activity: ";

    private long backPressedTime;
    private Toast backToast;
    private MenuItem menuLogin;
    private MenuItem buttonLogin;
    private ResultViewModel resultViewModel;


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

        resultViewModel = new ViewModelProvider(this).get(ResultViewModel.class);

        createNotificationChannel();
        setFragment(new HomeFragment());
        setToolBar();
        setNavigationView();
        setDBListener();
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID")
                .setSmallIcon(R.drawable.ic_home_black_24dp)
                .setContentTitle("PEDIDO NUEVO!")
                .setContentText("Han confirmado un nuevo pedido")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        double idDouble = Math.random();
        int id = (int) idDouble;
        notificationManager.notify(id, builder.build());
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
        menuLogin = navigationView.getMenu().findItem(R.id.navigationViewLoginItem);
        buttonLogin = navigationView.getMenu().findItem(R.id.login);
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
        String table = "1";
        /*IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if ((result != null) && (result.getContents() != null)) {
            table = result.getContents();
        } else Toast.makeText(this, "Ocurrio un error", Toast.LENGTH_SHORT).show();*/
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
    //TODO progressbar


    @Override
    public void loginFragmentListener() {
        setFragment(new HomeFragment());
        setNavigationView();
    }


}