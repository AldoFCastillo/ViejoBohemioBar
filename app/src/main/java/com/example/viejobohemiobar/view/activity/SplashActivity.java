package com.example.viejobohemiobar.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.viejobohemiobar.R;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_INTRO_TIME = 2000;
    private final int SPLASH_CONFIRM_TIME = 5000;
    public static final String KEY_BOOL = "bool";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean bool = false;
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            bool = bundle.getBoolean(KEY_BOOL);
        }

        if (bool) {
            setContentView(R.layout.order_confirm);
            setHandler(SPLASH_CONFIRM_TIME);
        } else {
            setContentView(R.layout.activity_splash);
            setHandler(SPLASH_INTRO_TIME);
        }


    }

    public void setHandler(int time) {
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, time);
    }
}