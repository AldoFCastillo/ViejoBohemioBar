package com.example.viejobohemiobar.view.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.viejobohemiobar.R;
import com.example.viejobohemiobar.view.fragment.ProductDetailsFragment;
import com.example.viejobohemiobar.model.pojo.Product;
import com.example.viejobohemiobar.model.pojo.Result;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductDetailsActivity extends AppCompatActivity {

    public static final String KEY_POSITION = "position";
    public static final String KEY_RESULT = "result";

    private List<Product> productList = new ArrayList<>();
    private FragmentManager fragmentManager;

    @BindView(R.id.constraintDetails)
    ConstraintLayout constraintDetails;
    @BindView(R.id.toolbarDetails)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        ButterKnife.bind(this);

        setToolBar();

        Result result =(Result) getIntent().getExtras().getSerializable(KEY_RESULT);
        productList = result.getResults();
        Product product = (Product) productList.get(getIntent().getExtras().getInt(KEY_POSITION));
        ProductDetailsFragment productDetailsFragment = ProductDetailsFragment.newInstance(product);
        setFragment(productDetailsFragment);
    }

    private void setToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemToolbarYourOrder:
                Toast.makeText(this, "Tu pedido", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setFragment(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.constraintDetails, fragment);
        fragmentTransaction.commit();
    }


}