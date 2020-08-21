package com.example.viejobohemiobar.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.viejobohemiobar.R;
import com.example.viejobohemiobar.view.activity.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeFragment extends Fragment {


    private listener listener;

    @BindView(R.id.buttonScan)
    ImageView buttonScan;
    @BindView(R.id.textViewHome)
    TextView textViewHome;

    //TODO BORRAR
    @BindView(R.id.buttonSkip)
    Button getButtonSkip;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (listener) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);


        //TODO BORRAR
        getButtonSkip.setOnClickListener(v -> listener.homeListener(false));

        buttonScan.setOnClickListener(v -> listener.homeListener(true));

        return view;
    }

    public interface listener{
        void homeListener(Boolean boo);
    }
}