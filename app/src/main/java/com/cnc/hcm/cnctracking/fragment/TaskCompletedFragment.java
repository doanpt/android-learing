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
import com.cnc.hcm.cnctracking.adapter.TaskListAdapter;
import com.cnc.hcm.cnctracking.model.ItemTask;
import com.cnc.hcm.cnctracking.util.CommonMethod;

import java.util.ArrayList;


/**
 * Created by giapmn on 9/27/17.
 */

public class TaskCompletedFragment extends Fragment implements TaskListAdapter.OnItemWorkClickListener {

    private MainActivity mainActivity;
    private TaskListAdapter taskListAdapter;

    private RecyclerView rvCompleteWork;
    private ArrayList<ItemTask> arrTask = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("onCreate", "Completed");
        initObject();
    }

    private void initObject() {
        mainActivity = (MainActivity) getActivity();
        taskListAdapter = new TaskListAdapter(getContext());
        taskListAdapter.notiDataChange(arrTask);
        taskListAdapter.setOnItemWorkClickListener(this);
        updateDistanceCompleteWork(mainActivity.getLatitude(), mainActivity.getLongtitude());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed_task, container, false);
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
        rvCompleteWork.setAdapter(taskListAdapter);
    }


    public void updateDistanceCompleteWork(double latitude, double longitude) {
        if (taskListAdapter != null) {
            taskListAdapter.updateDistanceForTask(mainActivity.isNetworkConnected(), latitude, longitude);
        }
    }


    public void notiDataChange() {
        if (taskListAdapter != null) {
            taskListAdapter.notifyDataSetChanged();
        }
    }

    public void addItem(ItemTask itemTask) {
        arrTask.add(itemTask);
    }

    @Override
    public void onClickItemWork(int position) {
        CommonMethod.makeToast(getContext(), taskListAdapter.getItem(position).getTaskResult().title);
    }
}
