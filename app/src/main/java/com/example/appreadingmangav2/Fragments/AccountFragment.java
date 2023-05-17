package com.example.appreadingmangav2.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.appreadingmangav2.AuthActivity;
import com.example.appreadingmangav2.Constant;
import com.example.appreadingmangav2.HomeActivity;
import com.example.appreadingmangav2.MainActivity;
import com.example.appreadingmangav2.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AccountFragment extends Fragment {
    private View view;
    private MaterialToolbar toolbar;
    private TextInputLayout layoutPass, layoutName,layoutPass2,layoutOldPass;
    private EditText edtPass,edtPass2,edtName,edtOldPass;
    private TextView txtName;
    private Button btnEditInfo,btnEditPass;
    private ProgressDialog dialog;
    private SharedPreferences userPref;
    public AccountFragment(){
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_account,container,false);
        init();
        return view;
    }
    private void init() {
        toolbar = view.findViewById(R.id.toolbarAccount);
        ((HomeActivity)getContext()).setTitle("");
        ((HomeActivity)getContext()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        layoutOldPass= view.findViewById(R.id.txtLayoutOldPassEdit);
        layoutPass = view.findViewById(R.id.txtLayoutPassEdit);
        layoutPass2 = view.findViewById(R.id.txtLayoutPass2Edit);
        layoutName = view.findViewById(R.id.txtLayoutNameEdit);
        edtOldPass = view.findViewById(R.id.edtoldpassedit);
        edtPass = view.findViewById(R.id.edtpassedit);
        edtName = view.findViewById(R.id.edtnameedit);
        edtPass2 = view.findViewById(R.id.edtpass2edit);
        txtName = view.findViewById(R.id.txtAccountName);
        btnEditInfo = view.findViewById(R.id.btnEditAccount);
        btnEditPass = view.findViewById(R.id.btnEditPass);
        userPref = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        txtName.setText(userPref.getString("name",""));
        edtName.setText(userPref.getString("name",""));
        dialog = new ProgressDialog(getContext());
        dialog.setCancelable(false);
        btnEditInfo.setOnClickListener(v -> {
            if (validateinfo()){
                editinfo();
            }
        });
        btnEditPass.setOnClickListener(v -> {
            if (validatepass()){
                editpass();
            }
        });
        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!edtName.getText().toString().isEmpty()){
                    layoutName.setErrorEnabled(false);
                }
            }
        });
        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!edtName.getText().toString().isEmpty()){
                    layoutName.setErrorEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edtPass.getText().toString().length() >5){
                    layoutPass.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtPass2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!edtPass2.getText().toString().isEmpty()){
                    layoutPass2.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean validateinfo(){
        if (edtName.getText().toString().isEmpty())
        {
            layoutName.setErrorEnabled(true);
            layoutName.setError("Name is Required");
        }
        else {
            return true;
        }
        return false;
    }
    private boolean validatepass(){
        if ( edtOldPass.getText().toString().isEmpty())
        {
            layoutOldPass.setErrorEnabled(true);
            layoutOldPass.setError("Old Password required");
        }
        else if ( edtPass.getText().toString().length()<6)
        {
            layoutPass.setErrorEnabled(true);
            layoutPass.setError("Password at least 6 characters");
        }
        else if (!edtPass2.getText().toString().trim().matches(edtPass.getText().toString().trim()))
        {
            layoutPass2.setErrorEnabled(true);
            layoutPass2.setError("Password doesn't match");
        }
        else {
            return true;
        }
        return false;
    }
    private void editinfo(){
        dialog.setMessage("Editing");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Constant.EDIT, response -> {
            //get respone if connection success
            try {
                JSONObject object = new JSONObject(response);
                if(object.getBoolean("success")){
                    Toast.makeText(getContext(),"Edit Infomation Success", Toast.LENGTH_SHORT).show();
                    JSONObject user = object.getJSONObject("user");
                    SharedPreferences.Editor editor = userPref.edit();
                    editor.putString("name",user.getString("name"));
                    editor.putString("email",user.getString("email"));
                    editor.apply();
                    txtName.setText(userPref.getString("name",""));
                    edtName.setText(userPref.getString("name",""));
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
                map.put("id", userPref.getString("id",""));
                map.put("name",edtName.getText().toString());
                map.put("oldpassword","");
                map.put("password","");
                return map;

            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
    private void editpass(){
        dialog.setMessage("Editing");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Constant.EDIT, response -> {
            //get respone if connection success
            try {
                JSONObject object = new JSONObject(response);
                if(object.getBoolean("success")){
                    Toast.makeText(getContext(),"Edit Password Success", Toast.LENGTH_SHORT).show();
                }
                if (object.getBoolean("oldpass")){
                    Toast.makeText(getContext(),"Old Password Invalid", Toast.LENGTH_SHORT).show();
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
                map.put("id", userPref.getString("id",""));
                map.put("name","");
                map.put("oldpassword",edtOldPass.getText().toString());
                map.put("password",edtPass.getText().toString());
                return map;

            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_account,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.item_logout: {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Do you want to logout?");
                builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        }

        return super.onOptionsItemSelected(item);
    }
    private void logout(){
        StringRequest request = new StringRequest(Request.Method.GET,Constant.LOGOUT,res->{

            try {
                JSONObject object = new JSONObject(res);
                if (object.getBoolean("success")){
                    SharedPreferences.Editor editor = userPref.edit();
                    editor.clear();
                    editor.apply();
                    startActivity(new Intent(((HomeActivity)getContext()), AuthActivity.class));
                    ((HomeActivity)getContext()).finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        },error -> {
            error.printStackTrace();
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token =userPref.getString("token","");
                HashMap<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer "+token);
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
}
