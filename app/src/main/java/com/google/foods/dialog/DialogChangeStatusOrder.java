package com.google.foods.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.foods.R;
import com.google.foods.adapter.OrderManagerAdapter;
import com.google.foods.utils.CommonValue;

/**
 * Created by giapmn on 8/29/17.
 */

public class DialogChangeStatusOrder extends Dialog implements View.OnClickListener {

    private Context context;

    private Button btnStatus1;
    private Button btnStatus2;
    private Button btnStatus3;

    private int position;
    private OrderManagerAdapter orderManagerAdapter;


    public DialogChangeStatusOrder(@NonNull Context context, OrderManagerAdapter orderManagerAdapter) {
        super(context);
        this.context = context;
        this.orderManagerAdapter = orderManagerAdapter;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_change_status_order);
        initView();
    }

    private void initView() {
        btnStatus1 = (Button) findViewById(R.id.btn_status_not_handl);
        btnStatus2 = (Button) findViewById(R.id.btn_status_handling);
        btnStatus3 = (Button) findViewById(R.id.btn_status_handled);
        btnStatus1.setOnClickListener(this);
        btnStatus2.setOnClickListener(this);
        btnStatus3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_status_not_handl:
                orderManagerAdapter.updateStatusOrder(position, CommonValue.STATUS_ORDER_NOT_HANDL);
                break;
            case R.id.btn_status_handling:
                orderManagerAdapter.updateStatusOrder(position, CommonValue.STATUS_ORDER_HANDLING);
                break;
            case R.id.btn_status_handled:
                orderManagerAdapter.updateStatusOrder(position, CommonValue.STATUS_ORDER_HANDLED);
                break;
        }
        dismiss();
    }

    public void setPosition(int position) {
        this.position = position;
    }


}
