package com.cnc.hcm.cnctracking.fragment;

import android.content.Intent;
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
import com.cnc.hcm.cnctracking.activity.WorkDetailActivity;
import com.cnc.hcm.cnctracking.util.CommonMethod;
import com.cnc.hcm.cnctracking.util.Conts;

/**
 * Created by giapmn on 9/27/17.
 */

public class WorkDoingFragment extends Fragment {

//    private WorkDoingAdapter doingWorkAdapter;
    private MainActivity mainActivity;

    private RecyclerView rvDoingWork;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("onCreate", "WorkDoing");
        initObject();
    }

    private void initObject() {
        mainActivity = (MainActivity) getActivity();
//        doingWorkAdapter = new WorkDoingAdapter(getContext(), mainActivity.getDataByWorkType(Conts.TYPE_DOING_TASK), R.layout.item_doing_complete_task);
//        doingWorkAdapter.setOnClickButtonItemDoingComleteWorkListener(this);
        updateDistanceDoingWork(mainActivity.getLatitude(), mainActivity.getLongtitude());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_doing, container, false);
        Log.i("onCreateView", "WorkDoing");

        rvDoingWork = (RecyclerView) view.findViewById(R.id.rv_doing_tasks);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvDoingWork.setLayoutManager(linearLayoutManager);
        rvDoingWork.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
//        rvDoingWork.setAdapter(doingWorkAdapter);
    }

    public void updateDistanceDoingWork(double latitude, double longitude) {
//        if (doingWorkAdapter != null) {
//            doingWorkAdapter.updateDistanceDoingWork(latitude, longitude);
//        }
    }

//    @Override
//    public void onClickButtonCall(int position) {
//        CommonMethod.actionCall(getContext(), doingWorkAdapter.getItem(position).getContactPhone());
//    }
//
//    @Override
//    public void onClickButtonSMS(int position) {
//        CommonMethod.actionSMS(getContext(), doingWorkAdapter.getItem(position).getContactPhone());
//    }
//
//    @Override
//    public void onClickButtonAddress(int position) {
//
//        double longitude_cur = mainActivity.getLongtitude();
//        double latitude_cur = mainActivity.getLatitude();
//        double latitude = doingWorkAdapter.getItem(position).getLatitude();
//        double longitude = doingWorkAdapter.getItem(position).getLongitude();
//
//        CommonMethod.actionFindWayInMapApp(getContext(), latitude_cur, longitude_cur, latitude, longitude);
//    }
//
//    @Override
//    public void onClickButtonMoreDetail(int position) {
//        Intent intent = new Intent(getActivity(), WorkDetailActivity.class);
//        intent.putExtra(Conts.KEY_OBJECT_ITEM_WORK, doingWorkAdapter.getItem(position));
//        startActivity(intent);
//    }
}
