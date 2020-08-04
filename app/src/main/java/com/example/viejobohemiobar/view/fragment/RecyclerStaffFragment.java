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


public class RecyclerStaffFragment extends Fragment implements OrderAdapter.listener{

    private static final String ARG_PARAM1 = "param1";
    public static final String ARG_PATH = "path";

    private String mParam1;
    private String mParam2;
    private ResultViewModel resultViewModel;
    private String path;
    private String title;
    private OrderAdapter orderAdapter;
    private Order order;
    private int adapterPosition;
    private listener listener;


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
        View view =inflater.inflate(R.layout.fragment_recycler_staff, container, false);
        ButterKnife.bind(this, view);
        resultViewModel = ViewModelProviders.of(this).get(ResultViewModel.class);

        /*switch (path) {
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

        textViewTitle.setText(title);*/

        resultViewModel = ViewModelProviders.of(this).get(ResultViewModel.class);
        resultViewModel.getOrderLog(path).observe(getViewLifecycleOwner(), new Observer<OrderLog>() {
            @Override
            public void onChanged(OrderLog orderLog) {
                if (orderLog!=null){
                    if (orderLog.getOrderList().isEmpty()) {
                        textViewEmptyList.setVisibility(View.VISIBLE);
                    }else setAdapterRecycler(orderLog.getOrderList());
                }else textViewEmptyList.setVisibility(View.VISIBLE);

            }
        });


        return view;
    }


    private void setAdapterRecycler(List<Order> orderList) {
        textViewEmptyList.setVisibility(View.GONE);
        recyclerView = ConfigRecyclerView.getRecyclerView(recyclerView, getContext());
        orderAdapter = new OrderAdapter(RecyclerStaffFragment.this, orderList, path);
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