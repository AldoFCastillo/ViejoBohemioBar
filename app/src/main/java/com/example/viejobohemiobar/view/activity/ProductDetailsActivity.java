package com.example.viejobohemiobar.view.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.viejobohemiobar.R;
import com.example.viejobohemiobar.view.adapter.ViewPagerAdapter;
import com.example.viejobohemiobar.view.fragment.ProductDetailsFragment;
import com.example.viejobohemiobar.model.pojo.Product;
import com.example.viejobohemiobar.model.pojo.Result;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductDetailsActivity extends AppCompatActivity  {

    public static final String KEY_POSITION = "position";
    public static final String KEY_RESULT = "result";

    private List<Product> productList = new ArrayList<>();
    private List<Product> productListOrder;
    private FragmentManager fragmentManager;

    @BindView(R.id.viewPagerDetails)
    ViewPager viewPager;
    @BindView(R.id.toolbarDetails)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        ButterKnife.bind(this);

        productListOrder = new ArrayList<>();

        setToolBar();

        Result result =(Result) getIntent().getExtras().getSerializable(KEY_RESULT);
        productList = result.getResults();
        int adapterPostion = getIntent().getExtras().getInt(KEY_POSITION);
        buildDetailsFragmentList(productList, adapterPostion);

    }

    private void setToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void buildDetailsFragmentList(List<Product> productList, Integer adapterPosition) {
        List<Fragment> fragmentList = new ArrayList<>();
        for (Product details : productList) {
            ProductDetailsFragment detailsFragment = ProductDetailsFragment.newInstance(details);
            fragmentList.add(detailsFragment);
        }
        setViewPager(adapterPosition, fragmentList);
    }

    private void setViewPager(Integer adapterPosition, List<Fragment> fragmentList) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(adapterPosition);
    }





}