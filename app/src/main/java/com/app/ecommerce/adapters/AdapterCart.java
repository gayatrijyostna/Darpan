package com.app.ecommerce.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.ecommerce.Config;
import com.app.ecommerce.R;
import com.app.ecommerce.activities.CartActivity;
import com.app.ecommerce.models.Cart;
import com.app.ecommerce.utilities.DBHelper;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;
import java.util.Locale;

public class AdapterCart extends RecyclerView.Adapter<AdapterCart.ViewHolder> {

    private Context context;
    private List<Cart> arrayCart;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView product_name;
        TextView product_quantity;
        TextView product_price;
        ImageView product_image;
        EditText quantitiy;
        DBHelper dbHelper;
        public ViewHolder(View view) {
            super(view);
            product_name = view.findViewById(R.id.product_name);
            product_quantity = view.findViewById(R.id.product_quantity);
            product_price = view.findViewById(R.id.product_price);
            product_image = view.findViewById(R.id.product_image);
            quantitiy = view.findViewById(R.id.quantitiy);
            dbHelper = new DBHelper(context);
        }

    }

    public AdapterCart(Context context, List<Cart> arrayCart) {
        this.context = context;
        this.arrayCart = arrayCart;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.product_name.setText(CartActivity.product_name.get(position));



        holder.quantitiy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {



            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        if (Config.ENABLE_DECIMAL_ROUNDING)
        {
            double _single_item = CartActivity.sub_total_price.get(position) / CartActivity.product_quantity.get(position);
            String single_item_price = String.format(Locale.GERMAN, "%1$,.0f", _single_item);

            holder.product_quantity.setText(single_item_price + " " + CartActivity.currency_code.get(position) + " x " + CartActivity.product_quantity.get(position));

            String price = String.format(Locale.GERMAN, "%1$,.0f", CartActivity.sub_total_price.get(position));
            holder.product_price.setText(price + " " + CartActivity.currency_code.get(position));
        } else {
            double _single_item = CartActivity.sub_total_price.get(position) / CartActivity.product_quantity.get(position);

            holder.product_quantity.setText(_single_item + " " + CartActivity.currency_code.get(position) + " x " + CartActivity.product_quantity.get(position));

            holder.product_price.setText(CartActivity.sub_total_price.get(position) + " " + CartActivity.currency_code.get(position));
        }

        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(8)
                .oval(false)
                .build();

        Picasso.with(context)
                .load(Config.ADMIN_PANEL_URL + "/upload/product/")
                .placeholder(R.drawable.placeholder)
                .resize(250, 250)
                .centerCrop()
                .transform(transformation)
                .into(holder.product_image);

    }

    @Override
    public int getItemCount() {
        return CartActivity.product_id.size();
    }

}
