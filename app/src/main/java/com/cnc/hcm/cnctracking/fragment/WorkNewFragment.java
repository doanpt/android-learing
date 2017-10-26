package com.cnc.hcm.cnctracking.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnc.hcm.cnctracking.R;

/**
 * Created by giapmn on 9/27/17.
 */

public class WorkNewFragment extends Fragment {

    private TextView tvStatus;

    private static WorkNewFragment workNewFragment;

    public static WorkNewFragment getInstance() {
        if (workNewFragment == null) {
            workNewFragment = new WorkNewFragment();
        }
        return workNewFragment;
    }

    public WorkNewFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("onCreate", "New");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_new, container, false);
        Log.i("onCreateView", "New");
        tvStatus = (TextView) view.findViewById(R.id.tv_status);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void setTextStatus(String status) {
        if (tvStatus != null)
            tvStatus.setText(status);
    }
}
