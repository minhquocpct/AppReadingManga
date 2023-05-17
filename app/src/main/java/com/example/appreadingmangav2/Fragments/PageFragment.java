package com.example.appreadingmangav2.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appreadingmangav2.Apdater.MangaAdapter;
import com.example.appreadingmangav2.Apdater.PageAdapter;
import com.example.appreadingmangav2.ChapActivity;
import com.example.appreadingmangav2.Constant;
import com.example.appreadingmangav2.Model.Manga;
import com.example.appreadingmangav2.Model.Page;
import com.example.appreadingmangav2.PageActivity;
import com.example.appreadingmangav2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PageFragment extends Fragment {
    private View view;
    private ArrayList<Page> arrayList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private PageAdapter pageAdapter;
    private RecyclerView recyclerView;
    private SharedPreferences pagePref;
    private ImageView imgNext, imgBack;
    public PageFragment(){}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_page,container,false);
        init();
        return view;
    }
    private void init(){
        imgNext=view.findViewById(R.id.nextchap);
        imgBack=view.findViewById(R.id.prechap);
        recyclerView = view.findViewById(R.id.recyclepage);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout = view.findViewById(R.id.swipePage);
        pagePref = getActivity().getApplicationContext().getSharedPreferences("idchap",getContext().MODE_PRIVATE);
        getPage();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPage();
            }
        });
        imgBack.setOnClickListener(v -> {
            checkPreChap();
        });
        imgNext.setOnClickListener(v -> {
            checkNextChap();
        });
    }
    private void getPage(){
        arrayList = new ArrayList<>();
        swipeRefreshLayout.setRefreshing(true);
        StringRequest request = new StringRequest(Request.Method.POST, Constant.CHAP, response -> {
            try {
                JSONObject object = new JSONObject(response);

                if (object.getBoolean("success")){
                    arrayList = new ArrayList<>();
                    JSONArray array = new JSONArray(object.getString("listpage"));
                    for(int i=0;i<array.length();i++){
                        int j=i+1;
                        JSONObject listObject = array.getJSONObject(i);
                        Page page = new Page();
                        page.setLinkImagePage(listObject.getString("link"));
                        page.setNumberPage("Page "+j);
                        arrayList.add(page);
                    }
                    pageAdapter = new PageAdapter(getContext(),arrayList);
                    recyclerView.setAdapter(pageAdapter);
                    SharedPreferences.Editor editor = pagePref.edit();
                    editor.clear();
                    editor.apply();
                    SharedPreferences Manga2Pref = getActivity().getApplicationContext().getSharedPreferences("idmanga",getContext().MODE_PRIVATE);
                    JSONObject chap = object.getJSONObject("chap");
                    Integer i = chap.getInt("ThuTu");
                    SharedPreferences.Editor editor2 = Manga2Pref.edit();
                    editor2.putString("numbernextchap",String.valueOf(i+1));
                    editor2.putString("numberprechap",String.valueOf(i-1));
                    editor2.apply();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            swipeRefreshLayout.setRefreshing(false);
        },error -> {
            error.printStackTrace();
            swipeRefreshLayout.setRefreshing(false);
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String>map = new HashMap<>();
                SharedPreferences MangaPref = getActivity().getApplicationContext().getSharedPreferences("idmanga",getContext().MODE_PRIVATE);
                map.put("idManga",MangaPref.getString("idmanga",""));
                map.put("NumberNextChap",MangaPref.getString("numbernextchap",""));
                map.put("NumberPreChap",MangaPref.getString("numberprechap",""));
                map.put("idChap",pagePref.getString("idchap",""));
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
    private void checkNextChap(){
        StringRequest request = new StringRequest(Request.Method.POST, Constant.CHECK, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")){
                    SharedPreferences MangaPref = getActivity().getApplicationContext().getSharedPreferences("idmanga",getContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = MangaPref.edit();
                    editor2.putString("numberprechap","");
                    editor2.apply();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framePageContainer, new PageFragment()).commit();
                }
                else {
                    Toast.makeText(getContext(),"This is last chapter", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        },error -> {
            error.printStackTrace();
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String>map = new HashMap<>();
                SharedPreferences MangaPref = getActivity().getApplicationContext().getSharedPreferences("idmanga",getContext().MODE_PRIVATE);
                map.put("idManga",MangaPref.getString("idmanga",""));
                map.put("NumberNextChap",MangaPref.getString("numbernextchap",""));
                map.put("NumberPreChap","");
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
    private void checkPreChap(){
        StringRequest request = new StringRequest(Request.Method.POST, Constant.CHECK, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")){
                    SharedPreferences MangaPref = getActivity().getApplicationContext().getSharedPreferences("idmanga",getContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = MangaPref.edit();
                    editor2.putString("numbernextchap","");
                    editor2.apply();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framePageContainer, new PageFragment()).commit();
                }
                else {
                    Toast.makeText(getContext(),"This is first chapter", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        },error -> {
            error.printStackTrace();
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String>map = new HashMap<>();
                SharedPreferences MangaPref = getActivity().getApplicationContext().getSharedPreferences("idmanga",getContext().MODE_PRIVATE);
                map.put("idManga",MangaPref.getString("idmanga",""));
                map.put("NumberPreChap",MangaPref.getString("numberprechap",""));
                map.put("NumberNextChap","");
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
}
