package com.app.ecommerce.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.app.ecommerce.R;
import com.app.ecommerce.fragments.PlaceOrderFragment;
import com.app.ecommerce.fragments.OrderRecieptFragment;
import com.app.ecommerce.fragments.OrderHistoryFragment;
import com.app.ecommerce.fragments.TrackOrderFragment;
import com.app.ecommerce.utilities.SessionManager;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private TextView mTextMessage;
    private ViewPager viewPager;
    int pager_number = 5;
    MenuItem prevMenuItem;
    BottomNavigationView bottomNavigation;
    DrawerLayout drawer;
    SessionManager session;
    public static final String CartPREFERENCES = "CartPrefs" ;
    public static final String cartCount = "cartCount";
    SharedPreferences sharedpreferences;
    int mCartItemCount = 0;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener()
    {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId())
            {
                case R.id.place_order:
                    mTextMessage.setText(R.string.place_order);
                    return true;
                case R.id.track_order:
                    mTextMessage.setText(R.string.track_order);
                    return true;
                case R.id.order_history:
                    mTextMessage.setText(R.string.order_history);
                    return true;
                case R.id.receipt_order:
                    mTextMessage.setText(R.string.receipt_order);
                    return true;
                case R.id.collection_details:
                    mTextMessage.setText(R.string.collection_details);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            sharedpreferences = getSharedPreferences(CartPREFERENCES, Context.MODE_PRIVATE);
            String cartValue = sharedpreferences.getString(cartCount, "");
            mCartItemCount = Integer.parseInt(cartValue);
        }
        catch (Exception e) {

        }

        session = new SessionManager(MainActivity.this);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        mTextMessage = findViewById(R.id.message);
        mTextMessage.setText(R.string.place_order);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.place_order:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.track_order:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.order_history:
                        viewPager.setCurrentItem(2);
                        return true;
                    case R.id.receipt_order:
                        viewPager.setCurrentItem(3);
                        return true;
                    case R.id.collection_details:
                        viewPager.setCurrentItem(4);
                        return true;
                }

                if (drawer.isDrawerOpen(GravityCompat.START))
                {
                    drawer.closeDrawer(GravityCompat.START);
                }

                return false;
            }
        });

        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(pager_number);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            { }

            @Override
            public void onPageSelected(int position)
            {
                if (prevMenuItem != null)
                {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavigation.getMenu().getItem(0).setChecked(false);
                }
                bottomNavigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigation.getMenu().getItem(position);

                if (viewPager.getCurrentItem() == 0)
                {
                    mTextMessage.setText(R.string.place_order);
                }
                else if (viewPager.getCurrentItem() == 1)
                {
                    mTextMessage.setText(R.string.track_order);
                }
                else if (viewPager.getCurrentItem() == 2)
                {
                    mTextMessage.setText(R.string.order_history);
                }
                else if (viewPager.getCurrentItem() == 3)
                {
                    mTextMessage.setText(R.string.receipt_order);
                }
                else {
                    mTextMessage.setText(R.string.collection_details);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    public class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new PlaceOrderFragment();
                case 1:
                    return new TrackOrderFragment();
                case 2:
                    return new OrderHistoryFragment();
                case 3:
                    return new OrderRecieptFragment();
                case 4:
                    return new TrackOrderFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return pager_number;
        }

    }


    public void onResume()
    {
        super.onResume();


    }


    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }


    //////////////////////


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.place_order) {
            mTextMessage.setText(R.string.place_order);
            viewPager.setCurrentItem(0);
        } else if (id == R.id.track_order) {
            mTextMessage.setText(R.string.track_order);
            viewPager.setCurrentItem(1);
        } else if (id == R.id.order_history) {
            mTextMessage.setText(R.string.order_history);
            viewPager.setCurrentItem(2);
        } else if (id == R.id.receipt_order) {
            mTextMessage.setText(R.string.receipt_order);
            viewPager.setCurrentItem(3);

        } else if (id == R.id.collection_details) {
            mTextMessage.setText(R.string.collection_details);
            viewPager.setCurrentItem(4);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.logout)
        {
            session.logoutUser();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
