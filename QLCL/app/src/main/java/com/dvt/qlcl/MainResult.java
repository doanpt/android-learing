package com.dvt.qlcl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.dvt.fragment.ManagerFragment;

/**
 * Created by DoanPT1 on 6/23/2016.
 */
public class MainResult extends AppCompatActivity {
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FrameLayout mContentFrame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_result);
        initView();
        startFragment(new ManagerFragment());
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
                        Toast.makeText(MainResult.this,"Kết quả học tập",Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.navigation_item_2:
                        Toast.makeText(MainResult.this,"Kết quả thi",Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.navigation_item_3:
                        Intent intent=new Intent(MainResult.this,StudentReport.class);
                        startActivity(intent);
                        Toast.makeText(MainResult.this,"Điểm tích lũy",Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.navigation_item_4:
                        Toast.makeText(MainResult.this,"Lịch thi",Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.navigation_item_5:
                        Toast.makeText(MainResult.this,"Thông tin nhà phát triển",Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.navigation_item_6:
                        Toast.makeText(MainResult.this,"Xem sinh viên khác",Toast.LENGTH_SHORT).show();
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
            //ft.addToBackStack(backStateName);
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

}
