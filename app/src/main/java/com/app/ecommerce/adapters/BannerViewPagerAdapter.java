package com.app.ecommerce.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.ecommerce.Config;
import com.app.ecommerce.R;
import com.app.ecommerce.models.Product;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import java.util.List;

 public  class BannerViewPagerAdapter extends PagerAdapter
{
    private List<Product> imageList;
    private Context context;

    public BannerViewPagerAdapter(Context context, List<Product> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @Override
    public int getCount()
    {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.view_pager_item, container, false);
        ImageView imageView = layout.findViewById(R.id.imageView);
        final Product product = imageList.get(position);

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
                .into(imageView);

        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}