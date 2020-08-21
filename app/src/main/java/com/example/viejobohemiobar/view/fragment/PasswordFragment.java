package com.example.viejobohemiobar.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.viejobohemiobar.R;
import com.example.viejobohemiobar.viewModel.UserViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PasswordFragment extends Fragment {

    private UserViewModel userViewModel;

    @BindView(R.id.editTextFragmentPass)
    EditText editTextFragmentPass;
    @BindView(R.id.buttonAcceptPass)
    Button buttonAcceptPass;

    public PasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_password, container, false);
        ButterKnife.bind(this, view);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        buttonAcceptPass.setOnClickListener(v -> {
            String pass = editTextFragmentPass.getText().toString();
            userViewModel.changePassword(pass);
            changePasswordObserver();
        });

        return view;
    }

    private void changePasswordObserver() {
        userViewModel.liveChangePass.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                Toast.makeText(getContext(), "Exito!", Toast.LENGTH_SHORT).show();
            } else Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        });
    }
}