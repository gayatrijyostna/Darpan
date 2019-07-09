package com.app.ecommerce.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.app.ecommerce.Config;
import com.app.ecommerce.R;
import com.app.ecommerce.adapters.RecyclerAdapterProduct;
import com.app.ecommerce.models.Product;
import com.app.ecommerce.utilities.Constant;
import com.app.ecommerce.utilities.DBHelper;
import com.app.ecommerce.utilities.ItemOffsetDecoration;
import com.app.ecommerce.utilities.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import static com.app.ecommerce.utilities.Constant.GET_CATEGORY_DETAIL;

public class ProductListActivity extends AppCompatActivity implements RecyclerAdapterProduct.ProductAdapterListener {

    private RecyclerView recyclerView;
    private List<Product> productList;
    private RecyclerAdapterProduct mAdapter;
    SwipeRefreshLayout swipeRefreshLayout = null;
    private String category_id, category_name;
    private SearchView searchView;
    TextView textCartItemCount;
    int mCartItemCount = 0;
    public static final String CartPREFERENCES = "CartPrefs" ;
    public static final String cartCount = "cartCount";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        category_id = intent.getStringExtra("category_id");
        category_name = intent.getStringExtra("category_name");

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(category_name);

        sharedpreferences = getSharedPreferences(CartPREFERENCES, Context.MODE_PRIVATE);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.recycler_view);
        productList = new ArrayList<>();
        mAdapter = new RecyclerAdapterProduct(this, productList, this);

        //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        fetchData();
        onRefresh();

    }

    private void onRefresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                productList.clear();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Utils.isNetworkAvailable(ProductListActivity.this)) {
                            swipeRefreshLayout.setRefreshing(false);
                            fetchData();
                        } else {
                            swipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, 1500);
            }
        });
    }

    private void fetchData() {
        JsonArrayRequest request = new JsonArrayRequest(Constant.GET_RECENT_PRODUCT, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            Toast.makeText(getApplicationContext(), R.string.failed_fetch_data, Toast.LENGTH_LONG).show();
                            return;
                        }

                        List<Product> items = new Gson().fromJson(response.toString(), new TypeToken<List<Product>>() {
                        }.getType());

                        // adding contacts to contacts list
                        productList.clear();
                        productList.addAll(items);

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                Log.e("INFO", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        MyApplication.getInstance().addToRequestQueue(request);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.search, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        final MenuItem menuItem = menu.findItem(R.id.action_cart);

        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);
        setupBadge();
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });


        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        return true;
    }

    public void onResume()
    {
        super.onResume();

        sharedpreferences = getSharedPreferences(CartPREFERENCES, Context.MODE_PRIVATE);
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


    private void setupBadge() {

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
                Intent i = new Intent(this, CartActivity.class);
                startActivity(i);
                return true;
            case R.id.logout:
                break;
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }



    @Override
    public void onProductSelected(Product product)
    {

        sharedpreferences = getSharedPreferences(CartPREFERENCES, Context.MODE_PRIVATE);
        String cartValue=sharedpreferences.getString(cartCount, "");
        mCartItemCount = Integer.parseInt(cartValue);

        mCartItemCount = mCartItemCount+1;
        textCartItemCount.setText(mCartItemCount+"");
        textCartItemCount.setVisibility(View.VISIBLE);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(cartCount, mCartItemCount+"");
        editor.commit();

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        dbHelper.addData(product.getProduct_id(),product.getProduct_name(),1,product.getProduct_price(),product.getCurrency_code());

//        Intent intent = new Intent(getApplicationContext(), ProductDetailsActivity.class);
//        intent.putExtra("product_id", product.getProduct_id());
//        intent.putExtra("title", product.getProduct_name());
//        intent.putExtra("image", product.getProduct_image());
//        intent.putExtra("product_price", product.getProduct_price());
//        intent.putExtra("product_description", product.getProduct_description());
//        intent.putExtra("product_quantity", product.getProduct_quantity());
//        intent.putExtra("product_status", product.getProduct_status());
//        intent.putExtra("currency_code", product.getCurrency_code());
//        intent.putExtra("category_name", product.getCategory_name());
//        startActivity(intent);
    }

}
