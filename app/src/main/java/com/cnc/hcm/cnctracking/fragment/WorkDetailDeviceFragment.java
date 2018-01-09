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

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.activity.WorkDetailActivity;
import com.cnc.hcm.cnctracking.adapter.WorkDetailDeviceRecyclerViewAdapter;
import com.cnc.hcm.cnctracking.model.GetTaskDetailResult;

import java.util.Arrays;

public class WorkDetailDeviceFragment extends Fragment implements WorkDetailActivity.TaskDetailLoadedListener {

    private static final String TAG = WorkDetailDeviceFragment.class.getSimpleName();

    private RecyclerView rv_device;

    private WorkDetailDeviceRecyclerViewAdapter mWorkDetailDeviceRecyclerViewAdapter;

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
        mWorkDetailDeviceRecyclerViewAdapter = new WorkDetailDeviceRecyclerViewAdapter(getContext());
        rv_device.setAdapter(mWorkDetailDeviceRecyclerViewAdapter);

        ((WorkDetailActivity)getActivity()).setTaskDetailLoadedListener(this);
        return view;
    }

    @Override
    public void onTaskDetailLoaded(GetTaskDetailResult getTaskDetailResult) {
        try {
            if (getTaskDetailResult.result.process != null) {
                mWorkDetailDeviceRecyclerViewAdapter.updateDeviceList(Arrays.asList(getTaskDetailResult.result.process));
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e);
        }
    }

    @Override
    public void onLocationUpdate(double latitude, double longitude) {

    }
}
