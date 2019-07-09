package com.app.ecommerce.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.app.ecommerce.Config;
import com.app.ecommerce.R;
import com.app.ecommerce.activities.CartActivity;
import com.app.ecommerce.activities.HelpActivity;
import com.app.ecommerce.activities.MyApplication;
import com.app.ecommerce.adapters.RecyclerAdapterHelp;
import com.app.ecommerce.models.Help;
import com.app.ecommerce.utilities.MyDividerItemDecoration;
import com.app.ecommerce.utilities.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import static com.app.ecommerce.utilities.Constant.GET_HELP;

public class OrderRecieptFragment extends Fragment implements RecyclerAdapterHelp.ContactsAdapterListener {

    private RecyclerView recyclerView;
    private List<Help> helpList;
    private RecyclerAdapterHelp mAdapter;
    private SearchView searchView;
    SwipeRefreshLayout swipeRefreshLayout = null;
    LinearLayout lyt_root;
    TextView textCartItemCount;
    int mCartItemCount = 0;
    public static final String CartPREFERENCES = "CartPrefs" ;
    public static final String cartCount = "cartCount";
    SharedPreferences sharedpreferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.place_order_fragment, container, false);
        setHasOptionsMenu(true);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);

        lyt_root = view.findViewById(R.id.lyt_root);
        if (Config.ENABLE_RTL_MODE) {
            lyt_root.setRotationY(180);
        }

        recyclerView = view.findViewById(R.id.recycler_view);
        helpList = new ArrayList<>();
        mAdapter = new RecyclerAdapterHelp(getActivity(), helpList, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL, 0));
        recyclerView.setAdapter(mAdapter);

        fetchContacts();
        onRefresh();

        return view;
    }

    private void onRefresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                helpList.clear();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Utils.isNetworkAvailable(getActivity())) {
                            swipeRefreshLayout.setRefreshing(false);
                            fetchContacts();
                        } else {
                            swipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, 1500);
            }
        });
    }

    private void fetchContacts() {
        JsonArrayRequest request = new JsonArrayRequest(GET_HELP, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.failed_fetch_data), Toast.LENGTH_LONG).show();
                            return;
                        }

                        List<Help> items = new Gson().fromJson(response.toString(), new TypeToken<List<Help>>() {
                        }.getType());

                        // adding contacts to contacts list
                        helpList.clear();
                        helpList.addAll(items);

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                Log.e("INFO", "Error: " + error.getMessage());
                Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        MyApplication.getInstance().addToRequestQueue(request);
    }


    public void onResume()
    {
        super.onResume();

        sharedpreferences = getActivity().getSharedPreferences(CartPREFERENCES, Context.MODE_PRIVATE);
        String cartValue=sharedpreferences.getString(cartCount, "");

        if(cartValue.equals(null) || cartValue.equals(""))
        {
        }
        else
        {
            mCartItemCount = Integer.parseInt(cartValue);
            setupBadge();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.search, menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);


        final MenuItem menuItem = menu.findItem(R.id.action_cart);

        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);
        setupBadge();
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onOptionsItemSelected(menuItem);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });


        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setupBadge()
    {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.action_cart:
                // Do something
                Intent i = new Intent(getActivity(), CartActivity.class);
                startActivity(i);
                return true;
            case R.id.logout:
                break;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }


    @Override
    public void onContactSelected(Help help) {
        Intent intent = new Intent(getActivity(), HelpActivity.class);
        intent.putExtra("title", help.getTitle());
        intent.putExtra("content", help.getContent());
        startActivity(intent);
    }

}