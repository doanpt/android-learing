package com.cnc.hcm.cnctracking.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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
import com.cnc.hcm.cnctracking.activity.WorkDetailActivity;
import com.cnc.hcm.cnctracking.adapter.WorkDetailServiceRecyclerViewAdapter;
import com.cnc.hcm.cnctracking.model.GetTaskDetailResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorkDetailServiceFragment extends Fragment implements View.OnClickListener, WorkDetailActivity.TaskDetailLoadedListener {

    private static final String TAG = WorkDetailServiceFragment.class.getSimpleName();

    private RelativeLayout rlExpandHeaderBill;
    private LinearLayout ll_content_bill;
    private LinearLayout tv_detail_work_call_action;
    private LinearLayout tv_detail_work_sms_action;
    private LinearLayout tv_detail_work_address_action;
    private ImageView imv_expand_bill;
    private TextView tv_detail_work_contact_name;
    private TextView tv_detail_work_contact_phone;
    private TextView tv_total_value;
    private TextView tv_vat;
    private TextView tv_have_to_pay;
    private TextView tv_detail_work_total_payment;
    private double latitude;
    private double longitude;

    private RecyclerView rv_service;

    private WorkDetailServiceRecyclerViewAdapter mWorkDetailServiceRecyclerViewAdapter;

    public WorkDetailServiceFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_detail_service, container, false);
        ll_content_bill = (LinearLayout) view.findViewById(R.id.ll_content_bill);
        imv_expand_bill = (ImageView) view.findViewById(R.id.imv_expand_bill);
        rlExpandHeaderBill = (RelativeLayout) view.findViewById(R.id.rl_expand_header_bill);
        rlExpandHeaderBill.setOnClickListener(this);

        tv_detail_work_contact_name = (TextView) view.findViewById(R.id.tv_detail_work_contact_name);
        tv_detail_work_contact_phone = (TextView) view.findViewById(R.id.tv_detail_work_contact_phone);
        tv_detail_work_call_action = (LinearLayout) view.findViewById(R.id.tv_detail_work_call_action);
        tv_detail_work_sms_action = (LinearLayout) view.findViewById(R.id.tv_detail_work_sms_action);
        tv_detail_work_address_action = (LinearLayout) view.findViewById(R.id.tv_detail_work_address_action);

        tv_total_value = (TextView) view.findViewById(R.id.tv_total_value);
        tv_vat = (TextView) view.findViewById(R.id.tv_vat);
        tv_have_to_pay = (TextView) view.findViewById(R.id.tv_have_to_pay);
        tv_detail_work_total_payment = (TextView) view.findViewById(R.id.tv_detail_work_total_payment);

        rv_service = (RecyclerView) view.findViewById(R.id.rv_service);
        rv_service.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mWorkDetailServiceRecyclerViewAdapter = new WorkDetailServiceRecyclerViewAdapter(getContext());
        rv_service.setAdapter(mWorkDetailServiceRecyclerViewAdapter);

        ((WorkDetailActivity)getActivity()).setTaskDetailLoadedListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_expand_header_bill:
                if (ll_content_bill.getVisibility() == View.GONE) {
                    ll_content_bill.setVisibility(View.VISIBLE);
                    imv_expand_bill.setImageResource(R.drawable.ic_expand_task_details);
                } else {
                    ll_content_bill.setVisibility(View.GONE);
                    imv_expand_bill.setImageResource(R.drawable.ic_chevron_right);
                }
                break;
        }
    }

    @Override
    public void onTaskDetailLoaded(GetTaskDetailResult getTaskDetailResult) {
        try {
            Log.d(TAG, "onTaskDetailLoaded");
            if (getTaskDetailResult.result.address != null) {
                handleActions(getTaskDetailResult.result.address);
            }
            if (getTaskDetailResult.result.customer != null) {
                tv_detail_work_contact_name.setText(getTaskDetailResult.result.customer.fullname + "");
                tv_detail_work_contact_phone.setText(getTaskDetailResult.result.customer.phone + "");
                handleActions(getTaskDetailResult.result.customer);
            }

            List<WorkDetailServiceRecyclerViewAdapter.DetailService> detailServices = new ArrayList<>();
            if (getTaskDetailResult.result.service != null) {
                detailServices.add(new WorkDetailServiceRecyclerViewAdapter.DetailService(getTaskDetailResult.result.service.name, getTaskDetailResult.result.service.price, 1));
            }
            if (getTaskDetailResult.result.process != null) {
                for (int i = 0; i < getTaskDetailResult.result.process.length; i++) {
                    GetTaskDetailResult.Result.Process process = getTaskDetailResult.result.process[i];
                    if (process != null && process.process != null) {
                        GetTaskDetailResult.Result.Process.ProcessDetail.Service[] services = process.process.services;
                        if (services != null) {
                            for (GetTaskDetailResult.Result.Process.ProcessDetail.Service service : services){
                                if (service != null) {
                                    detailServices.add(new WorkDetailServiceRecyclerViewAdapter.DetailService(service.product.name, service.product.price, service.quantity));
                                }
                            }
                        }
                    }
                }
            }

            long totalValue = 0;
            for (WorkDetailServiceRecyclerViewAdapter.DetailService detailService : detailServices) {
                if (detailService != null) {
                    totalValue += detailService.totalPrice();
                }
            }

            tv_total_value.setText("" + totalValue);
            long vat = totalValue/10;
            tv_vat.setText("" + vat);
            tv_have_to_pay.setText("" + (totalValue - vat));
            tv_detail_work_total_payment.setText("" + (totalValue - vat));
            mWorkDetailServiceRecyclerViewAdapter.updateDeviceList(detailServices);

        } catch (Exception e) {
            Log.e(TAG, "onTaskDetailLoaded(), Exception: " + e);
        }
    }

    @Override
    public void onLocationUpdate(double latitude, double longitude) {
        Log.d(TAG, "onLocationUpdate, latitude: " + latitude + ", longitude: " + longitude);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private void handleActions(final GetTaskDetailResult.Result.Address address) {
        tv_detail_work_address_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + latitude + "," + longitude
                                + "&daddr=" + address.location.latitude + "," + address.location.longitude));
                startActivity(intent);
            }
        });
    }

    private void handleActions(final GetTaskDetailResult.Result.Customer customer) {
        tv_detail_work_call_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", customer.phone, null)));
            }
        });
        tv_detail_work_sms_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri uri = Uri.parse("smsto:" + customer.phone);
                    Intent smsIntent = new Intent(Intent.ACTION_SENDTO, uri);
                    smsIntent.putExtra("sms_body", "sms content");
                    startActivity(smsIntent);
                } catch (Exception e) {
                    Log.e(TAG, "send sms --> Exception occurs.", e);
                }
            }
        });
    }

}
