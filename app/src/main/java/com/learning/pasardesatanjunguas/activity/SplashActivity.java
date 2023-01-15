package com.learning.pasardesatanjunguas.activity;

/**
 * Dibuat oleh Maulana As'an, 10/01/23
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.learning.pasardesatanjunguas.R;

public class SplashActivity extends AppCompatActivity {

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}