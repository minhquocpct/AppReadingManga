package com.example.appreadingmangav2.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appreadingmangav2.Apdater.MangaAdapter;
import com.example.appreadingmangav2.ChapActivity;
import com.example.appreadingmangav2.Constant;
import com.example.appreadingmangav2.HomeActivity;
import com.example.appreadingmangav2.Model.Manga;
import com.example.appreadingmangav2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResultSearchFragment extends Fragment {
    private View view;
    private GridView gridView;
    private ArrayList<Manga> arrayList;
    private MangaAdapter mangaAdapter;
    private SharedPreferences sharedPreferences;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView ImgBack;
    public ResultSearchFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_result_search,container, false);
        sharedPreferences = getContext().getSharedPreferences("keyword", Context.MODE_PRIVATE);
        ImgBack= view.findViewById(R.id.searchback);
        swipeRefreshLayout= view.findViewById(R.id.swipeHome);
        intit();
        setClick();
        ImgBack.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameHomeContainer, new ListMangaFragment(), ListMangaFragment.class.getSimpleName()).commit();
        });
        return view;
    }
    private void intit(){
        gridView = view.findViewById(R.id.gridviewsearch);
        getManga();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getManga();
            }
        });
    }
    private void setClick(){
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("idmanga",getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("idmanga", String.valueOf(arrayList.get(position).getIdManga()));
                editor.apply();
                startActivity(new Intent(((HomeActivity)getContext()), ChapActivity.class));
            }
        });
    }
    private void getManga(){
        StringRequest request = new StringRequest(Request.Method.POST, Constant.SEARCH, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")){
                    arrayList = new ArrayList<>();
                    JSONArray array = new JSONArray(object.getString("list"));
                    for(int i=0;i<array.length();i++){
                        JSONObject listObject = array.getJSONObject(i);
                        Manga manga = new Manga();
                        manga.setIdManga(listObject.getInt("id"));
                        manga.setNameManga((listObject.getString("TieuDe")));
                        manga.setLinkImage(listObject.getString("Hinh"));
                        arrayList.add(manga);
                    }
                    mangaAdapter = new MangaAdapter(getContext(),0,arrayList);
                    gridView.setAdapter(mangaAdapter);

                }
                if (object.getBoolean("nonexist")){
                    Toast.makeText(getContext(),"No Result", Toast.LENGTH_SHORT).show();
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
                map.put("idcategory","");
                map.put("keyword",sharedPreferences.getString("keyword",""));
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
}
