package com.example.viejobohemiobar.view.fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.example.viejobohemiobar.R;
import com.example.viejobohemiobar.viewModel.UserViewModel;


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

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);


        if (newMember != null) {
            setButtons(false);
        } else setButtons(true);


        return view;
    }


    private void setButtons(Boolean newMember) {

        if (!newMember){
            buttonIngresarFragmentLogin.setText(R.string.registrar);
            textViewTitleLogin.setText(R.string.enter_mail);
            buttonIngresarFragmentLogin.setOnClickListener(v -> registerUSer());

        }else {
            buttonIngresarFragmentLogin.setText(R.string.login);
            textViewTitleLogin.setText(R.string.enter_mail_pass);
            buttonIngresarFragmentLogin.setOnClickListener(v -> loginUser());
        }

    }


    private void loginUser() {
        String mail = editTextEmailFragmentLogin.getText().toString();
        String pass = editTextPasswordFragmentLogin.getText().toString();
        userViewModel.loginUser(mail, pass);
        loginUserObserver();
    }

    private void loginUserObserver(){
        userViewModel.liveLoginBool.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                Toast.makeText(getContext(), "Exito!", Toast.LENGTH_SHORT).show();
                loginListener.loginFragmentListener();
            } else Toast.makeText(getContext(), "fallo ingreso", Toast.LENGTH_SHORT).show();
        });
    }

    private void registerUSer(){
        String mail = editTextEmailFragmentLogin.getText().toString();
        String pass = editTextPasswordFragmentLogin.getText().toString();
        userViewModel.registerUser(mail, pass);
        registerUserObserver();
    }

    private void registerUserObserver(){
        userViewModel.firebaseUserMutable.observe(getViewLifecycleOwner(), firebaseUser -> {
            if (firebaseUser!=null) {
                Toast.makeText(getContext(), "Exito!", Toast.LENGTH_SHORT).show();
                loginListener.loginFragmentListener();
            } else Toast.makeText(getContext(), "Fallo el registro", Toast.LENGTH_SHORT).show();
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