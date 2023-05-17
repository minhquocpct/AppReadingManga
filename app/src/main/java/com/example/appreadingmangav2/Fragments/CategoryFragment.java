package com.example.appreadingmangav2.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appreadingmangav2.Apdater.CategoryAdapter;
import com.example.appreadingmangav2.Apdater.MangaAdapter;
import com.example.appreadingmangav2.Constant;
import com.example.appreadingmangav2.Model.Category;
import com.example.appreadingmangav2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CategoryFragment extends Fragment implements CategoryAdapter.OnCategoryListner {
    private View view;
    private RecyclerView recyclerView;
    private ArrayList<Category> arrayList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CategoryAdapter categoryAdapter;
    private SharedPreferences sharedPreferences;
    public CategoryFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_category,container,false);
        init();
        return view;
    }
    private void init(){
        swipeRefreshLayout = view.findViewById(R.id.swipeCategory);
        recyclerView = view.findViewById(R.id.recycleviewcategory);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getCategory();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCategory();
            }
        });

    }
    private void getCategory(){
        arrayList = new ArrayList<>();
        swipeRefreshLayout.setRefreshing(true);
        StringRequest request = new StringRequest(Request.Method.GET, Constant.CATEGORY,response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")){
                    JSONArray array = new JSONArray(object.getString("category"));
                    for (int i= 0;i<array.length();i++){
                        JSONObject categoryObject = array.getJSONObject(i);
                        Category category = new Category();
                        category.setIdCategory(categoryObject.getInt("id"));
                        category.setNameCategory(categoryObject.getString("Ten"));
                        arrayList.add(category);
                    }
                    categoryAdapter = new CategoryAdapter(getContext(), arrayList,this);
                    recyclerView.setAdapter(categoryAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            swipeRefreshLayout.setRefreshing(false);
        },error -> {
            error.printStackTrace();
            swipeRefreshLayout.setRefreshing(false);

        }){

        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }


    @Override
    public void onCategoryClick(int position) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameHomeContainer, new ResultCategoryFragment(), CategoryFragment.class.getSimpleName()).commit();
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("idcategory",getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("idcategory", String.valueOf(arrayList.get(position).getIdCategory(position)));
        editor.apply();
    }
}
