package com.cnc.hcm.cnctracking.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.activity.ProductDetailActivity;
import com.cnc.hcm.cnctracking.adapter.WorkDetailDeviceRecyclerViewAdapter;
import com.cnc.hcm.cnctracking.dialog.DialogDetailTaskFragment;
import com.cnc.hcm.cnctracking.event.RecyclerViewItemClickListener;
import com.cnc.hcm.cnctracking.model.GetTaskDetailResult;
import com.cnc.hcm.cnctracking.util.Conts;
import com.cnc.hcm.cnctracking.util.UserInfo;

import java.util.Arrays;

public class WorkDetailDeviceFragment extends Fragment implements DialogDetailTaskFragment.TaskDetailLoadedListener, RecyclerViewItemClickListener {

    private static final String TAG = WorkDetailDeviceFragment.class.getSimpleName();

    private RecyclerView rv_device;

    private WorkDetailDeviceRecyclerViewAdapter mWorkDetailDeviceRecyclerViewAdapter;

    private String saveTaskId;

    public WorkDetailDeviceFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_detail_device, container, false);
        rv_device = view.findViewById(R.id.rv_device);
        rv_device.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mWorkDetailDeviceRecyclerViewAdapter = new WorkDetailDeviceRecyclerViewAdapter(this, getActivity());
        rv_device.setAdapter(mWorkDetailDeviceRecyclerViewAdapter);

        ((DialogDetailTaskFragment)getParentFragment()).setTaskDetailLoadedListener(this);
        return view;
    }

    @Override
    public void onTaskDetailLoaded(GetTaskDetailResult getTaskDetailResult) {
        try {
            saveTaskId = getTaskDetailResult.result._id;
            if (getTaskDetailResult.result.process != null) {
                Log.e(TAG, "onTaskDetailLoaded, process.length: " + getTaskDetailResult.result.process.length);
                mWorkDetailDeviceRecyclerViewAdapter.updateDeviceList(Arrays.asList(getTaskDetailResult.result.process));
            }
        } catch (Exception e) {
            Log.e(TAG, "onTaskDetailLoaded, Exception: " + e);
        }
    }

    @Override
    public void onClick(View view, int position) {
        switch (view.getId()) {
            case R.id.item_product :
                processDevice(position);
                break;
            default:
                break;
        }
    }

    private void processDevice(int position) {
        Log.d(TAG, "item_product.onClick(), taskId: " + saveTaskId + ", deviceId: " + mWorkDetailDeviceRecyclerViewAdapter.getProcesses().get(position).device._id);
        GetTaskDetailResult.Result.Process process = mWorkDetailDeviceRecyclerViewAdapter.getProcesses().get(position);
        Fragment parentFragment = getParentFragment();
        if (process != null && parentFragment instanceof DialogDetailTaskFragment) {
            try {
                Intent productDetail = new Intent(getActivity(), ProductDetailActivity.class);
                productDetail.putExtra(Conts.KEY_PRODUCT_ID, process.device._id);
                productDetail.putExtra(Conts.KEY_ACCESS_TOKEN, UserInfo.getInstance(getActivity()).getAccessToken());
                productDetail.putExtra(Conts.KEY_ID_TASK, saveTaskId);
                DialogDetailTaskFragment dialogDetailTaskFragment = (DialogDetailTaskFragment) getParentFragment();
                GetTaskDetailResult getTaskDetailResult = dialogDetailTaskFragment.getGetTaskDetailResult();
                if (getTaskDetailResult != null && getTaskDetailResult.result != null) {
                    productDetail.putExtra(Conts.KEY_WORK_NAME, getTaskDetailResult.result.title + "");
                    if (getTaskDetailResult.result.address != null) {
                        productDetail.putExtra(Conts.KEY_WORK_LOCATION, getTaskDetailResult.result.address.street + "");
                    } else if (getTaskDetailResult.result.customer.address != null) {
                        productDetail.putExtra(Conts.KEY_WORK_LOCATION, getTaskDetailResult.result.customer.address.street + "");
                    }
                    String date = getTaskDetailResult.result.createdDate;
                    if (!TextUtils.isEmpty(date)) {
                        productDetail.putExtra(Conts.KEY_WORK_TIME, date.substring(date.indexOf("T") + 1, date.indexOf("T") + 6));
                    }
                }
                startActivity(productDetail);
            } catch (Exception e) {
                Log.e(TAG, "processDevice, Exception: " + e);
            }
        }
    }
}
