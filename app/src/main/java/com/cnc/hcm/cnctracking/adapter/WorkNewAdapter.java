package com.cnc.hcm.cnctracking.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
 * Created by giapmn on 11/7/17.
 */

public class WorkNewAdapter extends RecyclerView.Adapter<WorkNewAdapter.ViewHolder> implements OnResultTimeDistance {

    private static final int NOTIDATA_SET_CHANGE = 111;
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<ItemWork> arrItemWorkNew = new ArrayList<>();
    private OnClickButtonItemNewTaskListener onClickButtonItemNewTaskListener;

    private double latitude;
    private double longitude;


    public WorkNewAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        //initData();
        CommonMethod.jsonRequest(latitude, longitude, getDestination(), this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_new_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemWork itemWork = arrItemWorkNew.get(position);
        if (!itemWork.isExpand()) {
            holder.llContentExpand.setVisibility(View.GONE);
            holder.imvExpand.setImageResource(R.drawable.ic_expand);
        } else {
            holder.llContentExpand.setVisibility(View.VISIBLE);
            holder.imvExpand.setImageResource(R.drawable.ic_expand_less);
        }

        holder.tvTimeGetWorl.setText(itemWork.getTimeGetWork());
        holder.tvTitleWork.setText(itemWork.getTitleWork());
        holder.tvDistance.setText(itemWork.getDistanceToMyLocation());
        holder.tvTimeGoToMyLocation.setText(itemWork.getTimeGoToMyLocation());
        holder.tvContact.setText(itemWork.getContact());
        holder.tvPhoneNo.setText(itemWork.getPhoneNo());
        holder.tvAddress.setText(itemWork.getAddress());
        holder.tvService.setText(itemWork.getRequestService());
        holder.tvPrice.setText(itemWork.getPrice());
        holder.tvNoteService.setText(itemWork.getNoteService());


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
        if (arrItemWorkNew.size() > Conts.DEFAULT_VALUE_INT_0) {
            StringBuilder builder = new StringBuilder();
            for (ItemWork itemWork : arrItemWorkNew) {
                builder.append(itemWork.getLatitude() + "," + itemWork.getLongitude() + "|");
            }
            result = builder.toString().substring(Conts.DEFAULT_VALUE_INT_0, builder.toString().lastIndexOf("|"));
        }
        return result;
    }

    @Override
    public int getItemCount() {
        return arrItemWorkNew.size();
    }

    public ItemWork getItem(int position) {
        return arrItemWorkNew.get(position);
    }

    public void updateDistance(double latitude, double longitude) {
        if (latitude != 0.0 || longitude != 0.0) {
            this.latitude = latitude;
            this.longitude = longitude;
            CommonMethod.jsonRequest(latitude, longitude, getDestination(), WorkNewAdapter.this);
        }
    }

    public void addItem(ItemWork itemWork) {
        arrItemWorkNew.add(itemWork);
        notifyDataSetChanged();
        Log.d("addItem", "Size: " + arrItemWorkNew.size());
    }

    @Override
    public void editData(int index, String distance, String duration) {
        arrItemWorkNew.get(index).setDistanceToMyLocation(distance);
        arrItemWorkNew.get(index).setTimeGoToMyLocation(duration);
    }

    @Override
    public void postToHandle() {
        handler.post(runableNotiDataSetChange);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvTimeGetWorl;
        private TextView tvTitleWork;
        private TextView tvDistance;
        private TextView tvTimeGoToMyLocation;
        private TextView tvContact;
        private TextView tvPhoneNo;
        private TextView tvAddress;
        private TextView tvService;
        private TextView tvPrice;
        private TextView tvNoteService;
        private Button btnCancelWorl;
        private Button btnReceiveWorl;
        private ImageView imvExpand;

        private LinearLayout llContentExpand;
        private RelativeLayout rlHeader;


        public ViewHolder(View view) {
            super(view);
            llContentExpand = (LinearLayout) itemView.findViewById(R.id.content);
            tvTimeGetWorl = (TextView) itemView.findViewById(R.id.tv_time_get_work);
            tvTitleWork = (TextView) itemView.findViewById(R.id.tv_title_work);
            tvDistance = (TextView) itemView.findViewById(R.id.tv_distance);
            tvTimeGoToMyLocation = (TextView) itemView.findViewById(R.id.tv_time_go_to_my_location);
            tvContact = (TextView) itemView.findViewById(R.id.tv_contact);
            tvPhoneNo = (TextView) itemView.findViewById(R.id.tv_phone_no);
            tvAddress = (TextView) itemView.findViewById(R.id.tv_address);
            tvService = (TextView) itemView.findViewById(R.id.tv_service_name);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            tvNoteService = (TextView) itemView.findViewById(R.id.tv_note_service);

            btnCancelWorl = (Button) itemView.findViewById(R.id.btn_cancel_work);
            btnCancelWorl.setOnClickListener(this);
            btnReceiveWorl = (Button) itemView.findViewById(R.id.btn_receive_work);
            btnReceiveWorl.setOnClickListener(this);

            imvExpand = (ImageView) itemView.findViewById(R.id.imv_expand);
            imvExpand.setOnClickListener(this);

            rlHeader = (RelativeLayout) itemView.findViewById(R.id.header);
            rlHeader.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_cancel_work:
                    if (onClickButtonItemNewTaskListener != null) {
                        onClickButtonItemNewTaskListener.onClickButtonCancelTask(getAdapterPosition());
                    }
                    break;
                case R.id.btn_receive_work:
                    if (onClickButtonItemNewTaskListener != null) {
                        onClickButtonItemNewTaskListener.onClickButtonReceiveTask(getAdapterPosition());
                    }
                    break;
                case R.id.imv_expand:
                case R.id.header:
                    boolean isExpand = arrItemWorkNew.get(getAdapterPosition()).isExpand();
                    if (isExpand) {
                        arrItemWorkNew.get(getAdapterPosition()).setExpand(false);
                    } else {
                        arrItemWorkNew.get(getAdapterPosition()).setExpand(true);
                    }
                    notifyDataSetChanged();
                    break;
            }
        }

    }


    public interface OnClickButtonItemNewTaskListener {
        void onClickButtonCancelTask(int position);

        void onClickButtonReceiveTask(int position);
    }

    public void setOnClickButtonItemNewTaskListener(OnClickButtonItemNewTaskListener onClickButtonItemNewTaskListener) {
        this.onClickButtonItemNewTaskListener = onClickButtonItemNewTaskListener;
    }

    public void setArrItemWorkNew(ArrayList<ItemWork> arrItemWorkNew) {
        this.arrItemWorkNew = arrItemWorkNew;
        notifyDataSetChanged();
    }

    public ArrayList<ItemWork> getArrItemWorkNew() {
        return arrItemWorkNew;
    }

}
