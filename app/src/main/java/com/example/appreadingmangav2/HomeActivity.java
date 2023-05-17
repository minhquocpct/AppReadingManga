package com.example.appreadingmangav2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.appreadingmangav2.Fragments.AccountFragment;
import com.example.appreadingmangav2.Fragments.CategoryFragment;
import com.example.appreadingmangav2.Fragments.ListMangaFragment;
import com.example.appreadingmangav2.Model.Category;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView navigationView;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        fragmentManager= getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frameHomeContainer, new ListMangaFragment(), ListMangaFragment.class.getSimpleName()).commit();
        init();
    }
    private void init(){
        navigationView=findViewById(R.id.bottom_nav);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_list:{
                        Fragment list = fragmentManager.findFragmentByTag(ListMangaFragment.class.getSimpleName());
                        Fragment category = fragmentManager.findFragmentByTag(CategoryFragment.class.getSimpleName());
                        Fragment account = fragmentManager.findFragmentByTag(AccountFragment.class.getSimpleName());
                        if (list!=null) {
                            if(category==null){
                                fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(ListMangaFragment.class.getSimpleName())).commit();
                                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(AccountFragment.class.getSimpleName())).commit();
                            }
                            else if (account==null){
                                fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(ListMangaFragment.class.getSimpleName())).commit();
                                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(CategoryFragment.class.getSimpleName())).commit();
                            }
                            else if(category==null && account ==null){
                                fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(ListMangaFragment.class.getSimpleName())).commit();
                            }
                            else {
                                fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(ListMangaFragment.class.getSimpleName())).commit();
                                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(CategoryFragment.class.getSimpleName())).commit();
                                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(AccountFragment.class.getSimpleName())).commit();
                            }
                        }
                        else {
                            fragmentManager.beginTransaction().add(R.id.frameHomeContainer, new ListMangaFragment(), ListMangaFragment.class.getSimpleName()).commit();
                        }
                        break;
                    }
                    case R.id.menu_category: {
                        Fragment list = fragmentManager.findFragmentByTag(ListMangaFragment.class.getSimpleName());
                        Fragment category = fragmentManager.findFragmentByTag(CategoryFragment.class.getSimpleName());
                        Fragment account = fragmentManager.findFragmentByTag(AccountFragment.class.getSimpleName());
                        if (category!=null) {
                            if(list==null){
                                fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(CategoryFragment.class.getSimpleName())).commit();
                                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(AccountFragment.class.getSimpleName())).commit();
                            }
                            else if (account==null){
                                fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(CategoryFragment.class.getSimpleName())).commit();
                                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(ListMangaFragment.class.getSimpleName())).commit();
                            }
                            else if(list==null && account ==null){
                                fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(CategoryFragment.class.getSimpleName())).commit();
                            }
                            else {
                                fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(CategoryFragment.class.getSimpleName())).commit();
                                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(ListMangaFragment.class.getSimpleName())).commit();
                                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(AccountFragment.class.getSimpleName())).commit();
                            }
                        }
                        else {
                            fragmentManager.beginTransaction().add(R.id.frameHomeContainer, new CategoryFragment(), CategoryFragment.class.getSimpleName()).commit();
                        }
                        break;
                    }

                    case R.id.menu_account:{
                        Fragment list = fragmentManager.findFragmentByTag(ListMangaFragment.class.getSimpleName());
                        Fragment category = fragmentManager.findFragmentByTag(CategoryFragment.class.getSimpleName());
                        Fragment account = fragmentManager.findFragmentByTag(AccountFragment.class.getSimpleName());
                        if (account!=null) {
                            if(list==null){
                                fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(AccountFragment.class.getSimpleName())).commit();
                                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(ListMangaFragment.class.getSimpleName())).commit();
                            }
                            else if (category==null){
                                fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(AccountFragment.class.getSimpleName())).commit();
                                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(ListMangaFragment.class.getSimpleName())).commit();
                            }
                            else if(category==null && list ==null){
                                fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(AccountFragment.class.getSimpleName())).commit();
                            }
                            else {
                                fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(AccountFragment.class.getSimpleName())).commit();
                                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(CategoryFragment.class.getSimpleName())).commit();
                                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(ListMangaFragment.class.getSimpleName())).commit();
                            }
                        }
                        else {
                            fragmentManager.beginTransaction().add(R.id.frameHomeContainer, new AccountFragment(), AccountFragment.class.getSimpleName()).commit();
                        }
                        break;
                    }

                }
                return true;
            }
        });

    }
}