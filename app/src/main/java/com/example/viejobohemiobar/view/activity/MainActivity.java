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

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.viejobohemiobar.R;
import com.example.viejobohemiobar.model.pojo.Result;
import com.example.viejobohemiobar.view.fragment.HomeFragment;
import com.example.viejobohemiobar.view.fragment.MenuFragment;
import com.example.viejobohemiobar.view.fragment.ScannerFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements HomeFragment.listener, MenuFragment.listener {

    private FragmentManager fragmentManager;
    private MenuItem menuLogin;

    @BindView(R.id.toolbar)
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

        HomeFragment homeFragment = new HomeFragment();
        setFragment(homeFragment);
    }

    public void setFragment(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerFragmentMain, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void homeListener(Boolean boo) {
        if (boo) {
            setFragment(new ScannerFragment());
        } else setFragment(new MenuFragment());

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ButterKnife.bind(this);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        setNavigationView();
        setToolBar();

        String text;
        if ((result != null) && (result.getContents() != null)) {
            text = result.getContents();
        } else text = "Error";
        HomeFragment homeFragment = HomeFragment.newInstance(text);
        setFragment(homeFragment);

        setFragment(new MenuFragment());
    }

    private void setToolBar() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemToolbarYourOrder:
                Toast.makeText(this, "Tu pedido", Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawers();
                return true;

            default:
                return super.onOptionsItemSelected(item);
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

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }*/

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        setFragment(new HomeFragment());
    }

    @Override
    public void menuListener(Integer adapterPosition, Result result) {
        Bundle bundle = new Bundle();
        bundle.putInt(ProductDetails.KEY_POSITION, adapterPosition);
        bundle.putSerializable(ProductDetails.KEY_RESULT, result);
        Intent intent = new Intent(this, ProductDetails.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}