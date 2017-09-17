package com.google.foods.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.foods.R;
import com.google.foods.customeview.DialogNotification;
import com.google.foods.dialog.DialogChangeStatusOrder;
import com.google.foods.fragment.OrderManagerFrament;
import com.google.foods.models.ItemOrder2;
import com.google.foods.utils.CommonMethod;
import com.google.foods.utils.CommonValue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sev_user on 08/10/2017.
 */

public class OrderManagerAdapter extends RecyclerView.Adapter<OrderManagerAdapter.ViewHolder> {

    private static final String TAGA = "OrderManagerAdapter";
    private ArrayList<ItemOrder2> arrOrder = new ArrayList<>();
    private ArrayList<ItemOrder2> arrFilterOrder = new ArrayList<>();
    private ArrayList<ItemOrder2> arrFilterStatusOrder = new ArrayList<>();
    private ArrayList<ItemOrder2> arrFilterByDate = new ArrayList<>();

    private Context context;

    private DatabaseReference mData;
    private OrderManagerFrament orderManagerFrament;
    private DialogChangeStatusOrder dialogChangeStatusOrder;
    private String dateOrder;

    private OnItemOrderManagerClickListener onItemOrderManagerClickListener;

    public OrderManagerAdapter(Context context) {
        this.context = context;
        dialogChangeStatusOrder = new DialogChangeStatusOrder(context, this);
        dateOrder = CommonMethod.getDateString(Calendar.getInstance().getTime());
        initData();

    }

    private void initData() {
        mData = FirebaseDatabase.getInstance().getReference();
        mData.child(CommonValue.DATABASE_TABLE_ORDER).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrOrder.clear();
                arrFilterByDate.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ItemOrder2 itemOrder = postSnapshot.getValue(ItemOrder2.class);
                    arrOrder.add(itemOrder);
                    notifyDataSetChanged();
                }
                arrFilterByDate.addAll(arrOrder);
                if (orderManagerFrament != null && arrOrder.size() > 0) {
                    orderManagerFrament.hideDumyLoadingData();
                    filterByDateOrder();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_mgr, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemOrder2 item = arrOrder.get(position);

        holder.tvSTT.setText((getItemCount() - position) + "");
        holder.tvUsername.setText(item.getUserName());
        holder.tvUSerPhoneNo.setText(item.getUserPhone());
        holder.tvUSerPhoneNo.setVisibility(View.GONE);
        holder.tvUserAddress.setText(item.getUserAddress());
        holder.tvTotalPrice.setText(CommonMethod.convertMoneyToVND(item.getTotalPrice()));
        holder.tvTotalPrice.setVisibility(View.GONE);
        String statusOrder = context.getResources().getString(R.string.status_not_handl);
        int color = context.getResources().getColor(android.R.color.holo_red_light);
        switch (item.getStatusOrder()) {
            case CommonValue.STATUS_ORDER_HANDLING:
                statusOrder = context.getResources().getString(R.string.status_handling);
                color = context.getResources().getColor(android.R.color.holo_orange_light);
                break;
            case CommonValue.STATUS_ORDER_HANDLED:
                statusOrder = context.getResources().getString(R.string.status_handled);
                color = context.getResources().getColor(android.R.color.holo_blue_light);
                break;
        }
        holder.tvStatusOrder.setText(statusOrder);
        holder.tvStatusOrder.setTextColor(color);
    }


    @Override
    public int getItemCount() {
        return arrOrder.size();
    }

    public void cancelOrder(final int position) {
        DialogNotification dialogNotification = new DialogNotification(context);
        dialogNotification.setHiddenBtnExit();
        if (arrOrder.get(position).getStatusOrder() == CommonValue.STATUS_ORDER_NOT_HANDL) {
            orderManagerFrament.updateTotalQuantityFood(arrOrder.get(position).getFoodList());
            mData.child(CommonValue.DATABASE_TABLE_CANCEL_ORDER).push().setValue(arrOrder.get(position));
            mData.child(CommonValue.DATABASE_TABLE_ORDER).child(arrOrder.get(position).getOrderId()).removeValue(new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError == null) {
                        Toast.makeText(context, context.getResources().getString(R.string.cancel_order_success), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.cancel_order_error), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else if (arrOrder.get(position).getStatusOrder() == CommonValue.STATUS_ORDER_HANDLED) {
            dialogNotification.setContentMessage(context.getResources().getString(R.string.order_not_cancel_handled));
            dialogNotification.show();
        } else if (arrOrder.get(position).getStatusOrder() == CommonValue.STATUS_ORDER_HANDLING) {
            dialogNotification.setContentMessage(context.getResources().getString(R.string.order_not_cancel_handling));
            dialogNotification.show();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvSTT;
        private TextView tvUsername;
        private TextView tvUSerPhoneNo;
        private TextView tvUserAddress;
        private TextView tvTotalPrice;
        private TextView tvStatusOrder;
        private LinearLayout llOnClick;


        public ViewHolder(View view) {
            super(view);
            tvSTT = (TextView) itemView.findViewById(R.id.itemorder_tv_stt);
            tvUsername = (TextView) itemView.findViewById(R.id.itemorder_tv_username);
            tvUSerPhoneNo = (TextView) itemView.findViewById(R.id.itemorder_tv_user_phone);
            tvUserAddress = (TextView) itemView.findViewById(R.id.itemorder_tv_user_address);
            tvTotalPrice = (TextView) itemView.findViewById(R.id.itemorder_tv_total_price);
            tvStatusOrder = (TextView) itemView.findViewById(R.id.itemorder_tv_status_order);
            tvStatusOrder.setOnClickListener(this);

            llOnClick = (LinearLayout) itemView.findViewById(R.id.place_holder);
            llOnClick.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.place_holder:
                    if (onItemOrderManagerClickListener != null) {
                        onItemOrderManagerClickListener.onClickItemOrder(getAdapterPosition());
                    }
                    break;
                case R.id.itemorder_tv_status_order:
                    dialogChangeStatusOrder.setPosition(getAdapterPosition());
                    dialogChangeStatusOrder.show();
                    break;
            }

        }
    }

    public void updateStatusOrder(int position, int value) {
        Map<String, Object> mapUpdate = new HashMap<String, Object>();
        mapUpdate.put(CommonValue.DATABASE_TABLE_ORDER_FIELD_STATUS_ORDER, value);
        mData.child(CommonValue.DATABASE_TABLE_ORDER).child(arrOrder.get(position).getOrderId()).updateChildren(mapUpdate);
    }

    public interface OnItemOrderManagerClickListener {
        void onClickItemOrder(int position);
    }

    public void setOnItemOrderManagerClickListener(OnItemOrderManagerClickListener onItemOrderManagerClickListener) {
        this.onItemOrderManagerClickListener = onItemOrderManagerClickListener;
    }

    public void filter(int deliveryType, int statusOrder) {

        if (deliveryType == CommonValue.DEFAULT_VALUE_INT_0) {
            arrFilterStatusOrder.clear();
            arrFilterStatusOrder.addAll(arrFilterOrder);
            arrOrder.clear();
            arrOrder.addAll(arrFilterOrder);
        } else {
            int size = arrFilterOrder.size();
            arrOrder.clear();
            arrFilterStatusOrder.clear();
            for (int index = 0; index < size; index++) {
                ItemOrder2 item = arrFilterOrder.get(index);
                if (item.getDeliveryType() == deliveryType) {
                    arrOrder.add(item);
                }
            }
            arrFilterStatusOrder.addAll(arrOrder);
        }

        filterStatusOrder(statusOrder);
        notifyDataSetChanged();
    }

    public void filterByDateOrder() {
        arrOrder.clear();
        for (ItemOrder2 item : arrFilterByDate) {
            if (CommonMethod.getDateString(item.getDateTime()).contains(dateOrder)) {
                arrOrder.add(item);
            }
        }

        setArrFilterOrder(arrOrder);
        filter(orderManagerFrament.getDeliveryType(), orderManagerFrament.getStatusOrder());
        orderManagerFrament.checkShowLayoutListEmpty(arrOrder);
        Collections.sort(arrOrder, Collections.<ItemOrder2>reverseOrder());
        notifyDataSetChanged();
    }

    public void filterStatusOrder(int statusOrder) {
        if (statusOrder == CommonValue.STATUS_ORDER_SHOW_ALL) {
            arrOrder.clear();
            arrOrder.addAll(arrFilterStatusOrder);
        } else {
            arrOrder.clear();
            for (int index = 0; index < arrFilterStatusOrder.size(); index++) {
                ItemOrder2 item = arrFilterStatusOrder.get(index);
                if (item.getStatusOrder() == statusOrder) {
                    arrOrder.add(item);
                }
            }
        }
    }

    public void setArrFilterOrder(ArrayList<ItemOrder2> arrFilter) {
        this.arrFilterOrder.clear();
        this.arrFilterStatusOrder.clear();
        this.arrFilterOrder.addAll(arrFilter);
        this.arrFilterStatusOrder.addAll(arrFilter);
    }

    public ArrayList<ItemOrder2> getArrOrder() {
        return arrOrder;
    }


    public void setOrderManagerFrament(OrderManagerFrament orderManagerFrament) {
        this.orderManagerFrament = orderManagerFrament;
    }

    public void setDateOrder(String dateOrder) {
        this.dateOrder = dateOrder;
    }
}
