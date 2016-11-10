package com.dvt.samsung.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dvt.samsung.finalapp.R;

/**
 * Created by Android on 11/7/2016.
 */

public class DownloadFragment extends Fragment {
    private View view;

    @SuppressLint("ValidFragment")
    public DownloadFragment(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.fragment_download, null);


    }

    public DownloadFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return view;
    }
}
