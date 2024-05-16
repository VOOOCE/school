package com.arabic.schoolg.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.arabic.schoolg.databinding.SplashBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.TimerTask;


public class Splash extends AppCompatActivity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashBinding binding = SplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();

        Log.e("MY AUTH", String.valueOf(auth.getCurrentUser()));
//        if (AppCompatDelegate.getApplicationLocales().toLanguageTags() == "ar") {} else {
//
//            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("ar"));
//        }
        new Handler().postDelayed(new TimerTask() {
            @Override
            public void run() {
                if (String.valueOf(auth.getCurrentUser()).equals("null")){
                    startActivity(new Intent(getBaseContext(), SignIn.class));
                }else {
                    startActivity(new Intent(getBaseContext(), Home.class));
                }
                finish();
            }
        }, 500);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
}