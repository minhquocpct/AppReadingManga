package com.example.appreadingmangav2.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.TextView;

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
import com.example.appreadingmangav2.AuthActivity;
import com.example.appreadingmangav2.ChapActivity;
import com.example.appreadingmangav2.Constant;
import com.example.appreadingmangav2.HomeActivity;
import com.example.appreadingmangav2.MainActivity;
import com.example.appreadingmangav2.Model.Manga;
import com.example.appreadingmangav2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListMangaFragment extends Fragment {
    private View view;
    private GridView gridView;
    private ArrayList<Manga> arrayList;
    private MangaAdapter mangaAdapter;
    private SharedPreferences sharedPreferences;
    private EditText edtSearch;
    private SwipeRefreshLayout swipeRefreshLayout;
    public ListMangaFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_list,container, false);
        intit();
        setClick();
        return view;
    }
    private void intit(){
        gridView = view.findViewById(R.id.gridviewlist);
        swipeRefreshLayout= view.findViewById(R.id.swipeHome);
        edtSearch = view.findViewById(R.id.edtsearch);
        getManga();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getManga();
            }
        });
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("keyword",getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("keyword", edtSearch.getText().toString());
                editor.apply();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameHomeContainer, new ResultSearchFragment(), ListMangaFragment.class.getSimpleName()).commit();
                return true;
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
        StringRequest request = new StringRequest(Request.Method.GET,Constant.LIST,response -> {
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
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
}
