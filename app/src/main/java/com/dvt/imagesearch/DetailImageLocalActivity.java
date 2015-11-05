package com.dvt.imagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.dvt.adapters.ItemImageLocal;
import com.dvt.adapters.ViewPagerLocalAdapter;

import java.util.ArrayList;

/**
 * Created by doantrung on 11/2/15.
 */
public class DetailImageLocalActivity extends Activity {
    private ViewPagerLocalAdapter viewPagerAdapter;
    private ViewPager mViewPager;
    private ArrayList<ItemImageLocal> arrImage;
    private Activity activity;
    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_image);
        initView();
    }

    private void initView() {
        activity = this;
        Intent intent = getIntent();
        arrImage = (ArrayList<ItemImageLocal>) intent.getSerializableExtra(LocalSearchActivity.KEY_ARRAY_IMAGE_LOCAL);
        position = Integer.parseInt(intent.getStringExtra(LocalSearchActivity.KEY_POSITION_LOCAL));
        viewPagerAdapter = new ViewPagerLocalAdapter(activity, arrImage);
        mViewPager = (ViewPager) findViewById(R.id.vp_pages_image);
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.setCurrentItem(position);
    }
}
