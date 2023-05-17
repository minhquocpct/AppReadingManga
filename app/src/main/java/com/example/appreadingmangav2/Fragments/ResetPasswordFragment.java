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
import com.android.volley.DefaultRetryPolicy;
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

public class ResetPasswordFragment extends Fragment {
    private View view;
    private TextInputLayout layoutEmail;
    private EditText edtEmail;
    private TextView txtSignUp,txtLogIn;
    private Button btnReset;
    private ProgressDialog dialog;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public ResetPasswordFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_resetpassword,container,false);
        init();
        return view;
    }
    private void init() {
        layoutEmail = view.findViewById(R.id.txtLayoutEmailReset);
        edtEmail = view.findViewById(R.id.edtemailreset);
        txtSignUp = view.findViewById(R.id.txtsignupfor);
        txtLogIn = view.findViewById(R.id.txtloginfor);
        btnReset = view.findViewById(R.id.btnreset);
        dialog = new ProgressDialog(getContext());
        dialog.setCancelable(false);
        txtSignUp.setOnClickListener(v -> {
            //change fragment
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameAuthContainer,new SignUpFragment()).commit();
        });
        txtLogIn.setOnClickListener(v -> {
            //change fragment
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameAuthContainer,new LogInFragment()).commit();
        });
        btnReset.setOnClickListener(v -> {
            if (validate()){
                reset();
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
            layoutEmail.setError("Invalid Email");
        }
        else{
            return true;
        }
        return false;
    }
    private void reset(){
        dialog.setMessage("Reseting Password");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Constant.RESET, response -> {
            //get respone if connection success
            try {
                JSONObject object = new JSONObject(response);
                if(object.getBoolean("success")){
                    Toast.makeText(getContext(),"Check your gmail", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameAuthContainer,new LogInFragment()).commit();
                }
                if(object.getBoolean("nonexist")){
                    Toast.makeText(getContext(),"This mail is't registered", Toast.LENGTH_SHORT).show();
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
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("email",edtEmail.getText().toString().trim());
                return map;

            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
}
