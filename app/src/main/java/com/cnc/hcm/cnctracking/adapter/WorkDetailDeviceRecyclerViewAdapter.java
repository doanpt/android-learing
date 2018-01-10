package com.cnc.hcm.cnctracking.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.model.GetTaskDetailResult;

import java.util.ArrayList;
import java.util.List;

public class WorkDetailDeviceRecyclerViewAdapter extends RecyclerView.Adapter<WorkDetailDeviceRecyclerViewAdapter.ViewHolder> {

    private List<GetTaskDetailResult.Result.Process> processes = new ArrayList<>();

    private Context context;

    public WorkDetailDeviceRecyclerViewAdapter(Context context) {
        this.context = context;

        initProcesses();
    }

    private void initProcesses() {
//        processes.add(new GetTaskDetailResult.Result.Process("Device1", null, null, "1", null, null, null));
//        processes.add(new GetTaskDetailResult.Result.Process("Device2", null, null, "3", null, null, null));
//        processes.add(new GetTaskDetailResult.Result.Process("Device3", null, null, "1", null, null, null));
//        processes.add(new GetTaskDetailResult.Result.Process("Device4", null, null, "2", null, null, null));
    }

    @Override
    public WorkDetailDeviceRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_work_detail_device, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WorkDetailDeviceRecyclerViewAdapter.ViewHolder holder, int position) {
        GetTaskDetailResult.Result.Process process = processes.get(position);
        holder.tv_title.setText(process.device + "");
        holder.tv_status.setText("Hoàn thành bước " + process.status);
        holder.iv_status.setImageResource("1".equals(process.status) ? R.drawable.step_1_complete : ("2".equals(process.status) ? R.drawable.step_2_complete : R.drawable.step_3_complete));
    }

    @Override
    public int getItemCount() {
        return processes.size();
    }

    public void updateDeviceList(List<GetTaskDetailResult.Result.Process> processes) {
        if (processes != null && !processes.isEmpty()) {
            this.processes = processes;
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_icon;
        private ImageView iv_status;
        private ImageView iv_history;
        private ImageView iv_fix;
        private TextView tv_title;
        private TextView tv_status;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            iv_status = itemView.findViewById(R.id.iv_status);
            iv_history = itemView.findViewById(R.id.iv_history);
            iv_fix = itemView.findViewById(R.id.iv_fix);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_status = itemView.findViewById(R.id.tv_status);
        }
    }
}
