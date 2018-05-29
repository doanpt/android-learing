package com.cnc.hcm.cnctrack.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cnc.hcm.cnctrack.R;
import com.cnc.hcm.cnctrack.util.Conts;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Android on 1/11/2018.
 */

public class ProductDetailAdapter extends RecyclerView.Adapter<ProductDetailAdapter.ProductHolder> {
    private ArrayList<String> arrUrl = new ArrayList<>();
    private Context mContext;

    public ProductDetailAdapter(Context context) {
        mContext = context;
    }

    public ProductDetailAdapter(Context context,ArrayList<String> arrUrl) {
        mContext = context;
        this.arrUrl=arrUrl;
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_image_item, parent, false);
        return new ProductDetailAdapter.ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductHolder holder, int position) {
        String urlImage = arrUrl.get(position);
        Picasso.with(mContext).load(Conts.URL_BASE+urlImage)
                .placeholder(R.drawable.ic_bg_place_holder).resize(110, 110).centerCrop()
                .error(R.drawable.ic_bg_place_error).resize(110, 110).centerCrop()
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return arrUrl.size();
    }

    public void addItem(String newImg) {
        arrUrl.add(newImg);
        notifyDataSetChanged();
    }

    public class ProductHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView img;

        public ProductHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.product_image);
            img.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
