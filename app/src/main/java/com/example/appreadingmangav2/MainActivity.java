package com.example.appreadingmangav2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    private static int SplashTimeOut = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences userPref = getApplicationContext().getSharedPreferences("user",Context.MODE_PRIVATE);
                boolean isLoggedIn = userPref.getBoolean("isLoggedIn",false);
//                if (isLoggedIn){
//                    Intent HomeIntent = new Intent(MainActivity.this, HomeActivity.class);
//                    startActivity(HomeIntent);
//                    finish();
//                }
//                else{
                Intent HomeIntent = new Intent(MainActivity.this, AuthActivity.class);
                startActivity(HomeIntent);
                finish();
//                }

            }
        },SplashTimeOut);
    }
}