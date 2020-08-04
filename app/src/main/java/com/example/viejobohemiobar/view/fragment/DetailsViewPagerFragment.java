package com.example.viejobohemiobar.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.viejobohemiobar.R;
import com.example.viejobohemiobar.model.pojo.Product;
import com.example.viejobohemiobar.model.pojo.Result;
import com.example.viejobohemiobar.view.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailsViewPagerFragment extends Fragment {


    private static final String ARG_POSITION = "position";
    private static final String ARG_RESULT = "result";

    private int adapterPosition;
    private Result result;
    private List<Product> productList = new ArrayList<>();

    @BindView(R.id.viewPagerDetails)
    ViewPager viewPager;

    public DetailsViewPagerFragment() {
        // Required empty public constructor
    }


    public static DetailsViewPagerFragment newInstance(int adapterPosition, Result result) {
        DetailsViewPagerFragment fragment = new DetailsViewPagerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, adapterPosition);
        args.putSerializable(ARG_RESULT, result);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            adapterPosition = getArguments().getInt(ARG_POSITION);
            result = (Result)getArguments().getSerializable(ARG_RESULT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details_view_pager, container, false);
        ButterKnife.bind(this, view);

        productList = result.getResults();
        buildDetailsFragmentList(productList, adapterPosition);

        return view;
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
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), fragmentList);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(adapterPosition);
    }


}