package com.cnc.hcm.cnctracking.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.activity.MainActivity;
import com.cnc.hcm.cnctracking.adapter.TaskListAdapter;
import com.cnc.hcm.cnctracking.customeview.MyRecyclerView;
import com.cnc.hcm.cnctracking.dialog.DialogDetailTaskFragment;
import com.cnc.hcm.cnctracking.model.ItemTask;
import com.cnc.hcm.cnctracking.service.GPSService;

import java.util.ArrayList;

/**
 * Created by giapmn on 9/27/17.
 */

public class TaskNewFragment extends Fragment implements TaskListAdapter.OnItemWorkClickListener {

    private static final String TAGG = TaskNewFragment.class.getSimpleName();
    private MyRecyclerView rvNewTask;
    private TaskListAdapter taskListAdapter;
    private MainActivity mainActivity;
    private ArrayList<ItemTask> arrTask = new ArrayList<>();
    private DialogDetailTaskFragment dialogDetailTaskFragment;
    private GPSService gpsService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("onCreate", "New");
    }

    private void initObject() {
        mainActivity = (MainActivity) getActivity();

        dialogDetailTaskFragment = new DialogDetailTaskFragment();
        dialogDetailTaskFragment.setMainActivity(mainActivity);
        taskListAdapter = new TaskListAdapter(getContext());
        getData();
        taskListAdapter.setOnItemWorkClickListener(this);
        updateDistanceNewWork(mainActivity.getLatitude(), mainActivity.getLongtitude());
    }

    public void getData() {
        if (mainActivity != null) {
            gpsService = mainActivity.getGpsService();
            if (gpsService != null) {
                arrTask.clear();
                arrTask.addAll(gpsService.getArrNewTask());
                taskListAdapter.notiDataChange(arrTask);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.i("onCreateView", "New");

        View view = inflater.inflate(R.layout.fragment_new_task, container, false);
        rvNewTask = (MyRecyclerView) view.findViewById(R.id.rv_new_tasks);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvNewTask.setLayoutManager(linearLayoutManager);
        rvNewTask.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initObject();

    }

    @Override
    public void onStart() {
        super.onStart();
        rvNewTask.setAdapter(taskListAdapter);
    }

    //
    public void updateDistanceNewWork(double latitude, double longitude) {
        if (taskListAdapter != null) {
            taskListAdapter.updateDistanceForTask(mainActivity.isNetworkConnected(), latitude, longitude);
        }
    }

    @Override
    public void onClickItemWork(int position) {
        String idTask = taskListAdapter.getItem(position).getTaskResult()._id;
        dialogDetailTaskFragment.setIdTask(idTask);
        dialogDetailTaskFragment.show(getActivity().getSupportFragmentManager(), dialogDetailTaskFragment.getTag());
        dialogDetailTaskFragment.setExpaned(true);
    }


    public void addItem(ItemTask itemTask) {
        arrTask.add(itemTask);
        notiDataChange();
    }

    public void notiDataChange() {
        if (taskListAdapter != null) {
            taskListAdapter.notiDataChange(arrTask);
        }
    }

    public void clearData() {
        if (arrTask != null)
            arrTask.clear();
    }
}
