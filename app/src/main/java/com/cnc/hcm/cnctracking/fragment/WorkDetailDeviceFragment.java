package com.cnc.hcm.cnctracking.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.adapter.WorkDetailDeviceRecyclerViewAdapter;
import com.cnc.hcm.cnctracking.api.ApiUtils;
import com.cnc.hcm.cnctracking.api.MHead;
import com.cnc.hcm.cnctracking.dialog.DialogDetailTaskFragment;
import com.cnc.hcm.cnctracking.event.RecyclerViewItemClickListener;
import com.cnc.hcm.cnctracking.model.GetTaskDetailResult;
import com.cnc.hcm.cnctracking.model.ProcessDeviceResult;
import com.cnc.hcm.cnctracking.util.CommonMethod;
import com.cnc.hcm.cnctracking.util.Conts;
import com.cnc.hcm.cnctracking.util.UserInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        mWorkDetailDeviceRecyclerViewAdapter = new WorkDetailDeviceRecyclerViewAdapter(this);
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
            case R.id.iv_fix :
                Log.d(TAG, "onClick, processDevice, taskId: " + saveTaskId + ", deviceId: " + mWorkDetailDeviceRecyclerViewAdapter.getProcesses().get(position).device._id);
                List<MHead> arrHeads = new ArrayList<>();
                arrHeads.add(new MHead(Conts.KEY_ACCESS_TOKEN, UserInfo.getInstance(getContext()).getAccessToken()));
                arrHeads.add(new MHead(Conts.KEY_DEVICE_ID, mWorkDetailDeviceRecyclerViewAdapter.getProcesses().get(position).device._id));
                ApiUtils.getAPIService(arrHeads).processDevice(saveTaskId).enqueue(new Callback<ProcessDeviceResult>() {
                    @Override
                    public void onResponse(Call<ProcessDeviceResult> call, Response<ProcessDeviceResult> response) {
                        int statusCode = response.code();
                        Log.d(TAG, "onClick.onResponse(), statusCode: " + statusCode);
                        if (response.isSuccessful()) {
                            ProcessDeviceResult processDeviceResult = response.body();
                            if (processDeviceResult != null) {
                                Log.e(TAG, "onClick.onResponse() success");
                                Toast.makeText(getActivity(), "onClick fix icon. process device --> success for " + processDeviceResult.result.title, Toast.LENGTH_LONG).show();
                            } else {
                                CommonMethod.makeToast(getContext(), "onClick fix icon. process device --> success but processDeviceResult is null");
                            }
                        } else {
                            CommonMethod.makeToast(getContext(), "onClick fix icon. process device --> not success: " + response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ProcessDeviceResult> call, Throwable t) {
                        CommonMethod.makeToast(getContext(), "onClick fix icon. process device --> onFailure" + t.toString());
                    }
                });
                break;
            default:
                break;
        }
    }
}
