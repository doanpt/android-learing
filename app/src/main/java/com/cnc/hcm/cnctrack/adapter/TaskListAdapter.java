package com.cnc.hcm.cnctrack.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnc.hcm.cnctrack.R;
import com.cnc.hcm.cnctrack.model.ItemTask;
import com.cnc.hcm.cnctrack.model.common.TaskListResult;
import com.cnc.hcm.cnctrack.util.CommonMethod;
import com.cnc.hcm.cnctrack.util.Conts;
import com.cnc.hcm.cnctrack.util.SettingApp;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by giapmn on 12/21/17.
 */

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private static final int NOTIDATA_SET_CHANGE = 32;
    private static final String TAGG = TaskListAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<ItemTask> arrTask = new ArrayList<>();
    private ArrayList<ItemTask> arrTaskTemp = new ArrayList<>();

    private OnItemWorkClickListener onItemWorkClickListener;


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
        TaskListResult result = itemTask.getTaskResult();
        holder.tvTitleWork.setText(result.title);
        if (itemTask.getTaskResult().address != null) {
            holder.tvAddressWork.setText(result.address.getStreet());
        } else {
            holder.tvAddressWork.setText(result.customer.address.getStreet());
        }
        long idTypeWork = result.status.getId();
        if (idTypeWork == Conts.TYPE_DOING_TASK) {
            holder.imvNotiTypeWork.setImageResource(R.drawable.ic_status_task_doing);
            holder.imvAlarmAppointment.setVisibility(View.INVISIBLE);
        } else if (idTypeWork == Conts.TYPE_COMPLETE_TASK) {
            holder.imvNotiTypeWork.setImageResource(R.drawable.ic_status_task_done);
            holder.imvAlarmAppointment.setVisibility(View.INVISIBLE);
        } else if (idTypeWork == Conts.TYPE_CANCEL_TASK) {
            holder.imvNotiTypeWork.setImageResource(R.drawable.ic_status_task_cancel);
            holder.imvAlarmAppointment.setVisibility(View.INVISIBLE);
        } else if (idTypeWork == Conts.TYPE_NEW_TASK) {
            holder.imvNotiTypeWork.setImageResource(R.drawable.ic_status_task_new);

            String appointmentDate = itemTask.getTaskResult().appointmentDate;

            Date currentDate = CommonMethod.getInstanceCalendar().getTime();
            Date dateBefor30Minute = CommonMethod.formatDateAppointmentDateBeforThirtyMinute(appointmentDate);
            Date dateAppointment = CommonMethod.formatTimeFromServerToDate(appointmentDate);

            if (currentDate.compareTo(dateBefor30Minute) >= 0) {
                holder.imvAlarmAppointment.setVisibility(View.VISIBLE);
                if (currentDate.compareTo(dateAppointment) < 0) {
                    holder.imvAlarmAppointment.setColorFilter(Color.parseColor("#FFC107"));
                } else {
                    holder.imvAlarmAppointment.setColorFilter(context.getResources().getColor(android.R.color.holo_red_light));
                }
            } else {
                holder.imvAlarmAppointment.setVisibility(View.INVISIBLE);
            }
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

        String appointmentDate = itemTask.getTaskResult().appointmentDate;
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private LinearLayout llOnClickItem;
        private ImageView imvNotiTypeWork, imvClock, imvAlarmAppointment;
        private TextView tvTitleWork, tvAddressWork, tvTime;

        public ViewHolder(View view) {
            super(view);
            llOnClickItem = (LinearLayout) itemView.findViewById(R.id.ll_on_click_item);
            llOnClickItem.setOnClickListener(this);
            imvNotiTypeWork = (ImageView) itemView.findViewById(R.id.imv_noti_type_work);
            tvTitleWork = (TextView) itemView.findViewById(R.id.tv_title_item_work);
            tvAddressWork = (TextView) itemView.findViewById(R.id.tv_address_item_work);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time_item_work);
            imvClock = itemView.findViewById(R.id.imv_clock_item_work);
            imvAlarmAppointment = itemView.findViewById(R.id.imv_alarm_appointment);
//            tvDistance = (TextView) itemView.findViewById(R.id.tv_distance_item_work);
        }

        @Override
        public void onClick(View view) {
            if (onItemWorkClickListener != null) {
                int position = getAdapterPosition();
                if (arrTask.get(position).getTaskResult().status.getId() != Conts.TYPE_CANCEL_TASK) {
                    onItemWorkClickListener.onClickItemWork(position);
                } else {
                    onItemWorkClickListener.onClickItemWork(Conts.DEFAULT_VALUE_INT_INVALID);
                }
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
                if (arrTaskTemp.get(index).getTaskResult().status.getId() == type) {
                    arrTask.add(arrTaskTemp.get(index));
                }
            }
        }
        notifyDataSetChanged();
    }

}
