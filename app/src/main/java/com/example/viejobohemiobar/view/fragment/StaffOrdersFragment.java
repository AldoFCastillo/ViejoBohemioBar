package com.example.viejobohemiobar.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.viejobohemiobar.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StaffOrdersFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    @BindView(R.id.textViewTitleStaff)
    TextView textViewTitle;
    @BindView(R.id.recyclerStaffFragment)
    RecyclerView recyclerView;

    public StaffOrdersFragment() {
        // Required empty public constructor
    }

    public static StaffOrdersFragment newInstance(String param1, String param2) {
        StaffOrdersFragment fragment = new StaffOrdersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_staff_orders, container, false);
        ButterKnife.bind(this, view);


        return view;
    }
}