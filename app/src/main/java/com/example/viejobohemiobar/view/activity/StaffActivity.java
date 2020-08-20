package com.example.viejobohemiobar.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.example.viejobohemiobar.R;
import com.example.viejobohemiobar.model.pojo.OrderLog;
import com.example.viejobohemiobar.view.fragment.OrderFragment;
import com.example.viejobohemiobar.view.fragment.RecyclerStaffFragment;
import com.example.viejobohemiobar.view.fragment.StaffOrdersFragment;
import com.example.viejobohemiobar.viewModel.ResultViewModel;
import com.google.android.material.snackbar.Snackbar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StaffActivity extends AppCompatActivity implements StaffOrdersFragment.listener, OrderFragment.listener, RecyclerStaffFragment.listener {


    private FragmentManager fragmentManager;



    @BindView(R.id.toolbarStaff)
    Toolbar toolbar;
    @BindView(R.id.constraintStaff)
    public ConstraintLayout constraintStaff;
    @BindView(R.id.constraintStaffActivity)
    ConstraintLayout constraintStaffActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);
        ButterKnife.bind(this);

        setToolBar();

        Snackbar.make(constraintStaff, "Registro de pedidos", Snackbar.LENGTH_LONG).show();


        setFragment(new StaffOrdersFragment());

    }

    private void setToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }




    public void setFragment(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerFragmentStaffActivity, fragment);
        fragmentTransaction.commit();
    }



    @Override
    public void StaffOrdersFragmentListener(Integer adapterPosition, OrderLog orderLog, String path) {
        OrderFragment orderFragment = OrderFragment.newInstance(orderLog.getOrderList().get(adapterPosition), path, adapterPosition);
        setFragment(orderFragment);

    }

    @Override
    public void onBackPressed() {
        Fragment fragment = fragmentManager.findFragmentById(R.id.containerFragmentStaffActivity);
        if(fragment instanceof OrderFragment){
            setFragment(new StaffOrdersFragment());
        }else super.onBackPressed();
    }

    @Override
    public void orderFragmentListener() {
        setFragment(new StaffOrdersFragment());
    }
}