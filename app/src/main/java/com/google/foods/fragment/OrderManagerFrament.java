package com.google.foods.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.foods.R;
import com.google.foods.activity.OrderFoodManagerActivity;
import com.google.foods.adapter.OrderManagerAdapter;
import com.google.foods.dialog.DialogInforOrder;
import com.google.foods.models.ItemFood;
import com.google.foods.models.ItemOrder2;
import com.google.foods.utils.CommonMethod;
import com.google.foods.utils.CommonValue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by sev_user on 08/14/2017.
 */

public class OrderManagerFrament extends Fragment implements OrderManagerAdapter.OnItemOrderManagerClickListener {
    private static final String TAG = "OrderManagerFrament";
    private TextView tvAddress;
    private TextView tvStatusOrder;
    private TextView tvInforDate;
    private ImageView imvChooseDate;
    private Spinner spinnerAddress;
    private Spinner spinnerStatus;
    private RecyclerView recyclerOrder;
    private LinearLayout llListEmpty;

    private OrderFoodManagerActivity orderFoodManagerActivity;
    private OrderManagerAdapter orderMgrAdapter;
    private DialogInforOrder dialogInforOrder;
    String arr[] = new String[]{
            "Tất cả địa điểm",
            "Cần ship",
            "Phục vụ tại quán"
    };

    String arrStatus[] = new String[]{
            "Tất cả",
            "Chưa xử lý",
            "Đang xử lý",
            "Đã xử lý"
    };

    private int deliveryType = CommonValue.DEFAULT_VALUE_INT_0;
    private int statusOrder = CommonValue.STATUS_ORDER_SHOW_ALL;
    private ProgressDialog progressDialog;

    private int position;

    private static OrderManagerFrament orderManagerFrament;
    private DatePickerDialog datePicker;

    public static OrderManagerFrament getInstance() {
        if (orderManagerFrament == null) {
            orderManagerFrament = new OrderManagerFrament();
        }
        return orderManagerFrament;
    }

    public OrderManagerFrament() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        orderFoodManagerActivity = (OrderFoodManagerActivity) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "OnCreate OrderManagerFrament");
        initDialog();
    }

    private void initDialog() {
        position = -1;
        dialogInforOrder = new DialogInforOrder(getContext());
        dialogInforOrder.setOrderManagerFrament(this);

        Calendar newCalendar = Calendar.getInstance();
        datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tvInforDate.setText(CommonMethod.getDateString(newDate.getTime()));
                orderMgrAdapter.setDateOrder(CommonMethod.getDateString(newDate.getTime()));
                orderMgrAdapter.filterByDateOrder();
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView OrderManagerFrament");
        showDumyLoadingData();
        View view = inflater.inflate(R.layout.fragment_oder_manager, container, false);

        llListEmpty = (LinearLayout) view.findViewById(R.id.ll_list_order_empty);
        //tvAddress = (TextView) view.findViewById(R.id.tv_mgr_address);
        //tvAddress.setText(arr[0]);
        spinnerAddress = (Spinner) view.findViewById(R.id.spinner_mgr_address);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, arr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAddress.setAdapter(adapter);
        spinnerAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerAddress.isEnabled()) {
                    //tvAddress.setText(arr[position]);
                    OrderManagerFrament.this.deliveryType = position;
                    orderMgrAdapter.filterByDateOrder();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //tvStatusOrder = (TextView) view.findViewById(R.id.tvStatusOrder);
        //tvStatusOrder.setText(arrStatus[0]);
        spinnerStatus = (Spinner) view.findViewById(R.id.spinner_mgr_status_order);
        ArrayAdapter<String> adapterStatus = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, arrStatus);
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapterStatus);
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerStatus.isEnabled()) {
                    //tvStatusOrder.setText(arrStatus[position]);
                    OrderManagerFrament.this.statusOrder = position;
                    orderMgrAdapter.filterByDateOrder();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tvInforDate = (TextView) view.findViewById(R.id.tv_infor_date);
        tvInforDate.setText(CommonMethod.getDateString(Calendar.getInstance().getTime()));

        imvChooseDate = (ImageView) view.findViewById(R.id.imv_select_filter_date);
        imvChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show();
            }
        });

        //RecycleView
        orderMgrAdapter = new OrderManagerAdapter(getActivity());
        orderMgrAdapter.setOrderManagerFrament(this);
        orderMgrAdapter.setOnItemOrderManagerClickListener(this);
        recyclerOrder = (RecyclerView) view.findViewById(R.id.recycler_view_mgr_order);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerOrder.setLayoutManager(layoutManager);
        recyclerOrder.setItemAnimator(new DefaultItemAnimator());
        recyclerOrder.setAdapter(orderMgrAdapter);

        return view;
    }

    @Override
    public void onClickItemOrder(int position) {
        this.position = position;
        dialogInforOrder.setInforOrder(orderMgrAdapter.getArrOrder().get(position));
        dialogInforOrder.show();

    }

    public int getDeliveryType() {
        return deliveryType;
    }

    public int getStatusOrder() {
        return statusOrder;
    }

    public void checkShowLayoutListEmpty(List<ItemOrder2> list) {
        if (list.size() == 0){
            llListEmpty.setVisibility(View.VISIBLE);
        } else {
            llListEmpty.setVisibility(View.GONE);
        }
    }

    private void showDumyLoadingData() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.show();

    }

    public void hideDumyLoadingData() {
        progressDialog.dismiss();
    }

    public void cancelOrder() {
        orderMgrAdapter.cancelOrder(position);
    }


    public void updateTotalQuantityFood(List<ItemFood> foodList) {
        orderFoodManagerActivity.updateTotalQuantityFood(foodList);
    }
}
