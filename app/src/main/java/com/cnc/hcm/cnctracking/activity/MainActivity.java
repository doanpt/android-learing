package com.cnc.hcm.cnctracking.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.adapter.WorkFragmentAdapter;
import com.cnc.hcm.cnctracking.dialog.DialogNotification;
import com.cnc.hcm.cnctracking.dialog.DialogSetTimePostServer;
import com.cnc.hcm.cnctracking.model.User;
import com.cnc.hcm.cnctracking.service.GPSService;
import com.cnc.hcm.cnctracking.util.Conts;
import com.cnc.hcm.cnctracking.util.UserInfo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends FragmentActivity implements View.OnClickListener
        , OnMapReadyCallback {


    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int REQUEST_CODE_LOCATION_UPDATE = 12132;
    private GoogleMap mMap;
    private ViewPager viewPager;

    private LinearLayout llClickNetwork;
    private LinearLayout llClickGPS;
    private LinearLayout llClickSetting;
    private LinearLayout llClickHelp;
    private TextView tvStatusNetwork, tvStatusGPS;
    private Button btnLogout;
    private CircleImageView imvAvatar;
    private TextView tvUsername, tvUserMail;
    private ImageView imvMenuDrawer, imvSearch;
    private ImageView imvProfile;
    private DrawerLayout drawer;
    //private TextView tvTimeProgress, tvStatus;


    private GPSService gpsService;

    private boolean isNetworkConnected;


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            gpsService = ((GPSService.MyBinder) iBinder).getGPSService();
            gpsService.setMainActivity(MainActivity.this);
            Log.d(TAG, "ServiceConnection, onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG, "ServiceConnection, onServiceDisconnected");
        }
    };

    private void registerBroadcastReciver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Conts.ACTION_NETWORK_CHANGE);
        registerReceiver(receiver, filter);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Conts.ACTION_NETWORK_CHANGE:
                    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetInfor = connectivityManager.getActiveNetworkInfo();
                    isNetworkConnected = activeNetInfor != null && activeNetInfor.isConnectedOrConnecting();
                    if (tvStatusNetwork != null)
                        if (isNetworkConnected) {
                            tvStatusNetwork.setTextColor(getResources().getColor(R.color.color_text_status));
                            tvStatusNetwork.setText(getResources().getString(R.string.On));
                        } else {
                            tvStatusNetwork.setTextColor(getResources().getColor(android.R.color.darker_gray));
                            tvStatusNetwork.setText(getResources().getString(R.string.Off));
                        }
                    break;
            }

        }
    };

    //TODO remove, method only for test
    private String getInforSeriveDestroy() {
        SharedPreferences sharedPreferences = getSharedPreferences("InforService", MODE_PRIVATE);
        return sharedPreferences.getString("KEY_INFOR_SERVICE_DESTROY", "Service Running!");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UserInfo.getInstance(this).setMainActivityActive(true);
        registerBroadcastReciver();
        initViews();
        if (!checkGPSServiceRunning(GPSService.class)) {
            Intent intent = new Intent(this, GPSService.class);
            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            startService(intent);
        }
        bindService(new Intent(this, GPSService.class), serviceConnection, Context.BIND_AUTO_CREATE);
        initObjects();
        checkUserLoginOnOtherDevice();
        initMap();

        Log.d("MainActivity", "getInforService: " + getInforSeriveDestroy());
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void checkUserLoginOnOtherDevice() {
        if (UserInfo.getInstance(this).getUserLoginOnOtherDevice()) {
            showMessageRequestLogout();
        }
    }

    private void initObjects() {
    }

    private void initViews() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        imvMenuDrawer = (ImageView) findViewById(R.id.imv_menu);
        imvSearch = (ImageView) findViewById(R.id.imv_seach_location);
        imvProfile = (ImageView) findViewById(R.id.imv_profile);
        imvProfile.setOnClickListener(this);
        imvMenuDrawer.setOnClickListener(this);
        imvSearch.setOnClickListener(this);

        llClickNetwork = (LinearLayout) findViewById(R.id.ll_click_network);
        llClickGPS = (LinearLayout) findViewById(R.id.ll_click_gps);
        llClickSetting = (LinearLayout) findViewById(R.id.ll_click_setting);
        llClickHelp = (LinearLayout) findViewById(R.id.ll_click_help);

        llClickNetwork.setOnClickListener(this);
        llClickGPS.setOnClickListener(this);
        llClickSetting.setOnClickListener(this);
        llClickHelp.setOnClickListener(this);

        tvStatusNetwork = (TextView) findViewById(R.id.tv_status_network);
        tvStatusGPS = (TextView) findViewById(R.id.tv_status_gps);

        btnLogout = (Button) findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(this);

        imvAvatar = (CircleImageView) findViewById(R.id.imv_avatar_profile);
        imvAvatar.setOnClickListener(this);
        tvUsername = (TextView) findViewById(R.id.tv_username_profile);
        tvUserMail = (TextView) findViewById(R.id.tv_user_email_profile);

        Picasso.with(this).load(Conts.URL_BASE + UserInfo.getInstance(this).getUserUrlImage())
                .placeholder(R.drawable.ic_account_avatar)
                .error(R.drawable.ic_account_avatar).into(imvAvatar);
        tvUsername.setText(UserInfo.getInstance(this).getUsername());
        tvUserMail.setText(UserInfo.getInstance(this).getUserEmail());

//        WorkFragmentAdapter adapter = new WorkFragmentAdapter(getSupportFragmentManager());
//        viewPager = (ViewPager) findViewById(R.id.viewPager);
//        viewPager.setAdapter(adapter);
//        PagerTabStrip pagerTabStrip = (PagerTabStrip) findViewById(R.id.pager_tab_strip);
//        pagerTabStrip.setTextColor(Color.WHITE);
//        pagerTabStrip.setTabIndicatorColor(Color.YELLOW);

        //tvTimeProgress = (TextView) findViewById(R.id.tv_time_run_service);
//        tvStatus = (TextView) findViewById(R.id.textView);

    }

    public void appendText(String str) {
//        tvStatus.append("\n" + str);
    }

    public void disPlayTimeProgress(int time) {
        //tvTimeProgress.setText("Time: " + time);
    }

    private boolean checkGPSServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(serviceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void setTitleStatusGPS(boolean status) {
        if (status) {
            tvStatusGPS.setTextColor(getResources().getColor(R.color.color_text_status));
            tvStatusGPS.setText(getResources().getString(R.string.On));
        } else {
            tvStatusGPS.setTextColor(getResources().getColor(android.R.color.darker_gray));
            tvStatusGPS.setText(getResources().getString(R.string.Off));
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_LOCATION_UPDATE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (gpsService != null) {
                        gpsService.requestLocationUpdate();
                    }

                } else {
                    Toast.makeText(this, "Permision denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imv_avatar_profile:
            case R.id.imv_profile:
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.ll_click_network:
                Intent intentWifi = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(intentWifi);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.ll_click_gps:
                Intent intentGps = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intentGps);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.btn_logout:
                actionLogout();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.ll_click_setting:
                Toast.makeText(this, "Tính năng đang hoàn thiện. Vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.ll_click_help:
                Toast.makeText(this, "Tính năng đang hoàn thiện. Vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.imv_menu:
                drawer.openDrawer(GravityCompat.START);
                break;
            case R.id.imv_seach_location:
                Toast.makeText(this, "Tính năng đang hoàn thiện. Vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void actionLogout() {
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.text_logout));
        progressDialog.show();

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UserInfo.getInstance(MainActivity.this).setUserInfoLogout();
                if (gpsService != null) {
                    gpsService.stopForeground(true);
                    gpsService.stopSelf();
                }
                progressDialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserInfo.getInstance(this).setMainActivityActive(false);
        unbindService(serviceConnection);
        unregisterReceiver(receiver);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void showMessageRequestLogout() {
        final DialogNotification dialog = new DialogNotification(MainActivity.this);
        dialog.setHiddenBtnExit();
        dialog.setContentMessage("Tài khoản này đã được đăng nhập trên thiết bị khác. Ứng dụng sẽ đăng xuất tài khoản. Vui lòng đăng nhập lại để tiếp tục sử dụng.");
        dialog.setCancelable(true);
        dialog.setOnClickDialogNotificationListener(new DialogNotification.OnClickDialogNotificationListener() {
            @Override
            public void onClickButtonOK() {
                actionLogout();
            }

            @Override
            public void onClickButtonExit() {

            }
        });
        dialog.show();
    }

    public void myLocation(double latitude, double longitude, float accuracy, String addName) { // TODO did we need accuracy here?
        if (mMap != null) {
            mMap.clear();
            LatLng myLocation = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(myLocation).title(addName));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String permissions[] = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
            requestPermissions(permissions, MainActivity.REQUEST_CODE_LOCATION_UPDATE);
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        // Add a marker in Sydney, Australia, and move the camera.
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
    }
}
