package com.cnc.hcm.cnctrack.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

    public abstract int getLayoutID();

    public abstract void onViewReady(View view);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutID(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onViewReady(view);
    }

    public void actionLogout() {
        if (getActivity() instanceof BaseActivity) {
            if (getActivity() != null && !(getActivity()).isFinishing()) {
                ((BaseActivity) getActivity()).showMessageRequestLogout();
            }
        }
    }

    public void dismisProgressLoading() {
        if (getActivity() != null && !getActivity().isFinishing() && getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).dismisProgressLoading();
        }
    }

    public void showProgressLoading() {
        if (getActivity() instanceof BaseActivity) {
            if (!(getActivity()).isFinishing()) {
                ((BaseActivity) getActivity()).showProgressLoadding();
            }
        }
    }
}
