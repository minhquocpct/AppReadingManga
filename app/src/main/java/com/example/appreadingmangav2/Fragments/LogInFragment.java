package com.example.appreadingmangav2.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appreadingmangav2.AuthActivity;
import com.example.appreadingmangav2.Constant;
import com.example.appreadingmangav2.HomeActivity;
import com.example.appreadingmangav2.R;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LogInFragment extends Fragment {
    private View view;
    private TextInputLayout layoutPass, layoutEmail;
    private EditText edtPass,edtEmail;
    private TextView txtSignUp, txtReset;
    private Button btnLogin;
    private ProgressDialog dialog;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public LogInFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_login,container,false);
        init();
        return view;
    }
    private void init() {
        layoutEmail = view.findViewById(R.id.txtLayoutEmailLogin);
        layoutPass = view.findViewById(R.id.txtLayoutPassLogin);
        edtPass = view.findViewById(R.id.edtpasslogin);
        edtEmail = view.findViewById(R.id.edtemaillogin);
        txtSignUp = view.findViewById(R.id.txtsignup);
        txtReset = view.findViewById(R.id.forgetpasslogin);
        btnLogin = view.findViewById(R.id.btnlogin);
        dialog = new ProgressDialog(getContext());
        dialog.setCancelable(false);
        txtSignUp.setOnClickListener(v -> {
            //change fragment
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameAuthContainer,new SignUpFragment()).commit();
        });
        txtReset.setOnClickListener(v -> {
            //change fragment
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameAuthContainer,new ResetPasswordFragment()).commit();
        });
        btnLogin.setOnClickListener(v -> {
            if (validate()){
                login();
            }
        });

        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!edtEmail.getText().toString().isEmpty()){
                    layoutEmail.setErrorEnabled(false);
                }
                if (edtEmail.getText().toString().trim().matches(emailPattern) && edtEmail.getText().toString().length() > 0)
                {
                    layoutEmail.setErrorEnabled(false);
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
    }

    private boolean validate(){

        if (edtEmail.getText().toString().isEmpty())
        {
            layoutEmail.setErrorEnabled(true);
            layoutEmail.setError("Email is Required");
        }
        else if (!edtEmail.getText().toString().trim().matches(emailPattern) && edtEmail.getText().toString().length() > 0)
        {
            layoutEmail.setErrorEnabled(true);
            layoutEmail.setError("Valid Email");
        }
        else if (edtPass.getText().toString().length()<6)
        {
            layoutPass.setErrorEnabled(true);
            layoutPass.setError("Password at least 6 characters");
        }
        else{
            return true;
        }
        return false;
    }
    private void login(){
            dialog.setMessage("Logging in");
            dialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, Constant.LOGIN,response -> {
            //get respone if connection success
            try {
                JSONObject object = new JSONObject(response);
                if(object.getBoolean("success")){
                    JSONObject user = object.getJSONObject("user");
                    SharedPreferences userPref = getActivity().getApplicationContext().getSharedPreferences("user",getContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor = userPref.edit();
                    editor.putString("token",object.getString("token"));
                    editor.putString("id",user.getString("id"));
                    editor.putString("name",user.getString("name"));
                    editor.putString("email",user.getString("email"));
                    editor.putBoolean("isLoggedIn",true);
                    editor.apply();
                    Toast.makeText(getContext(),"Login Success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(((AuthActivity)getContext()), HomeActivity.class));
                    ((AuthActivity) getContext()).finish();
                }else{
                    Toast.makeText(getContext(),"Email or Passwrod invalid", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                dialog.dismiss();
            }
            dialog.dismiss();

        },error -> { error.printStackTrace();
            dialog.dismiss();
        }){
            //add parameter

            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                HashMap<String, String> map = new HashMap<>();
                map.put("email",edtEmail.getText().toString().trim());
                map.put("password",edtPass.getText().toString());
                return map;

            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
}
