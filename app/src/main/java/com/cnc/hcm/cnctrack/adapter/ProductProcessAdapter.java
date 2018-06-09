package com.cnc.hcm.cnctrack.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnc.hcm.cnctrack.R;
import com.cnc.hcm.cnctrack.model.detailproduct.Device_Products;
import com.cnc.hcm.cnctrack.util.Conts;

import java.util.ArrayList;

/**
 * Created by Android on 2/2/2018.
 */

public class ProductProcessAdapter extends RecyclerView.Adapter<ProductProcessAdapter.ProductHolder> {
    private static final String TAG = ProductProcessAdapter.class.getSimpleName();
    private ArrayList<Device_Products> arrProduct = new ArrayList<>();
    private Context mContext;

    public ProductProcessAdapter(Context context) {
        mContext = context;
    }

    public ProductProcessAdapter(Context context, ArrayList<Device_Products> arrUrl) {
        mContext = context;
        this.arrProduct = arrUrl;
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_services, parent, false);
        return new ProductProcessAdapter.ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductHolder holder, int position) {
        Device_Products item = arrProduct.get(position);
        holder.tvName.setText(item.getProduct().getName() + "");
        holder.tvAmount.setText(item.getQuantity() + " " + (item.getProduct().getUnit() != null ? item.getProduct().getUnit().getTitle() : Conts.BLANK));
        holder.tvPrice.setText(item.getProduct().getPrice() + "");
        int price = 0;
        int total = 0;
        try {
            price = Integer.parseInt(item.getProduct().getPrice().toString() + "");
            total = item.getQuantity() * price;
        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder", e);
        }
        holder.tvTotalPrice.setText(total + "");
    }

    @Override
    public int getItemCount() {
        return arrProduct.size();
    }

    public class ProductHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvPrice, tvAmount, tvTotalPrice;

        public ProductHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name_product_service);
            tvPrice = itemView.findViewById(R.id.tv_price_product_service);
            tvAmount = itemView.findViewById(R.id.tv_amount_product_service);
            tvTotalPrice = itemView.findViewById(R.id.tv_total_price_product_service);
        }
    }
}
