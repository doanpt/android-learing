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
import android.widget.Toast;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.activity.MainActivity;
import com.cnc.hcm.cnctracking.util.Conts;

/**
 * Created by giapmn on 9/27/17.
 */

public class WorkNewFragment extends Fragment{

    private static final String TAGG = WorkNewFragment.class.getSimpleName();
    private RecyclerView rvNewTask;
//    private WorkNewAdapter workNewAdapter;
    private MainActivity mainActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("onCreate", "New");
        initObject();
    }

    private void initObject() {
        mainActivity = (MainActivity) getActivity();
//        workNewAdapter = new WorkNewAdapter(getContext(), mainActivity.getDataByWorkType(Conts.TYPE_NEW_TASK));
//        workNewAdapter.setOnClickButtonItemNewTaskListener(this);
//        updateDistanceNewWork(mainActivity.getLatitude(), mainActivity.getLongtitude());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.i("onCreateView", "New");

        View view = inflater.inflate(R.layout.fragment_work_new, container, false);
        rvNewTask = (RecyclerView) view.findViewById(R.id.rv_new_tasks);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvNewTask.setLayoutManager(linearLayoutManager);
        rvNewTask.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
//        rvNewTask.setAdapter(workNewAdapter);
    }

//    @Override
//    public void onClickButtonCancelTask(int position) {
//        Toast.makeText(getContext(), "Cancel: " + workNewAdapter.getItem(position).getContactName(), Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onClickButtonReceiveTask(int position) {
//        Toast.makeText(getContext(), "Receive: " + workNewAdapter.getItem(position).getRequestService(), Toast.LENGTH_LONG).show();
//    }
//
//    public void updateDistanceNewWork(double latitude, double longitude) {
//        if (workNewAdapter != null) {
//            workNewAdapter.updateDistanceNewWork(latitude, longitude);
//        }
//    }
}
