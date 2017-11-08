package com.cnc.hcm.cnctracking.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cnc.hcm.cnctracking.fragment.WorkCancelFragment;
import com.cnc.hcm.cnctracking.fragment.WorkCompletedFragment;
import com.cnc.hcm.cnctracking.fragment.WorkNewFragment;
import com.cnc.hcm.cnctracking.fragment.WorkDoingFragment;
import com.cnc.hcm.cnctracking.util.Conts;

/**
 * Created by giapmn on 9/27/17.
 */

public class WorkFragmentAdapter extends FragmentPagerAdapter {

    private Fragment[] listFrag;

    private String arrPageTitle[] = new String[]{
            "Mới",
            "Đang thực hiện",
            "Hoàn thành",
            "Huỷ"};

    public WorkFragmentAdapter(FragmentManager fm, Fragment[] listFrag) {
        super(fm);
        this.listFrag = listFrag;
    }

    @Override
    public Fragment getItem(int position) {
        return listFrag[position];
    }

    @Override
    public int getCount() {
        return listFrag.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return arrPageTitle[position];
    }
}
