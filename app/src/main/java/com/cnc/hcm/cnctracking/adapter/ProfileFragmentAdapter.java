package com.cnc.hcm.cnctracking.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cnc.hcm.cnctracking.fragment.ChangePasswordFragment;
import com.cnc.hcm.cnctracking.fragment.UserInforFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giapmn on 10/2/17.
 */

public class ProfileFragmentAdapter extends FragmentPagerAdapter {

    private static final int USER_INFOR_FRAGMENT_POSITION = 0;

    private final List<Fragment> fragments;

    public ProfileFragmentAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
        fragments.add(new UserInforFragment());
        fragments.add(new ChangePasswordFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
