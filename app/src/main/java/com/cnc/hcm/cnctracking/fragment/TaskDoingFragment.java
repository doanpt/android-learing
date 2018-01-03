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
import com.cnc.hcm.cnctracking.dialog.DialogFragment;
import com.cnc.hcm.cnctracking.model.ItemTask;
import com.cnc.hcm.cnctracking.util.CommonMethod;
import com.cnc.hcm.cnctracking.util.Conts;

import java.util.ArrayList;

/**
 * Created by giapmn on 9/27/17.
 */

public class TaskDoingFragment extends Fragment implements TaskListAdapter.OnItemWorkClickListener {

    private TaskListAdapter taskListAdapter;
    private MainActivity mainActivity;

    private RecyclerView rvDoingWork;
    private ArrayList<ItemTask> arrTask = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("onCreate", "WorkDoing");
        initObject();
    }

    private void initObject() {
        mainActivity = (MainActivity) getActivity();
        taskListAdapter = new TaskListAdapter(getContext());
        taskListAdapter.notiDataChange(arrTask);
        taskListAdapter.setOnItemWorkClickListener(this);

        updateDistanceDoingWork(mainActivity.getLatitude(), mainActivity.getLongtitude());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doing_task, container, false);
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
        rvDoingWork.setAdapter(taskListAdapter);
    }

    public void updateDistanceDoingWork(double latitude, double longitude) {
        if (taskListAdapter != null) {
            taskListAdapter.updateDistanceForTask(mainActivity.isNetworkConnected(), latitude, longitude);
        }
    }

    public void addItem(ItemTask itemTask) {
        arrTask.add(itemTask);
    }

    @Override
    public void onClickItemWork(int position) {
        String idTask = taskListAdapter.getItem(position).getTaskResult()._id;
        Intent intent = new Intent(getContext(), WorkDetailActivity.class);
        intent.putExtra(Conts.KEY_ID_TASK, idTask);
        startActivity(intent);
//        DialogFragment dialogFragment = new DialogFragment(idTask);
//        dialogFragment.show(getActivity().getSupportFragmentManager(), dialogFragment.getTag());
//        dialogFragment.showExpaned();
    }

    public void notiDataChange() {
        if (taskListAdapter != null) {
            taskListAdapter.notiDataChange(arrTask);
        }
    }
}
