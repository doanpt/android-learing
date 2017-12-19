package com.cnc.hcm.cnctracking.fragment;

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

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.activity.MainActivity;
import com.cnc.hcm.cnctracking.adapter.WorkDoingAdapter;
import com.cnc.hcm.cnctracking.util.Conts;

/**
 * Created by giapmn on 9/27/17.
 */

public class WorkAllFragment extends Fragment implements WorkDoingAdapter.OnClickButtonItemDoingComleteWorkListener {

    private WorkDoingAdapter doingAdapter;
    private MainActivity mainActivity;

    private RecyclerView rvAllWork;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("onCreate", "Cancel");
        initObject();

    }

    private void initObject() {
        mainActivity = (MainActivity) getActivity();
        doingAdapter = new WorkDoingAdapter(getContext(), mainActivity.getDataByWorkType(Conts.TYPE_ALL_TASK), R.layout.item_all_task);
        doingAdapter.setOnClickButtonItemDoingComleteWorkListener(this);
        updateDistanceAllWork(mainActivity.getLatitude(), mainActivity.getLongtitude());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_all, container, false);
        Log.i("onCreateView", "Cancel");

        rvAllWork = (RecyclerView) view.findViewById(R.id.rv_all_tasks);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvAllWork.setLayoutManager(layoutManager);
        rvAllWork.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        rvAllWork.setAdapter(doingAdapter);

    }

    public void updateDistanceAllWork(double latitude, double longitude) {
        if (doingAdapter != null) {
            doingAdapter.updateDistanceDoingWork(latitude, longitude);
        }
    }

    @Override
    public void onClickButtonCall(int position) {

    }

    @Override
    public void onClickButtonSMS(int position) {

    }

    @Override
    public void onClickButtonAddress(int position) {

    }

    @Override
    public void onClickButtonMoreDetail(int position) {

    }
}
