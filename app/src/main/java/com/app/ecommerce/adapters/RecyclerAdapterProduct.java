package com.app.ecommerce.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.ecommerce.Config;
import com.app.ecommerce.R;
import com.app.ecommerce.models.Product;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecyclerAdapterProduct extends RecyclerView.Adapter<RecyclerAdapterProduct.MyViewHolder> implements Filterable {

    private Context context;
    private List<Product> productList;
    private List<Product> productListFiltered;
    private ProductAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder 
    {
        public TextView product_name, product_description,product_price,product_status,product_mrp,product_discount,btn_add_cart;
        public ImageView product_image;

        public MyViewHolder(View view) {
            super(view);
            product_name = view.findViewById(R.id.product_name);
            product_description= view.findViewById(R.id.product_description);
            product_price = view.findViewById(R.id.product_price);
            product_image = view.findViewById(R.id.category_image);
            product_status = view.findViewById(R.id.product_status);
            product_mrp = view.findViewById(R.id.product_mrp);
            product_discount = view.findViewById(R.id.product_discount);
            btn_add_cart = view.findViewById(R.id.btn_add_cart);

            btn_add_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onProductSelected(productListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public RecyclerAdapterProduct(Context context, List<Product> productList, ProductAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.productList = productList;
        this.productListFiltered = productList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Product product = productListFiltered.get(position);

        String productName = product.getProduct_name().substring(0, 1).toUpperCase() + product.getProduct_name().substring(1).toLowerCase();
        holder.product_name.setText(productName);
        String description = product.getProduct_description().substring(0, 1).toUpperCase() + product.getProduct_description().substring(1).toLowerCase();

        holder.product_description.setText(description);
        holder.product_mrp.setText("\u20B9 "+ product.getProduct_roll());
        holder.product_discount.setText("\u20B9 "+ product.getProduct_cut());
        holder.product_status.setText(product.getProduct_status());

        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(6)
                .oval(false)
                .build();

        Picasso.with(context)
                .load(Config.ADMIN_PANEL_URL + "/upload/product/" + product.getProduct_image())
                .placeholder(R.drawable.placeholder)
                .resize(250, 250)
                .centerCrop()
                .transform(transformation)
                .into(holder.product_image);

    }

    @Override
    public int getItemCount() {
        return productListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    productListFiltered = productList;
                } else {
                    List<Product> filteredList = new ArrayList<>();
                    for (Product row : productList) {
                        if (row.getProduct_name().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    productListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productListFiltered = (ArrayList<Product>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ProductAdapterListener {
        void onProductSelected(Product product);
    }
}
