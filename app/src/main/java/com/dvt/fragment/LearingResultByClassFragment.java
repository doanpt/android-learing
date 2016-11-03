package com.dvt.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.dvt.qlcl.R;

/**
 * Created by Doanp on 7/9/2016.
 */
public class LearingResultByClassFragment extends Fragment {
    TextView textView;
    View myFragmentView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.fragment_exam_result_by_class, container, false);
        setHasOptionsMenu(true);
        return myFragmentView;
    }

    public LearingResultByClassFragment() {
    }
}
