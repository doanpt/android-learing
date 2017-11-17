package com.cnc.hcm.cnctracking.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.event.OnResultTimeDistance;
import com.cnc.hcm.cnctracking.model.ItemWork;
import com.cnc.hcm.cnctracking.util.CommonMethod;
import com.cnc.hcm.cnctracking.util.Conts;

import java.util.ArrayList;

/**
 * Created by giapmn on 11/9/17.
 */

public class WorkDoingAdapter extends RecyclerView.Adapter<WorkDoingAdapter.ViewHolderDoingTask> implements OnResultTimeDistance {

    private static final int NOTIDATA_SET_CHANGE = 23;
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<ItemWork> arrTaskDoing = new ArrayList<>();
    private int resource;

    private OnClickButtonItemDoingComleteWorkListener onClickButtonItemDoingComleteWorkListener;
    private double latitude;
    private double longitude;

    public WorkDoingAdapter(Context context, ArrayList<ItemWork> arrTaskDoing, int resource) {
        this.context = context;
        this.resource = resource;
        this.arrTaskDoing = arrTaskDoing;
        inflater = LayoutInflater.from(context);
        CommonMethod.jsonRequest(longitude, latitude, CommonMethod.getDestination(arrTaskDoing), this);
    }

    @Override
    public ViewHolderDoingTask onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(resource, parent, false);

        return new ViewHolderDoingTask(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderDoingTask holder, int position) {

        ItemWork work = arrTaskDoing.get(position);
//        String schedule[] = work.getAppointmentDate().split(new String(","));

        if (!work.isExpand()) {
            holder.llContentExpand.setVisibility(View.GONE);
            holder.imvExpand.setImageResource(R.drawable.ic_expand);
        } else {
            holder.llContentExpand.setVisibility(View.VISIBLE);
            holder.imvExpand.setImageResource(R.drawable.ic_expand_less);
        }

        holder.tvTimeGetWork.setText(work.getTimeGetWork());
        holder.tvTitleWork.setText(work.getTitleWork());
        holder.tvDistance.setText(work.getDistanceToMyLocation());
        holder.tvTimeGoToMyLocation.setText(work.getTimeGoToMyLocation());

        holder.tvScheduleHours.setText(work.getAppointmentDate());
        holder.tvScheduleDate.setText(work.getAppointmentDate());
        if (work.isStatusPayment()) {
            holder.tvStatusPayment.setText(context.getResources().getString(R.string.status_payment_completed));
        } else {
            holder.tvStatusPayment.setText(context.getResources().getString(R.string.status_payment_not_completed));
        }

        if (work.isStatusWork()) {
            holder.tvStatusWork.setText(context.getResources().getString(R.string.status_work_completed));
        } else {
            holder.tvStatusWork.setText(context.getResources().getString(R.string.status_work_not_completed));
        }

    }

    @Override
    public int getItemCount() {
        return arrTaskDoing.size();
    }

    public ItemWork getItem(int position) {
        return arrTaskDoing.get(position);
    }

    @Override
    public void editData(int index, String distance, String duration) {
        arrTaskDoing.get(index).setDistanceToMyLocation(distance);
        arrTaskDoing.get(index).setTimeGoToMyLocation(duration);
    }

    @Override
    public void postToHandle() {
        handler.post(runableNotiDataSetChange);
    }

    public void updateDistanceDoingWork(double latitude, double longitude) {
        if (latitude != 0.0 || longitude != 0.0) {
            this.latitude = latitude;
            this.longitude = longitude;
            CommonMethod.jsonRequest(latitude, longitude, CommonMethod.getDestination(arrTaskDoing), WorkDoingAdapter.this);
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


    public class ViewHolderDoingTask extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvTimeGetWork;
        private TextView tvTitleWork;
        private TextView tvDistance;
        private TextView tvTimeGoToMyLocation;
        private TextView tvScheduleHours;
        private TextView tvScheduleDate;
        private TextView tvStatusPayment;
        private TextView tvStatusWork;
        private TextView tvMoreDetail;

        private ImageView imvExpand;
        private LinearLayout llCall;
        private LinearLayout llSms;
        private LinearLayout llAddress;
        private RelativeLayout rlHeader;
        private LinearLayout llContentExpand;

        public ViewHolderDoingTask(View view) {
            super(view);
            tvTimeGetWork = (TextView) itemView.findViewById(R.id.tv_time_get_work);
            tvTitleWork = (TextView) itemView.findViewById(R.id.tv_title_work);
            tvDistance = (TextView) itemView.findViewById(R.id.tv_distance);
            tvTimeGoToMyLocation = (TextView) itemView.findViewById(R.id.tv_time_go_to_my_location);
            tvScheduleHours = (TextView) itemView.findViewById(R.id.tv_schedule_hour);
            tvScheduleDate = (TextView) itemView.findViewById(R.id.tv_schedule_date);
            tvStatusPayment = (TextView) itemView.findViewById(R.id.tv_status_payment);
            tvStatusWork = (TextView) itemView.findViewById(R.id.tv_status_work);
            tvMoreDetail = (TextView) itemView.findViewById(R.id.tv_more_detail);
            tvMoreDetail.setOnClickListener(this);

            imvExpand = (ImageView) itemView.findViewById(R.id.imv_expand);
            imvExpand.setOnClickListener(this);

            llCall = (LinearLayout) itemView.findViewById(R.id.ll_call);
            llSms = (LinearLayout) itemView.findViewById(R.id.ll_sms);
            llAddress = (LinearLayout) itemView.findViewById(R.id.ll_location);
            llCall.setOnClickListener(this);
            llSms.setOnClickListener(this);
            llAddress.setOnClickListener(this);

            rlHeader = (RelativeLayout) itemView.findViewById(R.id.header);
            rlHeader.setOnClickListener(this);

            llContentExpand = (LinearLayout) itemView.findViewById(R.id.content);


        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_more_detail:
                    if (onClickButtonItemDoingComleteWorkListener != null) {
                        onClickButtonItemDoingComleteWorkListener.onClickButtonMoreDetail(getAdapterPosition());
                    }
                    break;
                case R.id.ll_call:
                    if (onClickButtonItemDoingComleteWorkListener != null) {
                        onClickButtonItemDoingComleteWorkListener.onClickButtonCall(getAdapterPosition());
                    }
                    break;
                case R.id.ll_sms:
                    if (onClickButtonItemDoingComleteWorkListener != null) {
                        onClickButtonItemDoingComleteWorkListener.onClickButtonSMS(getAdapterPosition());
                    }
                    break;
                case R.id.ll_location:
                    if (onClickButtonItemDoingComleteWorkListener != null) {
                        onClickButtonItemDoingComleteWorkListener.onClickButtonAddress(getAdapterPosition());
                    }
                    break;
                case R.id.header:
                case R.id.imv_expand:
                    boolean isExpand = arrTaskDoing.get(getAdapterPosition()).isExpand();
                    if (isExpand) {
                        arrTaskDoing.get(getAdapterPosition()).setExpand(false);
                    } else {
                        arrTaskDoing.get(getAdapterPosition()).setExpand(true);
                    }
                    notifyDataSetChanged();
                    break;
            }
        }
    }

    public interface OnClickButtonItemDoingComleteWorkListener {
        void onClickButtonCall(int position);

        void onClickButtonSMS(int position);

        void onClickButtonAddress(int position);

        void onClickButtonMoreDetail(int position);
    }

    public void setOnClickButtonItemDoingComleteWorkListener(OnClickButtonItemDoingComleteWorkListener onClickButtonItemDoingComleteWorkListener) {
        this.onClickButtonItemDoingComleteWorkListener = onClickButtonItemDoingComleteWorkListener;
    }

    public ArrayList<ItemWork> getArrTaskDoing() {
        return arrTaskDoing;
    }

    public void setArrTaskDoing(ArrayList<ItemWork> arrTaskDoing) {
        this.arrTaskDoing = arrTaskDoing;
    }
}
