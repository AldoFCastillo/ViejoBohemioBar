package com.example.viejobohemiobar.view.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.viejobohemiobar.R;
import com.example.viejobohemiobar.model.pojo.Order;
import com.example.viejobohemiobar.model.pojo.OrderLog;
import com.example.viejobohemiobar.view.adapter.OrderAdapter;
import com.example.viejobohemiobar.viewModel.ResultViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StaffOrdersFragment extends Fragment {


    private static final String ARG_LOG = "orderLog";
    public static final String ARG_PATH = "path";

    private OrderAdapter orderAdapter;
    private OrderLog orderLog;
    private String path;
    private String title;
    private listener listener;
    private Order order;
    private ResultViewModel resultViewModel;
    private int adapterPosition;

    @BindView(R.id.textViewTitleStaff)
    TextView textViewTitle;
    @BindView(R.id.viewPagerStaffTabs)
    ViewPager viewPagerStaffTabs;
    @BindView(R.id.appBarStaff)
    AppBarLayout appBarStaff;


    public StaffOrdersFragment() {
        // Required empty public constructor
    }

    public static StaffOrdersFragment newInstance(OrderLog orderLog, String path) {
        StaffOrdersFragment fragment = new StaffOrdersFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_LOG, orderLog);
        args.putString(ARG_PATH, path);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.orderLog = (OrderLog) getArguments().getSerializable(ARG_LOG);
            this.path = getArguments().getString(ARG_PATH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_staff_orders, container, false);
        ButterKnife.bind(this, view);


        TabLayout tabLayout = new TabLayout(getActivity());
        tabLayout.setTabTextColors(Color.parseColor("#000000"), Color.parseColor("#000000"));
        appBarStaff.addView(tabLayout);
        ViewPagerAdapterStaffTabs viewPagerAdapterStaffTabs = new ViewPagerAdapterStaffTabs(getChildFragmentManager());
        viewPagerStaffTabs.setAdapter(viewPagerAdapterStaffTabs);

        tabLayout.setupWithViewPager(viewPagerStaffTabs);

        return view;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.listener = (listener) context;
    }



    public interface listener {
        void StaffOrdersFragmentListener(Integer adapterPosition, OrderLog orderLog, String Path);
    }

    public class ViewPagerAdapterStaffTabs extends FragmentStatePagerAdapter {

        String[] titlePages = {"Pedidos Pendientes","Pedidos en proceso","Pedidos cerrados"};


        public ViewPagerAdapterStaffTabs(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            RecyclerStaffFragment recyclerStaffFragment;
            switch(position){
                case 0 :
                    recyclerStaffFragment = RecyclerStaffFragment.newInstance("p");
                    return recyclerStaffFragment;
                case 1 :
                    recyclerStaffFragment = RecyclerStaffFragment.newInstance("i");
                    return recyclerStaffFragment;
                case 2 :
                    recyclerStaffFragment = RecyclerStaffFragment.newInstance("c");
                    return recyclerStaffFragment;
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