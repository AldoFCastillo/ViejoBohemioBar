package com.example.viejobohemiobar.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.viejobohemiobar.R;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_TIME = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras()!=null){
            setContentView(R.layout.order_confirm);
        } else setContentView(R.layout.activity_splash);

        setHandler();

    }

    public void setHandler(){
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_TIME);
    }
}