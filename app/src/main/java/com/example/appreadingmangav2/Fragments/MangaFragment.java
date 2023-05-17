package com.example.appreadingmangav2.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.example.appreadingmangav2.Apdater.ChapAdapter;
import com.example.appreadingmangav2.Apdater.CommentAdapter;
import com.example.appreadingmangav2.ChapActivity;
import com.example.appreadingmangav2.Constant;
import com.example.appreadingmangav2.HomeActivity;
import com.example.appreadingmangav2.Model.Chap;
import com.example.appreadingmangav2.Model.Comment;
import com.example.appreadingmangav2.PageActivity;
import com.example.appreadingmangav2.R;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MangaFragment extends Fragment implements ChapAdapter.OnChapListner {
    private View view;
    private ArrayList<Chap> arrayList;
    private ArrayList<Comment> arrayListComment;
    private TextView txtName, txtDecription,txtNumberChap,txtNumberComment;
    private ImageView imgImage;
    private EditText edtComment;
    private TextInputLayout textInputLayout;
    private SharedPreferences sharedPreferences;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerViewListChap, recyclerViewComment;
    private ProgressDialog dialog;
    public MangaFragment(){}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_manga,container,false);
        init();
        return view;

    }
    private void init(){
        recyclerViewListChap = view.findViewById(R.id.recycleviewlistchap);
        recyclerViewListChap.setHasFixedSize(true);
        recyclerViewListChap.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewComment = view.findViewById(R.id.recycleviewcomment);
        recyclerViewComment.setHasFixedSize(true);
        recyclerViewComment.setLayoutManager(new LinearLayoutManager(getContext()));
        txtName = view.findViewById(R.id.txtnamemanga);
        txtNumberChap = view.findViewById(R.id.numberchap);
        txtNumberComment = view.findViewById(R.id.numbercmt);
        txtDecription = view.findViewById(R.id.txtdecription);
        txtDecription .setMovementMethod(new ScrollingMovementMethod());
        imgImage = view.findViewById(R.id.imgmanga);
        edtComment = view.findViewById(R.id.edtcomment);
        textInputLayout = view.findViewById(R.id.txtLayoutComment);
        swipeRefreshLayout =view.findViewById(R.id.swipeChap);
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("idmanga",getContext().MODE_PRIVATE);
        dialog = new ProgressDialog(getContext());
        dialog.setCancelable(false);
        getManga();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getManga();
            }
        });
        edtComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (validate()){
                    Comment();
                }
                getManga();
                return true;
            }
        });
        edtComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!edtComment.getText().toString().isEmpty()){
                    textInputLayout.setErrorEnabled(false);
                }
            }
        });
    }
    private void getManga(){
        arrayList = new ArrayList<>();
        arrayListComment = new ArrayList<>();
        swipeRefreshLayout.setRefreshing(true);
        StringRequest request = new StringRequest(Request.Method.POST, Constant.MANGA, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")){
                    JSONArray array = new JSONArray(object.getString("listchap"));
                    for(int i=0;i<array.length();i++){
                        JSONObject listObject = array.getJSONObject(i);
                        Chap chap = new Chap();
                        chap.setIdChap(listObject.getInt("id"));
                        chap.setNameChap(listObject.getString("TieuDe"));
                        arrayList.add(chap);
                    }
                    ChapAdapter chapAdapter = new ChapAdapter(getContext(), arrayList,this);
                    recyclerViewListChap.setAdapter(chapAdapter);
                    JSONArray comments = new JSONArray(object.getString("comment"));
                    for (int i = 0; i < comments.length(); i++) {
                        JSONObject comment = comments.getJSONObject(i);
                        JSONObject user = comment.getJSONObject("user");
                        Comment mComment = new Comment();
                        mComment.setNameUser(user.getString("name"));
                        mComment.setComment(comment.getString("NoiDung"));
                        arrayListComment.add(mComment);
                    }
                    CommentAdapter commentAdapter = new CommentAdapter(getContext(), arrayListComment);
                    recyclerViewComment.setAdapter(commentAdapter);
                    JSONObject manga = new JSONObject(object.getString("manga"));
                    if (array.length()>1){
                        txtNumberChap.setText(array.length()+" chapters");
                    }
                    else {
                        txtNumberChap.setText(array.length()+" chapter");
                    }
                    if (comments.length()>1){
                        txtNumberComment.setText(comments.length()+" comments");
                    }
                    else {
                        txtNumberComment.setText(comments.length()+" comment");
                    }
                    txtName.setText(manga.getString("TieuDe"));
                    txtDecription.setText(manga.getString("TomTat"));
                    Picasso.get().load(manga.getString("Hinh")).into(imgImage);
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
                map.put("idmanga",sharedPreferences.getString("idmanga",""));
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
    public void Comment(){
        dialog.setMessage("Comment");
        dialog.show();
        StringRequest request2 = new StringRequest(Request.Method.POST, Constant.COMMENT, response -> {
            //get respone if connection success
            try {
                JSONObject object = new JSONObject(response);
                if(object.getBoolean("success")){
                    Toast.makeText(getContext(),"Comment Success", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                dialog.dismiss();
            }
            dialog.dismiss();

        },error -> {
            error.printStackTrace();
            dialog.dismiss();
        }){
            //add parameter

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                SharedPreferences mangaPref = getActivity().getApplicationContext().getSharedPreferences("idmanga",getContext().MODE_PRIVATE);
                SharedPreferences userPref = getActivity().getApplicationContext().getSharedPreferences("user",getContext().MODE_PRIVATE);
                map.put("idUser", userPref.getString("id",""));
                map.put("idManga",mangaPref.getString("idmanga",""));
                map.put("comment",edtComment.getText().toString());
                return map;

            }
        };
        RequestQueue queue2 = Volley.newRequestQueue(getContext());
        queue2.add(request2);
    }

    private boolean validate(){
        if (edtComment.getText().toString().isEmpty())
        {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError("Comment is Required");
        }
        else {
            return true;
        }
        return false;
    }
    @Override
    public void onChapClick(int position) {
        SharedPreferences PagePref = getActivity().getApplicationContext().getSharedPreferences("idchap",getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = PagePref.edit();
        editor.putString("idchap", String.valueOf(arrayList.get(position).getIdChap()));
        editor.apply();
        startActivity(new Intent(((ChapActivity)getContext()), PageActivity.class));
    }
}
