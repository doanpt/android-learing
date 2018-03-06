package com.cnc.hcm.cnctracking.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.event.RecyclerViewItemClickListener;
import com.cnc.hcm.cnctracking.model.GetTaskDetailResult;
import com.cnc.hcm.cnctracking.util.Conts;
import com.cnc.hcm.cnctracking.util.UserInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class WorkDetailDeviceRecyclerViewAdapter extends RecyclerView.Adapter<WorkDetailDeviceRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = WorkDetailDeviceRecyclerViewAdapter.class.getSimpleName();

    private List<GetTaskDetailResult.Result.Process> processes = new ArrayList<>();

    public List<GetTaskDetailResult.Result.Process> getProcesses() {
        return processes;
    }

    private final RecyclerViewItemClickListener mListener;

    private final Context mContext;

    public WorkDetailDeviceRecyclerViewAdapter(RecyclerViewItemClickListener listener, Context context) {
        mListener = listener;
        mContext = context;
    }

    @Override
    public WorkDetailDeviceRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_work_detail_device, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WorkDetailDeviceRecyclerViewAdapter.ViewHolder holder, final int position) {
        final GetTaskDetailResult.Result.Process process = processes.get(position);
        try {
            holder.tv_title.setText(process.device.detail.name + "");
            holder.tv_status.setText("Hoàn thành bước " + process.status._id);
//            String iconPath = ""; // TODO Confirm with backend team
//            Picasso.with(mContext).load(Conts.URL_BASE + iconPath).into(holder.iv_icon);
            if (TextUtils.equals(UserInfo.getInstance(mContext).getUserId(), process.user._id)) {
                holder.iv_status.setImageResource(process.status._id == 1 ? R.drawable.step_1_complete : (process.status._id == 2 ? R.drawable.step_2_complete : R.drawable.step_3_complete));
                holder.item_product.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onClick(v, position);
                    }
                });
            } else {
                holder.iv_status.setImageResource(R.drawable.icon_cnc_lock);
                holder.item_product.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e(TAG, "You can't process this device. Owner of this device is " + process.user.fullname);
                    }
                });
                Log.e(TAG, "Photo: " + process.user.photo);
                if (!TextUtils.isEmpty(process.user.photo)) {
                    Picasso.with(mContext).load(Conts.URL_BASE + process.user.photo).into(holder.iv_user_added_device);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder()", e);
        }
    }

    @Override
    public int getItemCount() {
        return processes.size();
    }

    public void updateDeviceList(List<GetTaskDetailResult.Result.Process> processes) {
        if (processes != null) {
            this.processes = processes;
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout item_product;
        private ImageView iv_icon;
        private ImageView iv_status;
//        private ImageView iv_history;
        private CircleImageView iv_user_added_device;
        private TextView tv_title;
        private TextView tv_status;

        public ViewHolder(View itemView) {
            super(itemView);
            item_product = itemView.findViewById(R.id.item_product);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            iv_status = itemView.findViewById(R.id.iv_status);
//            iv_history = itemView.findViewById(R.id.iv_history);
            iv_user_added_device = itemView.findViewById(R.id.iv_user_added_device);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_status = itemView.findViewById(R.id.tv_status);
        }
    }
}
