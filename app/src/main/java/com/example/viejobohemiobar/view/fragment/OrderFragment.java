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
import com.example.viejobohemiobar.model.pojo.OrderLog;
import com.example.viejobohemiobar.model.pojo.Product;
import com.example.viejobohemiobar.model.pojo.Result;
import com.example.viejobohemiobar.utils.ConfigRecyclerView;
import com.example.viejobohemiobar.utils.MenuUtils;
import com.example.viejobohemiobar.view.adapter.ProductAdapter;
import com.example.viejobohemiobar.viewModel.ResultViewModel;
import com.example.viejobohemiobar.viewModel.UserViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class OrderFragment extends Fragment implements ProductAdapter.adapterListener {

    public static final String ARG_SERIAL = "serial";
    public static final String ARG_PATH = "path";
    public static final String ARG_POSITION = "position";


    private Result result;
    private Order order;
    private ProductAdapter productAdapter;
    private String table = "1";
    private ResultViewModel resultViewModel;
    private UserViewModel userViewModel;
    private listener listener;
    private String path;
    private String button;
    private String next;
    private int position;
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

    public static OrderFragment newInstance(Serializable result, String path, int position) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_SERIAL, result);
        args.putString(ARG_PATH, path);
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().getSerializable(ARG_SERIAL) instanceof Result) {
                this.result = (Result) getArguments().getSerializable(ARG_SERIAL);
                this.order = null;
            } else {
                this.order = (Order) getArguments().getSerializable(ARG_SERIAL);
                this.path = getArguments().getString(ARG_PATH);
                this.position = getArguments().getInt(ARG_POSITION);
            }
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

        if (order != null) {
            setMadeOrder();
        } else setToConfirmOrder();


        return view;
    }

    private void setMadeOrder() {
        productAdapter = new ProductAdapter(OrderFragment.this, order.getResult().getResults());
        recyclerViewOrder.setAdapter(productAdapter);

        List<Product> resultList = order.getResult().getResults();


        setBottomButton();

        buttonToProcessOrder.setVisibility(View.VISIBLE);
        buttonToProcessOrder.setText(button);
        buttonToProcessOrder.setOnClickListener(v -> migrateOrder());

        textViewTotalOrder.setText(order.getTotal());
        //set RadioButtons
        editTextCommentsOrder.setText(order.getComments());
        checkBoxNeedWait.setChecked(order.getCallWait());
        checkBoxNeedWait.setEnabled(false);
        buttonCancelOrder.setVisibility(View.INVISIBLE);
        buttonConfirmOrder.setVisibility(View.INVISIBLE);
        editTextPass.setVisibility(View.GONE);
    }

    private void setBottomButton() {
        switch (path) {
            case "p":
                button = "Tomar Pedido";
                next = "i";
                break;
            case "i":
                button = "Pedido Entregado";
                next = "c";
                break;
            case "c":
                buttonToProcessOrder.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void migrateOrder() {

        resultViewModel.getOrderLog(path);
        getOrderLogObserver(order, next, true);
    }

    private void setToConfirmOrder() {
        path = "p";
        productAdapter = new ProductAdapter(OrderFragment.this, result.getResults());
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
                addOrderToLog(actualOrder, path, false);
            } else Toast.makeText(getContext(), "Clave incorrecta!", Toast.LENGTH_SHORT).show();
        });
    }


    private void addOrderToLog(Order actualOrder, String path, Boolean staff) {
        resultViewModel.getOrderLog(path);
        getOrderLogObserver(actualOrder, path, staff);
    }

    private void getOrderLogObserver(Order actualOrder, String nextPath, Boolean staff) {
        resultViewModel.orderLogData.observe(getViewLifecycleOwner(), orderLog -> {
            if (path.equals(nextPath)) {
                List<Order> orderList;
                if (orderLog == null) {
                    orderLog = new OrderLog();
                    orderList = new ArrayList<>();
                } else orderList = orderLog.getOrderList();
                orderList.add(actualOrder);
                orderLog.setOrderList(orderList);
                updateOrderLog(orderLog, nextPath, staff);
                if (staff) listener.orderFragmentListener();
            } else {
                List<Order> orderList = orderLog.getOrderList();
                orderList.remove(position);
                orderLog.setOrderList(orderList);
                updateOrderLog(orderLog, path, staff);
                path = nextPath;
                addOrderToLog(order, nextPath, true);
            }
        });
    }

    private void updateOrderLog(OrderLog orderLog, String path, Boolean staff) {
        resultViewModel.updateOrderLog(orderLog, path);
        updateOrderLogObserver(staff);

    }

    private void updateOrderLogObserver(Boolean staff) {
        resultViewModel.orderLogBool.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean && !staff) {
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