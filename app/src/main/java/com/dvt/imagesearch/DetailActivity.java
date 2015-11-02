package com.dvt.imagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.dvt.adapters.ItemImage;
import com.dvt.adapters.ViewPagerAdapter;

import java.util.ArrayList;

/**
 * Created by doantrung on 10/26/15.
 */
public class DetailActivity extends Activity {
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager mViewPager;
    private ArrayList<ItemImage> arrImage;
    private Activity activity;
    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_image);
        activity = this;
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        arrImage = (ArrayList<ItemImage>) intent.getSerializableExtra(MainActivity.KEY_ARRAY_IMAGE);
        position = Integer.parseInt(intent.getStringExtra(MainActivity.KEY_POSITION));
        viewPagerAdapter = new ViewPagerAdapter(activity, arrImage);
        mViewPager = (ViewPager) findViewById(R.id.vp_pages_image);
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.setCurrentItem(position);
    }
}