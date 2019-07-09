package com.app.ecommerce.fragments;

import android.os.AsyncTask;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.ecommerce.R;
import com.app.ecommerce.activities.LoginRegisterActivity;
import com.app.ecommerce.utilities.HttpClicentService;
import com.app.ecommerce.utilities.Constant;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class RegisterFragment extends Fragment
{
    public static final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    EditText nameEdit,emailEdit,mobileEdit;
    TextView registerButton;
    ProgressBar progressBar;
    String name,email,mobile;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view =  inflater.inflate(R.layout.fragment_register, container, false);

        nameEdit =  view.findViewById(R.id.name);
        emailEdit =  view.findViewById(R.id.email);
        mobileEdit =  view.findViewById(R.id.mobile);
        registerButton =  view.findViewById(R.id.register);
        progressBar =  view.findViewById(R.id.progressBar);

        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                name = nameEdit.getText().toString().trim();
                email = emailEdit.getText().toString().trim();
                mobile = mobileEdit.getText().toString().trim();


                if(name.equals("") || name ==null )
                {
                    Toast.makeText(getActivity(), getResources().getString(R.string.enter_name), Toast.LENGTH_SHORT).show();
                }
                else if(email.equals("") || email == null )
                {
                    Toast.makeText(getActivity(), getResources().getString(R.string.enter_email), Toast.LENGTH_SHORT).show();

                }
                else if(!isValidEmailId(email))
                {

                    Toast.makeText(getActivity(), getResources().getString(R.string.invalid_email_address), Toast.LENGTH_SHORT).show();

                }
                else if(mobile.equals("") || mobile== null   || mobile.length() < 10  )
                {

                    Toast.makeText(getActivity(), getResources().getString(R.string.enter_mobile), Toast.LENGTH_SHORT).show();

                }
                else
                {

                    getRegistration(name,email,mobile);
                }

            }
        });

        return  view;
    }


    private boolean isValidEmailId(String email)
    {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }


    public void getRegistration(final String name, final String email, final String mobile)
    {

        progressBar.setVisibility(View.VISIBLE);
        Map<String, String> params = new HashMap();
        params.put("name", name);
        params.put("email", email);
        params.put("mobile", mobile);

        JSONObject parameters = new JSONObject(params);


        // Initialize a new RequestQueue instance
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                Constant.POST_REGISTER,
                parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {

                        progressBar.setVisibility(View.INVISIBLE);

                        try{
                            String message = response.getString("message");
                            String error = response.getString("error");

                            if(error.equals(false))
                            {
                               // LoginRegisterActivity.viewPager.setCurrentItem(0);
                            }
                            Toast.makeText(getActivity(), message,Toast.LENGTH_SHORT).show();
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

                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

}