package com.cnc.hcm.cnctracking.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.model.GetTaskDetailResult;
import com.cnc.hcm.cnctracking.util.Conts;

import java.util.ArrayList;
import java.util.List;

public class WorkDetailRequireRecyclerViewAdapter extends RecyclerView.Adapter<WorkDetailRequireRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = WorkDetailRequireRecyclerViewAdapter.class.getSimpleName();

    private List<GetTaskDetailResult.Result.RecommendedServices> itemRecommendedServoces = new ArrayList<>();

    private Context context;

    public WorkDetailRequireRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public List<GetTaskDetailResult.Result.RecommendedServices> getItemRecommendedServoces() {
        return itemRecommendedServoces;
    }

    @Override
    public WorkDetailRequireRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_work_detail_require, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WorkDetailRequireRecyclerViewAdapter.ViewHolder holder, int position) {
        GetTaskDetailResult.Result.RecommendedServices recommendedServices = this.itemRecommendedServoces.get(position);
        try {
            holder.tv_service_name.setText(recommendedServices.service.name + Conts.BLANK);
            holder.tv_device_name.setText(recommendedServices.device + Conts.BLANK);
            holder.tv_number_device.setText(recommendedServices.quantity + Conts.BLANK);
        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder, Exception: " + e);
        }
    }

    @Override
    public int getItemCount() {
        return itemRecommendedServoces.size();
    }

    public void updateRequireList(List<GetTaskDetailResult.Result.RecommendedServices> itemPrices) {
        if (itemPrices != null) {
            this.itemRecommendedServoces = itemPrices;
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_service_name;
        private TextView tv_device_name;
        private TextView tv_number_device;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_service_name = itemView.findViewById(R.id.tv_service_name);
            tv_device_name = itemView.findViewById(R.id.tv_device_name);
            tv_number_device = itemView.findViewById(R.id.tv_number_device);
        }
    }
}
