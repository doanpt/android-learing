package com.cnc.hcm.cnctracking.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.activity.ProductDetailActivity;
import com.cnc.hcm.cnctracking.adapter.WorkDetailDeviceRecyclerViewAdapter;
import com.cnc.hcm.cnctracking.api.ApiUtils;
import com.cnc.hcm.cnctracking.api.MHead;
import com.cnc.hcm.cnctracking.dialog.DialogDetailTaskFragment;
import com.cnc.hcm.cnctracking.event.RecyclerViewItemClickListener;
import com.cnc.hcm.cnctracking.event.RecyclerViewMenuItemClickListener;
import com.cnc.hcm.cnctracking.model.GetTaskDetailResult;
import com.cnc.hcm.cnctracking.model.RemoveDeviceFromTaskResult;
import com.cnc.hcm.cnctracking.util.CommonMethod;
import com.cnc.hcm.cnctracking.util.Conts;
import com.cnc.hcm.cnctracking.util.UserInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkDetailDeviceFragment extends Fragment implements DialogDetailTaskFragment.TaskDetailLoadedListener, RecyclerViewItemClickListener, RecyclerViewMenuItemClickListener {

    private static final String TAG = WorkDetailDeviceFragment.class.getSimpleName();

    private RecyclerView rv_device;

    private WorkDetailDeviceRecyclerViewAdapter mWorkDetailDeviceRecyclerViewAdapter;

    private String saveTaskId;

    private ProgressDialog mProgressDialog;

    public WorkDetailDeviceFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_detail_device, container, false);
        rv_device = view.findViewById(R.id.rv_device);
        rv_device.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mWorkDetailDeviceRecyclerViewAdapter = new WorkDetailDeviceRecyclerViewAdapter(this, this, getActivity());
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

    @Override
    public void onRecyclerViewMenuItemClicked(int position, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_item_task_remove_device:
                showDialogConfirmRemovingDevice(position);
                break;
            default:
                break;
        }
    }

    private void showDialogConfirmRemovingDevice(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();

        View view = getLayoutInflater().inflate(R.layout.dialog_confirm_removing_device, null);
        dialog.setView(view);
        TextView tv_message = view.findViewById(R.id.tv_message);
        tv_message.setText(getContext().getResources().getString(R.string.confirm_removing_device));
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                tryToRemoveDevice(position, mWorkDetailDeviceRecyclerViewAdapter.getProcesses().get(position).device._id);
            }
        });
        dialog.show();
    }

    private void tryToRemoveDevice(final int position, String deviceId) {
        showDialogLoadding();
        String accessToken = UserInfo.getInstance(getActivity()).getAccessToken();
        Log.e(TAG, "tryToRemoveDevice(), accessToken: " + accessToken + ", idTask: " + saveTaskId + ", deviceId: " + deviceId);
        List<MHead> arrHeads = new ArrayList<>();
        arrHeads.add(new MHead(Conts.KEY_ACCESS_TOKEN, accessToken));
        arrHeads.add(new MHead(Conts.KEY_DEVICE_ID, deviceId));
        ApiUtils.getAPIService(arrHeads).removeDeviceFromTask(saveTaskId).enqueue(new Callback<RemoveDeviceFromTaskResult>() {
            @Override
            public void onResponse(Call<RemoveDeviceFromTaskResult> call, Response<RemoveDeviceFromTaskResult> response) {
                int statusCode = response.code();
                if (response.isSuccessful()) {
                    RemoveDeviceFromTaskResult result = response.body();
                    Log.e(TAG, "tryToRemoveDevice.onResponse(), statusCode: " + statusCode + ", result: " + result);
                    onDeviceRemovedFromTask(position, result);
                    dismisDialogLoading();
                }
            }

            @Override
            public void onFailure(Call<RemoveDeviceFromTaskResult> call, Throwable t) {
                dismisDialogLoading();
                Log.e(TAG, "tryToRemoveDevice.onFailure() --> " + t);
                t.printStackTrace();
                CommonMethod.makeToast(getContext(), t.getMessage() != null ? t.getMessage() : "onFailure");
            }
        });
    }

    private void onDeviceRemovedFromTask(int position, RemoveDeviceFromTaskResult result) {
        try {
            if (result != null && result.getStatusCode() == 200) {
                CommonMethod.makeToast(getContext(), "Xóa thiết bị thành công");
                mWorkDetailDeviceRecyclerViewAdapter.removeDeviceAtPosition(position);
            } else {
                CommonMethod.makeToast(getContext(), "Không thể xóa thiết bị. Lý do từ server trả về: " + result.getMessage());
            }
        } catch (Exception e) {
            Log.e(TAG, "onTaskInfoLoaded() --> Exception occurs.", e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismisDialogLoading();
    }

    private void showDialogLoadding() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(getResources().getString(R.string.loadding));
        mProgressDialog.show();
    }

    private void dismisDialogLoading() {
        if (mProgressDialog!= null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
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
                        productDetail.putExtra(Conts.KEY_WORK_LOCATION, getTaskDetailResult.result.address.getStreet() + "");
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
