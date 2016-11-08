package com.dvt.samsung.finalapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.dvt.samsung.adapter.MainAdapter;
import com.dvt.samsung.adapter.MainBaseAdapter;
import com.dvt.samsung.model.TypeItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView lvType;
    private ArrayList<TypeItem> arrType;
    private MainAdapter adapter;
    //    private ListView lvType;
//    private MainBaseAdapter adapter;
    private DrawerLayout drawerLayout;
    private NavigationView navigation;
    private ImageView btnMenu;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btnMenu = (ImageView) findViewById(R.id.im_main);
        lvType = (RecyclerView) findViewById(R.id.recycler_view_main);
        lvType.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lvType.setLayoutManager(linearLayoutManager);
//        lvType = (ListView) findViewById(R.id.recycler_view_main);
        arrType = new ArrayList<>();
        arrType.add(new TypeItem(R.drawable.song, "Bài hát", "122"));
        arrType.add(new TypeItem(R.drawable.album, "Album", "12"));
        arrType.add(new TypeItem(R.drawable.artist, "Nghệ sĩ", "14"));
        arrType.add(new TypeItem(R.drawable.playlist, "Playlist", "11"));
        arrType.add(new TypeItem(R.drawable.download, "Download", "10"));
        adapter = new MainAdapter(this, arrType);
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
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    //    private boolean isMyServiceRunning(Class<?> serviceClass) {
//        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if (serviceClass.getName().equals(service.service.getClassName())) {
//                return true;
//            }
//        }
//        return false;
//    }
}
