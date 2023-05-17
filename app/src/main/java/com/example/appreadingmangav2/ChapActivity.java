package com.example.appreadingmangav2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.appreadingmangav2.Fragments.MangaFragment;

public class ChapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chap);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameChapContainer, new MangaFragment()).commit();
    }
}