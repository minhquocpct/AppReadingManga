package com.example.appreadingmangav2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.appreadingmangav2.Fragments.LogInFragment;
import com.example.appreadingmangav2.Fragments.SignUpFragment;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameAuthContainer, new LogInFragment()).commit();
    }
}