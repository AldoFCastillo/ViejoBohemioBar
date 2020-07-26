package com.example.viejobohemiobar.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.viejobohemiobar.R;
import com.example.viejobohemiobar.model.pojo.Product;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProductDetailsFragment extends Fragment {


    private static final String ARG_PRODUCT = "product";


    private Product product;


    @BindView(R.id.textViewTitleDetails)
    TextView textViewTitleDetails;
    @BindView(R.id.textViewPriceDetails)
    TextView textViewPriceDetails;
    @BindView(R.id.textViewDescriptionDetails)
    TextView textViewDescriptionDetails;
    @BindView(R.id.imageViewDetails)
    ImageView imageViewDetails;

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

        textViewTitleDetails.setText(product.getTitle());
        textViewPriceDetails.setText(product.getPrice());
        textViewDescriptionDetails.setText(product.getDescription());
        Glide.with(view).load(product.getPicture()).into(imageViewDetails);


        return view;
    }
}