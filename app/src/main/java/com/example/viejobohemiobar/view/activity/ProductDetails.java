package com.example.viejobohemiobar.view.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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

public class ProductDetails extends AppCompatActivity {

    public static final String KEY_POSITION = "position";
    public static final String KEY_RESULT = "result";

    private List<Product> productList = new ArrayList<>();
    private FragmentManager fragmentManager;

    @BindView(R.id.constraintDetails)
    ConstraintLayout constraintDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Result result =(Result) getIntent().getExtras().getSerializable(KEY_RESULT);
        productList = result.getResults();
        Product product = (Product) productList.get(getIntent().getExtras().getInt(KEY_POSITION));
        ProductDetailsFragment productDetailsFragment = ProductDetailsFragment.newInstance(product);
        setFragment(productDetailsFragment);
    }

    public void setFragment(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.constraintDetails, fragment);
        fragmentTransaction.commit();
    }


}