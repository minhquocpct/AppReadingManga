package com.example.appreadingmangav2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.appreadingmangav2.Fragments.MangaFragment;
import com.example.appreadingmangav2.Fragments.PageFragment;

public class PageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        getSupportFragmentManager().beginTransaction().replace(R.id.framePageContainer, new PageFragment()).commit();
    }
}