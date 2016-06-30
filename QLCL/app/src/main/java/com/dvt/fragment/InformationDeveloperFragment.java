package com.dvt.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dvt.qlcl.R;

/**
 * Created by DoanPT1 on 6/23/2016.
 */
public class InformationDeveloperFragment extends Fragment {
    private View myFragmentView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.fragment_developer_information, container, false);
        return myFragmentView;
    }
}
