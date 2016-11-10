package com.dvt.samsung.finalapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.dvt.samsung.adapter.MainAdapter;
import com.dvt.samsung.model.TypeItem;
import com.dvt.samsung.utils.CommonValue;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView lvType;
    private ArrayList<TypeItem> arrType;
    private MainAdapter adapter;
    //  private ListView lvType;
//  private MainBaseAdapter adapter;
    private DrawerLayout drawerLayout;
    private NavigationView navigation;
    private ImageView btnMenu;
    boolean doubleBackToExitPressedOnce = false;
    SharedPreferences sharedPreferences;
    private boolean detectShaking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(CommonValue.KEY_SAVE_MODE, Context.MODE_PRIVATE);
        initView();
    }

    private void initView() {
        btnMenu = (ImageView) findViewById(R.id.im_main);
        lvType = (RecyclerView) findViewById(R.id.recycler_view_main);
        lvType.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lvType.setLayoutManager(linearLayoutManager);
        arrType = new ArrayList<>();
        arrType.add(new TypeItem(R.drawable.song, "Bài hát", "122"));
        arrType.add(new TypeItem(R.drawable.album, "Album", "12"));
        arrType.add(new TypeItem(R.drawable.artist, "Nghệ sĩ", "14"));
        arrType.add(new TypeItem(R.drawable.playlist, "Playlist", "11"));
        arrType.add(new TypeItem(R.drawable.download, "Download", "10"));
        adapter = new MainAdapter(this, arrType);
        //lvType = (ListView) findViewById(R.id.recycler_view_main);
        //adapter=new MainBaseAdapter(this,arrType);
        lvType.setAdapter(adapter);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        navigation = (NavigationView) findViewById(R.id.navigation);
        navigation.setNavigationItemSelectedListener(this);
        detectShaking = sharedPreferences.getBoolean(CommonValue.KEY_DETECT_SHAKING, false);
        SwitchCompat item = (SwitchCompat) navigation.getMenu().findItem(R.id.shaking_detect).getActionView();
        item.setChecked(detectShaking);
        item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                detectShaking = isChecked;
                sharedPreferences.edit().putBoolean(CommonValue.KEY_DETECT_SHAKING, detectShaking).commit();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        item.setChecked(true);
        switch (item.getItemId()) {
            case R.id.nav_about_us:
                Toast.makeText(MainActivity.this, "123", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
