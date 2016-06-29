package com.dvt.qlcl;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dvt.fragment.ExamResultFragment;
import com.dvt.fragment.ExamScheduleFragment;
import com.dvt.fragment.InformationDeveloperFragment;
import com.dvt.fragment.LearingResultFragment;
import com.dvt.util.CommonMethod;
import com.dvt.util.CommonValue;

import android.support.v7.widget.SearchView;

/**
 * Created by DoanPT1 on 6/23/2016.
 */
public class MainResult extends AppCompatActivity {
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FrameLayout mContentFrame;
    private String code;
    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private EditText edtSeach;
    Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_result);
        initView();
        code = CommonMethod.getCode(MainResult.this);
        bundle.putString(CommonValue.KEY_CODE, code);
        getSupportActionBar().setTitle("Kết quả học tập");
        LearingResultFragment learingResultFragment = new LearingResultFragment();
        learingResultFragment.setArguments(bundle);
        startFragment(learingResultFragment);
    }

    public void replaceFragment(Fragment fragment) {
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.nav_contentframe, fragment);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.e("onQueryTextChange", "called");
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainResult.this, "Search" + query, Toast.LENGTH_SHORT).show();
                return false;
            }

        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_reload:
                Toast.makeText(MainResult.this, "Reload", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_search:
                Intent intent = getIntent();
                if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
                    String query = intent.getStringExtra(SearchManager.QUERY);
                    //doMySearch(query);
                    Toast.makeText(MainResult.this, "Search", Toast.LENGTH_SHORT).show();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void doSearch() {
        //progress after enter text
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
                        getSupportActionBar().setTitle("Kết quả học tập");
                        Toast.makeText(MainResult.this, "Kết quả học tập", Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.navigation_item_2:
                        ExamResultFragment examResultFragment = new ExamResultFragment();
                        examResultFragment.setArguments(bundle);
                        replaceFragment(examResultFragment);
                        getSupportActionBar().setTitle("Kết quả thi");
                        Toast.makeText(MainResult.this, "Kết quả thi", Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.navigation_item_3:
//                        St learingResultFragment=new LearingResultFragment();
//                        learingResultFragment.setArguments(bundle);
//                        replaceFragment(learingResultFragment);
                        StudentReport studentReport = new StudentReport();
                        replaceFragment(studentReport);
                        getSupportActionBar().setTitle("Kết quả thi");
                        Toast.makeText(MainResult.this, "Điểm tích lũy", Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.navigation_item_4:
                        ExamScheduleFragment examScheduleFragment = new ExamScheduleFragment();
                        examScheduleFragment.setArguments(bundle);
                        replaceFragment(examScheduleFragment);
                        getSupportActionBar().setTitle("Lịch thi");
                        Toast.makeText(MainResult.this, "Lịch thi", Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.navigation_item_5:
                        InformationDeveloperFragment informationDeveloperFragment = new InformationDeveloperFragment();
                        replaceFragment(informationDeveloperFragment);
                        getSupportActionBar().setTitle("Thông tin nhà phát triển");
                        Toast.makeText(MainResult.this, "Thông tin nhà phát triển", Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.navigation_item_6:
                        Intent intentBack = new Intent(MainResult.this, MainActivity.class);
                        CommonMethod.setCode(MainResult.this, "");
                        startActivity(intentBack);
                        Toast.makeText(MainResult.this, "Xem sinh viên khác", Toast.LENGTH_SHORT).show();
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
