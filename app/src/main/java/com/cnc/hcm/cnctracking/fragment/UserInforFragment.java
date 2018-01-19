package com.cnc.hcm.cnctracking.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.util.UserInfo;

/**
 * Created by giapmn on 10/2/17.
 */

public class UserInforFragment extends Fragment {

    private TextView mTvFullName;
    private TextView mTvEmail;
    private TextView mTvPhone;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_infor, container, false);
        mTvFullName = (TextView) view.findViewById(R.id.tv_full_name);
        mTvEmail = (TextView) view.findViewById(R.id.tv_email);
        mTvPhone = (TextView) view.findViewById(R.id.tv_phone);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadUserProfile();
    }

    public void loadUserProfile() {
        mTvFullName.setText(UserInfo.getInstance(getContext()).getUsername());
        mTvEmail.setText(UserInfo.getInstance(getContext()).getUserEmail());
        mTvPhone.setText(UserInfo.getInstance(getContext()).getUserPhoneNo());

    }
}
