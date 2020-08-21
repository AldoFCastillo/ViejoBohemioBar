package com.example.viejobohemiobar.view.fragment;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.viejobohemiobar.R;
import com.example.viejobohemiobar.model.pojo.Order;
import com.example.viejobohemiobar.model.pojo.Product;
import com.example.viejobohemiobar.model.pojo.Result;
import com.example.viejobohemiobar.view.adapter.ProductAdapter;
import com.example.viejobohemiobar.viewModel.ResultViewModel;
import com.example.viejobohemiobar.viewModel.UserViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StaffOrderFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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

    private String mParam1;
    private String mParam2;


    @BindView(R.id.textViewTitleOrderStaff)
    TextView textViewTitleOrderStaff;
    @BindView(R.id.listViewOrderStaff)
    MyListView listViewOrderStaff;
    @BindView(R.id.textViewTotalOrderStaff)
    TextView textViewTotalOrderStaff;
    @BindView(R.id.radioGroupOrderStaff)
    RadioGroup radioGroupOrderStaff;
    @BindView(R.id.radioButtonEfectivoStaff)
    RadioButton radioButtonEfectivoStaff;
    @BindView(R.id.radioButtonOtrosStaff)
    RadioButton radioButtonOtrosStaff;
    @BindView(R.id.textViewCommentsOrderStaff)
    TextView textViewCommentsOrderStaff;
    @BindView(R.id.checkBoxNeedWaitStaff)
    CheckBox checkBoxNeedWaitStaff;
    @BindView(R.id.buttonToProcessOrderStaff)
    Button buttonToProcessOrderStaff;


    public StaffOrderFragment() {
        // Required empty public constructor
    }

    public static StaffOrderFragment newInstance(Serializable result, String path, int position) {
        StaffOrderFragment fragment = new StaffOrderFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_staff_order, container, false);
        ButterKnife.bind(this, view);

        resultViewModel = new ViewModelProvider(this).get(ResultViewModel.class);

        setBottomButton();
        setViews();

        return view;
    }


    private void setViews() {
        List<Product> resultList = order.getResult().getResults();
        List<String> titleList = new ArrayList<>();
        List<String> stringList = new ArrayList<>();
        String countA;

        for (Product product : resultList) {
            titleList.add(product.getTitle());
        }
        for (String title : titleList) {
            countA = title + " X " + Collections.frequency(titleList, title);
            if (!stringList.contains(countA)) {
                stringList.add(countA);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_list_item_1, stringList);
        listViewOrderStaff.setAdapter(adapter);

        String titleOrder = "Pedido mesa " + order.getTable() + " (" + order.getTime() + "hs)";
        textViewTitleOrderStaff.setText(titleOrder);

        textViewTotalOrderStaff.setText(order.getTotal());

        if (order.getCash()) {
            radioButtonEfectivoStaff.setChecked(true);
        } else radioButtonOtrosStaff.setChecked(true);

        radioButtonEfectivoStaff.setEnabled(false);
        radioButtonOtrosStaff.setEnabled(false);


        String comms;
        if (!order.getComments().isEmpty()) {
            comms = "'" + order.getComments() + "'";
        } else comms = "Sin comentarios";
        textViewCommentsOrderStaff.setText(comms);
        checkBoxNeedWaitStaff.setChecked(order.getCallWait());
        checkBoxNeedWaitStaff.setEnabled(false);

        buttonToProcessOrderStaff.setText(button);
        buttonToProcessOrderStaff.setOnClickListener(v -> deleteOrder(path));
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
                buttonToProcessOrderStaff.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void deleteOrder(String path){
        resultViewModel.deleteOrder(path, order.getId());
        deleteOrderObserver();
    }

    private void deleteOrderObserver(){
        resultViewModel.deleteOrderBool.observe(getViewLifecycleOwner(), aBoolean -> {
            updateOrderLog(order, next);
        });
    }

    private void updateOrderLog(Order order, String nextPath) {
        resultViewModel.updateOrderLog(order, nextPath, order.getId());
        updateOrderLogObserver();

    }

    private void updateOrderLogObserver() {
        resultViewModel.orderLogBool.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                Toast.makeText(getContext(), "Pedido Migrado", Toast.LENGTH_SHORT).show();
                listener.orderFragmentListener();

            } else Toast.makeText(getContext(), "Ocurrio un error", Toast.LENGTH_SHORT).show();
        });
    }

    public static class MyListView extends ListView {

        public MyListView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public MyListView(Context context) {
            super(context);
        }

        public MyListView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                    MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);
        }


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (listener) context;
    }

    public interface listener {
        void orderFragmentListener();
    }

}