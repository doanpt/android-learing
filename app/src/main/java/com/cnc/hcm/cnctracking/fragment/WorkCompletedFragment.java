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
import com.cnc.hcm.cnctracking.util.CommonMethod;
import com.cnc.hcm.cnctracking.util.Conts;


/**
 * Created by giapmn on 9/27/17.
 */

public class WorkCompletedFragment extends Fragment{

    private MainActivity mainActivity;

    private RecyclerView rvCompleteWork;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("onCreate", "Completed");
        initObject();
    }

    private void initObject() {
        mainActivity = (MainActivity) getActivity();
//        doingAdapter = new WorkDoingAdapter(getContext(), mainActivity.getDataByWorkType(Conts.TYPE_COMPLETE_TASK), R.layout.item_doing_complete_task);
//        doingAdapter.setOnClickButtonItemDoingComleteWorkListener(this);
        updateDistanceCompleteWork(mainActivity.getLatitude(), mainActivity.getLongtitude());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_completed, container, false);
        Log.i("onCreateView", "Completed");

        rvCompleteWork = (RecyclerView) view.findViewById(R.id.rv_complete_tasks);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCompleteWork.setLayoutManager(linearLayoutManager);
        rvCompleteWork.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
//        rvCompleteWork.setAdapter(doingAdapter);
    }


    public void updateDistanceCompleteWork(double latitude, double longitude) {
//        if (doingAdapter != null) {
//            doingAdapter.updateDistanceDoingWork(latitude, longitude);
//        }
    }
//
//    @Override
//    public void onClickButtonCall(int position) {
//        CommonMethod.actionCall(getContext(), doingAdapter.getItem(position).getContactPhone());
//
//    }
//
//    @Override
//    public void onClickButtonSMS(int position) {
//        CommonMethod.actionSMS(getContext(), doingAdapter.getItem(position).getContactPhone());
//
//    }

//    @Override
//    public void onClickButtonAddress(int position) {
//        double longitude_cur = mainActivity.getLongtitude();
//        double latitude_cur = mainActivity.getLatitude();
//        double latitude = doingAdapter.getItem(position).getLatitude();
//        double longitude = doingAdapter.getItem(position).getLongitude();
//
//        CommonMethod.actionFindWayInMapApp(getContext(), latitude_cur, longitude_cur, latitude, longitude);
//    }

}
