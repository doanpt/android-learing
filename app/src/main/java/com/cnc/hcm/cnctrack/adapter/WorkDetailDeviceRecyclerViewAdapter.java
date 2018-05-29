package com.cnc.hcm.cnctrack.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnc.hcm.cnctrack.R;
import com.cnc.hcm.cnctrack.event.RecyclerViewItemClickListener;
import com.cnc.hcm.cnctrack.event.RecyclerViewMenuItemClickListener;
import com.cnc.hcm.cnctrack.model.GetTaskDetailResult;
import com.cnc.hcm.cnctrack.util.Conts;
import com.cnc.hcm.cnctrack.util.UserInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class WorkDetailDeviceRecyclerViewAdapter extends RecyclerView.Adapter<WorkDetailDeviceRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = WorkDetailDeviceRecyclerViewAdapter.class.getSimpleName();

    private final List<GetTaskDetailResult.Result.Process> processes;

    private final RecyclerViewItemClickListener mListener;

    private final RecyclerViewMenuItemClickListener mMenuItemClickListener;

    private final Context mContext;

    public WorkDetailDeviceRecyclerViewAdapter(RecyclerViewItemClickListener listener, RecyclerViewMenuItemClickListener menuItemClickListener, Context context) {
        mListener = listener;
        mMenuItemClickListener = menuItemClickListener;
        mContext = context;
        processes = new ArrayList<>();
    }

    public List<GetTaskDetailResult.Result.Process> getProcesses() {
        return processes;
    }

    @Override
    public WorkDetailDeviceRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_work_detail_device, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final WorkDetailDeviceRecyclerViewAdapter.ViewHolder holder, final int position) {
        final GetTaskDetailResult.Result.Process process = processes.get(position);
        try {
            holder.tv_title.setText(process.device.detail.name + "");
            holder.tv_status.setText("Hoàn thành bước " + process.status._id);
            String iconPath = process.device.detail.photo;
            if (TextUtils.isEmpty(iconPath)) {
                iconPath = process.device.detail.brand.photo;
            }
            if (!TextUtils.isEmpty(iconPath)) {
                Picasso.with(mContext).load(Conts.URL_BASE + iconPath).into(holder.iv_icon);
            }

            final boolean isOwner = TextUtils.equals(UserInfo.getInstance(mContext).getUserId(), process.user._id);
            if (isOwner) {
                holder.iv_status.setImageResource(process.status._id == 1 ? R.drawable.ic_step_1_complete
                        : (process.status._id == 2 ? R.drawable.ic_step_2_complete : R.drawable.ic_step_3_complete));
                holder.item_product.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onClick(v, holder.getAdapterPosition());
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

            holder.tv_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isOwner && (process.before == null || process.before.photos == null || process.before.photos.length == 0)) {
                        PopupMenu popup = new PopupMenu(mContext, view);
                        popup.getMenuInflater().inflate(R.menu.task_device_popup_menu, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                Log.e(TAG, "tv_more.setOnMenuItemClickListener()");
                                mMenuItemClickListener.onRecyclerViewMenuItemClicked(position, menuItem);
                                return true;
                            }
                        });
                        popup.show();
                    } else {
                        mMenuItemClickListener.onRecyclerViewMenuItemClickedFailue(!isOwner ? "Không thể chọn xóa do thiết bị này do nhân viên khác thực hiện!"
                                : "Không thể chọn xóa do thiết bị đang trong quá trình sửa chữa!");
                    }
                }
            });
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
            this.processes.clear();
            this.processes.addAll(processes);
            notifyDataSetChanged();
        }
    }

    public void removeDeviceAtPosition(int position) {
        Log.e(TAG, "removeDeviceAtPosition(), position: " + position);
        try {
            if (position >= 0 && position < processes.size())
            this.processes.remove(position);
            notifyDataSetChanged();
        } catch (Exception e) {
            Log.e(TAG, "removeDeviceAtPosition() -> Exception: ", e);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout item_product;
        private ImageView iv_icon;
        private ImageView iv_status;
        private CircleImageView iv_user_added_device;
        private TextView tv_title;
        private TextView tv_status;
        private TextView tv_more;

        public ViewHolder(View itemView) {
            super(itemView);
            item_product = itemView.findViewById(R.id.item_product);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            iv_status = itemView.findViewById(R.id.iv_status);
            iv_user_added_device = itemView.findViewById(R.id.iv_user_added_device);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_more = itemView.findViewById(R.id.tv_more);
        }
    }
}
