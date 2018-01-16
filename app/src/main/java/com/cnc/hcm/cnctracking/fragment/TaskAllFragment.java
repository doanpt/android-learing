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
import com.cnc.hcm.cnctracking.adapter.TaskListAdapter;
import com.cnc.hcm.cnctracking.dialog.DialogDetailTaskFragment;
import com.cnc.hcm.cnctracking.model.ItemTask;
import com.cnc.hcm.cnctracking.util.Conts;

import java.util.ArrayList;

/**
 * Created by giapmn on 9/27/17.
 */

public class TaskAllFragment extends Fragment implements TaskListAdapter.OnItemWorkClickListener {

    private TaskListAdapter taskListAdapter;
    private MainActivity mainActivity;

    private RecyclerView rvAllWork;
    private ArrayList<ItemTask> arrTask = new ArrayList<>();
    private DialogDetailTaskFragment dialogDetailTaskFragment;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("onCreate", "Cancel");
        initObject();

    }

    private void initObject() {
        dialogDetailTaskFragment = new DialogDetailTaskFragment();

        mainActivity = (MainActivity) getActivity();
        taskListAdapter = new TaskListAdapter(getContext());
        taskListAdapter.notiDataChange(arrTask);
        taskListAdapter.setOnItemWorkClickListener(this);
        updateDistanceForAllTask(mainActivity.getLatitude(), mainActivity.getLongtitude());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_task, container, false);
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
        rvAllWork.setAdapter(taskListAdapter);

    }

    public void updateDistanceForAllTask(double latitude, double longitude) {
        if (taskListAdapter != null) {
            taskListAdapter.updateDistanceForTask(mainActivity.isNetworkConnected(), latitude, longitude);
        }
    }

    @Override
    public void onClickItemWork(int position) {
        String idTask = taskListAdapter.getItem(position).getTaskResult()._id;
        String customerID = taskListAdapter.getItem(position).getTaskResult().customer._id;
        Intent intent = new Intent(getContext(), WorkDetailActivity.class);
        intent.putExtra(Conts.KEY_ID_TASK, idTask);
        intent.putExtra(Conts.KEY_CUSTOMER_ID, customerID);
        startActivity(intent);
//        dialogDetailTaskFragment.setIdTask(idTask);
//        dialogDetailTaskFragment.show(getActivity().getSupportFragmentManager(), dialogDetailTaskFragment.getTag());
//        dialogDetailTaskFragment.setExpaned(true);
    }

    public void addItem(ItemTask itemTask) {
        arrTask.add(itemTask);
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
