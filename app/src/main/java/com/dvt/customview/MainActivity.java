package com.dvt.customview;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dvt.customview.view.IndicatorView;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private DemAdapter adapter;
    private IndicatorView indicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter = new DemAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.vp_main);
        viewPager.setAdapter(adapter);
        indicatorView = findViewById(R.id.indicator);
        try {
            indicatorView.setViewPager(viewPager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
