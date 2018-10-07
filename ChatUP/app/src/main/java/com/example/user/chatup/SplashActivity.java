package com.example.user.chatup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        final String restoredName = prefs.getString("fname", null);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(restoredName != null) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    SplashActivity.this.finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, RegistrationActivity.class));
                    SplashActivity.this.finish();
                }
            }
        }, 2000);
    }
}
