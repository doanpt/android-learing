package com.cnc.hcm.cnctracking.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.model.ItemPrice;
import com.cnc.hcm.cnctracking.util.CommonMethod;
import com.cnc.hcm.cnctracking.util.Conts;

import java.util.ArrayList;
import java.util.List;

public class WorkDetailServiceRecyclerViewAdapter extends RecyclerView.Adapter<WorkDetailServiceRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = WorkDetailServiceRecyclerViewAdapter.class.getSimpleName();

    private List<ItemPrice> itemPrices = new ArrayList<>();

    private Context context;

    public WorkDetailServiceRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public List<ItemPrice> getItemPrices() {
        return itemPrices;
    }

    @Override
    public WorkDetailServiceRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_work_detail_service, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WorkDetailServiceRecyclerViewAdapter.ViewHolder holder, int position) {
        ItemPrice itemPrice = this.itemPrices.get(position);
        try {
            holder.tv_service_name.setText(itemPrice.getName() + Conts.BLANK);
            holder.tv_single_price.setText(CommonMethod.formatCurrency((long)itemPrice.getPrice()));
            holder.tv_volume.setText(itemPrice.getQuantity() + Conts.BLANK);
            holder.tv_service_price.setText(CommonMethod.formatCurrency((long)(itemPrice.getPrice() * itemPrice.getQuantity())));
        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder, Exception: " + e);
        }
    }

    @Override
    public int getItemCount() {
        return itemPrices.size();
    }

    public void updateServiceList(List<ItemPrice> itemPrices) {
        if (itemPrices != null && !itemPrices.isEmpty()) {
            this.itemPrices = itemPrices;
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_service_name;
        private TextView tv_single_price;
        private TextView tv_volume;
        private TextView tv_service_price;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_service_name = itemView.findViewById(R.id.tv_service_name);
            tv_single_price = itemView.findViewById(R.id.tv_single_price);
            tv_volume = itemView.findViewById(R.id.tv_volume);
            tv_service_price = itemView.findViewById(R.id.tv_service_price);
        }
    }
}
