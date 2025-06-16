package com.example.app_techspehre_news;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_splash);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashScreen.this, SignInScreen.class);
            startActivity(intent);
            finish();
        }, 3000);
    }
}
