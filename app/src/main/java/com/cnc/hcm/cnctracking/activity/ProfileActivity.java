package com.cnc.hcm.cnctracking.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.adapter.ProfileFragmentAdapter;
import com.cnc.hcm.cnctracking.base.BaseActivity;
import com.cnc.hcm.cnctracking.util.Conts;
import com.cnc.hcm.cnctracking.util.UserInfo;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by giapmn on 10/2/17.
 */

public class ProfileActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ProfileActivity.class.getSimpleName();
    private static final int DEFAULT_TAB_POSITION = 0;
    private CircleImageView imvAvatar;
    private TextView tvName;

    private ViewPager viewPagerProfile;

    @Override
    public void onViewReady(@Nullable Bundle savedInstanceState) {
        initViews();
        loadDatas();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_profile;
    }

    private void loadDatas() {
        UserInfo userInfo = UserInfo.getInstance(ProfileActivity.this);
        Picasso.with(ProfileActivity.this).load(Conts.URL_BASE + userInfo.getUserUrlImage())
                .placeholder(R.drawable.ic_account)
                .error(R.drawable.ic_account)
                .into(imvAvatar);
        tvName.setText(!TextUtils.isEmpty(userInfo.getUsername()) ? userInfo.getUsername() : Conts.UNKNOWN);
    }

    private void initViews() {
        ImageView imvBack = (ImageView) findViewById(R.id.imv_back_profile);
        ImageView imvMore = (ImageView) findViewById(R.id.imv_more_profile);
        ImageView imvEditProfile = (ImageView) findViewById(R.id.imv_edit_profile);
        imvAvatar = (CircleImageView) findViewById(R.id.imv_avatar_profile);
        tvName = (TextView) findViewById(R.id.tv_name_profile);
        viewPagerProfile = (ViewPager) findViewById(R.id.viewpager_profile);

        imvBack.setOnClickListener(this);
        imvMore.setOnClickListener(this);
        imvEditProfile.setOnClickListener(this);

        ProfileFragmentAdapter profileFragmentAdapter = new ProfileFragmentAdapter(getSupportFragmentManager());

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPagerProfile.setAdapter(profileFragmentAdapter);
        viewPagerProfile.setCurrentItem(DEFAULT_TAB_POSITION);

        viewPagerProfile.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerProfile.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imv_back_profile:
                finish();
                break;
            case R.id.imv_more_profile:

                break;
            case R.id.imv_edit_profile:
                break;
        }
    }

}
