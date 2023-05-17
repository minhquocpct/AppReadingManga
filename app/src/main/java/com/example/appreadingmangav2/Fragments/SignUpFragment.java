package com.example.appreadingmangav2.Fragments;

import android.app.ProgressDialog;
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
import com.example.appreadingmangav2.Constant;
import com.example.appreadingmangav2.R;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpFragment extends Fragment {
    private View view;
    private TextInputLayout layoutPass, layoutEmail,layoutName,layoutPass2;
    private EditText edtPass,edtEmail,edtPass2,edtName;
    private TextView txtLogin,txtReset;
    private Button btnSignUp;
    private ProgressDialog dialog;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public SignUpFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_signup,container,false);
        init();
        return view;
    }
    private void init() {
        layoutEmail = view.findViewById(R.id.txtLayoutEmailSignUp);
        layoutPass = view.findViewById(R.id.txtLayoutPassSignUp);
        layoutPass2 = view.findViewById(R.id.txtLayoutPass2SignUp);
        layoutName = view.findViewById(R.id.txtLayoutNameSignUp);
        edtPass = view.findViewById(R.id.edtpasssignup);
        edtEmail = view.findViewById(R.id.edtemailsignup);
        edtName = view.findViewById(R.id.edtnamesignup);
        edtPass2 = view.findViewById(R.id.edtpass2signup);
        txtLogin = view.findViewById(R.id.txtlogin);
        txtReset = view.findViewById(R.id.forgetpasssignup);
        btnSignUp = view.findViewById(R.id.btnSingUp);
        dialog = new ProgressDialog(getContext());
        dialog.setCancelable(false);
        txtLogin.setOnClickListener(v -> {
            //change fragment
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameAuthContainer,new LogInFragment()).commit();
        });
        txtReset.setOnClickListener(v -> {
            //change fragment
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameAuthContainer,new ResetPasswordFragment()).commit();
        });
        btnSignUp.setOnClickListener(v -> {
            if (validate()){
                signup();
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

    private boolean validate(){
        if (edtName.getText().toString().isEmpty())
        {
            layoutName.setErrorEnabled(true);
            layoutName.setError("Name is Required");
        }
        else if (edtEmail.getText().toString().isEmpty())
        {
            layoutEmail.setErrorEnabled(true);
            layoutEmail.setError("Email is Required");
        }
        else if (!edtEmail.getText().toString().trim().matches(emailPattern) && edtEmail.getText().toString().length() > 0)
        {
            layoutEmail.setErrorEnabled(true);
            layoutEmail.setError("Invalid Email");
        }
        else if (edtPass.getText().toString().length()<6)
        {
            layoutPass.setErrorEnabled(true);
            layoutPass.setError("Password at least 6 characters");
        }
        else if (!edtPass2.getText().toString().trim().matches(edtPass.getText().toString().trim()))
        {
            layoutPass2.setErrorEnabled(true);
            layoutPass2.setError("Password doesn't match");
        }
        else{
            return true;
        }
        return false;
    }
    private void signup(){
        dialog.setMessage("Signing up");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Constant.SIGNUP, response -> {
            //get respone if connection success
            try {
                JSONObject object = new JSONObject(response);
                if(object.getBoolean("success")){
                    Toast.makeText(getContext(),"Sign up success", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameAuthContainer,new LogInFragment()).commit();
                } if(object.getBoolean("unique")){
                    Toast.makeText(getContext(),"Email has already been taken", Toast.LENGTH_SHORT).show();
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
                map.put("name",edtName.getText().toString());
                map.put("email",edtEmail.getText().toString().trim());
                map.put("password",edtPass.getText().toString());
                return map;

            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }

}
