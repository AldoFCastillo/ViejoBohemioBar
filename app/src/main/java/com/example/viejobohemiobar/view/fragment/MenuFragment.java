package com.example.viejobohemiobar.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.viejobohemiobar.R;
import com.example.viejobohemiobar.model.pojo.Product;
import com.example.viejobohemiobar.view.activity.MainActivity;
import com.example.viejobohemiobar.model.pojo.Result;
import com.example.viejobohemiobar.view.activity.MenuActivity;
import com.example.viejobohemiobar.view.adapter.ProductAdapter;
import com.example.viejobohemiobar.viewModel.ResultViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuFragment extends Fragment {


    @BindView(R.id.viewPagerMenuTabs)
    ViewPager viewPagerMenuTabs;
    @BindView(R.id.appBarMenu)
    AppBarLayout appBarMenu;


    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        ButterKnife.bind(this, view);


        setViewPagerTabs();

        return view;
    }



    private void setViewPagerTabs() {
        TabLayout tabLayout = new TabLayout(getActivity());
        tabLayout.setTabTextColors(Color.parseColor("#000000"), Color.parseColor("#000000"));
        appBarMenu.addView(tabLayout);

        ViewPagerAdapterTabs viewPagerAdapterTabs = new ViewPagerAdapterTabs(getChildFragmentManager());

        viewPagerMenuTabs.setAdapter(viewPagerAdapterTabs);

        tabLayout.setupWithViewPager(viewPagerMenuTabs);
    }


    public static class ViewPagerAdapterTabs extends FragmentStatePagerAdapter {

        String[] titlePages = {"Hamburguesas","Extras","Bebidas"};


        public ViewPagerAdapterTabs(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            RecyclerMenuFragment recyclerMenuFragment;
            switch(position){
                case 0 :
                    recyclerMenuFragment = RecyclerMenuFragment.newInstance("1", MenuActivity.result);
                    return recyclerMenuFragment;
                case 1 :
                    recyclerMenuFragment = RecyclerMenuFragment.newInstance("2", MenuActivity.result);
                    return recyclerMenuFragment;
                case 2 :
                    recyclerMenuFragment = RecyclerMenuFragment.newInstance("3", MenuActivity.result);
                    return recyclerMenuFragment;
            }

            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titlePages[position];
        }




    }
}