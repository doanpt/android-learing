package com.dvt.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dvt.adapter.HtmlParse;
import com.dvt.adapter.ViewPagerAdapter;
import com.dvt.qlcl.MainActivity;
import com.dvt.qlcl.R;

/**
 * Created by minhpq on 3/29/16.
 */
public class ManagerFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    LearingResultFragment learingResultFragment;
    ExamResultFragment examResultFragment;
    ExamScheduleFragment examScheduleFragment;
    private String code="";

    public ManagerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_fragment, container, false);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        return v;
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        learingResultFragment=new LearingResultFragment();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String mCode = preferences.getString(MainActivity.RESULT_CODE_STUDENT, "");
        if(!mCode.equalsIgnoreCase(""))
        {
            code = mCode;  /* Edit the value here*/
        }
        Bundle bundle=new Bundle();
        bundle.putString("Code",code);
        learingResultFragment.setArguments(bundle);
        examResultFragment=new ExamResultFragment();
        examResultFragment.setArguments(bundle);
        examScheduleFragment=new ExamScheduleFragment();
        examScheduleFragment.setArguments(bundle);
        adapter.addFragment(learingResultFragment, "Kết quả học tập");
        adapter.addFragment(examResultFragment, "Kết quả thi");
        adapter.addFragment(examScheduleFragment, "Lịch thi");
        viewPager.setAdapter(adapter);
    }
}
