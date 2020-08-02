package com.example.viejobohemiobar.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.viejobohemiobar.R;
import com.example.viejobohemiobar.model.pojo.Order;
import com.example.viejobohemiobar.model.pojo.OrderLog;
import com.example.viejobohemiobar.service.ConfigRecyclerView;
import com.example.viejobohemiobar.view.adapter.OrderAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StaffOrdersFragment extends Fragment implements OrderAdapter.listener{


    private static final String ARG_LOG = "orderLog";
    public static final String ARG_PATH = "path";

    private OrderAdapter orderAdapter;
    private OrderLog orderLog;
    private String path;
    private String title;
    private listener listener;

    @BindView(R.id.textViewTitleStaff)
    TextView textViewTitle;
    @BindView(R.id.recyclerStaffFragment)
    RecyclerView recyclerView;

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
        View view =  inflater.inflate(R.layout.fragment_staff_orders, container, false);
        ButterKnife.bind(this, view);

        switch(path){
            case "p": title = "Pedidos Pendientes";
                break;
            case "i": title = "Pedidos en proceso";
                break;
            case "c": title = "Pedidos cerrados";
                break;
        }

        textViewTitle.setText(title);
        recyclerView = ConfigRecyclerView.getRecyclerView(recyclerView, getContext());
        orderAdapter = new OrderAdapter(StaffOrdersFragment.this, orderLog.getOrderList());
        recyclerView.setAdapter(orderAdapter);


        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.listener = (listener) context;
    }

    @Override
    public void orderAdapterListener(Integer adapterPosition, OrderLog orderLog) {
            listener.StaffOrdersFragmentListener(adapterPosition, orderLog, path);
    }

    public interface listener{
        void StaffOrdersFragmentListener(Integer adapterPosition, OrderLog orderLog, String Path);
    }
}