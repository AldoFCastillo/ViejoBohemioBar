package com.example.viejobohemiobar.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.viejobohemiobar.R;
import com.example.viejobohemiobar.view.adapter.ViewPagerAdapter;
import com.example.viejobohemiobar.view.fragment.DetailsViewPagerFragment;
import com.example.viejobohemiobar.view.fragment.HomeFragment;
import com.example.viejobohemiobar.view.fragment.MenuFragment;
import com.example.viejobohemiobar.view.fragment.OrderFragment;
import com.example.viejobohemiobar.view.fragment.ProductDetailsFragment;
import com.example.viejobohemiobar.model.pojo.Product;
import com.example.viejobohemiobar.model.pojo.Result;
import com.example.viejobohemiobar.view.fragment.RecyclerMenuFragment;
import com.example.viejobohemiobar.view.fragment.StaffOrdersFragment;
import com.example.viejobohemiobar.viewModel.ResultViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuActivity extends AppCompatActivity implements RecyclerMenuFragment.listener, OrderFragment.listener {

    public static final String ARG_TABLE = "table";


    private String table;
    private ResultViewModel resultViewModel;
    private FragmentManager fragmentManager;
    private MenuFragment menuFragment = new MenuFragment();
    private long backPressedTime;
    private Toast backToast;


    @BindView(R.id.toolbarDetails)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ButterKnife.bind(this);

        resultViewModel = new ViewModelProvider(this).get(ResultViewModel.class);

        Bundle bundle = getIntent().getExtras();
        table = bundle.getString(ARG_TABLE);


        setToolBar();

        setFragment(new MenuFragment());
        Toast.makeText(MenuActivity.this, "Ya podes armar tu pedido", Toast.LENGTH_SHORT).show();


    }

    public void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.constraintMenuActivity, fragment);
        fragmentTransaction.commit();
    }

    private void setToolBar() {
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemToolbarYourOrder:
                resultViewModel.getActualOrder(table);
                getActualOrderObserver();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getActualOrderObserver() {
        resultViewModel.resultActualData.observe(this, result -> {
            if (result != null) {
                OrderFragment orderFragment = OrderFragment.newInstance(result, "", 0);
                setFragment(orderFragment);
                Toast.makeText(MenuActivity.this, "Tu pedido", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(MenuActivity.this, "Aun no has agregado ningun producto", Toast.LENGTH_SHORT).show();

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }


    @Override
    public void recyclerMenuListener(Integer adapterPosition, Result result) {
        DetailsViewPagerFragment detailsViewPagerFragment = DetailsViewPagerFragment.newInstance(adapterPosition, result, table);
        setFragment(detailsViewPagerFragment);
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();

        } else {
            backToast = Toast.makeText(getBaseContext(), "Presiona atras nuevamente para salir", Toast.LENGTH_SHORT);
            backToast.show();
            setFragment(new MenuFragment());
        }
        backPressedTime = System.currentTimeMillis();

    }

    @Override
    public void orderFragmentListener() {
        onBackPressed();
       /* Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);*/
    }
}