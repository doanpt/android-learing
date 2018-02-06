package com.cnc.hcm.cnctracking.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.event.OnResultTimeDistance;
import com.cnc.hcm.cnctracking.model.GetTaskDetailResult;
import com.cnc.hcm.cnctracking.model.GetTaskListResult;
import com.cnc.hcm.cnctracking.model.ItemTask;
import com.cnc.hcm.cnctracking.util.CommonMethod;
import com.cnc.hcm.cnctracking.util.Conts;
import com.cnc.hcm.cnctracking.util.SettingApp;

import java.util.ArrayList;

/**
 * Created by giapmn on 12/21/17.
 */

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> implements OnResultTimeDistance {

    private static final int NOTIDATA_SET_CHANGE = 32;
    private static final String TAGG = TaskListAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<ItemTask> arrTask = new ArrayList<>();
    private ArrayList<ItemTask> arrTaskTemp = new ArrayList<>();

    private OnItemWorkClickListener onItemWorkClickListener;
    private double latitude;
    private double longitude;


    public TaskListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_work, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemTask itemTask = arrTask.get(position);
        GetTaskListResult.Result result = itemTask.getTaskResult();
        holder.tvTitleWork.setText(result.title);
        if (itemTask.getTaskResult().address != null) {
            holder.tvAddressWork.setText(result.address.street);
        } else {
            holder.tvAddressWork.setText(result.customer.address.street);
        }
        long idTypeWork = result.status._id;
        if (idTypeWork == Conts.TYPE_DOING_TASK) {
            holder.imvNotiTypeWork.setImageResource(R.drawable.ic_status_task_doing);
        } else if (idTypeWork == Conts.TYPE_COMPLETE_TASK) {
            holder.imvNotiTypeWork.setImageResource(R.drawable.ic_status_task_done);
        } else if (idTypeWork == Conts.TYPE_NEW_TASK) {
            holder.imvNotiTypeWork.setImageResource(R.drawable.ic_status_task_new);
        }

        boolean isRead = result.isRead;
        if (!isRead) {
            holder.tvTitleWork.setTypeface(holder.tvTitleWork.getTypeface(), Typeface.BOLD);
            holder.tvAddressWork.setTypeface(holder.tvAddressWork.getTypeface(), Typeface.BOLD);
        } else {
            holder.tvTitleWork.setTypeface(holder.tvTitleWork.getTypeface(), Typeface.NORMAL);
            holder.tvAddressWork.setTypeface(holder.tvAddressWork.getTypeface(), Typeface.NORMAL);
        }
        //K can thiet nua
        //holder.tvDistance.setText(itemTask.getDistanceToMyLocation());

        String appointmentDate = itemTask.getTaskResult().appointmentDate.substring(0, itemTask.getTaskResult().appointmentDate.lastIndexOf(".")) + "Z";
        String time = CommonMethod.formatTimeFromServerToString(appointmentDate);
        holder.tvTime.setText(time);


    }

    @Override
    public int getItemCount() {
        return arrTask.size();
    }

    public ItemTask getItem(int position) {
        return arrTask.get(position);
    }

    public void addItem(ItemTask itemTask) {
        arrTask.add(itemTask);
        notifyDataSetChanged();
    }

    public void notiDataChange(ArrayList<ItemTask> itemTasks) {
        this.arrTask.clear();
        this.arrTask.addAll(itemTasks);
        arrTaskTemp.clear();
        this.arrTaskTemp.addAll(itemTasks);
        notifyDataSetChanged();
        filter(SettingApp.getInstance(context).getTypeFilterList());
    }

    public void updateDistanceForTask(boolean isNetworkConnected, double latitude, double longitude) {
        if (latitude != 0 && longitude != 0) {
            this.latitude = latitude;
            this.longitude = longitude;
//            CommonMethod.jsonRequestUpdateDistance(isNetworkConnected, latitude, longitude, getDestination(), TaskListAdapter.this);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NOTIDATA_SET_CHANGE:
                    notifyDataSetChanged();
                    break;
            }
        }
    };
    private Runnable runableNotiDataSetChange = new Runnable() {
        @Override
        public void run() {

            Message message = new Message();
            message.what = NOTIDATA_SET_CHANGE;
            message.setTarget(handler);
            message.sendToTarget();
        }
    };


    private String getDestination() {
        String result = Conts.BLANK;
        if (arrTask.size() > Conts.DEFAULT_VALUE_INT_0) {
            StringBuilder builder = new StringBuilder();
            for (ItemTask itemWork : arrTask) {
                builder.append(itemWork.getTaskResult().customer.address.location.latitude + "," + itemWork.getTaskResult().customer.address.location.longitude + "|");
            }
            result = builder.toString().substring(Conts.DEFAULT_VALUE_INT_0, builder.toString().lastIndexOf("|"));
        }
        return result;
    }

    @Override
    public void editData(int index, String distance, String duration) {
        if (arrTask != null && arrTask.size() > 0) {
            arrTask.get(index).setDistanceToMyLocation(distance);
            arrTask.get(index).setTimeGoToMyLocation(duration);
        }
    }

    @Override
    public void postToHandle() {
        handler.post(runableNotiDataSetChange);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private LinearLayout llOnClickItem;
        private ImageView imvNotiTypeWork;
        private TextView tvTitleWork, tvAddressWork, tvTime;

        public ViewHolder(View view) {
            super(view);
            llOnClickItem = (LinearLayout) itemView.findViewById(R.id.ll_on_click_item);
            llOnClickItem.setOnClickListener(this);
            imvNotiTypeWork = (ImageView) itemView.findViewById(R.id.imv_noti_type_work);
            tvTitleWork = (TextView) itemView.findViewById(R.id.tv_title_item_work);
            tvAddressWork = (TextView) itemView.findViewById(R.id.tv_address_item_work);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time_item_work);
//            tvDistance = (TextView) itemView.findViewById(R.id.tv_distance_item_work);
        }

        @Override
        public void onClick(View view) {
            if (onItemWorkClickListener != null) {
                onItemWorkClickListener.onClickItemWork(getAdapterPosition());
            }
        }
    }

    public interface OnItemWorkClickListener {
        void onClickItemWork(int position);
    }

    public void setOnItemWorkClickListener(OnItemWorkClickListener onItemWorkClickListener) {
        this.onItemWorkClickListener = onItemWorkClickListener;
    }


    public void filter(int type) {
        arrTask.clear();
        if (type == Conts.TYPE_ALL_TASK) {
            arrTask.addAll(arrTaskTemp);
        } else {
            for (int index = 0; index < arrTaskTemp.size(); index++) {
                if (arrTaskTemp.get(index).getTaskResult().status._id == type) {
                    arrTask.add(arrTaskTemp.get(index));
                }
            }
        }
        notifyDataSetChanged();
    }

}
