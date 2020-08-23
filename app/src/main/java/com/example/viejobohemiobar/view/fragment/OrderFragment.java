package com.example.viejobohemiobar.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.viejobohemiobar.R;
import com.example.viejobohemiobar.model.pojo.Order;
import com.example.viejobohemiobar.model.pojo.Product;
import com.example.viejobohemiobar.model.pojo.Result;
import com.example.viejobohemiobar.utils.ConfigRecyclerView;
import com.example.viejobohemiobar.utils.MenuUtils;
import com.example.viejobohemiobar.view.adapter.ProductAdapter;
import com.example.viejobohemiobar.viewModel.ResultViewModel;
import com.example.viejobohemiobar.viewModel.UserViewModel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class OrderFragment extends Fragment implements ProductAdapter.adapterListener {

    public static final String ARG_SERIAL = "serial";
    private static final String ARG_TABLE = "table";


    private Result result;
    private String table;
    private ResultViewModel resultViewModel;
    private UserViewModel userViewModel;
    private listener listener;
    private String path;
    private String stringTotal;

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
    @BindView(R.id.buttonToProcessOrder)
    Button buttonToProcessOrder;
    @BindView(R.id.editTextPassFragmentOrder)
    EditText editTextPass;
    @BindView(R.id.radioButtonEfectivo)
    RadioButton radioButtonEfectivo;

    public OrderFragment() {
        // Required empty public constructor
    }

    public static OrderFragment newInstance(Serializable result, String table) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_SERIAL, result);
        args.putString(ARG_TABLE, table);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.result = (Result) getArguments().getSerializable(ARG_SERIAL);
            this.table = getArguments().getString(ARG_TABLE);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        ButterKnife.bind(this, view);

        resultViewModel = new ViewModelProvider(this).get(ResultViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        recyclerViewOrder = ConfigRecyclerView.getRecyclerView(recyclerViewOrder, getContext());

        setViews();


        return view;
    }


    private void setViews() {
        path = "p";
        ProductAdapter productAdapter = new ProductAdapter(OrderFragment.this, result.getResults());
        recyclerViewOrder.setAdapter(productAdapter);
        editTextPass.setVisibility(View.VISIBLE);
        buttonToProcessOrder.setVisibility(View.INVISIBLE);
        buttonCancelOrder.setVisibility(View.VISIBLE);
        buttonConfirmOrder.setVisibility(View.VISIBLE);
        stringTotal = MenuUtils.getTotal(result);
        textViewTotalOrder.setText(stringTotal);

        buttonConfirmOrder.setOnClickListener(v -> {
            String pass = editTextPass.getText().toString();
            confirmPass(pass);
        });

        buttonCancelOrder.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Pedido Cancelado", Toast.LENGTH_SHORT).show();
            listener.orderFragmentListener();
        });
    }


    private void confirmPass(String pass) {
        userViewModel.getPassword();
        getPassObserver(pass);
    }

    private void getPassObserver(String pass) {
        userViewModel.liveStringPass.observe(getViewLifecycleOwner(), s -> {
            if (s.equals(pass)) {
                String comments = editTextCommentsOrder.getText().toString();
                Date date = new Date();
                String id = table + date;
                Boolean cash = radioButtonEfectivo.isChecked();
                Order actualOrder = Order.getOrderInstance(result, stringTotal, cash, checkBoxNeedWait.isChecked(), comments, id, table, MenuUtils.getTime());
                updateOrderLog(actualOrder, path);
            } else Toast.makeText(getContext(), "Clave incorrecta!", Toast.LENGTH_SHORT).show();
        });
    }


    private void updateOrderLog(Order order, String path) {
        resultViewModel.updateOrderLog(order, path, order.getId());
        updateOrderLogObserver();

    }

    private void updateOrderLogObserver() {
        resultViewModel.orderLogBool.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                Toast.makeText(getContext(), "Pedido Confirmado", Toast.LENGTH_SHORT).show();
                listener.orderFragmentListener();

            } else Toast.makeText(getContext(), "Ocurrio un error", Toast.LENGTH_SHORT).show();
        });
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
        void orderFragmentListener();
    }
}