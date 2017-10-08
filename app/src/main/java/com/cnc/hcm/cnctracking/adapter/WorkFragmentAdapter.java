package com.cnc.hcm.cnctracking.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cnc.hcm.cnctracking.fragment.WorkCancelFragment;
import com.cnc.hcm.cnctracking.fragment.WorkCompletedFragment;
import com.cnc.hcm.cnctracking.fragment.WorkNewFragment;
import com.cnc.hcm.cnctracking.fragment.WorkReceivedFragment;
import com.cnc.hcm.cnctracking.util.Conts;

/**
 * Created by giapmn on 9/27/17.
 */

public class WorkFragmentAdapter extends FragmentPagerAdapter {

    private String arrPageTitle[] = new String[]{
            "Công việc mới",
            "Công việc đã nhận",
            "Công việc đã hoàn thành",
            "Công việc đã huỷ"};

    public WorkFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case Conts.DEFAULT_VALUE_INT_0:
                return new WorkNewFragment();
            case Conts.DEFAULT_VALUE_INT_1:
                return new WorkReceivedFragment();
            case Conts.DEFAULT_VALUE_INT_2:
                return new WorkCompletedFragment();
            case Conts.DEFAULT_VALUE_INT_3:
                return new WorkCancelFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return arrPageTitle[position];
    }
}
