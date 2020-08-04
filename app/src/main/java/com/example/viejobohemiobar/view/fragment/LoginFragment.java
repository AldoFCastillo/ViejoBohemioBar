package com.example.viejobohemiobar.view.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.viejobohemiobar.R;
import com.example.viejobohemiobar.model.pojo.Result;
import com.example.viejobohemiobar.view.activity.MainActivity;
import com.example.viejobohemiobar.viewModel.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    public static final String ARG_NEW = "newMember";


    private UserViewModel userViewModel;
    private loginListener loginListener;
    private Boolean newMember;


    @BindView(R.id.textViewTitleLogin)
    TextView textViewTitleLogin;
    @BindView(R.id.editTextEmailFragmentLogin)
    EditText editTextEmailFragmentLogin;
    @BindView(R.id.editTextPasswordFragmentLogin)
    EditText editTextPasswordFragmentLogin;
    @BindView(R.id.buttonIngresarFragmentLogin)
    Button buttonIngresarFragmentLogin;


    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(Boolean newMember) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_NEW, newMember);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            newMember = getArguments().getBoolean(ARG_NEW);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);


        if (newMember != null) {
            setButtons(false);
        } else setButtons(true);


        return view;
    }


    private void setButtons(Boolean newMember) {

        if (!newMember){
            buttonIngresarFragmentLogin.setText("Registar");
            textViewTitleLogin.setText("Ingresa un mail valido y crea una contraseña para registrarte");
            buttonIngresarFragmentLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    registerUSer();
                }
            });

        }else {
            buttonIngresarFragmentLogin.setText("Login");
            textViewTitleLogin.setText("Si sos parte del staff ingresa tu mail y contraseña");
            buttonIngresarFragmentLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginUser();
                }
            });
        }
        /*buttonIngresarFragmentLogin.setOnClickListener(view -> {
            boolean vacio = (editTextEmailFragmentLogin.getText().toString().equals("") || editTextPasswordFragmentLogin.getText().toString().equals(""));
            if (!vacio) {
                loginUser();
            } else
                Toast.makeText(getContext(), "Ambos campos deben estar completos", Toast.LENGTH_SHORT).show();
        });*/

    }


    private void loginUser() {
        String mail = editTextEmailFragmentLogin.getText().toString();
        String pass = editTextPasswordFragmentLogin.getText().toString();
        userViewModel.loginUser(mail, pass).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Toast.makeText(getContext(), "Exito!", Toast.LENGTH_SHORT).show();
                    loginListener.loginFragmentListener();
                } else {
                    Toast.makeText(getContext(), "fallo ingreso", Toast.LENGTH_SHORT).show();

                }
            }

        });

    }

    private void registerUSer(){
        String mail = editTextEmailFragmentLogin.getText().toString();
        String pass = editTextPasswordFragmentLogin.getText().toString();
        userViewModel.registerUser(mail, pass).observe(getViewLifecycleOwner(), new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser!=null) {
                    Toast.makeText(getContext(), "Exito!", Toast.LENGTH_SHORT).show();
                    loginListener.loginFragmentListener();
                } else {
                    Toast.makeText(getContext(), "Fallo el registro", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.loginListener = (loginListener) context;
    }

    public interface loginListener {
        void loginFragmentListener();
    }


}