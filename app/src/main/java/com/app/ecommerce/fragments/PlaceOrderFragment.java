package com.app.ecommerce.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.app.ecommerce.R;
import com.app.ecommerce.activities.CartActivity;
import com.app.ecommerce.activities.MyApplication;
import com.app.ecommerce.activities.ProductListActivity;
import com.app.ecommerce.adapters.BannerViewPagerAdapter;
import com.app.ecommerce.adapters.BannerViewPagerAdapter2;
import com.app.ecommerce.adapters.RecyclerAdapterCategory;
import com.app.ecommerce.adapters.RecyclerAdapterProduct;
import com.app.ecommerce.models.Category;
import com.app.ecommerce.models.Product;
import com.app.ecommerce.transfermations.ZoomOutTransformation;
import com.app.ecommerce.utilities.ItemOffsetDecoration;
import com.app.ecommerce.utilities.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.List;
import static com.app.ecommerce.utilities.Constant.GET_CATEGORY;

public class PlaceOrderFragment extends Fragment implements RecyclerAdapterCategory.ContactsAdapterListener
{

    private RecyclerView recyclerView;
    private ViewPager viewPager;
    private List<Category> categoryList;
    private List<Product> productList;

    private RecyclerAdapterCategory mAdapter;
    private RecyclerAdapterProduct pAdapter;
    private BannerViewPagerAdapter2 bannerAdapter;
    private SearchView searchView;
    SwipeRefreshLayout swipeRefreshLayout = null;
    TextView textCartItemCount;
    int mCartItemCount = 0;
    public static final String CartPREFERENCES = "CartPrefs" ;
    public static final String cartCount = "cartCount";
    SharedPreferences sharedpreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        setHasOptionsMenu(true);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setRefreshing(true);

        recyclerView = view.findViewById(R.id.recycler_view);
        categoryList = new ArrayList<>();
        mAdapter = new RecyclerAdapterCategory(getActivity(), categoryList, this);

        viewPager =  view.findViewById(R.id.bannerPager);
        bannerAdapter = new BannerViewPagerAdapter2(getActivity(), categoryList);


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        viewPager.setAdapter(bannerAdapter);
        ZoomOutTransformation zoomOutTransformation = new ZoomOutTransformation();
        viewPager.setPageTransformer(true, zoomOutTransformation);


        fetchData();
        onRefresh();

        return view;
    }

    private void onRefresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                categoryList.clear();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Utils.isNetworkAvailable(getActivity())) {
                            swipeRefreshLayout.setRefreshing(false);
                            fetchData();
                        } else {
                            swipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, 1500);
            }
        });
    }

    private void fetchData()
    {
        JsonArrayRequest request = new JsonArrayRequest(GET_CATEGORY, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                Log.e(">>>>>>>>>>>>", response+"");
                if (response == null) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.failed_fetch_data), Toast.LENGTH_LONG).show();
                    return;
                }

                List<Category> items = new Gson().fromJson(response.toString(), new TypeToken<List<Category>>() {
                }.getType());

                // adding contacts to contacts list
                categoryList.clear();
                categoryList.addAll(items);

                // refreshing recycler view
                mAdapter.notifyDataSetChanged();
                bannerAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                Log.e("INFO", "Error: " + error.getMessage());
                Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);


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
    public void onContactSelected(Category category) {
        //Toast.makeText(getActivity(), "Selected: " + category.getCategory_name(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getActivity(), ProductListActivity.class);
        intent.putExtra("category_id", category.getCategory_id());
        intent.putExtra("category_name", category.getCategory_name());
        startActivity(intent);
    }
}