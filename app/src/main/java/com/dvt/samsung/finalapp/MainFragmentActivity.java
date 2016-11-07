package com.dvt.samsung.finalapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dvt.samsung.adapter.ViewPagerAdapter;

/**
 * Created by Android on 11/7/2016.
 */

public class MainFragmentActivity extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener {
    private CoordinatorLayout coordinator;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigation;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private TabLayout tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);
        initView();

    }

    private void initView() {
        //open va close menu
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.open_navigation, R.string.close_navigation) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        //khởi tạo và lắng nghe navigation menu
        navigation = (NavigationView) findViewById(R.id.navigation);
        navigation.setNavigationItemSelectedListener(this);
        viewPagerAdapter = new ViewPagerAdapter(this,getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(viewPagerAdapter);

        String[] tabBackgroundIds = new String[]{"Bài hát", "Album", "Nghệ sĩ", "Playlist", "Download"};
        tab = (TabLayout) findViewById(R.id.tab);
        tab.setupWithViewPager(viewPager);
        for (int i = 0; i < viewPagerAdapter.getCount(); i++) {
            tab.getTabAt(i).setText(tabBackgroundIds[i]);
        }
        coordinator = (CoordinatorLayout) findViewById(R.id.coordinator);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
//        switch (item.getItemId()) {
//            case R.id.nav_call_logs:
//                break;
//            case R.id.nav_log_out:
//                break;
//            case R.id.nav_about_us:
//                Toast.makeText(this,"Open Deverloper",Toast.LENGTH_LONG).show();
//                break;
//            case R.id.nav_settings:
//                Toast.makeText(this,"Open Setting Sysmte",Toast.LENGTH_LONG).show();
//                break;
//            case R.id.nav_my_account:
//                Toast.makeText(this,"Open setting Account",Toast.LENGTH_LONG).show();
//                break;
//            case R.id.nav_mail:
//                Toast.makeText(this,"Open Mail",Toast.LENGTH_LONG).show();
//                break;
//        }
        return true;
    }
}
