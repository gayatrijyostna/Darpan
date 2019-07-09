package com.app.ecommerce.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.ecommerce.R;
import com.app.ecommerce.activities.LoginRegisterActivity;
import com.app.ecommerce.activities.MainActivity;
import com.app.ecommerce.utilities.Constant;
import com.app.ecommerce.utilities.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class LoginFragment extends Fragment
{

    EditText mobileEdit,passEdit;
    TextView loginButton;
    ProgressBar progressBar;
    String mobile,password;
    SessionManager session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view =  inflater.inflate(R.layout.fragment_login, container, false);
        session = new SessionManager(getActivity());
        mobileEdit =  view.findViewById(R.id.phone_number);
        passEdit =  view.findViewById(R.id.pwd);
        loginButton =  view.findViewById(R.id.login);
        progressBar =  view.findViewById(R.id.progressBar);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mobile = mobileEdit.getText().toString().trim();
                password = passEdit.getText().toString().trim();

                 if(mobile.equals("") || mobile== null || mobile.length() < 10 )
                {
                    Toast.makeText(getActivity(), getResources().getString(R.string.enter_mobile), Toast.LENGTH_SHORT).show();

                }
                else if(password.equals("") || password == null)
                {
                    Toast.makeText(getActivity(), getResources().getString(R.string.enter_password), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    getLogin(mobile,password);
                }
            }
        });

        return  view;
    }

    public void getLogin(final String mobile, final String password)
    {

        progressBar.setVisibility(View.VISIBLE);
        Map<String, String> params = new HashMap();
        params.put("mobile", mobile);
        params.put("password", password);
        JSONObject parameters = new JSONObject(params);
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                Constant.POST_LOGIN,
                parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        progressBar.setVisibility(View.INVISIBLE);

                        try{
                            String jwt = response.getString("jwt");
                            JSONObject jsonObject = response.getJSONObject("user_details");
                            String user_id = jsonObject.getString("user_id");
                            String name = jsonObject.getString("name");
                            String mobile = jsonObject.getString("mobile");
                            String password = jsonObject.getString("password");
                            String email = jsonObject.getString("email");
                            String landline = jsonObject.getString("landline_number");
                            String address = jsonObject.getString("address");
                            String pincode = jsonObject.getString("pincode");
                            String city = jsonObject.getString("city");
                            String executive_id = jsonObject.getString("executive_id");
                            String otp_code = jsonObject.getString("otp_code");
                            String mobile_verified = jsonObject.getString("mobile_verified");
                            String fcm_registration_id = jsonObject.getString("fcm_registration_id");
                            String status = jsonObject.getString("status");
                            String created_at = jsonObject.getString("created_at");
                            String updated_at = jsonObject.getString("updated_at");

                            if(!user_id.equals(null))
                            {
                                session.createLoginSession(user_id, name, mobile, password, email,landline,address,pincode,
                                        city,executive_id,otp_code,mobile_verified,fcm_registration_id,status,created_at,updated_at);

                                Intent i = new Intent(getActivity(), MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                getActivity().overridePendingTransition(R.anim.pull_in_left, R.anim.pull_out_right);
                                getActivity().finish();
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(), error+"",Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

}