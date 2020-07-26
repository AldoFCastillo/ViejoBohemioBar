package com.example.viejobohemiobar.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.viejobohemiobar.R;
import com.example.viejobohemiobar.view.adapter.ProductAdapter;
import com.example.viejobohemiobar.model.pojo.Product;
import com.example.viejobohemiobar.model.pojo.Result;
import com.example.viejobohemiobar.viewModel.ResultViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuFragment extends Fragment implements ProductAdapter.adapterListener{

    private static final String ARG_LIST = "productList";
    private List<Product> productList;
    private ProductAdapter productAdapter;

    @BindView(R.id.recyclerMenuFragment)
    RecyclerView recyclerView;

    public MenuFragment() {
        // Required empty public constructor
    }

    public static MenuFragment newInstance(Result result) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_LIST, result);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Result result=(Result) getArguments().getSerializable(ARG_LIST);
            productList = result.getResults();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        ButterKnife.bind(this, view);

        initRecycler();

        ResultViewModel resultViewModel = ViewModelProviders.of(this).get(ResultViewModel.class);

        resultViewModel.getResults().observe(this, new Observer<Result>() {
                    @Override
                    public void onChanged(Result result) {
                        productList = result.getResults();
                        productAdapter = new ProductAdapter(MenuFragment.this, productList);
                        recyclerView.setAdapter(productAdapter);

                    }
                });

        return view;
    }

    private void initRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(7);

    }

    @Override
    public void selection(Integer adapterPosition) {
        Toast.makeText(getContext(), productList.get(adapterPosition).getTitle(), Toast.LENGTH_SHORT).show();
    }
}