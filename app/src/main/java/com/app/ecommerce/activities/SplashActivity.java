package com.app.ecommerce.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.app.ecommerce.R;
import com.app.ecommerce.utilities.NetworkStatus;
import com.app.ecommerce.utilities.SessionManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;



public class SplashActivity extends Activity
{
    SessionManager session;
    String userid, token, appurl;
    Boolean lan_flag_check;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        session = new SessionManager(SplashActivity.this);
        HashMap<String, String> users = session.getUserDetails();
        userid = users.get(SessionManager.KEY_user_id);

        if (!NetworkStatus.isNetworkAvailable(SplashActivity.this))
        {
            new CountDownTimer(4000, 4000)
            {
                @Override
                public void onFinish()
                {

                    Intent i = new Intent(SplashActivity.this, NoInternetActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.pull_in_right, R.anim.pull_out_left);
                    //finish();
                }
                @Override
                public void onTick(long millisUntilFinished) {

                }
            }.start();


        }
        else if(session.isLoggedIn())
        {
            new CountDownTimer(4000, 4000)
            {
                @Override
                public void onFinish()
                {
                    Intent i =new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }
                @Override
                public void onTick(long millisUntilFinished) {

                }
            }.start();
        }
        else
            {

                new CountDownTimer(4000, 4000)
                {
                    @Override
                    public void onFinish()
                    {

                        Intent i =new Intent(SplashActivity.this,LoginRegisterActivity.class);
                        startActivity(i);
                        finish();
                    }
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }
                }.start();
        }
    }
}