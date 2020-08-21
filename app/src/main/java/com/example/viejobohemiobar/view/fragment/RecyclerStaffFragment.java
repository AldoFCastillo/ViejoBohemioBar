package com.example.viejobohemiobar.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.viejobohemiobar.R;
import com.example.viejobohemiobar.model.pojo.Order;
import com.example.viejobohemiobar.model.pojo.OrderLog;
import com.example.viejobohemiobar.utils.ConfigRecyclerView;
import com.example.viejobohemiobar.view.adapter.OrderAdapter;
import com.example.viejobohemiobar.viewModel.ResultViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecyclerStaffFragment extends Fragment implements OrderAdapter.listener {

    public static final String ARG_PATH = "path";

    private ResultViewModel resultViewModel;
    private String path;
    private Order order;
    private int adapterPosition;
    private listener listener;
    private List<Order> orderList = new ArrayList<>();


    @BindView(R.id.recyclerStaffFragment)
    RecyclerView recyclerView;
    @BindView(R.id.textViewEmptyList)
    TextView textViewEmptyList;
    @BindView(R.id.constraintStaff)
    ConstraintLayout constraintLayoutStaff;

    public RecyclerStaffFragment() {
        // Required empty public constructor
    }


    public static RecyclerStaffFragment newInstance(String path) {
        RecyclerStaffFragment fragment = new RecyclerStaffFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PATH, path);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            path = getArguments().getString(ARG_PATH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recycler_staff, container, false);
        ButterKnife.bind(this, view);
        resultViewModel = new ViewModelProvider(this).get(ResultViewModel.class);


        resultViewModel.getOrderLog(path);
        getOrderLogObserver();


        return view;
    }

    private void getOrderLogObserver() {
        resultViewModel.orderLogData.observe(getViewLifecycleOwner(), orderLog -> {
                if (orderLog != null && orderLog.getOrderList()!=null) {
                    if (orderLog.getOrderList().isEmpty() ) {
                        textViewEmptyList.setVisibility(View.VISIBLE);
                    } else{
                        this.orderList = orderLog.getOrderList();
                        setAdapterRecycler(orderList);
                    }
                } else textViewEmptyList.setVisibility(View.VISIBLE);
        });

    }

    private void deleteOrder(){
        resultViewModel.deleteOrder(path, order.getId());
        deleteOrderObserver();
    }

    private void deleteOrderObserver(){
        resultViewModel.deleteOrderBool.observe(this, aBoolean -> {
            Toast.makeText(getContext(), "Pedido eliminado!", Toast.LENGTH_SHORT).show();
            orderList.remove(adapterPosition);
            setAdapterRecycler(orderList);
        });
    }


    private void setAdapterRecycler(List<Order> orderList) {
        textViewEmptyList.setVisibility(View.GONE);
        recyclerView = ConfigRecyclerView.getRecyclerView(recyclerView, getContext());
        OrderAdapter orderAdapter = new OrderAdapter(RecyclerStaffFragment.this, orderList);
        recyclerView.setAdapter(orderAdapter);
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

    @Override
    public void orderAdapterDeleteListener(Integer adapterPosition, Order order) {
        this.order = order;
        this.adapterPosition = adapterPosition;
        Snackbar mySnackbar = Snackbar.make(constraintLayoutStaff, "Confirma que desea eliminar la orden?", Snackbar.LENGTH_SHORT);
        mySnackbar.setAction("ACEPTAR", v -> {
            deleteOrder();
        });
        mySnackbar.show();
    }

    public interface listener {
        void StaffOrdersFragmentListener(Integer adapterPosition, OrderLog orderLog, String Path);
    }

}