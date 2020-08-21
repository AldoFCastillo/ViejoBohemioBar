package com.example.viejobohemiobar.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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
    private OrderFragment.listener listener;
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


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, stringList);
        listViewOrderStaff.setAdapter(adapter);


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

}