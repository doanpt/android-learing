package com.dvt.qlcl;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.dvt.fragment.ExamResultFragment;
import com.dvt.fragment.ExamScheduleFragment;
import com.dvt.fragment.InformationDeveloperFragment;
import com.dvt.fragment.LearingResultFragment;
import com.dvt.fragment.StudentReportFragment;
import com.dvt.util.CommonMethod;
import com.dvt.util.CommonValue;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by DoanPT1 on 6/23/2016.
 */
public class MainResult extends AppCompatActivity {
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FrameLayout mContentFrame;
    private String code;
    Bundle bundle = new Bundle();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_result);
        initView();
        code = CommonMethod.getCode(MainResult.this);
        bundle.putString(CommonValue.KEY_CODE, code);
        getSupportActionBar().setTitle(R.string.title_actionbar_kqht);
        LearingResultFragment learingResultFragment = new LearingResultFragment();
        learingResultFragment.setArguments(bundle);
        startFragment(learingResultFragment);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void replaceFragment(Fragment fragment) {
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.nav_contentframe, fragment);
        ft.commit();
    }

    private void initView() {
        setUpToolbar();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        setUpNavDrawer();
        mContentFrame = (FrameLayout) findViewById(R.id.nav_contentframe);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_1:
                        LearingResultFragment learingResultFragment = new LearingResultFragment();
                        learingResultFragment.setArguments(bundle);
                        replaceFragment(learingResultFragment);
                        getSupportActionBar().setTitle(R.string.title_actionbar_kqht);
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.navigation_item_2:
                        ExamResultFragment examResultFragment = new ExamResultFragment();
                        examResultFragment.setArguments(bundle);
                        replaceFragment(examResultFragment);
                        getSupportActionBar().setTitle(R.string.title_actionbar_kqt);
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.navigation_item_3:
                        StudentReportFragment studentReport = new StudentReportFragment();
                        replaceFragment(studentReport);
                        getSupportActionBar().setTitle(R.string.title_actionbar_dtl);
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.navigation_item_4:
                        ExamScheduleFragment examScheduleFragment = new ExamScheduleFragment();
                        examScheduleFragment.setArguments(bundle);
                        replaceFragment(examScheduleFragment);
                        getSupportActionBar().setTitle(R.string.title_actionbar_dt);
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.navigation_item_5:
                        InformationDeveloperFragment informationDeveloperFragment = new InformationDeveloperFragment();
                        replaceFragment(informationDeveloperFragment);
                        getSupportActionBar().setTitle(R.string.title_actionbar_ttnpt);
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.navigation_item_6:
                        Intent intentBack = new Intent(MainResult.this, MainActivity.class);
                        CommonMethod.setCode(MainResult.this, "");
                        startActivity(intentBack);
                        mDrawerLayout.closeDrawers();
                        return true;
                    default:
                        return true;
                }
            }
        });
    }

    public void startFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        String fragmentTag = backStateName;
        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);
        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null) { //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.nav_contentframe, fragment, fragmentTag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        }
    }

    private void setUpNavDrawer() {
        if (mToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationIcon(R.drawable.ic_drawer);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }
    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    @Override
    public void onBackPressed() {
        CommonMethod.setCode(MainResult.this,"");
        super.onBackPressed();
    }
}
