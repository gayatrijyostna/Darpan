package com.app.ecommerce.activities;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import com.app.ecommerce.R;
import com.app.ecommerce.utilities.NetworkStatus;
import com.app.ecommerce.utilities.SessionManager;
import java.util.ArrayList;
import java.util.HashMap;

public class NoInternetActivity extends AppCompatActivity {

    SessionManager session;
    String appurl, userid, token, product1, product2;
    String imageUrl;
    TableLayout Table;
    TableLayout.LayoutParams tableRowParams;
    ProgressBar progressBar;
    TextView tv1, tv2, connectingtv;
    Button tryagain;
    private ArrayList<String> arraylist, newarraylist;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nointernet_activity);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> users = session.getUserDetails();
        userid = users.get(SessionManager.KEY_user_id);

        tryagain = (Button) findViewById(R.id.tryagain);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        connectingtv = (TextView) findViewById(R.id.tv3);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        tryagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNetwork();
            }

        });


    }


    public void checkNetwork()
    {

        if (!NetworkStatus.isNetworkAvailable(NoInternetActivity.this)) {

            new CountDownTimer(3000, 3000) {
                @Override
                public void onFinish() {

                    progressBar.setVisibility(View.INVISIBLE);
                    tv1.setVisibility(View.VISIBLE);
                    tv2.setVisibility(View.VISIBLE);
                    connectingtv.setVisibility(View.GONE);
                    tryagain.setVisibility(View.VISIBLE);

                }

                @Override
                public void onTick(long millisUntilFinished) {
                    progressBar.setVisibility(View.VISIBLE);
                    tv1.setVisibility(View.INVISIBLE);
                    tv2.setVisibility(View.INVISIBLE);
                    connectingtv.setVisibility(View.VISIBLE);
                    tryagain.setVisibility(View.GONE);
                }
            }.start();


        }
        else if (NetworkStatus.isNetworkAvailable(NoInternetActivity.this) && session.isLoggedIn()) {
            Intent i = new Intent(NoInternetActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        } else {
            Intent i = new Intent(NoInternetActivity.this, LoginRegisterActivity.class);
            startActivity(i);
            finish();
        }

    }


    @Override
    public void onBackPressed() {
        checkNetwork();

    }
}
