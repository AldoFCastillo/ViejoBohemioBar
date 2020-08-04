package com.example.viejobohemiobar.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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
import com.example.viejobohemiobar.service.ConfigRecyclerView;
import com.example.viejobohemiobar.view.adapter.OrderAdapter;
import com.example.viejobohemiobar.viewModel.ResultViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StaffOrdersFragment extends Fragment implements OrderAdapter.listener {


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
    @BindView(R.id.recyclerStaffFragment)
    RecyclerView recyclerView;
    @BindView(R.id.constraintStaff)
    ConstraintLayout constraintLayoutStaff;
    @BindView(R.id.textViewEmptyList)
    TextView textViewEmptyList;

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

        resultViewModel = ViewModelProviders.of(this).get(ResultViewModel.class);

        switch (path) {
            case "p":
                title = "Pedidos Pendientes";
                break;
            case "i":
                title = "Pedidos en proceso";
                break;
            case "c":
                title = "Pedidos cerrados";
                break;
        }

        textViewTitle.setText(title);
        if (!orderLog.getOrderList().isEmpty()) {
            setAdapterRecycler(orderLog.getOrderList());
        } else textViewEmptyList.setVisibility(View.VISIBLE);


        return view;
    }

    private void setAdapterRecycler(List<Order> orderList) {
        textViewEmptyList.setVisibility(View.GONE);
        recyclerView = ConfigRecyclerView.getRecyclerView(recyclerView, getContext());
        orderAdapter = new OrderAdapter(StaffOrdersFragment.this, orderList, path);
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
    public void orderAdapterDeleteListener(Integer adapterPosition,Order order) {
        this.order = order;
        this.adapterPosition = adapterPosition;
        Snackbar mySnackbar = Snackbar.make(constraintLayoutStaff, "Confirma que desea eliminar la orden?", Snackbar.LENGTH_SHORT);
        mySnackbar.setAction("ACEPTAR", new ConfirmOrderDelete());
        mySnackbar.show();
    }

    public interface listener {
        void StaffOrdersFragmentListener(Integer adapterPosition, OrderLog orderLog, String Path);
    }

    public class ConfirmOrderDelete implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            resultViewModel.getOrderLog(path).observe(getViewLifecycleOwner(), new Observer<OrderLog>() {
                @Override
                public void onChanged(OrderLog orderLog) {
                    List<Order> newOrderList = orderLog.getOrderList();
                    newOrderList.remove(adapterPosition);
                    orderLog.setOrderList(newOrderList);
                    resultViewModel.updateOrderLog(orderLog, path).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean aBoolean) {
                            if (aBoolean) {
                                Toast.makeText(getContext(), "Pedido eliminado!", Toast.LENGTH_SHORT).show();
                                setAdapterRecycler(newOrderList);
                            }
                        }
                    });
                }
            });
        }
    }
}