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

import java.util.ArrayList;
import java.util.List;

public class WorkDetailServiceRecyclerViewAdapter extends RecyclerView.Adapter<WorkDetailServiceRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = WorkDetailServiceRecyclerViewAdapter.class.getSimpleName();

    private List<GetTaskDetailResult.Result.Service> services = new ArrayList<>();

    private Context context;

    public WorkDetailServiceRecyclerViewAdapter(Context context) {
        this.context = context;

        initProcesses();
    }

    public List<GetTaskDetailResult.Result.Service> getServices() {
        return services;
    }

    private void initProcesses() {
        services.add(new GetTaskDetailResult.Result.Service("123", "Sửa tủ lạnh", "50000", null, 2, null, 2));
        services.add(new GetTaskDetailResult.Result.Service("345", "Mát xa", "150000", null, 3, null, 2));
    }

    @Override
    public WorkDetailServiceRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_work_detail_service, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WorkDetailServiceRecyclerViewAdapter.ViewHolder holder, int position) {
        GetTaskDetailResult.Result.Service service = this.services.get(position);
        try {
            holder.tv_service_name.setText("" + service.name);
            holder.tv_single_price.setText("" + service.price);
            holder.tv_volume.setText("" + service.__v);
            long servicePrice = Integer.parseInt(service.price) * service.__v;
            holder.tv_service_price.setText("" + servicePrice);
        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder, Exception: " + e);
        }
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public void updateDeviceList(List<GetTaskDetailResult.Result.Service> services) {
        if (services != null && !services.isEmpty()) {
            this.services = services;
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
