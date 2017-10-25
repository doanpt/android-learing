package com.cnc.hcm.cnctracking.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.adapter.ProfileFragmentAdapter;
import com.cnc.hcm.cnctracking.api.APIService;
import com.cnc.hcm.cnctracking.api.ApiUtils;
import com.cnc.hcm.cnctracking.model.GetUserProfileResponseStatus;
import com.cnc.hcm.cnctracking.model.LoginResponseStatus;
import com.cnc.hcm.cnctracking.model.UserProfile;
import com.cnc.hcm.cnctracking.util.Conts;
import com.cnc.hcm.cnctracking.util.UserInfo;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by giapmn on 10/2/17.
 */

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ProfileActivity.class.getSimpleName();
    private static final int DEFAULT_TAB_POSITION = 0;
    private ImageView imvBack, imvMore;
    private ImageView imvEditProfile;
    private CircleImageView imvAvatar;
    private TextView tvName;

    private ViewPager viewPagerProfile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initViews();
        loadDatas();
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
        imvBack = (ImageView) findViewById(R.id.imv_back_profile);
        imvMore = (ImageView) findViewById(R.id.imv_more_profile);
        imvEditProfile = (ImageView) findViewById(R.id.imv_edit_profile);
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
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
