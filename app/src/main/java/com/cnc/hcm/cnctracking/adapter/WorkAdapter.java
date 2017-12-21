package com.cnc.hcm.cnctracking.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.model.GetTaskListResult;
import com.cnc.hcm.cnctracking.util.Conts;

import java.util.ArrayList;

/**
 * Created by giapmn on 12/21/17.
 */

public class WorkAdapter extends RecyclerView.Adapter<WorkAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GetTaskListResult.Result> arrTask = new ArrayList<>();

    private OnItemWorkClickListener onItemWorkClickListener;


    public WorkAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_work, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GetTaskListResult.Result result = arrTask.get(position);
        holder.tvTitleWork.setText(result.title);
        holder.tvAddressWork.setText(result.customer.address.street);

        long idTypeWork = result.status._id;
        if (idTypeWork == Conts.TYPE_DOING_TASK) {
            holder.imvNotiTypeWork.setImageResource(R.drawable.ic_noti_work_doing);
        } else if (idTypeWork == Conts.TYPE_COMPLETE_TASK) {
            holder.imvNotiTypeWork.setImageResource(R.drawable.ic_noti_work_complete);
        }
    }

    @Override
    public int getItemCount() {
        return arrTask.size();
    }

    public void notiDataChange(ArrayList<GetTaskListResult.Result> resultArrayList) {
        this.arrTask.clear();
        this.arrTask.addAll(resultArrayList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private LinearLayout llOnClickItem;
        private ImageView imvNotiTypeWork;
        private TextView tvTitleWork, tvAddressWork, tvTime, tvDistance;

        public ViewHolder(View view) {
            super(view);
            llOnClickItem = (LinearLayout) itemView.findViewById(R.id.ll_on_click_item);
            llOnClickItem.setOnClickListener(this);
            imvNotiTypeWork = (ImageView) itemView.findViewById(R.id.imv_noti_type_work);
            tvTitleWork = (TextView) itemView.findViewById(R.id.tv_title_item_work);
            tvAddressWork = (TextView) itemView.findViewById(R.id.tv_address_item_work);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time_item_work);
            tvDistance = (TextView) itemView.findViewById(R.id.tv_distance_item_work);
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
}
