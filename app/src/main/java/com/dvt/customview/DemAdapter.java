package com.dvt.customview;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.BaseAdapter;

import com.dvt.customview.fragment.ABCFragment;

/**
 * Created by Android on 19/08/2018.
 */

public class DemAdapter extends FragmentPagerAdapter {
    public DemAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ABCFragment();
            case 1:
                return new ABCFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
