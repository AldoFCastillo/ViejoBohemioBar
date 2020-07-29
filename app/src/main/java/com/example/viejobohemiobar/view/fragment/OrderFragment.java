package com.example.viejobohemiobar.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.viejobohemiobar.R;
import com.example.viejobohemiobar.model.pojo.Order;
import com.example.viejobohemiobar.model.pojo.OrderLog;
import com.example.viejobohemiobar.model.pojo.Product;
import com.example.viejobohemiobar.model.pojo.Result;
import com.example.viejobohemiobar.service.ConfigRecyclerView;
import com.example.viejobohemiobar.view.activity.MainActivity;
import com.example.viejobohemiobar.view.adapter.ProductAdapter;
import com.example.viejobohemiobar.viewModel.ResultViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderFragment extends Fragment implements ProductAdapter.adapterListener {

    private static final String ARG_RESULT = "param1";


    private Result result;
    private ProductAdapter productAdapter;
    private String table = "1";
    private ResultViewModel resultViewModel;
    private listener listener;

    @BindView(R.id.recyclerViewOrder)
    RecyclerView recyclerViewOrder;
    @BindView(R.id.buttonConfirmOrder)
    Button buttonConfirmOrder;
    @BindView(R.id.buttonCancelOrder)
    Button buttonCancelOrder;
    @BindView(R.id.editTextCommentsOrder)
    EditText editTextCommentsOrder;
    @BindView(R.id.checkBoxNeedWait)
    CheckBox checkBoxNeedWait;
    @BindView(R.id.textViewTotalOrder)
    TextView textViewTotalOrder;

    public OrderFragment() {
        // Required empty public constructor
    }

    public static OrderFragment newInstance(Result result) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RESULT, result);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.result = (Result) getArguments().getSerializable(ARG_RESULT);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        ButterKnife.bind(this, view);

        resultViewModel = ViewModelProviders.of(this).get(ResultViewModel.class);
        recyclerViewOrder = ConfigRecyclerView.getRecyclerView(recyclerViewOrder, getContext());
        productAdapter = new ProductAdapter(OrderFragment.this, result.getResults());
        recyclerViewOrder.setAdapter(productAdapter);

        Double total = 0.0;
        for (Product product : result.getResults()) {
            String stringPrice = product.getPrice().substring(1);
            double doble = Double.parseDouble(stringPrice);
            total = total + doble;
        }
        String stringTotal = "$" + total;

        textViewTotalOrder.setText(stringTotal);
        buttonConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String comments = editTextCommentsOrder.getText().toString();
                String id = "id" + Math.random(); //get id firestore

                Order order = Order.getOrderInstance(result, stringTotal, true, checkBoxNeedWait.isChecked(), comments, id, table, getTime());

                resultViewModel.getOrderLog().observe(getViewLifecycleOwner(), new Observer<OrderLog>() {
                    @Override
                    public void onChanged(OrderLog orderLog) {
                        List<Order> orderList;
                        if (orderLog.getOrderList() == null) {
                            orderList = new ArrayList<>();
                        } else orderList = orderLog.getOrderList();
                        orderList.add(order);
                        orderLog.setOrderList(orderList);
                        updateOrderLog(orderLog);
                    }
                });

            }
        });

        buttonCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteActualOrder().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if (aBoolean) {
                            Toast.makeText(getContext(), "Pedido Cancelado", Toast.LENGTH_SHORT).show();
                            listener.orderFragmentListener(false);
                        } else
                            Toast.makeText(getContext(), "Ocurrio un error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return view;
    }


    private String getTime() {
        Calendar calendario = Calendar.getInstance();
        int hour = calendario.get(Calendar.HOUR_OF_DAY);
        int minutes = calendario.get(Calendar.MINUTE);
        String time = (hour + ":" + minutes);
        return time;
    }

    private void updateOrderLog(OrderLog orderLog) {
        resultViewModel.updateOrderLog(orderLog).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Toast.makeText(getContext(), "Pedido Confirmado", Toast.LENGTH_SHORT).show();
                    deleteActualOrder();
                    listener.orderFragmentListener(true);
                } else Toast.makeText(getContext(), "Ocurrio un error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private LiveData<Boolean> deleteActualOrder() {
        MutableLiveData<Boolean> liveBool = new MutableLiveData();
        resultViewModel.deleteActualOrder().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                liveBool.setValue(aBoolean);
            }
        });
        return liveBool;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.listener = (listener) context;
    }

    @Override
    public void selection(Integer adapterPosition, Result result) {

    }

    public interface listener {
        void orderFragmentListener(Boolean confirm);
    }
}