package com.cnc.hcm.cnctracking.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.model.Services;

import java.util.ArrayList;

/**
 * Created by Android on 2/2/2018.
 */

public class ServiceProcessAdapter extends RecyclerView.Adapter<ServiceProcessAdapter.ServiceHolder> {
    private ArrayList<Services.Result> arrServices = new ArrayList<>();
    private Context mContext;

    public ServiceProcessAdapter(Context context) {
        mContext = context;
    }

    public ServiceProcessAdapter(Context context, ArrayList<Services.Result> arrUrl) {
        mContext = context;
        this.arrServices = arrUrl;
    }

    @Override
    public ServiceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_services, parent, false);
        return new ServiceProcessAdapter.ServiceHolder(view);
    }

    @Override
    public void onBindViewHolder(ServiceHolder holder, int position) {
        Services.Result item = arrServices.get(position);
        holder.tvName.setText(item.getName() + "");
        holder.tvAmount.setText("1 " + item.getUnit().getTitle());
        holder.tvPrice.setText(item.getPrice() + "");
        holder.tvTotalPrice.setText(item.getPrice() + "");
    }

    @Override
    public int getItemCount() {
        return arrServices.size();
    }

    public class ServiceHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvPrice, tvAmount, tvTotalPrice;

        public ServiceHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name_product_service);
            tvPrice = itemView.findViewById(R.id.tv_price_product_service);
            tvAmount = itemView.findViewById(R.id.tv_amount_product_service);
            tvTotalPrice = itemView.findViewById(R.id.tv_total_price_product_service);
        }
    }
}
