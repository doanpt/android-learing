package com.cnc.hcm.cnctracking.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.activity.ListProductAndServiceActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListProductFragment extends Fragment implements ListProductAndServiceActivity.OnTextChangeProductListener {

    private ListProductAndServiceActivity activity;

    public ListProductFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (ListProductAndServiceActivity) getActivity();
        if (activity != null) {
            activity.setOnTextChangeProductListener(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_product, container, false);
        return view;
    }

    @Override
    public void onTextChange(CharSequence str) {
        Log.d("onTextChangeProduct", str.toString());
    }
}
