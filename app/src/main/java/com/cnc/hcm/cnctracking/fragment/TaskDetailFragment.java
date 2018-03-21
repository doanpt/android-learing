package com.cnc.hcm.cnctracking.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.util.CommonMethod;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskDetailFragment extends Fragment {

    private Button btnClick;

    public TaskDetailFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_detail, container, false);
        Log.i("onCreateView", "TaskDetailFragment");
        btnClick = (Button) view.findViewById(R.id.btnClick);
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonMethod.makeToast(getContext(), "AAAAAAAAAA");
            }
        });
        return view;
    }

}
