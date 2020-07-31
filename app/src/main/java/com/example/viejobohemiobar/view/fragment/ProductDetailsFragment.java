package com.example.viejobohemiobar.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.viejobohemiobar.R;
import com.example.viejobohemiobar.model.pojo.Product;
import com.example.viejobohemiobar.model.pojo.Result;
import com.example.viejobohemiobar.viewModel.ResultViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProductDetailsFragment extends Fragment {


    private static final String ARG_PRODUCT = "product";


    private Product product;
    private ResultViewModel resultViewModel;


    @BindView(R.id.textViewTitleDetails)
    TextView textViewTitleDetails;
    @BindView(R.id.textViewPriceDetails)
    TextView textViewPriceDetails;
    @BindView(R.id.textViewDescriptionDetails)
    TextView textViewDescriptionDetails;
    @BindView(R.id.imageViewDetails)
    ImageView imageViewDetails;
    @BindView(R.id.floatingAdd)
    FloatingActionButton floatingAdd;

    public ProductDetailsFragment() {
        // Required empty public constructor
    }

    public static ProductDetailsFragment newInstance(Product product) {
        ProductDetailsFragment fragment = new ProductDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT, product);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = (Product) getArguments().getSerializable(ARG_PRODUCT);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_details, container, false);

        ButterKnife.bind(this, view);

        resultViewModel = ViewModelProviders.of(this).get(ResultViewModel.class);

        textViewTitleDetails.setText(product.getTitle());
        textViewPriceDetails.setText(product.getPrice());
        textViewDescriptionDetails.setText(product.getDescription());
        Glide.with(view).load(product.getPicture()).into(imageViewDetails);

        floatingAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resultViewModel.getActualOrder().observe(getViewLifecycleOwner(), new Observer<Result>() {
                    @Override
                    public void onChanged(Result result) {
                        List<Product> productList;
                        if (result==null){
                            result = new Result();
                            productList = new ArrayList<>();
                        }else productList = result.getResults();
                        productList.add(product);
                        result.setResults(productList);
                        updateActualOrder(result);
                    }
                });

            }
        });


        return view;
    }

    private void updateActualOrder(Result result) {

        resultViewModel.updateActualOrder(result).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    Toast.makeText(getContext(), "Producto agregado a tu pedido", Toast.LENGTH_SHORT).show();
                }else Toast.makeText(getContext(), "Ocurrio un error", Toast.LENGTH_SHORT).show();
            }
        });

    }






}