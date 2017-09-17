package com.google.foods.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.foods.R;
import com.google.foods.adapter.DialogInforOrderAdapter;
import com.google.foods.fragment.OrderManagerFrament;
import com.google.foods.models.ItemFood;
import com.google.foods.models.ItemOrder2;
import com.google.foods.utils.CommonMethod;
import com.google.foods.utils.CommonValue;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by giapmn on 8/21/17.
 */

public class DialogInforOrder extends Dialog implements View.OnClickListener {

    private TextView tvUsername;
    private TextView tvUserPhoneNo;
    private TextView tvUserAddress;
    private TextView tvComboPrice;
    private TextView tvTotalPrice;
    private TextView tvPriceNeedPay;
    private TextView tvStatus;
    private TextView tvDateOrder;
    private RecyclerView recyclerViewListOrder;
    private Button btnCancelOrder;
    private Button btnPrint;
    private ArrayList<ItemFood> arrFood = new ArrayList<>();
    private DialogInforOrderAdapter adapter;
    private Context context;
    private OrderManagerFrament orderManagerFrament;

    public DialogInforOrder(@NonNull Context context) {
        super(context);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_infor_order);
        initView();
    }

    private void initView() {
        tvUsername = (TextView) findViewById(R.id.dialog_infor_order_tv_username);
        tvUserPhoneNo = (TextView) findViewById(R.id.dialog_infor_order_tv_user_phone_no);
        tvUserAddress = (TextView) findViewById(R.id.dialog_infor_order_tv_user_address);
        tvComboPrice = (TextView) findViewById(R.id.dialog_infor_order_tv_combo_price);
        tvTotalPrice = (TextView) findViewById(R.id.dialog_infor_order_tv_total_price);
        tvPriceNeedPay = (TextView) findViewById(R.id.dialog_infor_order_tv_price_need_pay);
        tvStatus = (TextView) findViewById(R.id.dialog_infor_order_tv_status_delivery);
        tvDateOrder = (TextView) findViewById(R.id.dialog_infor_order_tv_date_order);
        recyclerViewListOrder = (RecyclerView) findViewById(R.id.dialog_infor_order_recycleview_list_order);
        btnCancelOrder = (Button) findViewById(R.id.btn_cancel_order);
        btnPrint = (Button) findViewById(R.id.btn_print);
        btnCancelOrder.setOnClickListener(this);
        btnPrint.setOnClickListener(this);

        adapter = new DialogInforOrderAdapter(context, arrFood);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewListOrder.setLayoutManager(layoutManager);
        recyclerViewListOrder.setItemAnimator(new DefaultItemAnimator());
        recyclerViewListOrder.setAdapter(adapter);

    }


    public void setInforOrder(ItemOrder2 itemOrder) {
        tvUsername.setText(itemOrder.getUserName());
        tvUserPhoneNo.setText(itemOrder.getUserPhone());
        tvUserAddress.setText(itemOrder.getUserAddress());
        tvComboPrice.setText(CommonMethod.convertMoneyToVND(itemOrder.getComboPrice()));
        tvTotalPrice.setText(CommonMethod.convertMoneyToVND(itemOrder.getTotalPrice()));
        tvPriceNeedPay.setText(CommonMethod.convertMoneyToVND(itemOrder.getPriceNeedPay()));
        tvDateOrder.setText(CommonMethod.getDateTimeString(itemOrder.getDateTime()));

        switch (itemOrder.getStatusOrder()){
            case CommonValue.STATUS_ORDER_NOT_HANDL:
                tvStatus.setTextColor(context.getResources().getColor(android.R.color.holo_red_light));
                tvStatus.setText(context.getResources().getString(R.string.status_not_handl));
                break;
            case CommonValue.STATUS_ORDER_HANDLING:
                tvStatus.setTextColor(context.getResources().getColor(android.R.color.holo_orange_light));
                tvStatus.setText(context.getResources().getString(R.string.status_handling));
                break;
            case CommonValue.STATUS_ORDER_HANDLED:
                tvStatus.setTextColor(context.getResources().getColor(android.R.color.holo_blue_light));
                tvStatus.setText(context.getResources().getString(R.string.status_handled));
                break;
        }



        arrFood.clear();
        if (itemOrder.getFoodList() != null) {
            arrFood.addAll(itemOrder.getFoodList());
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cancel_order:
                orderManagerFrament.cancelOrder();
                dismiss();
                break;
            case R.id.btn_print:
                Toast.makeText(context, "Tính năng đang hoàn thiện. Thử lại sau", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void setOrderManagerFrament(OrderManagerFrament orderManagerFrament) {
        this.orderManagerFrament = orderManagerFrament;
    }
}
