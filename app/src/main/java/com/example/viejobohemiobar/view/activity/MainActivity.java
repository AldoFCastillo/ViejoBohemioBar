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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.viejobohemiobar.R;
import com.example.viejobohemiobar.model.pojo.Result;
import com.example.viejobohemiobar.view.fragment.HomeFragment;
import com.example.viejobohemiobar.view.fragment.MenuFragment;
import com.example.viejobohemiobar.view.fragment.OrderFragment;
import com.example.viejobohemiobar.view.fragment.ScannerFragment;
import com.example.viejobohemiobar.viewModel.ResultViewModel;
import com.example.viejobohemiobar.viewModel.UserViewModel;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements HomeFragment.listener, MenuFragment.listener, OrderFragment.listener {

    public static final String KEY_RESULT = "result";

    private FragmentManager fragmentManager;
    private MenuFragment menuFragment;
    private HomeFragment homeFragment;
    private long backPressedTime;
    private Toast backToast;
    private MenuItem menuLogin;
    private Result resultOrder = new Result();

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
        checkUser();
        menuFragment = new MenuFragment();
        setNavigationView();
        setToolBar();

    }


    public void setFragment(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemToolbarYourOrder:
                ResultViewModel resultViewModel = ViewModelProviders.of(this).get(ResultViewModel.class);
                resultViewModel.getActualOrder().observe(this, new Observer<Result>() {
                    @Override
                    public void onChanged(Result result) {
                        if (result != null) {
                            OrderFragment orderFragment = OrderFragment.newInstance(result);
                            setFragment(orderFragment);
                            Toast.makeText(MainActivity.this, "Tu pedido", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(MainActivity.this, "Aun no has agregado ningun producto", Toast.LENGTH_SHORT).show();
                    }
                });
                drawerLayout.closeDrawers();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void checkUser(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String unMail = currentUser.getEmail();
        if (currentUser!=null){
            setFragment(new MenuFragment());
        }else{
            setFragment(new HomeFragment());
        }
    }


    private void setNavigationView() {
        // menuLogin = navigationView.getMenu().findItem(R.id.navigationViewLoginItem);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigationViewPedidosPendientesItem:
                        // setFragment(new ProfileFragment());
                        Snackbar.make(mainLayout, "Pedidos pendientes", Snackbar.LENGTH_LONG).show();
                        break;

                    case R.id.navigationViewPedidosPreparacionItem:
                        //  setFragment(new FavsFragment());
                        Snackbar.make(mainLayout, "Pedidos en preparacion", Snackbar.LENGTH_LONG).show();
                        break;

                    case R.id.navigationViewPedidosEntregadosItem:
                        Toast.makeText(MainActivity.this, "Pedidos entregados", Toast.LENGTH_SHORT).show();
                        //setVisibilityNavigation(false, "");
                        break;

                    case R.id.navigationViewCerrarSesionItem:
                        // FirebaseAuth.getInstance().signOut();
                        drawerLayout.closeDrawers();
                        Toast.makeText(MainActivity.this, "Desconexion exitosa", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.aboutUsMenuNavigation:
                        //setFragment(aboutUSFragment);
                        Snackbar.make(mainLayout, "About Us", Snackbar.LENGTH_LONG).show();
                        break;
                }

                drawerLayout.closeDrawers();

                return false;
            }
        });

    }

    private void registerUser(String mail, String pass) {
        UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.registerUser(mail, pass).observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    setFragment(new MenuFragment());
                    Toast.makeText(MainActivity.this, "Hola! Ya podes armar tu pedido", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }


    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            Fragment fragment = fragmentManager.findFragmentById(R.id.containerFragmentMain);
            if (fragment instanceof MenuFragment) {
                setFragment(homeFragment);
            } else {
                if (fragment instanceof HomeFragment) {
                    super.onBackPressed();
                } else {
                    setFragment(menuFragment);
                    backToast = Toast.makeText(getBaseContext(), "Presiona atras nuevamente para salir", Toast.LENGTH_SHORT);
                    backToast.show();
                }
            }
        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String table ="1";
        /*IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        String text;
        if ((result != null) && (result.getContents() != null)) {
            text = result.getContents();
        } else text = "Error";*/
        registerUser("cliente"+table+"@cliente.com","cliente"+table);

    }

    @Override
    public void homeListener(Boolean boo) {
        if (boo) {
            setFragment(new ScannerFragment());
        } else {
            registerUser("cliente1@cliente.com","cliente1");
        }

    }

    @Override
    public void menuListener(Integer adapterPosition, Result result) {
        Bundle bundle = new Bundle();
        bundle.putInt(ProductDetailsActivity.KEY_POSITION, adapterPosition);
        bundle.putSerializable(ProductDetailsActivity.KEY_RESULT, result);
        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void orderFragmentListener(Boolean confirm) {
        //TODO fragments de confirmacion?
        if (confirm) {
            setFragment(new MenuFragment());
        } else {
            setFragment(new MenuFragment());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ResultViewModel resultViewModel = ViewModelProviders.of(this).get(ResultViewModel.class);
        resultViewModel.deleteActualOrder();

        //TODO BORRAR USUARIO
    }
}