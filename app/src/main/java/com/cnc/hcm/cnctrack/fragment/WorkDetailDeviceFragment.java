package com.cnc.hcm.cnctrack.fragment;

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
import android.widget.Toast;

import com.cnc.hcm.cnctrack.R;
import com.cnc.hcm.cnctrack.activity.ProductDetailActivity;
import com.cnc.hcm.cnctrack.adapter.WorkDetailDeviceRecyclerViewAdapter;
import com.cnc.hcm.cnctrack.api.ApiUtils;
import com.cnc.hcm.cnctrack.api.MHead;
import com.cnc.hcm.cnctrack.base.BaseFragment;
import com.cnc.hcm.cnctrack.dialog.DialogNotification;
import com.cnc.hcm.cnctrack.event.RecyclerViewItemClickListener;
import com.cnc.hcm.cnctrack.event.RecyclerViewMenuItemClickListener;
import com.cnc.hcm.cnctrack.model.GetTaskDetailResult;
import com.cnc.hcm.cnctrack.model.RemoveDeviceFromTaskResult;
import com.cnc.hcm.cnctrack.util.CommonMethod;
import com.cnc.hcm.cnctrack.util.Conts;
import com.cnc.hcm.cnctrack.util.UserInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkDetailDeviceFragment extends BaseFragment implements DialogDetailTaskFragment.TaskDetailLoadedListener, RecyclerViewItemClickListener, RecyclerViewMenuItemClickListener {

    private static final String TAG = WorkDetailDeviceFragment.class.getSimpleName();

    private RecyclerView rv_device;

    private WorkDetailDeviceRecyclerViewAdapter mWorkDetailDeviceRecyclerViewAdapter;

    private String saveTaskId;

    private DialogNotification mDialogNotification;

    public WorkDetailDeviceFragment() {
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_work_detail_device;
    }

    @Override
    public void onViewReady(View view) {
        rv_device = view.findViewById(R.id.rv_device);
        rv_device.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mWorkDetailDeviceRecyclerViewAdapter = new WorkDetailDeviceRecyclerViewAdapter(this, this, getActivity());
        rv_device.setAdapter(mWorkDetailDeviceRecyclerViewAdapter);

        ((DialogDetailTaskFragment) getParentFragment()).setTaskDetailLoadedListener(this);
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
            case R.id.item_product:
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

    @Override
    public void onRecyclerViewMenuItemClickedFailue(String message) {
        showDialogNotification(message);
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
        showProgressLoading();
        String accessToken = UserInfo.getInstance(getActivity()).getAccessToken();
        Log.e(TAG, "tryToRemoveDevice(), accessToken: " + accessToken + ", idTask: " + saveTaskId + ", deviceId: " + deviceId);
        List<MHead> arrHeads = new ArrayList<>();
        arrHeads.add(new MHead(Conts.KEY_ACCESS_TOKEN, accessToken));
        arrHeads.add(new MHead(Conts.KEY_DEVICE_ID, deviceId));
        ApiUtils.getAPIService(arrHeads).removeDeviceFromTask(saveTaskId).enqueue(new Callback<RemoveDeviceFromTaskResult>() {
            @Override
            public void onResponse(Call<RemoveDeviceFromTaskResult> call, Response<RemoveDeviceFromTaskResult> response) {
                int statusCode = response.body().getStatusCode();
                if (response.isSuccessful()) {
                    if (statusCode == Conts.RESPONSE_STATUS_OK) {
                        RemoveDeviceFromTaskResult result = response.body();
                        Log.e(TAG, "tryToRemoveDevice.onResponse(), statusCode: " + statusCode + ", result: " + result);
                        dismisProgressLoading();
                        onDeviceRemovedFromTask(position, result);
                    } else if (statusCode == Conts.RESPONSE_STATUS_TOKEN_WRONG) {
                        actionLogout();
                    }
                }
            }

            @Override
            public void onFailure(Call<RemoveDeviceFromTaskResult> call, Throwable t) {
                dismisProgressLoading();
                Log.e(TAG, "tryToRemoveDevice.onFailure() --> " + t);
                t.printStackTrace();
                CommonMethod.makeToast(getContext(), t.getMessage() != null ? t.getMessage() : "onFailure");
            }
        });
    }

    private void onDeviceRemovedFromTask(final int position, final RemoveDeviceFromTaskResult result) {
        try {
            Log.e(TAG, "onDeviceRemovedFromTask. getStatusCode: " + result.getStatusCode() + ", getMessage: " + result.getMessage());
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (result != null && result.getStatusCode() == 200) {
                        Toast.makeText(getActivity(), "Xóa thiết bị thành công", Toast.LENGTH_LONG).show();
                        mWorkDetailDeviceRecyclerViewAdapter.removeDeviceAtPosition(position);
                    } else {
                        Toast.makeText(getActivity(), "Không thể xóa thiết bị. Lý do từ server trả về: " + result.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "onDeviceRemovedFromTask() --> Exception occurs.", e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismisProgressLoading();
        dismisDialogNotification();
    }


    private void showDialogNotification(String massage) {
        dismisDialogNotification();
        mDialogNotification = new DialogNotification(getActivity());
        mDialogNotification.setCancelable(true);
        mDialogNotification.setContentMessage(massage);
        mDialogNotification.setHiddenBtnOK();
        mDialogNotification.setTextBtnExit("OK");
        mDialogNotification.show();
    }

    public void dismisDialogNotification() {
        if (mDialogNotification != null && mDialogNotification.isShowing()) {
            mDialogNotification.dismiss();
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
