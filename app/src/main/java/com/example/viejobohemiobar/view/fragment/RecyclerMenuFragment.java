package com.example.viejobohemiobar.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.viejobohemiobar.R;
import com.example.viejobohemiobar.model.pojo.Product;
import com.example.viejobohemiobar.model.pojo.Result;
import com.example.viejobohemiobar.utils.ConfigRecyclerView;
import com.example.viejobohemiobar.view.adapter.ProductAdapter;
import com.example.viejobohemiobar.viewModel.ResultViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerMenuFragment extends Fragment implements ProductAdapter.adapterListener {

    private static final String ARG_TYPE = "type";


    private List<Product> productList;
    private ProductAdapter productAdapter;
    private listener listener;
    private String type;
    private ResultViewModel resultViewModel;



    @BindView(R.id.recyclerMenuFragment)
    RecyclerView recyclerView;

    public RecyclerMenuFragment() {
        // Required empty public constructor
    }

    public static RecyclerMenuFragment newInstance(String type) {
        RecyclerMenuFragment fragment = new RecyclerMenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString(ARG_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recycler_menu, container, false);
        ButterKnife.bind(this, view);


        recyclerView = ConfigRecyclerView.getRecyclerView(recyclerView, getContext());

        resultViewModel = new ViewModelProvider(this).get(ResultViewModel.class);

        resultViewModel.getResults();
        getResultsObserver();

        return view;
    }

    private void getResultsObserver(){
        resultViewModel.resultData.observe(getViewLifecycleOwner(), result -> {
            productList = result.getResults();
            List<Product> selectionList = new ArrayList<>();

            for (Product product: productList) {
                if(product.getType().equals(type)){
                    selectionList.add(product);
                }
            }
            productAdapter = new ProductAdapter(RecyclerMenuFragment.this, selectionList);
            recyclerView.setAdapter(productAdapter);
        });
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.listener = (listener) context;
    }

    @Override
    public void selection(Integer adapterPosition, Result result) {
        listener.recyclerMenuListener(adapterPosition, result);
    }
    public interface listener{
        void recyclerMenuListener(Integer adapterPosition, Result result);
    }
}